#parse("detail-utils.vm")
<template>
    <div class="project-page-style">
        #detailContainer()
    </div>
</template>

<script setup lang="ts" name="${basicConfig.apiFileNameFirstCase}Detail">
    let detailData=$ref({})
    const { isDetail, row } = getQueryParam()
    if (isDetail) {
        onBeforeMount(async () => {
            const { data } = await getDetailByIdReq(row.id)
            detailData=data
        })
    }
    const getDetailByIdReq = (id) => {
        return axiosReq({
            url: '${apiConfig.detailApi}',
            data: { id },
            method: '${apiConfig.detailMethod}'
        })
    }
</script>

<style lang="scss" scoped>
    .detail-row-container:not(:last-child) {
        margin-right: 76px;
    }

    .detail-item {
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items: flex-start;
        margin-bottom: 18px;
        color: #595959;
    }

    .detail-item > span {
        display: inline-block;
    }
</style>
