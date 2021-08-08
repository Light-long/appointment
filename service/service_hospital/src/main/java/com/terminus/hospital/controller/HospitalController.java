package com.terminus.hospital.controller;

import com.terminus.commonutil.result.Result;
import com.terminus.hospital.service.HospitalService;
import com.terminus.model.model.hosp.Hospital;
import com.terminus.model.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@RequestMapping("/admin/hospital/hospital")
@Slf4j
@Api(description = "医院管理接口")
public class HospitalController {

    @Resource
    private HospitalService hospitalService;

    @ApiOperation(value = "根据条件分页查询医院信息")
    @PostMapping("/list/{page}/{limit}")
    public Result getHospitalList(@PathVariable(value = "page") Integer page,
                                  @PathVariable(value = "limit") Integer limit,
                                  @RequestBody(required = false) HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitalPage = hospitalService.selectPage(page, limit, hospitalQueryVo);
        log.info("hospital page count :" + hospitalPage.getTotalElements());
        return Result.ok(hospitalPage);
    }

    @ApiOperation(value = "医院上线、下线")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateHospitalStatus(@PathVariable(value = "id") String id,
                                       @PathVariable(value = "status") Integer status) {
        hospitalService.updateStatus(id, status);
        return Result.ok();
    }

    @ApiOperation(value = "医院详细信息")
    @GetMapping("/showDetail/{id}")
    public Result showHospitalDetail(@PathVariable(value = "id") String id) {
        Hospital hospital = hospitalService.showHospitalDetail(id);
        return Result.ok(hospital);
    }
}
