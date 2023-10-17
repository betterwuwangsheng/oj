<template>
  <div id="questions">
    <!--搜索栏-->
    <a-form :model="searchParams" layout="inline">
      <a-form-item
        field="title"
        label="题目："
        tooltip="请输入搜索的题目"
        style="min-width: 400px"
      >
        <a-input v-model="searchParams.title" placeholder="请输入搜索题目" />
      </a-form-item>
      <a-form-item
        field="tags"
        label="题目标签："
        tooltip="请输入搜索题目标签[回车确认]"
        style="min-width: 400px"
      >
        <a-input-tag
          v-model="searchParams.tags"
          placeholder="请输入搜索题目标签[回车确认]"
        />
      </a-form-item>
      <a-form-item>
        <a-button
          type="outline"
          shape="round"
          status="normal"
          @click="doSubmit"
        >
          搜索</a-button
        >
      </a-form-item>
    </a-form>
    <a-divider size="0" />

    <a-divider size="0" />
    <!--数据表格-->
    <a-table
      :ref="tableRef"
      :columns="columns"
      :data="dataList"
      :pagination="{
        showTotal: true,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total,
        showJumper: true,
        showPageSize: true,
      }"
      @page-change="onPageChange"
      @pageSizeChange="onPageSizeChange"
    >
      <template #tags="{ record }">
        <a-space wrap>
          <a-tag v-for="(tag, index) of record.tags" :key="index" color="green">
            {{ tag }}
          </a-tag>
        </a-space>
      </template>
      <template #acceptedRate="{ record }">
        {{
          `${
            Math.round(
              (record.submitNum > 0
                ? (record.acceptedNum / record.submitNum) * 100
                : "0" * 100) * 100
            ) / 100
          }% (${record.acceptedNum}/${record.submitNum})`
        }}
      </template>
      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button
            shape="round"
            status="normal"
            type="primary"
            @click="toQuestionPage(record)"
          >
            做题</a-button
          >
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import moment from "moment";
import { onMounted, ref, watchEffect } from "vue";
import { Question, QuestionControllerService } from "../../../backapi";
import message from "@arco-design/web-vue/es/message";
import { formatTime } from "@/utils/dateUtiles";
import { useRouter } from "vue-router";

const router = useRouter();
const tableRef = ref();

/**
 * 数据列表 -> 问题列表
 */
const dataList = ref([]);
const total = ref(0);

/**
 * 查询条件
 */
const searchParams = ref({
  userId: undefined,
  tags: [],
  title: "",
  pageSize: 10,
  current: 1,
  content: "",
});

/**
 * 加载问题列表数据
 */
const loadData = async () => {
  const res = await QuestionControllerService.listQuestionVoByPageUsingPost(
    searchParams.value
  );
  if (res.code === 0) {
    dataList.value = formatTime(res.data.records);
    console.log(res.data.total);
    total.value = res.data.total;
  } else {
    message.error("查询失败: " + res.message);
  }
};

const columns = [
  { title: "题号", dataIndex: "id" },
  { title: "问题名称", dataIndex: "title" },
  { title: "标签", slotName: "tags" },
  { title: "通过率", slotName: "acceptedRate" },
  { title: "创建时间", slotName: "createTime" },
  { slotName: "optional" },
];

/**
 * 更新
 * @param question
 */
const doUpdate = (question: Question) => {
  // 跳转到更新页面
  router.push({
    path: "/question/update",
    query: {
      id: question.id,
    },
  });
};

/**
 * 删除
 * @param question
 */
const doDelete = async (question: Question) => {
  const res = await QuestionControllerService.deleteQuestionUsingPost({
    id: question.id,
  });

  if (res.code === 0) {
    message.success("删除成功");
    await loadData();
  } else {
    message.error("删除失败");
  }
};

/**
 * 表格分页发生改变时触发
 */
const onPageChange = (page: number) => {
  searchParams.value = {
    // 复制旧的值
    ...searchParams.value,
    current: page,
  };
};

/**
 * 分页大小
 * @param size
 */
const onPageSizeChange = (size: number) => {
  searchParams.value = {
    ...searchParams.value,
    pageSize: size,
  };
};

/**
 * 跳转到做题页面
 * @param question
 */
const toQuestionPage = (question: Question) => {
  router.push({
    path: `/question/view/${question.id}`,
  });
};

/**
 * 确认搜索，重新加载数据
 */
const doSubmit = () => {
  // 重置搜索页号 -> 回到第一页
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
};

/**
 * 监听 searchParams 变量，改变时触发页面的重新加载
 */
watchEffect(() => {
  loadData();
});

/**
 * 页面渲染完毕后执行
 */
onMounted(() => {
  // 页面加载完毕后请求数据
  loadData();
});
</script>

<style scoped>
#questions {
  max-width: 1280px;
  margin: 0 auto;
}
</style>
