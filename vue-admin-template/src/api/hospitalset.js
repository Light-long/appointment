import request from '@/utils/request'

const API_NAME = '/admin/hospital/hospitalSet'

export default {
  // 根据条件分页查询
  getHospitalSetPageByCondition(current, limit, hospitalSetQueryVo) {
    return request({
      url: `${API_NAME}/findPageByCondition/${current}/${limit}`,
      method: 'post',
      // 已json的形式传递
      data: hospitalSetQueryVo
    })
  },

  // 根据id删除
  deleteHospitalSet(id) {
    return request({
      url: `${API_NAME}/${id}`,
      method: 'delete'
    })
  },

  // 批量删除
  batchRemoveHospitalSet(idList) {
    return request({
      url: `${API_NAME}/batchRemove`,
      method: 'delete',
      data: idList
    })
  },

  // 锁定与取消锁定
  lockHospitalSet(id, status) {
    return request({
      url: `${API_NAME}/lockHospitalSet/${id}/${status}`,
      method: 'put'
    })
  },

  // 添加医院设置
  addHospitalSet(hospital) {
    return request({
      url: `${API_NAME}/saveHospitalSet`,
      method: 'post',
      data: hospital
    })
  },

  // 根据id查询医院设置
  getHospitalSetById(id) {
    return request({
      url: `${API_NAME}/${id}`,
      method: 'get'
    })
  },

  // 根据id修改医院设置
  updateHospitalSet(hospital) {
    return request({
      url: `${API_NAME}/updateHospitalSet`,
      method: 'post',
      data: hospital
    })
  }
}
