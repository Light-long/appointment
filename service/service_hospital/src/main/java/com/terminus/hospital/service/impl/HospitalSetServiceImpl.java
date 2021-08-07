package com.terminus.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.terminus.hospital.mapper.HospitalSetMapper;
import com.terminus.hospital.service.HospitalSetService;
import com.terminus.model.model.hosp.HospitalSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    private final HospitalSetMapper hospitalSetMapper;

    @Override
    public String getSignKeyByHoscode(String hoscode) {
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hoscode", hoscode);
        HospitalSet hospitalSet = hospitalSetMapper.selectOne(queryWrapper);
        return hospitalSet.getSignKey();
    }
}
