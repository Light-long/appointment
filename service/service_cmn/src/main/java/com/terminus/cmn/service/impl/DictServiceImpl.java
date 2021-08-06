package com.terminus.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.terminus.cmn.listener.DictExcelListener;
import com.terminus.cmn.mapper.DictMapper;
import com.terminus.cmn.service.DictService;
import com.terminus.model.model.cmn.Dict;
import com.terminus.model.vo.cmn.DictEeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    private final DictMapper dictMapper;

    /**
     * 根据id查询子类数据集合
     * 使用缓存将第一次查的数据放入缓存
     * @param id parent_id
     * @return 子类数据
     */
    @Override
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        List<Dict> dictList = dictMapper.selectList(queryWrapper);
        // 循环判断每个子类是否还存在子类
        for (Dict dict : dictList) {
            Long dictId = dict.getId();
            boolean containsChild = isContainsChild(dictId);
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

    /**
     * 导出数据字典
     */
    @Override
    public void exportDict(HttpServletResponse response) {
        // 设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
        // 查询数据库
        List<Dict> dictList = dictMapper.selectList(null);
        // dict --> dictVO
        List<DictEeVo> dictEeVoList = new ArrayList<>(dictList.size());
        for (Dict dict : dictList) {
            DictEeVo vo = new DictEeVo();
            BeanUtils.copyProperties(dict, vo);
            dictEeVoList.add(vo);
        }
        // 调用方法写操作
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, DictEeVo.class).sheet("dict")
                    .doWrite(dictEeVoList);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 导入dict
     * 导入数据后清空所有缓存
     * @param file dict文件
     */
    @Override
    @CacheEvict(value = "dict", allEntries = true)
    public void importDict(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictExcelListener(dictMapper))
                .sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
