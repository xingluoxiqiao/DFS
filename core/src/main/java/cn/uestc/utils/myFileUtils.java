
/*
 * Copyright (c) 2016 Sohu. All Rights Reserved
 */
package cn.uestc.utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class myFileUtils {

    public static byte[] fileToByteArray(File file) throws IOException {
        byte[] fileBytes = new byte[(int)file.length()];
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            inputStream.read(fileBytes);
        }
        return fileBytes;
    }
    /**
     * @param filePath    需要分块的文件许哦在路径
     * @param blockSize   分块的大小
     * @param uuid        唯一标识
     *                    分块后的文件所在相对路径uuid + "blocks"
     */
    public static void splitFile(String filePath, int blockSize, String uuid) {
            try {
                // Create a folder to store the blocks
                String folderPath = uuid + "blocks";
                File folder = new File(folderPath);
                if (!folder.exists()) {
                    folder.mkdir();
                }

                // Read the file in chunks
                try (InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath))) {
                    byte[] buffer = new byte[blockSize];
                    int bytesRead;
                    int blockNumber = 0;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        // Create a block file
                        String blockFileName = folderPath + File.separator +"block_" + blockNumber + ".bin";
                        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(blockFileName))) {
                            outputStream.write(buffer, 0, bytesRead);
                        }

                        blockNumber++;
                    }
                }
                System.out.println("File split successfully into " + folder.listFiles().length + " blocks.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    public static void fileMerge(String blockSourcePath, String targetFilePath, String uuid) {
        try {
            // Get all files in the folder
            File folder = new File(blockSourcePath);

            // Specify the path for the reconstructed file
            String reconstructedFilePath = targetFilePath;

            try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(reconstructedFilePath))) {
                // Iterate through files and concatenate them to reconstruct the original file
                int blockNumber = 0;
                File file;
                do {
                    // Create the file path for the block
                    String blockFileName = blockSourcePath + File.separator + uuid + "_block_" + blockNumber;

                    // Check if the block file exists
                    file = new File(blockFileName);
                    if (file.exists()) {
                        // Read the block file and write its content to the output stream
                        byte[] data = Files.readAllBytes(file.toPath());
                        outputStream.write(data);

                        // Increment block number
                        blockNumber++;
                    }
                } while (file.exists());

                System.out.println("File successfully reconstructed.");
            }

            // Delete the split files
            Files.walk(Paths.get(blockSourcePath))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(File::delete);
            //删除文件夹
            FileUtils.deleteDirectory(new File(blockSourcePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取指定路径下的指定名称的文件
     *
     */
    public static File getFile(String filePath, String fileName) throws IOException {
        List<File> collect = Files.walk(Paths.get(filePath),
                        Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)
                .filter(path -> path.getFileName().toString().equals(fileName))
                .map(Path::toFile)
                .toList();
        if (collect.isEmpty()) {
            return null;
        }
        return collect.get(0);
    }



    public static void deleteFile(String uuid) throws IOException {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        File tempDirectory = new File(uuid + "blocks");

        executorService.schedule(() -> {
            try {
                org.apache.commons.io.FileUtils.deleteDirectory(tempDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 5, TimeUnit.SECONDS);
    }

    public static int countFilesInFolder(String folderPath) throws IOException {
        try {
            return (int) Files.list(Paths.get(folderPath)).count();
        } catch (IOException e) {
            // Handle IOException if needed
            e.printStackTrace();
            return -1; // or throw an exception
        }
    }

    public static void createFolder(String folderPath) {
        Path path = Paths.get(folderPath);

        try {
            // 创建文件夹（如果不存在）
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文件数据复制到指定文件夹下的指定路径
     * @param fileData 文件数据
     * @param targetFolderPath 指定文件夹
     * @param targetFileName 指定路径
     */
    public static void copyFileToFolder(byte[] fileData, String targetFolderPath, String targetFileName) throws Exception {
            // 如果目标文件夹不存在，则先创建
            File targetFolder = new File(targetFolderPath);
            if (!targetFolder.exists()) {
                boolean folderCreated = targetFolder.mkdirs();
                if (!folderCreated) {
                    throw new Exception("无法创建目标文件夹：" + targetFolderPath);
                }
            }

            // 创建目标文件
            File targetFile = new File(targetFolder, targetFileName);

            try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(targetFile))) {
                fos.write(fileData);
            }
        }
        //删除指定文件夹下的所有文件
        public static void deleteAllFilesInFolder(String folderPath) {
            File folder = new File(folderPath);
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        file.delete();
                    }
                }
            }
        }
    }
