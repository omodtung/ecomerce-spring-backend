package com.shop.ecommerceGo.service;

import jakarta.servlet.ServletContext;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.channels.MulticastChannel;
import org.aspectj.apache.bcel.classfile.Field;
import org.springframework.boot.autoconfigure.jersey.JerseyProperties.Servlet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {

  private final ServletContext servletContext;

  public UploadService(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  public String handleSaveUpLoadFile(MultipartFile file, String targetFoler) {
    String rootPath = this.servletContext.getRealPath("/resources/images");
    String finalName = "";
    try {
      byte[] bytes = file.getBytes();
      File dir = new File(rootPath + File.separator + targetFoler);
      if (!dir.exists()) dir.mkdirs();
      finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
      File serverFile = new File(
        dir.getAbsolutePath() + File.separator + finalName
      );

      BufferedOutputStream stream = new BufferedOutputStream(
        new FileOutputStream(serverFile)
      );
      stream.write(bytes);
      stream.close();
    } catch (IOException e) {
      // TODO: handle exception
      e.printStackTrace();
    }
    return finalName;
  }
}
