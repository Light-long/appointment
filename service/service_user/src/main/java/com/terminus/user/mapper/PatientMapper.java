package com.terminus.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.terminus.model.model.user.Patient;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PatientMapper extends BaseMapper<Patient> {
}
