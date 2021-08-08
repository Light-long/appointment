package com.terminus.hospital.controller.api;

import com.terminus.commonutil.result.Result;
import com.terminus.commonutil.result.ResultCodeEnum;
import com.terminus.hospital.service.DepartmentService;
import com.terminus.hospital.service.HospitalService;
import com.terminus.hospital.service.ScheduleService;
import com.terminus.hospital.util.SignCheck;
import com.terminus.model.model.hosp.Department;
import com.terminus.model.model.hosp.Hospital;
import com.terminus.model.model.hosp.Schedule;
import com.terminus.model.vo.hosp.DepartmentQueryVo;
import com.terminus.model.vo.hosp.ScheduleQueryVo;
import com.terminus.serviceutil.exception.AppointmentException;
import com.terminus.serviceutil.helper.HttpRequestHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
@Api(description = "医院管理API接口")
public class HospitalApiController {

    @Resource
    private HospitalService hospitalService;
    @Resource
    private SignCheck signCheck;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private ScheduleService scheduleService;

    @ApiOperation(value = "上传医院")
    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request) {
        // 获取请求信息
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String) paramMap.get("hoscode");
        if (!signCheck.check(request)) {
            throw new AppointmentException(ResultCodeEnum.SIGN_ERROR);
        } else {
            // 修改logo数据
            String logoData = (String) paramMap.get("logoData");
            logoData = logoData.replaceAll(" ", "+");
            paramMap.put("logoData", logoData);
            hospitalService.save(paramMap);
        }
        return Result.ok();
    }

    @ApiOperation(value = "根据id获取医院信息")
    @PostMapping("/hospital/show")
    public Result getHospital(HttpServletRequest request) {
        // 获取请求信息
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        String hoscode = (String) paramMap.get("hoscode");
        if (!signCheck.check(request)) {
            throw new AppointmentException(ResultCodeEnum.SIGN_ERROR);
        } else {
            Hospital hospital = hospitalService.getHospitalByHoscode(hoscode);
            return Result.ok(hospital);
        }
    }

    @ApiOperation(value = "上传科室信息")
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(map);
        // 校验签名
        if (!signCheck.check(request)) {
            throw new AppointmentException(ResultCodeEnum.SIGN_ERROR);
        } else {
            departmentService.save(paramMap);
            return Result.ok();
        }
    }

    @ApiOperation(value = "获取科室列表")
    @PostMapping("/department/list")
    public Result getDepartmentList(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(map);
        // 参数校验
        String hoscode = (String) paramMap.get("hoscode");
        // 默认第1页，每页10条数据
        int page = StringUtils.isEmpty(paramMap.get("page"))
                ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit"))
                ? 10 : Integer.parseInt((String) paramMap.get("limit"));
        if (StringUtils.isEmpty(hoscode)) {
            throw new AppointmentException(ResultCodeEnum.PARAM_ERROR);
        }
        // 签名校验
        if (!signCheck.check(request)) {
            throw new AppointmentException(ResultCodeEnum.SIGN_ERROR);
        } else {
            DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
            departmentQueryVo.setHoscode(hoscode);
            Page<Department> pageModel = departmentService.selectPage(page, limit, departmentQueryVo);
            return Result.ok(pageModel);
        }
    }

    @ApiOperation(value = "删除科室信息")
    @PostMapping("/department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(map);
        // 获取参数
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");
        if (!signCheck.check(request)) {
            throw new AppointmentException(ResultCodeEnum.SIGN_ERROR);
        } else {
            departmentService.delete(hoscode, depcode);
            return Result.ok();
        }
    }

    @ApiOperation(value = "上传排班信息")
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(map);
        // 参数校验
        if (StringUtils.isEmpty(paramMap.get("hoscode"))) {
            throw new AppointmentException(ResultCodeEnum.PARAM_ERROR);
        }
        if (!signCheck.check(request)) {
            throw new AppointmentException(ResultCodeEnum.SIGN_ERROR);
        } else {
            scheduleService.save(paramMap);
            return Result.ok();
        }
    }

    @ApiOperation(value = "查询排班信息")
    @PostMapping("/schedule/list")
    public Result getScheduleList(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(map);
        //必须参数校验
        String hoscode = (String) paramMap.get("hoscode");
        //非必填
        String depcode = (String)paramMap.get("depcode");
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 10 : Integer.parseInt((String)paramMap.get("limit"));
        if(StringUtils.isEmpty(hoscode)) {
            throw new AppointmentException(ResultCodeEnum.PARAM_ERROR);
        }
        if (!signCheck.check(request)) {
            throw new AppointmentException(ResultCodeEnum.SIGN_ERROR);
        } else {
            ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
            scheduleQueryVo.setHoscode(hoscode);
            scheduleQueryVo.setDepcode(depcode);
            Page<Schedule> schedules = scheduleService.selectPage(page, limit, scheduleQueryVo);
            return Result.ok(schedules);
        }
    }

    @ApiOperation(value = "删除排班接口")
    @PostMapping("/schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(map);
        String hoscode = (String) paramMap.get("hoscode");
        String hosScheduleId = (String) paramMap.get("hosScheduleId");
        if(StringUtils.isEmpty(hoscode)) {
            throw new AppointmentException(ResultCodeEnum.PARAM_ERROR);
        }
        if(StringUtils.isEmpty(hosScheduleId)) {
            throw new AppointmentException(ResultCodeEnum.PARAM_ERROR);
        }
        if (!signCheck.check(request)) {
            throw new AppointmentException(ResultCodeEnum.SIGN_ERROR);
        } else {
            scheduleService.remove(hoscode, hosScheduleId);
            return Result.ok();
        }
    }

}
