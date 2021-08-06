<template>
  <div class= "app-container">
    <div align="center"><h3>添加医院设置</h3></div>
    <el-form  label-width="120px">
      <el-form-item  label="医院名称">
        <el-input  v-model="hospitalSet.hosname"/>
      </el-form-item>
      <el-form-item  label="医院编号">
        <el-input  v-model="hospitalSet.hoscode"/>
      </el-form-item>
      <el-form-item  label="api基础路径">
        <el-input  v-model="hospitalSet.apiUrl"/>
      </el-form-item>
      <el-form-item  label="联系人姓名">
        <el-input  v-model="hospitalSet.contactsName"/>
      </el-form-item>
      <el-form-item  label="联系人手机">
        <el-input  v-model="hospitalSet.contactsPhone"/>
      </el-form-item>
      <el-form-item align="center">
        <el-button  type="primary"  @click="saveOrUpdate">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

import hospitalset from "@/api/hospitalset";

export default {
    name: "add",
    data() {
      return {
        hospitalSet: {}
      }
    },
    created() {
      // 获取路由中的id，如果有id进行数据回显
      if (this.$route.params && this.$route.params.id) {
        const id = this.$route.params.id
        // 根据id查询进行数据回显
        this.getHospitalSetById(id)
      } else {
        // 添加进来时页面为空
        this.hospitalSet = {}
      }
    },
    methods: {
      // 根据id查询（数据回显）
      getHospitalSetById(id) {
        hospitalset.getHospitalSetById(id)
          .then(response => {
            this.hospitalSet = response.data
          })
      },
      save() {
        hospitalset.addHospitalSet(this.hospitalSet)
          .then(response => {
            //提示
            this.$message({
              type: 'success',
              message: '添加成功!'
            })
            //跳转列表页面，使用路由跳转方式实现
            this.$router.push({path:'/hospitalSet/list'})
          })
      },
      update() {
        hospitalset.updateHospitalSet(this.hospitalSet)
          .then(response => {
            //提示
            this.$message({
              type: 'success',
              message: '修改成功!'
            })
            //跳转列表页面，使用路由跳转方式实现
            this.$router.push({path:'/hospitalSet/list'})
          })
      },
      saveOrUpdate() {
        if (!this.hospitalSet.id) {
          // 没有id添加
          this.save()
        } else {
          this.update()
        }
      }
    }
}
</script>

<style scoped>

</style>
