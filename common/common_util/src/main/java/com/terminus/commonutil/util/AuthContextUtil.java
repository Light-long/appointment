package com.terminus.commonutil.util;

import com.terminus.commonutil.helper.JWTHelper;

import javax.servlet.http.HttpServletRequest;

// 获取当前用户信息工具类
public class AuthContextUtil {

    // 获取当前用户id
    public static Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("token");
        return JWTHelper.getUserId(token);
    }

    // 获取当前用户name
    public static String getUserName(HttpServletRequest request) {
        String token = request.getHeader("token");
        return JWTHelper.getUserName(token);
    }

}
