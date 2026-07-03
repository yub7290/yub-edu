package com.yub.edu.biz.controller;

import com.yub.edu.biz.service.FileUploadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @Author: bing.yu
 * @CreateTime: 2026-07-01
 * @Description: 文件上传 Controller 测试
 * @Version: 1.0.0
 */
class FileUploadControllerTest {

    /**
     * 文件上传服务
     */
    private FileUploadService fileUploadService;

    /**
     * 被测 Controller
     */
    private FileUploadController controller;

    @BeforeEach
    void setUp() {
        fileUploadService = mock(FileUploadService.class);
        controller = new FileUploadController(fileUploadService);
    }

    @Test
    void uploadVideoUsesQiniuUploadWithoutLocalFallback() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(fileUploadService.uploadToQiniu(file, "edu/editor"))
                .thenReturn("https://cdn.example.com/edu/editor/lesson.mp4");

        var response = controller.uploadVideo(file, "edu/editor");

        assertThat(response.getData()).isEqualTo("https://cdn.example.com/edu/editor/lesson.mp4");
        verify(fileUploadService).validateVideo(file);
        verify(fileUploadService).uploadToQiniu(file, "edu/editor");
    }
}
