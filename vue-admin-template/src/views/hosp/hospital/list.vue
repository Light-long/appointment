<template>
  <div class= "app-container">
  <div align="center"><h3>医院列表</h3></div>
  <div align="center">
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-select
          v-model="searchObj.provinceCode"
          placeholder="请选择省"
          @change="provinceChanged">
          <el-option
            v-for="item in provinceList"
            :key="item.id"
            :label="item.name"
            :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="searchObj.cityCode"
          placeholder="请选择市"
          @change="cityChanged">
          <el-option
            v-for="item in cityList"
            :key="item.id"
            :label="item.name"
            :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-input v-model="searchObj.hosname" placeholder="医院名称"/>
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="getHospitalPage()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>
  </div>

  <!-- banner列表 -->
    <el-table v-loading="listLoading" :data="hospitalList" border fit highlight-current-row>
        <el-table-column
        label="序号"
        width="60"
        align="center">
            <template slot-scope="scope">
                    {{ (page - 1) * limit + scope.$index + 1 }}
            </template>
      </el-table-column>

      <el-table-column label="医院logo">
          <template slot-scope="scope">
          <img :src="'data:image/jpeg;base64,'+scope.row.logoData" width="80">
          </template>
      </el-table-column>

      <el-table-column prop="hosname" label="医院名称"/>
      <el-table-column prop="param.hosTypeString" label="等级" width="90"/>
      <el-table-column prop="param.fullAddress" label="详情地址"/>
      <el-table-column label="状态" width="80">
          <template slot-scope="scope">
                  {{ scope.row.status === 0 ? '未上线' : '已上线' }}
          </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间"/>

      <el-table-column label="操作" width="230" align="center">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status==1" type="danger" size="mini" @click="updateStatus(scope.row.id,0)">下线
          </el-button>
          <el-button v-if="scope.row.status==0" type="success" size="mini" @click="updateStatus(scope.row.id,1)">上线
          </el-button>
          <router-link :to="'/hospitalSet/hospital/show/'+scope.row.id">
              <el-button type="primary" size="mini">查看详情</el-button>
          </router-link>
        </template>
      </el-table-column>
  </el-table>

    <!-- 分页组件 -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      :page-sizes="[5, 10]"
      style="padding: 30px 0; text-align: center;"
      layout="sizes, prev, pager, next, jumper, ->, total, slot"
      @current-change="getHospitalPage"
      @size-change="changeSize"
    />
  </div>
</template>

<script>

import hospital from "@/api/hospital/hospital";

export default {
    name: "list",
    data() {
      return {
        listLoading: true,
        hospitalList: [],
        total: 0,
        page: 1,
        limit: 10,
        searchObj: {},
        provinceList: [],
        cityList: [],
        districtList: []
      }
    },
    created() {
      this.getHospitalPage(1)
      this.getProvinceList()
    },
    methods: {
      // 获取医院数据
      getHospitalPage(page = 1) {
        this.page = page
        hospital.getHospitalPage(page, this.limit, this.searchObj)
          .then(response => {
            this.hospitalList = response.data.content
            console.log(response.data.content)
            this.total = response.totalElements
            this.listLoading = false
          })
      },
      // 获取省份信息
      getProvinceList() {
        hospital.getChildByDictCode('Province')
          .then(response => {
            this.provinceList = response.data
          })
      },
      // 省份选定以后，二级联动查询市级信息
      provinceChanged() {
        this.cityList = []
        this.searchObj.cityCode = null
        this.districtList = []
        this.searchObj.districtCode = null
        hospital.getChildByParentId(this.searchObj.provinceCode)
          .then(response => {
            this.cityList = response.data
          })
      },
      // 重置表单
      resetData() {
        this.searchObj = {}
        this.getHospitalPage(1)
      },
      // 修改每页记录数
      changeSize(size) {
        this.limit = size
        this.getHospitalPage(1)
      },
      cityChanged() {
        this.$forceUpdate()
      },
      // 更新上线状态
      updateStatus(id, status) {
        hospital.updateHospitalStatus(id, status)
          .then(response => {
            this.getHospitalPage(1)
          })
      }
    }
}
</script>

<style scoped>

</style>
