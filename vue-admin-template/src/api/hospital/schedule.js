import request from '@/utils/request'

const SCHEDULE_API = '/admin/hospital/schedule'

export default {
  // 获取所有排班信息
  getScheduleRule(page, limit, hoscode, depcode) {
    return request({
      url: `${SCHEDULE_API}/getScheduleRule/${page}/${limit}/${hoscode}/${depcode}`,
      method: 'get'
    })
  },

  // 获取排班详情
  getScheduleDetail(hoscode, depcode, workDate) {
    return request({
      url: `${SCHEDULE_API}/getScheduleDetail/${hoscode}/${depcode}/${workDate}`,
      method: 'get'
    })
  }
}
