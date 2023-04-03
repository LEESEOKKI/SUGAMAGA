package com.tayobus.sugamaga.api.controller;


import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/file")
@Api(tags = "파일 API")
public class FileController {
    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    public FileController() {
        File file = null;

        List<String> folderList = new ArrayList<>();
        folderList.add("/images/");
        folderList.add("/notice/");

        for (String folder : folderList) {
            String filePath = fileDirectory + folder;
            file = new File(filePath);
            logger.info(file.toString());

            if (!file.exists()) {
                file.mkdirs();
            }
        }

    }

    @GetMapping("/download")
    public ResponseEntity<?> download(@RequestParam String filename) {
        String path = "./release_file/" + filename;
        logger.info("file path - " + path);
        logger.info("root path now - " + Paths.get("").toAbsolutePath());

        File dir = new File("/");

        String[] filenames = dir.list();
        for (String files : filenames) {
            logger.info("filename : " + files);
        }

        try {
            Path filePath = Paths.get(path);
            Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기

            File file = new File(path);
            String contentType = Files.probeContentType(filePath);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);

            // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더
            headers
                    .setContentDisposition(
                            ContentDisposition.builder("attachment")
                                    .filename(file.getName()).build());

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch(Exception e) {
            logger.warn("file download error - ", e);

            return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
        }
    }
}
