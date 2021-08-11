package com.terminus.user.controller;

import com.terminus.commonutil.result.Result;
import com.terminus.model.vo.user.LoginVo;
import com.terminus.user.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author long
 */
@RestController
@RequestMapping("/api/user")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation(value = "根据手机号和验证码登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo) {
        Map<String, Object> userInfo = userInfoService.loginUser(loginVo);
        return Result.ok(userInfo);
    }

}
