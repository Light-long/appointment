package com.terminus.cmn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.terminus.cmn.mapper.DictMapper;
import com.terminus.cmn.service.DictService;
import com.terminus.model.model.cmn.Dict;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    private final DictMapper dictMapper;

    /**
     * 根据id查询子类数据集合
     * @param id parent_id
     * @return 子类数据
     */
    @Override
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        List<Dict> dictList = dictMapper.selectList(queryWrapper);
        // 循环判断每个子类是否还存在子类
        for (Dict dict : dictList) {
            Long dictId = dict.getId();
            boolean containsChild = isContainsChild(id);
            dict.setHasChildren(containsChild);
        }
        return dictList;
    }

    /**
     * 判断该分类是否存在子分类
     */
    private boolean isContainsChild(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        Integer count = dictMapper.selectCount(wrapper);
        return count > 0;
    }
}
