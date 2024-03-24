
/*
 * Copyright (c) 2016 Sohu. All Rights Reserved
 */
package cn.uestc;

import cn.uestc.common.NettyClientBootstrap;
import lombok.extern.slf4j.Slf4j;
import cn.uestc.utils.myFileUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


import java.io.File;
import java.io.InputStream;
import java.sql.SQLOutput;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

@SpringBootApplication
public class MainClass implements CommandLineRunner {
    @Autowired
    private MainHelper mainHelper;

    public static void main(String[] args) {
        SpringApplication.run(MainClass.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请选择上传或者下载（1为上传，2为下载）");
            System.out.println("按4可以查看所有已上传的文件");
            int choose = scanner.nextInt();
            if (choose == 1) {
                System.out.println("请输入文件或文件夹的绝对路径:");
                String path = scanner.next();
                File file = new File(path);
                LocalDateTime start = LocalDateTime.now();
                int count=0;
                if(file.exists()&&file.isDirectory()){
                    int numFiles = myFileUtils.countFilesInFolder(String.valueOf(file));
                    for (int i = 0; i < numFiles; i++) {
                        File[] files = file.listFiles();
                        if (files[i].exists()) {
                            if (mainHelper.upload(files[i].getAbsolutePath())) {
                                System.out.println("文件"+file.getName()+"上传成功");
                                count+= (int) files[i].length();
                            }
                        } else {
                            System.out.println("文件不存在");
                        }
                    }
                } else if (file.exists()&&file.isFile()) {
                    if (mainHelper.upload(file.getAbsolutePath())) {
                        System.out.println("文件上传成功");
                        count= (int) file.length();
                    }
                } else {
                    System.out.println("文件不存在");
                }
                LocalDateTime end = LocalDateTime.now();
                System.out.println("文件大小"+count+"字节");
                System.out.println("上传完成，共耗时"+ Duration.between(start,end).toMillis()+"毫秒");
            }
            if (choose == 2) {
                System.out.println("请输入你想要下载的文件名称");
                String fileName = scanner.next();
                System.out.println("请输入你想要存储该文件的绝对路径");
                String path = scanner.next();
                if (mainHelper.download(fileName, path)) {
                    System.out.println("文件下载成功");
                }
            }
            if (choose == 3) {
                scanner.close();
                System.out.println("Exiting application...");
                System.exit(0);
            }
            if (choose==4){
                mainHelper.listFiles();
            }
            System.out.println("请输入3退出程序或继续操作（1为上传，2为下载）");
        }
    }
}