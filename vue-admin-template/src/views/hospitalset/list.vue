<template>
  <div class= "app-container">
    医院设置列表
  </div>
</template>

<script>

import hospitalset from "@/api/hospitalset"

export default {
  name: "list",
  data() {
    return {
      current: 1,
      limit: 2,
      searchObj: {},
      hospitalSetList: [],
      total: 0
    }
  },
  created() {
    this.getPageList()
  },
  methods: {
    // 根据传递的page分页查询
    getPageList(page = 1) {
      this.current = page
      hospitalset.getHospitalSetPageByCondition(this.current, this.limit, this.searchObj)
        .then(response => {
          console.log(response)
          this.hospitalSetList = response.data.records
          this.total = total
        })
        .catch(error => {
          console.log(error)
        })
    }
  }
}
</script>

<style scoped>

</style>
