package com.terminus.cmn.controller;

import com.terminus.cmn.service.DictService;
import com.terminus.commonutil.result.Result;
import com.terminus.model.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/admin/cmn/dict")
@Api(description = "数据字典接口")
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;

    @ApiOperation(value = "根据id查询子数据列表")
    @GetMapping("/findChildData/{id}")
    public Result findChildData(@PathVariable(value = "id") Long id) {
        List<Dict> dictChildList = dictService.findChildData(id);
        return Result.ok(dictChildList).message("查询子数据列表成功");
    }

}
