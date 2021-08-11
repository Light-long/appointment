package com.terninus.sms.controller;

import com.terminus.commonutil.result.Result;
import com.terninus.sms.service.SmsService;
import com.terninus.sms.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author long
 */
@RestController
@RequestMapping("/api/sms")
@Slf4j
public class SmsController {

    @Resource
    private SmsService service;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/send/{phone}")
    public Result sendSms(@PathVariable(value = "phone") String phone) {
        // 从redis获取验证码，如果获取获取到，返回ok
        // key 手机号  value 验证码
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return Result.ok();
        }
        // 如果从redis获取不到
        // 生成验证码
        code = RandomUtil.getSixBitRandom();
        log.info("验证码：" + code);
        boolean isSuccess = service.send(phone, code);
        //生成验证码放到redis里面，设置有效时间
        if (isSuccess) {
            redisTemplate.opsForValue().set(phone, code, 10, TimeUnit.MINUTES);
            return Result.ok();
        } else {
            return Result.fail().message("生成验证码失败");
        }
    }


}
