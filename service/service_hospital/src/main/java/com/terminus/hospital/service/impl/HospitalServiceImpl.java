package com.terminus.hospital.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.terminus.hospital.feignclient.DictClient;
import com.terminus.hospital.repository.HospitalRepository;
import com.terminus.hospital.service.HospitalService;
import com.terminus.model.model.hosp.Hospital;
import com.terminus.model.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Resource
    private HospitalRepository hospitalRepository;
    @Resource
    private DictClient dictClient;

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
            hospital.setStatus(1);
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

    /**
     * 根据条件查询hospital信息
     * @param page 页数
     * @param limit 每页记录数
     * @param hospitalQueryVo 条件对象
     * @return 分页集合
     */
    @Override
    public Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        //0为第一页
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        // 创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写
        Example<Hospital> example = Example.of(hospital, matcher);
        Page<Hospital> pageList = hospitalRepository.findAll(example, pageable);
        // 远程调用，将数字-->具体的名字（等级编码 --> 等级）
        pageList.getContent().forEach(item -> {
            this.convert(item);
        });
        return pageList;
    }

    // 远程调用具体实现
    private void convert(Hospital hospital) {
        String hosTypeString = dictClient.getNameByDictCodeAndValue("Hostype", hospital.getHostype());
        hospital.getParam().put("hosTypeString", hosTypeString);
        String provinceString = dictClient.getNameByValue(hospital.getProvinceCode());
        String cityString = dictClient.getNameByValue(hospital.getCityCode());
        String districtString = dictClient.getNameByValue(hospital.getDistrictCode());
        hospital.getParam().put("fullAddress", provinceString+cityString+districtString);
    }

    /**
     * 修改上下线状态
     */
    @Override
    public void updateStatus(String id, Integer status) {
        // 根据id查询
        Hospital hospital = hospitalRepository.findById(id).get();
        // update
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        // save
        hospitalRepository.save(hospital);
    }

    /**
     * 获取医院详细信息
     * @param id
     * @return
     */
    @Override
    public Hospital showHospitalDetail(String id) {
        Hospital hospital = hospitalRepository.findById(id).get();
        // 填充dict信息
        this.convert(hospital);
        return hospital;
    }


}
