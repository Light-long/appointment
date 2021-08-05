import request from '@/utils/request'

const API_NAME = '/admin/hospital/hospitalSet'

export default {
  getHospitalSetPageByCondition(current, limit, hospitalSetQueryVo) {
    return request({
      url: `${API_NAME}/findPageByCondition/${current}/${limit}`,
      method: 'post',
      // 已json的形式传递
      data: hospitalSetQueryVo
    })
  }
}
