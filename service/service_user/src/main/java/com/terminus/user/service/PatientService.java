package com.terminus.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.terminus.model.model.user.Patient;

import java.util.List;

public interface PatientService extends IService<Patient> {
    /**
     * 根据用户id获取就诊人列表（一个用户可以绑定多个就诊人）
     */
    List<Patient> findPatientListByUserId(Long userId);
}
