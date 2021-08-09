package com.terminus.cmn.controller;

import com.terminus.cmn.service.DictService;
import com.terminus.commonutil.result.Result;
import com.terminus.model.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
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

    @ApiOperation(value = "导出字典数据")
    @GetMapping("/exportDict")
    public void exportDict(HttpServletResponse response) {
        dictService.exportDict(response);
    }

    @ApiOperation(value = "导入数据字典")
    @PostMapping("/importDict")
    public Result importDict(MultipartFile file) {
        dictService.importDict(file);
        return Result.ok();
    }

    @ApiOperation(value = "根据dict_value获取dict_name")
    @GetMapping("/getName/{value}")
    public String getNameByValue(@PathVariable(value = "value") String value) {
        return dictService.getNameByValue(value);
    }

    @ApiOperation(value = "根据dict_code和value获取dict_name")
    @GetMapping("/getName/{dictCode}/{value}")
    public String getNameByDictCodeAndValue(@PathVariable(value = "dictCode") String dictCode,
                                            @PathVariable(value = "value") String value) {
        return dictService.getNameByDictCodeAndValue(dictCode, value);
    }

    @ApiOperation(value = "根据dictCode获取子集合")
    @GetMapping("/getChildByDictCode/{dictCode}")
    public Result getChildByDictCode(@PathVariable(value = "dictCode") String dictCode) {
        List<Dict> dictList = dictService.getChildByDictCode(dictCode);
        return Result.ok(dictList);
    }

}
