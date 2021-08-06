<template>
  <div class= "app-container">
    <div align="center"><h3>医院设置列表</h3></div>
    <!-- 查询表单-->
    <div align="center">
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item>
          <el-input  v-model="searchObj.hosname" placeholder="医院名称"/>
        </el-form-item>
        <el-form-item>
          <el-input v-model="searchObj.hoscode" placeholder="医院编号"/>
        </el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="getPageList()">查询</el-button>
      </el-form>
    </div>

    <!-- 工具条 -->
    <div>
       <el-button type="danger" size="mini" @click="batchRemove()">批量删除</el-button>
    </div>

    <!-- 列表集合 -->
    <el-table
      :data="hospitalSetList"
      stripe
      style="width: 100%"
      @selection-change="handleSelectionChange">
      <!-- 复选框 -->
      <el-table-column type="selection" width="55"/>
      <el-table-column type="index" width="50" label="序号"/>
      <el-table-column prop="hosname" label="医院名称"/>
      <el-table-column prop="hoscode" label="医院编号"/>
      <el-table-column prop="apiUrl" label="api基础路径" />
      <el-table-column prop="contactsName" label="联系人姓名"/>
      <el-table-column prop="contactsPhone" label="联系人手机"/>
      <el-table-column label="状态" width="80">
        <template slot-scope="scope">
          {{ scope.row.status === 1 ? '可用' : '不可用' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
         <template slot-scope="scope">
           <router-link :to="'/hospitalSet/update/'+scope.row.id">
              <el-button type="primary" size="mini" icon="el-icon-edit"></el-button>
           </router-link>
           <el-button  type="danger"  size="mini"
               icon="el-icon-delete"  @click="removeHospitalSetById(scope.row.id)">
           </el-button>
            <el-button v-if="scope.row.status==1" type="primary" size="mini"
               icon="el-icon-delete" @click="lockHospitalSet(scope.row.id,0)">锁定
            </el-button>
            <el-button v-if="scope.row.status==0" type="danger" size="mini"
               icon="el-icon-delete" @click="lockHospitalSet(scope.row.id,1)">解锁
            </el-button>
         </template>
      </el-table-column>
    </el-table>

    <!--分页-->
    <el-pagination
      :current-page="current"
      :page-size="limit"
      :total="total"
      style="padding:30px 0; text-align: center;"
      layout="total,prev,pager,next,jumper"
      @current-change="getPageList"
    />
  </div>
</template>

<script>

import hospitalset from "@/api/hospitalset"

export default {
  name: "list",
  data() {
    return {
      current: 1,
      limit: 5,
      searchObj: {},
      hospitalSetList: [],
      total: 0,
      batchRemoveIdList: []
    }
  },
  created() {
    this.getPageList()
  },
  methods: {
    // 锁定与解锁
    lockHospitalSet(id, status) {
      hospitalset.lockHospitalSet(id, status)
        .then(response => {
          // 刷新
          this.getPageList(1)
        })
    },
    // 获取批量删除的id列表
    handleSelectionChange(selection) {
      this.batchRemoveIdList = selection
    },
    // 批量删除
    batchRemove() {
      this.$confirm('此操作将永久删除医院是设置信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { //确定执行then方法
        let idList = []
        //遍历数组得到每个id值，设置到idList里面
        for(let i = 0; i < this.batchRemoveIdList.length; i++) {
          let id = this.batchRemoveIdList[i].id
          idList.push(id)
        }
        //调用接口
        hospitalset.batchRemoveHospitalSet(idList)
          .then(response => {
            //提示
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            //刷新页面
            this.getPageList(1)
          })
      })
    },
    // 根据传递的page分页查询
    getPageList(page = 1) {
      this.current = page
      hospitalset.getHospitalSetPageByCondition(this.current, this.limit, this.searchObj)
        .then(response => {
          console.log(response)
          this.hospitalSetList = response.data.records
          this.total = response.data.total
        })
        .catch(error => {
          console.log(error)
        })
    },
    removeHospitalSetById(id) {
      this.$confirm('此操作将永久删除医院是设置信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { //确定执行then方法
        //调用接口
        hospitalset.deleteHospitalSet(id)
          .then(response => {
            //提示
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            //刷新页面
            this.getPageList(1)
          })
      })
    }
  }
}
</script>

<style scoped>

</style>
