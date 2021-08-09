package com.terminus.hospital.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.terminus.commonutil.result.ResultCodeEnum;
import com.terminus.hospital.repository.DepartmentRepository;
import com.terminus.hospital.service.DepartmentService;
import com.terminus.model.model.hosp.Department;
import com.terminus.model.vo.hosp.DepartmentQueryVo;
import com.terminus.model.vo.hosp.DepartmentVo;
import com.terminus.serviceutil.exception.AppointmentException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Resource
    private DepartmentRepository departmentRepository;


    @Override
    public void save(Map<String, Object> paramMap) {
        String departmentString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(departmentString, Department.class);
        // 根据医院编号 和 科室编号 查询
        Department targetDepartment = departmentRepository.getDepartmentByHoscodeAndDepcode(
                department.getHoscode(), department.getDepcode());
        if (targetDepartment != null) {
            targetDepartment.setUpdateTime(new Date());
            targetDepartment.setIsDeleted(0);
            departmentRepository.save(targetDepartment);
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    /**
     * 根据条件分页查询department
     * @param page 当前页码
     * @param limit 每页记录数
     * @param departmentQueryVo 查询条件
     * @return page
     */
    @Override
    public Page<Department> selectPage(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        // 0 为第一页
        Pageable pageable = PageRequest.of(page-1, limit, sort);
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        department.setIsDeleted(0);
        // 创建匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                // 模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                // 忽略大小写
                .withIgnoreCase(true);
        Example<Department> example = Example.of(department, exampleMatcher);
        return departmentRepository.findAll(example, pageable);
    }

    /**
     * 根据 医院编号 和 科室编号 删除科室
     * @param hoscode 医院编号
     * @param depcode 科室编号
     */
    @Override
    public void delete(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            departmentRepository.deleteById(department.getId());
        }
    }

    /**
     * 查询科室编号（树形）
     */
    @Override
    public List<DepartmentVo> getDetailTree(String hoscode) {
        // 返回集合
        List<DepartmentVo> result = new ArrayList<>();
        // 根据医院编号查出所有科室信息
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        Example<Department> example = Example.of(departmentQuery);
        List<Department> allDepartment = departmentRepository.findAll(example);
        // 根据bigcode（大科室）分组
        Map<String, List<Department>> groupDepartment = allDepartment.stream()
                .collect(Collectors.groupingBy(Department::getBigcode));
        // 移除全部科室标签
        groupDepartment.remove("44f162029abb45f9ff0a5f743da0650d");
        for (String bigCode : groupDepartment.keySet()) {
            // 获取所有的对应的小科室
            List<Department> smallDepartments = groupDepartment.get(bigCode);
            //根据大科室编号  bigcode 分组，获取每个大科室里面下级子科室
            DepartmentVo departmentVo = new DepartmentVo();
            // 封装大科室数据
            departmentVo.setDepcode(bigCode);
            departmentVo.setDepname(smallDepartments.get(0).getBigname());
            // 封装小科室数据
            List<DepartmentVo> child = new ArrayList<>();
            for (Department smallDepartment : smallDepartments) {
                DepartmentVo smallVo = new DepartmentVo();
                smallVo.setDepcode(smallDepartment.getDepcode());
                smallVo.setDepname(smallDepartment.getDepname());
                child.add(smallVo);
            }
            departmentVo.setChildren(child);
            result.add(departmentVo);
        }
        return result;
    }

    @Override
    public String getDepartmentName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            return department.getDepname();
        }
        throw new AppointmentException("hoscode和depcode错误", 404);
    }
}
