package com.terminus.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.terminus.cmn.mapper.DictMapper;
import com.terminus.model.model.cmn.Dict;
import com.terminus.model.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;

public class DictExcelListener extends AnalysisEventListener<DictEeVo> {

    private DictMapper dictMapper;

    public DictExcelListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    // 一行一行读取数据
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo, dict);
//        dict.setIsDeleted(0);
        // 插入数据库
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
