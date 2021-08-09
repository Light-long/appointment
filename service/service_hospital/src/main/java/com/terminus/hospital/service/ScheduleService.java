package com.terminus.hospital.service;

import com.terminus.model.model.hosp.Schedule;
import com.terminus.model.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService {

    void save(Map<String, Object> paramMap);

    /**
     * 分页查询
     * @param page 当前页码
     * @param limit 每页记录数
     * @param scheduleQueryVo 查询条件
     */
    Page<Schedule> selectPage(Integer page, Integer limit, ScheduleQueryVo scheduleQueryVo);

    void remove(String hoscode, String hosScheduleId);

    /**
     * 根据医院编号 科室编号 获取 排班信息
     */
    Map<String, Object> getScheduleByHoscodeAndDepcode(Integer page, Integer limit, String hoscode, String depcode);

    List<Schedule> getSchduleDetail(String hoscode, String depcode, String workDate);
}
