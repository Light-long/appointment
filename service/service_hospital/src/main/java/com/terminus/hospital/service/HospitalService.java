package com.terminus.hospital.service;

import com.terminus.model.model.hosp.Hospital;

import java.util.Map;

public interface HospitalService {

    /**
     * 上传医院信息
     */
    void save(Map<String, Object> paramMap);

    /**
     * 根据hoscode获取医院信息
     * @param hoscode 医院编码
     * @return hospital
     */
    Hospital getHospitalByHoscode(String hoscode);
}
