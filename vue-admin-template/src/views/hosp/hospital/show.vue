<template>
  <div class= "app-container">
    <div align="center"><h3>医院详细信息</h3></div>
    <h4>基本信息</h4>
    <table class="table table-striped table-condenseda table-bordered" width="100%">
      <tbody>
      <tr>
        <th width="15%">医院名称</th>
        <td width="35%"><b style="font-size: 14px">{{ hospital.hosname }}</b> | {{ hospital.param.hosTypeString }}</td>
        <th width="15%">医院logo</th>
        <td width="35%">
          <img :src="'data:image/jpeg;base64,'+hospital.logoData" width="80">
        </td>
      </tr>
      <tr>
        <th>医院编码</th>
        <td>{{ hospital.hoscode }}</td>
        <th>地址</th>
        <td>{{ hospital.param.fullAddress }}</td>
      </tr>
      <tr>
        <th>坐车路线</th>
        <td colspan="3">{{ hospital.route }}</td>
      </tr>
      <tr>
        <th>医院简介</th>
        <td colspan="3">{{ hospital.intro }}</td>
      </tr>
      </tbody>
    </table>

    <h4>预约规则信息</h4>
    <table class="table table-striped table-condenseda table-bordered" width="100%">
      <tbody>
      <tr>
        <th width="15%">预约周期</th>
        <td width="35%">{{ bookingRule.cycle }}天</td>
        <th width="15%">放号时间</th>
        <td width="35%">{{ bookingRule.releaseTime }}</td>
      </tr>
      <tr>
        <th>停挂时间</th>
        <td>{{ bookingRule.stopTime }}</td>
        <th>退号时间</th>
        <td>{{ bookingRule.quitDay == -1 ? '就诊前一工作日' : '就诊当日' }}{{ bookingRule.quitTime }} 前取消</td>
      </tr>
      <tr>
        <th>预约规则</th>
        <td colspan="3">
          <ol>
            <li v-for="item in bookingRule.rule" :key="item">{{ item }}</li>
          </ol>
        </td>
      </tr>
      <br>
      <el-row>
        <el-button @click="back">返回</el-button>
      </el-row>
      </tbody>
    </table>
  </div>

</template>

<script>

import hospital from "@/api/hospital/hospital";

export default {
    name: "show",
    data() {
      return {
        hospital: {},
        bookingRule: {}
      }
    },
    created() {
      const id = this.$route.params.id
      this.getDetail(id)
    },
    methods: {
      // 获取详细信息
      getDetail(id) {
        hospital.getHospitalDetail(id)
          .then(response => {
            this.hospital = response.data
            this.bookingRule = response.data.bookingRule
          })
      },
      back() {
        this.$router.push({ path: '/hospitalSet/hospital/list' })
      }
    }
}
</script>

<style scoped>

</style>
