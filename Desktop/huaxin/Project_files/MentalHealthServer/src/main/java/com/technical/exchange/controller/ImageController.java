//package com.technical.exchange.controller;
//
//import com.technical.exchange.bean.ApiResponse;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.util.List;
//import java.util.UUID;
//
////图片上传与下载
//@RestController
//@RequestMapping("/image/")
//public class ImageController {
//    public String FILE_PATH = "./image_files/";//路径
//
//    /**
//     * 获取图片
//     */
//    @GetMapping("file/{filename}")
//    //PathVariable接收请求路径中占位符的值
//    public void get(@PathVariable("filename") String filename, HttpServletResponse response) {
//        try {
//            File file = new File(FILE_PATH + filename);//文件
//            if (file.exists()) {//存在
//                //应答头
//                response.setHeader("content-type", "image/" + file.getName().substring(file.getName().lastIndexOf(".") + 1));
////                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(request.getParameter("filename"), "UTF-8"));
//                FileInputStream fis = new FileInputStream(file);//读取文件
//                BufferedInputStream bis = new BufferedInputStream(fis);
//                OutputStream os = response.getOutputStream();
//                byte[] buffer = new byte[1024];
//                int i = bis.read(buffer);
//                while (i != -1) {
//                    os.write(buffer, 0, i);//输出
//                    i = bis.read(buffer);
//                }
//                bis.close();
//                os.close();
//                fis.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 上传图片
//     */
//    @PostMapping("/upload")
//    public ApiResponse<String> upload(HttpServletRequest request) {
//        try {
//            List<MultipartFile> files = ((MultipartHttpServletRequest) request)
//                    .getFiles("file");
//            //获取上传的文件
//            MultipartFile file = files.get(0);
//            //获得文件名
//            String originalFileName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
//            //判断文件类型
//            if (!originalFileName.endsWith(".jpg") && !originalFileName.endsWith(".png") && !originalFileName.endsWith(".jpeg")) {
//                return ApiResponse.defaultError("文件类型错误");
//            }
//            //获取文件扩展名
//            originalFileName = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
//            BufferedOutputStream stream;
//            //不存在文件保存路径则新建文件夹
//            if (!new File(FILE_PATH).exists()) {
//                if (!new File(FILE_PATH).mkdirs()) {
//                    return ApiResponse.defaultError("上传错误，请重试");
//                }
//            }
//            String fileName = UUID.randomUUID().toString() + "." + originalFileName;//生成文件名
//            if (!file.isEmpty()) {
//                //返回文件内容作为一个字节数组
//                byte[] bytes = file.getBytes();
//                ////创建一个新的缓冲输出流，以将数据写入指定的底层输出流。
//                stream = new BufferedOutputStream(new FileOutputStream(
//                        FILE_PATH + fileName));//写入文件
//                stream.write(bytes);
//                stream.close();
//            } else {
//                return ApiResponse.defaultError("上传错误，请重试");
//            }
//            return new ApiResponse<String>().data(fileName).message("上传成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ApiResponse.defaultError("上传错误，请重试");
//        }
//    }
//}
package com.technical.exchange.controller;

import com.technical.exchange.bean.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;

//图片上传与下载
@RestController
@RequestMapping("/image/")
public class ImageController {
    public String FILE_PATH = "./image_files/";//路径

    /**
     * 获取图片
     */
    @GetMapping("file/{filename}")
    public void get(@PathVariable("filename") String filename, HttpServletResponse response) {
        try {
            File file = new File(FILE_PATH + filename);//文件
            if (file.exists()) {//存在
                //应答头
                response.setHeader("content-type", "image/" + file.getName().substring(file.getName().lastIndexOf(".") + 1));
//                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(request.getParameter("filename"), "UTF-8"));
                FileInputStream fis = new FileInputStream(file);//读取文件
                BufferedInputStream bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                byte[] buffer = new byte[1024];
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);//输出
                    i = bis.read(buffer);
                }
                bis.close();
                os.close();
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传图片
     */
    @PostMapping("/upload")
    public ApiResponse<String> upload(HttpServletRequest request) {
        try {
            List<MultipartFile> files = ((MultipartHttpServletRequest) request)
                    .getFiles("file");
            MultipartFile file = files.get(0);//获取上传的文件
            String originalFileName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase();
            //判断文件类型
            if (!originalFileName.endsWith(".jpg") && !originalFileName.endsWith(".png") && !originalFileName.endsWith(".jpeg")) {
                return ApiResponse.defaultError("文件类型错误");
            }
            //获取文件扩展名
            originalFileName = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            BufferedOutputStream stream;
            if (!new File(FILE_PATH).exists()) {
                if (!new File(FILE_PATH).mkdirs()) {
                    return ApiResponse.defaultError("上传错误，请重试");
                }
            }
            String fileName = UUID.randomUUID().toString() + "." + originalFileName;//生成文件名
            System.out.println("图片文件名：" + fileName);
            if (!file.isEmpty()) {
                byte[] bytes = file.getBytes();
                stream = new BufferedOutputStream(new FileOutputStream(
                        FILE_PATH + fileName));//写入文件
                stream.write(bytes);
                stream.close();
            } else {
                return ApiResponse.defaultError("上传错误，请重试");
            }
            return new ApiResponse<String>().data(fileName).message("上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.defaultError("上传错误，请重试");
        }
    }
}
