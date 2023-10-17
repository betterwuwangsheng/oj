package cn.little.prince.oj.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件
 *
 * @author 349807102
 */
public interface FileService {
    /**
     * 上传头像到 OSS
     *
     * @param file 文件
     * @return 存储位置
     */
    String uploadFileAvatar(MultipartFile file);
}
