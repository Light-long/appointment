<template>
  <div class="app-container">
    <div align="center"><h3>排班列表</h3></div>
    <div style="margin-bottom: 10px;font-size: 10px;">选择：</div>
    <el-container style="height: 100%">
      <el-aside width="200px" style="border: 1px silver solid">
        <!-- 部门 -->
        <el-tree
          :data="departmentTree"
          :props="defaultProps"
          :default-expand-all="true"
          @node-click="handleNodeClick">
        </el-tree>
      </el-aside>
      <el-main style="padding: 0 0 0 20px;">
        <el-row style="width: 100%">
          <!-- 排班日期 分页 -->
        </el-row>
        <el-row style="margin-top: 20px;">
          <!-- 排班日期对应的排班医生 -->
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script>

import department from "@/api/hospital/department";

export default {
    name: "schedule",
    data() {
      return {
        // department列表
        departmentTree: [],
        // 树节点
        defaultProps: {
          children: 'children',
          label: 'depname'
        },
        hoscode: null
      }
    },
    created() {
      this.hoscode = this.$route.params.hoscode
      this.getDepartmentTree()
    },
    methods: {
      // tree
      getDepartmentTree() {
        department.getDepartmentTree(this.hoscode)
          .then(response => {
            this.departmentTree = response.data

          })
      }
    }
}
</script>

<style>
  .el-tree-node.is-current > .el-tree-node__content {
      background-color: #409EFF !important;
      color: white;
     }

  .el-checkbox__input.is-checked+.el-checkbox__label {
      color: black;
     }
</style>
