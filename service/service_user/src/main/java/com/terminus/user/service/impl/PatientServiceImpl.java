package com.terminus.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.terminus.model.enums.DictEnum;
import com.terminus.model.model.user.Patient;
import com.terminus.user.client.DictClient;
import com.terminus.user.mapper.PatientMapper;
import com.terminus.user.service.PatientService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Resource
    private PatientMapper patientMapper;
    @Resource
    private DictClient dictClient;

    /**
     * 根据用户id获取就诊人列表（一个用户可以绑定多个就诊人）
     */
    @Override
    public List<Patient> findPatientListByUserId(Long userId) {
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        List<Patient> patientList = patientMapper.selectList(wrapper.eq("user_id", userId));
        // 远程调用，将数据中的编码（省市区的编码）转换为 名字
        patientList.stream().forEach(patient -> {
            this.convertCodeToName(patient);
        });
        return patientList;
    }

    private void convertCodeToName(Patient patient) {
        // 根据证件类型编码，获取证件类型具体值
        String certificationTypeString = dictClient.getNameByDictCodeAndValue(
                DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());
        // 联系人证件类型
        String contactsCertificatesTypeString =
                dictClient.getNameByDictCodeAndValue(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getContactsCertificatesType());
        //省
        String provinceString = dictClient.getNameByValue(patient.getProvinceCode());
        //市
        String cityString = dictClient.getNameByValue(patient.getCityCode());
        //区
        String districtString = dictClient.getNameByValue(patient.getDistrictCode());
        // 放到附加信息中
        patient.getParam().put("certificatesTypeString", certificationTypeString);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());
    }
}
