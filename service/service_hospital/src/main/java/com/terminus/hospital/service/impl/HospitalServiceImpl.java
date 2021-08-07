package com.terminus.hospital.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.terminus.hospital.repository.HospitalRepository;
import com.terminus.hospital.service.HospitalService;
import com.terminus.model.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    /**
     * 上传医院信息
     * @param paramMap 医院信息
     */
    @Override
    public void save(Map<String, Object> paramMap) {
        // 转换为json-->hospital object
        String hosptialString  = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(hosptialString, Hospital.class);
        // 判断是否存在
        String hoscode = hospital.getHoscode();
        Hospital hospitalExists = hospitalRepository.findHospitalByHoscode(hoscode);
        // 存在，更新
        if (hospitalExists != null) {
            hospital.setStatus(hospitalExists.getStatus());
            hospital.setCreateTime(hospitalExists.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {
            // 不存在，添加
            //0：未上线 1：已上线
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }

    }

    @Override
    public Hospital getHospitalByHoscode(String hoscode) {
        return hospitalRepository.findHospitalByHoscode(hoscode);
    }
}
