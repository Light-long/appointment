<template>
  <div class= "app-container">
    <div align="center"><h3>数据字典</h3></div>
    <!--  导出  -->
    <div class="el-toolbar">
      <div class="el-toolbar-body" style="justify-content: flex-start;">
        <el-button type="primary" @click="exportData"> <i class="fa fa-plus"/>导出></el-button>
        <el-button type="success" @click="importData"> <i class="fa fa-plus"/>导入</el-button>
      </div>
    </div>
    <br>
    <!--  上传文件dialog  -->
    <el-dialog title="导入" :visible.sync="dialogImportVisible" width="480px">
      <el-form label-position="right" label-width="170px">
        <el-form-item label="文件">
          <el-upload
            :multiple="false"
            :on-success="onUploadSuccess"
            :action="'http://localhost:8202/admin/cmn/dict/importDict'"
            class="upload-demo">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传xls文件，且不超过500kb</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogImportVisible = false">
          取消
        </el-button>
      </div>
    </el-dialog>
    <!--  dict列表  -->
    <el-table
      :data="dataList"
      style="width: 100%"
      row-key="id"
      border
      lazy
      :load="getChildrenList"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column label="名称" width="230" align="left">
        <template slot-scope="scope">
        <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="编码" width="220">
      <template slot-scope="{row}">
              {{ row.dictCode }}
      </template>
      </el-table-column>
      <el-table-column label="值" width="230" align="left">
      <template slot-scope="scope">
      <span>{{ scope.row.value }}</span>
      </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center">
      <template slot-scope="scope">
      <span>{{ scope.row.createTime }}</span>
      </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

import dict from "@/api/cmn/dict";

export default {
    name: "list",
    data() {
      return {
        dataList: [],
        // 上传组件是否显示
        dialogImportVisible: false
      }
    },
    created() {
      // 获取所有数据集合
      this.getDictList(1)
    },
    methods: {
      // 数据字典列表
      getDictList(id) {
        dict.getDictList(id)
          .then(response => {
            this.dataList = response.data
          })
      },
      // 查询不同层级
      getChildrenList(tree, treeNode, resolve) {
        dict.getDictList(tree.id)
          .then(response => {
            resolve(response.data)
          })
      },
      // 导出
      exportData() {
        // 直接调用导出接口
        window.location.href = 'http://localhost:8202/admin/cmn/dict/exportDict'
      },
      // 点击上传，组件显示
      importData() {
        this.dialogImportVisible = true
      },
      onUploadSuccess() {
        this.$message.info('上传成功')
        this.dialogImportVisible = false
        this.getDictList(1)
      }
    }
}
</script>

<style scoped>

</style>
