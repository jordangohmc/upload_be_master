package com.mcr.application.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.mcr.application.domain.UploadResultVo;
import com.mcr.application.domain.body.DestinationFilePath;
import com.mcr.application.service.FileService;
import com.mcr.common.BaseContext;
import com.mcr.common.R;
import com.mcr.common.RandomStringGenerator;
import com.mcr.framework.config.ControllerMessageConverterConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Slf4j
@RestController
@Import(ControllerMessageConverterConfig.class)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@RequestMapping("/upload")
public class UploadController {
    @Value("${TEMP_MEDIA_PATH}")
    private String tempPath;
    @Resource
    private FileService fileServ;
    @Value("${appsConf.fileNameLength}")
    private Integer fileNameLength = 8;
    private static final List<String> ALLOWED_IMG_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png");
    private static final List<String> ALLOWED_VIDEO_EXTENSIONS = Arrays.asList(".mp4", ".mov", ".3gp", ".avi");

    private static final List<String> ALLOWED_FILE_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".txt");

    @PostMapping("/file")
    public R<UploadResultVo> uploadFile(@RequestParam("file") MultipartFile file) {
        return handleUpload(file, null, ALLOWED_FILE_EXTENSIONS, false, null);
    }

    @PostMapping("/photo")
    public R<UploadResultVo> uploadPhoto(@RequestParam("file") MultipartFile file) {
        return handleUpload(file, ".jpg", ALLOWED_IMG_EXTENSIONS, false, null);
    }

    //        return handlePhotoUpload(file, null);
    @PostMapping("/video")
    public R<UploadResultVo> uploadVideo(@RequestParam("file") MultipartFile file) {
        // setting get need Compress
        return handleUpload(file, ".mp4", ALLOWED_VIDEO_EXTENSIONS, true, null);
//        return handleUpload(file, ".mp4", ALLOWED_VIDEO_EXTENSIONS, false, null);
    }

    @PostMapping("/video-compress")
    public R<UploadResultVo> uploadVideoCompress(@RequestParam("file") MultipartFile file, @RequestBody DestinationFilePath targetPath) {
        return handleUpload(file, null, ALLOWED_VIDEO_EXTENSIONS, true, targetPath);
//        return handleVideoUpload(file, "video", true, targetPath);
    }

    @PostMapping("/compress-and-shift")
    public R<UploadResultVo> compressAndSaveAs(@RequestBody DestinationFilePath targetPath) {
        log.info(targetPath.toString());
        try {
            fileServ.compressionVideo(targetPath.getSourceFileId(), targetPath.getFileName(), targetPath.getDestinationPath());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        String originalFile = targetPath.getFileName();
        String fileExtension = null;
        int lastIndex = targetPath.getFileName().lastIndexOf('.');
        if (lastIndex > 0 && lastIndex < originalFile.length() - 1) {
            fileExtension = originalFile.substring(lastIndex + 1);
        }
        UploadResultVo result = new UploadResultVo();
        result.setFileSuffix(fileExtension);
        return R.success(result);
    }

    private R<UploadResultVo> handleUpload(MultipartFile file, String defaultExtension,
                                           List<String> allowedExtensions, boolean compress, DestinationFilePath targetPath) {

        String userId = BaseContext.getCurrentId().toString();
        String originalFileName = file.getOriginalFilename();
        UploadResultVo uploadResultVo = new UploadResultVo();
        String tempFileName;
        long fileSizeInMB = 0;

        if (originalFileName == null) return R.error("Original filename is null");

        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        if (defaultExtension != null) suffix = defaultExtension;

        uploadResultVo.setFileSuffix(suffix);

        if (!allowedExtensions.contains(suffix))
            return R.error("Unsupported file format. Supported formats are " + allowedExtensions);

        String randomString = RandomStringGenerator.generateRandomString(fileNameLength);
        tempFileName = "Temp_" + userId + "_" + randomString + suffix;
        uploadResultVo.setUploadId(tempFileName);

        try {
            file.transferTo(new File(tempPath + tempFileName));
            long fileSizeInBytes = file.getSize();
            fileSizeInMB = fileSizeInBytes / 1048576;
            log.info("Uploaded file size: {} bytes, {} KB, {} MB", fileSizeInBytes, fileSizeInBytes / 1024, fileSizeInMB);
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("Fail to upload file");
        }

        if (compress && fileSizeInMB >= 4 && allowedExtensions.equals(ALLOWED_VIDEO_EXTENSIONS)) {
            new Thread(() -> {
                try {
                    fileServ.compressionVideo(tempFileName, targetPath.getFileName(), targetPath.getDestinationPath());
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            }).start();
            uploadResultVo.setUploadId(tempFileName + "-compressed");
        }
        return R.success(uploadResultVo);
    }

}

//    private R<UploadResultVo> handlePhotoUpload(MultipartFile file, DestinationFilePath targetPath) {
//        String userId = BaseContext.getCurrentId().toString();
//        String originalFileName = file.getOriginalFilename();
//        UploadResultVo uploadResultVo = new UploadResultVo();
//        String fileName;
//        long fileSizeInMB = 0;
//
//        if (originalFileName == null) return R.error("Original filename is null");
//
//        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
//        uploadResultVo.setFileSuffix(suffix);
//
//        if (!ALLOWED_IMG_EXTENSIONS.contains(suffix)) {
//            return R.error("Unsupported photo format. Supported formats are .jpg, .jpeg, .png");
//        }
//
//        String randomString = RandomStringGenerator.generateRandomString(8);
//        fileName = "Temp_" + userId + "_" + randomString + ".jpg";
//        uploadResultVo.setUploadId(fileName);
//
//        try {
//            file.transferTo(new File(mediaTempPath + fileName));
//            long fileSizeInBytes = file.getSize();
//            fileSizeInMB = fileSizeInBytes / 1048576;
//            log.info("Uploaded file size: {} bytes, {} KB, {} MB", fileSizeInBytes, fileSizeInBytes / 1024, fileSizeInMB);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return R.error("Fail to upload image");
//        }
//
//        return R.success(uploadResultVo);
//    }
//
//    private R<UploadResultVo> handleVideoUpload(MultipartFile file, boolean compress, DestinationFilePath targetPath) {
//        String userId = BaseContext.getCurrentId().toString();
//        String originalFileName = file.getOriginalFilename();
//        UploadResultVo uploadResultVo = new UploadResultVo();
//        String fileName;
//        long fileSizeInMB = 0;
//
//        if (originalFileName == null) return R.error("Original filename is null");
//
//        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
//        uploadResultVo.setFileSuffix(suffix);
//
//        if (!ALLOWED_VIDEO_EXTENSIONS.contains(suffix)) {
//            return R.error("Unsupported video format. Supported formats are .mp4, .mov, .3gp, .avi");
//        }
//
//        String randomString = RandomStringGenerator.generateRandomString(8);
//        fileName = "Temp_" + userId + "_" + randomString + suffix;
//        uploadResultVo.setUploadId(fileName);
//
//        try {
//            file.transferTo(new File(mediaTempPath + fileName));
//            long fileSizeInBytes = file.getSize();
//            fileSizeInMB = fileSizeInBytes / 1048576;
//            log.info("Uploaded file size: {} bytes, {} KB, {} MB", fileSizeInBytes, fileSizeInBytes / 1024, fileSizeInMB);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return R.error("Fail to upload video");
//        }
//
//        if (compress && fileSizeInMB >= 4) {
//            new Thread(() -> {
//                try {
//                    fileServ.compressionVideo(fileName, targetPath.getFileName(), targetPath.getDestinationPath());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    log.error(e.getMessage());
//                }
//            }).start();
//            uploadResultVo.setUploadId(fileName + "-compressed");
//        }
//        return R.success(uploadResultVo);
//    }



