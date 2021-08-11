package com.terminus.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.terminus.commonutil.helper.JWTHelper;
import com.terminus.commonutil.result.ResultCodeEnum;
import com.terminus.model.model.user.UserInfo;
import com.terminus.model.vo.user.LoginVo;
import com.terminus.serviceutil.exception.AppointmentException;
import com.terminus.user.mapper.UserInfoMapper;
import com.terminus.user.service.UserInfoService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 根据手机号和验证码登录
     */
    @Override
    public Map<String, Object> loginUser(LoginVo loginVo) {
        String phone = loginVo.getPhone();
        // 验证码
        String code = loginVo.getCode();
        // 校验参数
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new AppointmentException(ResultCodeEnum.PARAM_ERROR);
        }
        // 校验校验验证码
        String mobileCode = (String) redisTemplate.opsForValue().get(phone);
        if (!Objects.equals(mobileCode, code)) {
            throw new AppointmentException(ResultCodeEnum.CODE_ERROR);
        }
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        UserInfo user = userInfoMapper.selectOne(wrapper.eq("phone", phone));
        // 第一次登录，注册
        if (!Objects.nonNull(user)) {
            user = new UserInfo();
            user.setName("");
            user.setPhone(phone);
            user.setStatus(1);
            userInfoMapper.insert(user);
        }
        // 手机号已经存在，直接登录
        // 判断状态（是否被禁用）
        if (user.getStatus() == 0) {
            throw new AppointmentException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        // 页面返回结果
        Map<String, Object> result = new HashMap<>();
        String name = user.getName();
        if (StringUtils.isEmpty(name)) {
            name = user.getNickName();
        }
        // 如果name还为空，就是用电话号码
        if (StringUtils.isEmpty(name)) {
            name = user.getPhone();
        }
        result.put("name", name);
        // 生成token信息
        String token = JWTHelper.createToken(user.getId(), name);
        result.put("token", token);
        return result;
    }
}
