package cn.little.prince.oj.controller;

import cn.little.prince.oj.common.BaseResponse;
import cn.little.prince.oj.common.ErrorCode;
import cn.little.prince.oj.exception.BusinessException;
import cn.little.prince.oj.service.FileService;
import cn.little.prince.oj.utils.ResultUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 文件上传
 *
 * @author 349807102
 */
@RestController
@RequestMapping("/file")
@CrossOrigin(originPatterns = {"http://localhost:8080"}, allowCredentials = "true")
public class FileController {
    @Resource
    private FileService ossService;

    /**
     * 上传头像
     *
     * @param file 文件对象
     * @return 返回上传到 oss 的路径
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadOssFile(@RequestPart("file") MultipartFile file) {
        // 获取上传的文件
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "上传文件为空");
        }
        // 返回上传到 oss 的路径
        String url = ossService.uploadFileAvatar(file);

        // 返回对象
        return ResultUtils.success(url);
    }
}
