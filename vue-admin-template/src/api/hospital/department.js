import request from '@/utils/request'

const DEPARTMENT_API = '/admin/hospital/department'

export default {
  // 获取所有department
  getDepartmentTree(hoscode) {
    return request({
      url: `${DEPARTMENT_API}/getDepartmentDetailTree/${hoscode}`,
      method: 'get'
    })
  }
}
