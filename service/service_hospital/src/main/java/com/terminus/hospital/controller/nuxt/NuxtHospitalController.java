package com.terminus.hospital.controller.nuxt;

import com.terminus.commonutil.result.Result;
import com.terminus.hospital.service.HospitalService;
import com.terminus.model.model.hosp.Hospital;
import com.terminus.model.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/nuxt/hospital/hospital")
@Api(description = "医院前台管理接口")
public class NuxtHospitalController {

    @Resource
    private HospitalService hospitalService;

    @ApiOperation(value = "分页条件查询")
    @PostMapping("/getPageCondition/{page}/{limit}")
    public Result getPageCondition(@PathVariable(value = "page") Integer page,
                                   @PathVariable(value = "limit") Integer limit,
                                   @RequestBody(required = false) HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> pageModel = hospitalService.selectPage(page, limit, hospitalQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "根据医院名称获取医院列表")
    @GetMapping("/findByHosname/{hosname}")
    public Result findByHosname(
            @ApiParam(name = "hosname", value = "医院名称", required = true)
            @PathVariable(value = "hosname") String hosname) {
        return Result.ok(hospitalService.findByHosname(hosname));
    }

    @ApiOperation(value = "医院预约挂号详情")
    @GetMapping("{hoscode}")
    public Result bookingDetail( @ApiParam(name = "hoscode", value = "医院code", required = true)
                                     @PathVariable String hoscode) {
        return Result.ok(hospitalService.scheduleDetail(hoscode));
    }

}
