package com.example.demo1.console.controller;

import com.alibaba.excel.EasyExcel;
import com.example.demo1.console.common.CategoryTree;
import com.example.demo1.console.domain.CategoryItemListVo;
import com.example.demo1.module.common.ExcelDataListener;
import com.example.demo1.module.common.Response;
import com.example.demo1.module.entity.Category;
import com.example.demo1.module.entity.Coach;
import com.example.demo1.module.service.CategoryService;
import com.example.demo1.module.service.CoachService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CoachService coachService;


    @RequestMapping("/export")
    public void exportData(HttpServletResponse response) {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
        String fileName;
        try {
            fileName = URLEncoder.encode("分类表", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        // Content-Disposition主要用于指示浏览器如何处理响应内容
        // Content-Disposition: [disposition-type]; [parameters]
        // disposition-type : 1 inline（默认值） 表示内容应该在浏览器中直接显示    2 attachment 表示内容应该作为文件下载
        // parameters filename="filename.ext" 指定下载文件的文件名
        response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");

        // 获取输出流
        ServletOutputStream outputStream;
        try {
            outputStream = response.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // 查数据
        List<Category> data = categoryService.getList(null, null, null);
        EasyExcel.write(outputStream, Category.class).sheet("分类信息").doWrite(data);

        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("/parse")
    public Response<CategoryItemListVo> parseData(MultipartFile file) {
        String contentType = file.getContentType();
        if (!"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
            return new Response<>(5001);
        }

        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return new Response<>(3001);
        }

        ExcelDataListener<Category> listener = new ExcelDataListener<>();
        EasyExcel.read(inputStream, Category.class, listener).sheet().doRead();
        List<Category> dataList = listener.getDataList();

        return new Response<>(1001, CategoryTree.getCategoryTree(dataList));
    }

    private void zipFolder(File file, ZipOutputStream zos, String parentFolder) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (null == files) {
                return;
            }

            for (File f : files) {
                zipFolder(f, zos, parentFolder  + file.getName() + "/");
            }
        }
        else {
            // 添加文件到ZIP
            FileInputStream fis = new FileInputStream(file);
            String entryName = parentFolder + file.getName();
            ZipEntry zipEntry = new ZipEntry(entryName);
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
            fis.close();
        }
    }

    @RequestMapping("/exportMul")
    public void exportDataMul(HttpServletResponse response) {
        // 设置响应头
        response.setContentType("application/zip;charset=UTF-8");
        String zipName;
        try {
            zipName = URLEncoder.encode("压缩包", "UTF-8");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException){
            // 返回类型已经不是json了，就算有运行事异常也不用抛给全局异常处理了, 不会有json返回值，直接当场打印异常信息
            unsupportedEncodingException.printStackTrace();
            return;
        }
        response.setHeader("Content-disposition", "attachment; filename=" + zipName + ".zip");

        // 获取输出流
        ZipOutputStream zos;
        try {
            zos = new ZipOutputStream(response.getOutputStream());
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }

        // 使用多线程读数据库写文件
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        CountDownLatch countDownLatch = new CountDownLatch(2);

        // 线程1
        File file = new File("分类表.xlsx");
        executorService.execute(() -> {
            // 查数据
            List<Category> data = categoryService.getList(null, null, null);
            //写excel文件
            EasyExcel.write(file, Category.class).sheet("分类信息").doWrite(data);
            countDownLatch.countDown();
        });

        // 线程2
        File file1 = new File("教练表.xlsx");
        executorService.execute(() -> {
            // 查数据
            List<Coach> pageList = coachService.getPageList(1, null);
            //写excel文件
            EasyExcel.write(file1, Coach.class).sheet("教练信息").doWrite(pageList);
            countDownLatch.countDown();
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 打包1
        try {
            zipFolder(file, zos, "");
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // 打包2
        try {
            zipFolder(file1, zos, "");
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            zos.flush();
            zos.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
