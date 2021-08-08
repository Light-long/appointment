package com.terminus.hospital.service;

import com.terminus.model.model.hosp.Hospital;
import com.terminus.model.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

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

    /**
     * 根据条件查询hospital信息
     * @param page 页数
     * @param limit 每页记录数
     * @param hospitalQueryVo 条件对象
     * @return 分页集合
     */
    Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    /**
     * 修改上下线状态
     */
    void updateStatus(String id, Integer status);

    Hospital showHospitalDetail(String id);
}
