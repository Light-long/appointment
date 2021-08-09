package com.terminus.hospital.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.terminus.commonutil.result.Result;
import com.terminus.hospital.service.HospitalSetService;
import com.terminus.model.model.hosp.HospitalSet;
import com.terminus.model.vo.hosp.HospitalSetQueryVo;
import com.terminus.serviceutil.util.MD5;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/admin/hospital/hospitalSet")
@RequiredArgsConstructor
@Api(description = "医院设置管理")
public class HospitalSetController {

    private final HospitalSetService hospitalSetService;

    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("/findAll")
    public Result findAll() {
        List<HospitalSet> hospitalSetList = hospitalSetService.list();
        return Result.ok(hospitalSetList);
    }

    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("/{id}")
    public Result removeHospSet(@PathVariable(value = "id") Long id) {
        boolean flag = hospitalSetService.removeById(id);
        return flag ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "条件分页查询")
    @PostMapping("/findPageByCondition/{current}/{limit}")
    public Result findPageByCondition(@PathVariable(value = "current") long current,
                                      @PathVariable(value = "limit") long limit,
                                      @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        // 构建分页条件(当前页，每页记录数)
        Page<HospitalSet> page = new Page<>(current, limit);
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        // 获取查询条件
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        if (!StringUtils.isEmpty(hosname)) {
            queryWrapper.like("hosname", hosname);
        }
        if (!StringUtils.isEmpty(hoscode)) {
            queryWrapper.eq("hoscode", hoscode);
        }
        // 实现分页查询
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, queryWrapper);
        return Result.ok(pageHospitalSet);
    }

    @ApiOperation(value = "添加医院设置")
    @PostMapping("/saveHospitalSet")
    public Result saveHospitalSet(@RequestBody(required = true) HospitalSet hospitalSet) {
        // 设置状态
        hospitalSet.setStatus(1);
        // 设置sign
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(
                System.currentTimeMillis() + "" + random.nextInt(100)));
        boolean flag = hospitalSetService.save(hospitalSet);
        return flag ? Result.ok() :Result.fail();
    }

    @ApiOperation(value = "根据id获取医院设置")
    @GetMapping("/{id}")
    public Result getHospitalById(@PathVariable(value = "id") Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return hospitalSet == null ? Result.fail() : Result.ok(hospitalSet);
    }

    @ApiOperation(value = "修改医院设置")
    @PostMapping("/updateHospitalSet")
    public Result updateHospitalSet(@RequestBody(required = true) HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.updateById(hospitalSet);
        return flag ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "批量删除医院设置")
    @DeleteMapping("/batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> ids) {
        boolean flag = hospitalSetService.removeByIds(ids);
        return flag ? Result.ok() : Result.fail();
    }

    @ApiOperation(value = "根据id解锁或锁定医院")
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable(value = "id") Long id,
                                  @PathVariable(value = "status") Integer status) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        if (Objects.nonNull(hospitalSet)) {
            hospitalSet.setStatus(status);
            boolean flag = hospitalSetService.updateById(hospitalSet);
            return flag ? Result.ok() : Result.fail();
        }
        return Result.fail();
    }

    @ApiOperation(value = "发送签名秘钥")
    @PutMapping("/sendSignKey/{id}")
    public Result sendSignKey(@PathVariable(value = "id") Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        if (Objects.nonNull(hospitalSet)) {
            String hosname = hospitalSet.getHosname();
            String hoscode = hospitalSet.getHoscode();
            // TODO 发送短信
        }
        return Result.fail();
    }
}
