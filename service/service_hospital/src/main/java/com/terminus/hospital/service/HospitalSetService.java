package com.terminus.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.terminus.model.model.hosp.HospitalSet;

public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKeyByHoscode(String hoscode);
}
