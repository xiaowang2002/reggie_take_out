package com.atwzs.reggie.controller;

import com.atwzs.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName CommonController
 * @Description
 * @Author WangZhisheng
 * @Date 12:07 2022/12/30
 * @Version 11.0.15
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * @description: 文件上传
     **/
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除，临时文件的名字和位置我们可以通过调试来看
        log.info(file.toString());

        String originalFilename = file.getOriginalFilename();   //获取原始文件名xxx.xxx

        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用uuid重新生成文件名，防止名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;

        //创建一个目录对象
        File dir = new File(basePath);

        //判断当前目录是否存在
        if (!dir.exists()) {
            //目录不存在
            dir.mkdirs();
        }

        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        try {
            //返回图片类型，后缀为jpeg,可有可无
            response.setContentType("image/jpeg");

            //输入流，通过输入流读取文件内容
            FileInputStream inputStream = new FileInputStream(new File(basePath + name));

            //输出流，通过输出流写回浏览器,在浏览器端展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {    //读到-1表示读完了,并返回读进去的长度
                outputStream.write(bytes, 0, len);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
