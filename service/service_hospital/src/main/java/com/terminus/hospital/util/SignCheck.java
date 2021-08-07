package com.terminus.hospital.util;

import com.terminus.hospital.service.HospitalSetService;
import com.terminus.serviceutil.helper.HttpRequestHelper;
import com.terminus.serviceutil.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class SignCheck {

    @Autowired
    private HospitalSetService hospitalSetService;
    
    public boolean check(HttpServletRequest request) {
        // 获取请求信息
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        // 获取签名和数据库中的签名比较
        String sign = (String) paramMap.get("sign");
        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKeyByHoscode(hoscode);
        String md5Key = MD5.encrypt(signKey);
        return sign.equals(md5Key);
    }
}
