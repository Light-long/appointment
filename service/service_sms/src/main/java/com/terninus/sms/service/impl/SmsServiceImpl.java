package com.terninus.sms.service.impl;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.terninus.sms.service.SmsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author long
 */
@Service
public class SmsServiceImpl implements SmsService {

    @Override
    public boolean send(String phone, String code) {
        // 生产环境请求地址：app.cloopen.com
        String serverIp = "app.cloopen.com";
        //请求端口
        String serverPort = "8883";
        //主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
        String accountSId = "8aaf07087a331dc7017b3327c9b64a15";
        String accountToken = "2831848f198f4e1f84bc9b9f93549ae2";
        //请使用管理控制台中已创建应用的APPID
        String appId = "8aaf07087a331dc7017b3327cab74a1c";
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSId, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);
        String to = "19809407955";

        String templateId= "1";
        String[] datas = {code, "10"};
        HashMap<String, Object> result = sdk.sendTemplateSMS(to,templateId, datas);
        if("000000".equals(result.get("statusCode"))){
            return true;
        }else{
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
            return false;
        }
    }
}
