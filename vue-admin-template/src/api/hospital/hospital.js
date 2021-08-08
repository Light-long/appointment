import request from '@/utils/request'

const HOSPITAL_API = '/admin/hospital/hospital'
const DICT_API = '/admin/cmn/dict'

export default {
  // 获取医院列表
  getHospitalPage(page, limit, searchObj) {
    return request({
      url: `${HOSPITAL_API}/list/${page}/${limit}`,
      method: 'post',
      // 已json的形式传递
      data: searchObj
    })
  },

  // 根据dictCode查询下级数据字典
  getChildByDictCode(dictCode) {
    return request({
      url: `${DICT_API}/getChildByDictCode/${dictCode}`,
      method: 'get'
    })
  },

  // 根据parent_id查询子类数据
  getChildByParentId(id) {
    return request({
      url: `${DICT_API}/findChildData/${id}`,
      method: 'get'
    })
  },

  // 更新status
  updateHospitalStatus(id, status) {
    return request({
      url: `${HOSPITAL_API}/updateStatus/${id}/${status}`,
      method: 'get'
    })
  },

  // 医院详细信息
  getHospitalDetail(id) {
    return request({
      url: `${HOSPITAL_API}/showDetail/${id}`,
      method: 'get'
    })
  }
}
