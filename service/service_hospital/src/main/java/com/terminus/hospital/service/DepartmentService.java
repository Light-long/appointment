package com.terminus.hospital.service;

import com.terminus.model.model.hosp.Department;
import com.terminus.model.vo.hosp.DepartmentQueryVo;
import com.terminus.model.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DepartmentService {

    /**
     * 上传科室信息
     * @param paramMap 科室信息
     */
    void save(Map<String, Object> paramMap);

    /**
     * 分页查询
     * @param page 当前页码
     * @param limit 每页记录数
     * @param departmentQueryVo 查询条件
     * @return
     */
    Page<Department> selectPage(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo);

    /**
     * 根据 医院编号 和 科室编号 删除科室
     */
    void delete(String hoscode, String depcode);

    /**
     * 查询科室编号（树形）
     */
    List<DepartmentVo> getDetailTree(String hoscode);

    String getDepartmentName(String hoscode, String depcode);
}
