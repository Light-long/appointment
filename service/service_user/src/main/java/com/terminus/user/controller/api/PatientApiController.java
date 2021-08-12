package com.terminus.user.controller.api;

import com.terminus.commonutil.result.Result;
import com.terminus.commonutil.util.AuthContextUtil;
import com.terminus.model.model.user.Patient;
import com.terminus.user.service.PatientService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/user/patient")
public class PatientApiController {

    @Resource
    private PatientService patientService;

    // 获取用户绑定的patient列表
    @GetMapping("/auth/findListByUserId")
    public Result findListByUserId(HttpServletRequest request) {
        // 获取当前用户id
        Long userId = AuthContextUtil.getUserId(request);
        // 根据用户id查询 就诊人列表
        List<Patient> patientList = patientService.findPatientListByUserId(userId);
        return Result.ok(patientList);
    }

    // 添加就诊人
    @PostMapping("/auth/save")
    public Result savePatient(@RequestBody Patient patient, HttpServletRequest request) {
        // 获取当前登录用户id
        Long userId = AuthContextUtil.getUserId(request);
        patient.setUserId(userId);
        boolean isSuccess = patientService.save(patient);
        return isSuccess ? Result.ok() : Result.fail();
    }

    // 根据id获取就诊人详细信息
    @GetMapping("/auth/getById/{id}")
    public Result getPatientById(@PathVariable(value = "id") Long id) {
        Patient patient = patientService.getById(id);
        return Result.ok(patient);
    }

    // 修改就诊人信息
    @PostMapping("/auth/update")
    public Result updatePatient(@RequestBody Patient patient) {
        boolean isSuccess = patientService.updateById(patient);
        return isSuccess ? Result.ok() : Result.fail();
    }

    // 删除就诊人
    @DeleteMapping("auth/remove/{id}")
    public Result removePatient(@PathVariable(value = "id") Long id) {
        boolean isSuccess = patientService.removeById(id);
        return isSuccess ? Result.ok() : Result.fail();
    }
}
