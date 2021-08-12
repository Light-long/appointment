package com.terminus.oss.controller;

import com.terminus.commonutil.result.Result;
import com.terminus.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/oss/file")
@Api(description = "文件管理")
public class OssApiController {

    @Resource
    private OssService ossService;

    @PostMapping("/fileUpload")
    @ApiOperation(value = "文件上传")
    public Result fileUpload(MultipartFile file) {
        String url = ossService.uploadFile(file);
        return Result.ok(url);
    }


}
