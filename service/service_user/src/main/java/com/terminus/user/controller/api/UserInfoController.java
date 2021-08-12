package com.terminus.user.controller.api;

import com.terminus.commonutil.result.Result;
import com.terminus.commonutil.util.AuthContextUtil;
import com.terminus.model.model.user.UserInfo;
import com.terminus.model.vo.user.LoginVo;
import com.terminus.model.vo.user.UserAuthVo;
import com.terminus.user.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @ApiOperation(value = "用户认证")
    @PostMapping("auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo,
                           HttpServletRequest request) {
        Long userId = AuthContextUtil.getUserId(request);
        userInfoService.userAuth(userId, userAuthVo);
        return Result.ok();
    }

    @ApiOperation(value = "根据id获取用户信息")
    @GetMapping("/auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request) {
        Long userId = AuthContextUtil.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }

}
