package com.terminus.hospital.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.terminus.hospital.repository.ScheduleRepository;
import com.terminus.hospital.service.DepartmentService;
import com.terminus.hospital.service.HospitalService;
import com.terminus.hospital.service.ScheduleService;
import com.terminus.model.model.hosp.Schedule;
import com.terminus.model.vo.hosp.BookingScheduleRuleVo;
import com.terminus.model.vo.hosp.ScheduleQueryVo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Resource
    private ScheduleRepository scheduleRepository;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private HospitalService hospitalService;
    @Resource
    private DepartmentService departmentService;

    /**
     * 上传排班信息
     */
    @Override
    public void save(Map<String, Object> paramMap) {
        String departmentString = JSONObject.toJSONString(paramMap);
        Schedule schedule = JSONObject.parseObject(departmentString, Schedule.class);
        Schedule targetSchedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        if (targetSchedule != null) {
            targetSchedule.setUpdateTime(new Date());
            targetSchedule.setIsDeleted(0);
            targetSchedule.setStatus(1);
            scheduleRepository.save(targetSchedule);
        } else {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }
    }

    @Override
    public Page<Schedule> selectPage(Integer page, Integer limit, ScheduleQueryVo scheduleQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        // 0 为第一页
        Pageable pageable = PageRequest.of(page-1, limit, sort);
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setIsDeleted(0);
        // 创建匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                // 模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                // 忽略大小写
                .withIgnoreCase(true);
        Example<Schedule> example = Example.of(schedule, exampleMatcher);
        return scheduleRepository.findAll(example, pageable);
    }

    /**
     * 根据 医院编号 排班号 删除
     * @param hoscode 医院编号
     * @param hosScheduleId 排班号
     */
    @Override
    public void remove(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (schedule != null) {
            scheduleRepository.deleteById(schedule.getId());
        }
    }

    /**
     * 根据医院编号 科室编号 获取 排班信息
     */
    @Override
    public Map<String, Object> getScheduleByHoscodeAndDepcode(Integer page, Integer limit, String hoscode, String depcode) {
        // 构建条件,查询
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        // 根据工作日workDate期进行分组
        Aggregation aggregation = Aggregation.newAggregation(
                // 匹配条件
                Aggregation.match(criteria),
                // 分组字段
                Aggregation.group("workDate")
                        // 起别名
                        .first("workDate").as("workDate")
                        // 统计号源数量，as是起名字
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                // 排序
                Aggregation.sort(Sort.Direction.ASC, "workDate"),
                // 实现分页
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit)
        );
        // 执行：三个参数：规则，规则里面用到的实体类，最后要封装的实体类
        AggregationResults<BookingScheduleRuleVo> aggResult =
                mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        // 最后需要返回的结果
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = aggResult.getMappedResults();
        // 查询每个分组的总记录数
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalAggResults =
                mongoTemplate.aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        // 每个分组的记录数
        int total = totalAggResults.getMappedResults().size();
        // 把日期对应的星期返回
        for (BookingScheduleRuleVo bookingScheduleRuleVo : bookingScheduleRuleVoList) {
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            bookingScheduleRuleVo.setDayOfWeek(this.getDayOfWeek(new DateTime(workDate)));
        }
        // 设置最终数据，进行返回
        Map<String, Object> result = new HashMap<>();
        result.put("bookingScheduleRuleVoList", bookingScheduleRuleVoList);
        result.put("total", total);
        // 获取医院名称
        String hosName = hospitalService.getHospitalByHoscode(hoscode).getHosname();
        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname",hosName);
        result.put("baseMap",baseMap);
        return result;
    }

    @Override
    public List<Schedule> getSchduleDetail(String hoscode, String depcode, String workDate) {
        // 根据参数查询mongodb
        List<Schedule> scheduleList = scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, new DateTime(workDate).toDate());
        // 设置其他值：医院名称、科室名称、日期对应星期
        scheduleList.stream().forEach(item -> {
            item.getParam().put("hosname", hospitalService.getHospitalByHoscode(hoscode).getHosname());
            item.getParam().put("depname", departmentService.getDepartmentName(hoscode, depcode));
            item.getParam().put("dayOfWeak", this.getDayOfWeek(new DateTime(item.getWorkDate())));
        });
        return scheduleList;
    }

    /**
     * 根据日期获取周几数据
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }

}
