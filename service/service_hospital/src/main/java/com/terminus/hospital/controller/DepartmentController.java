package com.terminus.hospital.controller;

import com.terminus.commonutil.result.Result;
import com.terminus.hospital.service.DepartmentService;
import com.terminus.model.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/admin/hospital/department")
@Api(description = "科室管理")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    @ApiOperation(value = "根据医院编号查询所有科室（树形）")
    @GetMapping("/getDepartmentDetailTree/{hoscode}")
    public Result getDepartmentDetailTree(@PathVariable(value = "hoscode") String hoscode) {
        List<DepartmentVo> departmentVoList = departmentService.getDetailTree(hoscode);
        return Result.ok(departmentVoList);
    }
}
