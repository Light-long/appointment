package com.terminus.hospital.controller;

import com.terminus.commonutil.result.Result;
import com.terminus.hospital.service.ScheduleService;
import com.terminus.model.model.hosp.Schedule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@Slf4j
@Api(description = "排班信息管理")
@RequestMapping("/admin/hospital/schedule")
public class ScheduleController {

    @Resource
    private ScheduleService scheduleService;

    @ApiOperation(value = "根据医院编号和科室编号查询排班规则")
    @GetMapping("/getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getSchedule(@PathVariable(value = "page") Integer page,
                              @PathVariable(value = "limit") Integer limit,
                              @PathVariable(value = "hoscode") String hoscode,
                              @PathVariable(value = "depcode") String depcode) {
        Map<String, Object> scheduleList = scheduleService.getScheduleByHoscodeAndDepcode(page, limit, hoscode, depcode);
        return Result.ok(scheduleList);
    }

    @ApiOperation(value = "根据医院编号，科室编号，日期，获取详细排班信息")
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable(value = "hoscode") String hoscode,
                              @PathVariable(value = "depcode") String depcode,
                              @PathVariable(value = "workDate") String workDate) {
        List<Schedule> scheduleDetailList =scheduleService.getSchduleDetail(hoscode, depcode, workDate);
        return Result.ok(scheduleDetailList);
    }
}
