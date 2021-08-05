package com.terminus.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.terminus.hospital.mapper.HospitalSetMapper;
import com.terminus.hospital.service.HospitalSetService;
import com.terminus.model.model.hosp.HospitalSet;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    private final HospitalSetMapper hospitalSetMapper;
}
