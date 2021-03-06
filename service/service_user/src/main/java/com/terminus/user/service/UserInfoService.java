package com.terminus.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.terminus.model.model.user.UserInfo;
import com.terminus.model.vo.user.LoginVo;
import com.terminus.model.vo.user.UserAuthVo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {

    /**
     * 根据手机号和验证码登录
     */
    Map<String, Object> loginUser(LoginVo loginVo);

    /**
     * 用户认证
     */
    void userAuth(Long userId, UserAuthVo userAuthVo);
}