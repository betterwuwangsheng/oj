<template>
  <div id="manager-question">
    <!--搜索-->
    <a-form
      :model="searchParams"
      layout="inline"
      style="justify-content: center; align-content: center; margin: 25px"
    >
      <a-form-item field="title" label="题目：" tooltip="请输入搜索的题目">
        <a-input v-model="searchParams.title" placeholder="请输入搜索题目" />
      </a-form-item>
      <a-form-item field="title" label="创建者：" tooltip="请输入创建者的id">
        <a-input v-model="searchParams.userId" placeholder="请输入搜索用户" />
      </a-form-item>
      <a-form-item field="title" label="题目内容" tooltip="请输入题目内容">
        <a-input v-model="searchParams.content" placeholder="请输入题目内容" />
      </a-form-item>
      <a-form-item
        field="tags"
        label="题目标签："
        tooltip="请输入搜索题目标签"
        style="min-width: 280px"
      >
        <a-input-tag v-model="searchParams.tags" placeholder="请输入题目标签" />
      </a-form-item>
      <a-form-item>
        <a-button
          type="outline"
          shape="round"
          status="normal"
          @click="doSearch"
        >
          搜索
        </a-button>
      </a-form-item>
    </a-form>

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
      :bordered="{ cell: true }"
      column-resizable
    >
      <template #id="{ record }">
        <a-link
          status="normal"
          style="color: blue"
          @click="toQuestionPage(record)"
          >{{ record.id }}
        </a-link>
      </template>
      <template #tags="{ record }">
        <a-space wrap>
          <a-tag
            v-for="(tag, index) of JSON.parse(record.tags)"
            :key="index"
            color="cyan"
            >{{ tag }}
          </a-tag>
        </a-space>
      </template>
      <template #judgeConfig="{ record }">
        <a-space wrap>
          <a-tag
            v-for="(config, index) of JSON.parse(record.judgeConfig)"
            :key="index"
            color="orangered"
            >{{
              `${
                index === "timeLimit"
                  ? "时间(ms)"
                  : index === "memoryLimit"
                  ? "内存(Kb)"
                  : "堆栈(Kb)"
              }`
            }}
            {{ "：" + config }}
          </a-tag>
        </a-space>
      </template>
      <template #judgeCase="{ record }">
        <a-space wrap>
          <a-tag
            v-for="(config, index) of JSON.parse(record.judgeCase)"
            :key="index"
            color="blue"
            >示例{{ index + 1 }}: 输入：{{ config.input }} ，输出：{{
              config.output
            }}
          </a-tag>
        </a-space>
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="doUpdate(record)">修改</a-button>
          <a-popconfirm
            content="确定要删除此题目吗?"
            type="error"
            okText="是"
            cancelText="否"
            @cancel="
              () => {
                console.log(`已取消`);
              }
            "
            @ok="doDelete(record)"
          >
            <a-button status="danger">删除</a-button>
          </a-popconfirm>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
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
  const res = await QuestionControllerService.listQuestionByPageUsingPost(
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
  {
    title: "id",
    slotName: "id",
    ellipsis: true,
    tooltip: true,
    width: 80,
  },
  {
    title: "创建者",
    dataIndex: "userId",
    ellipsis: true,
    tooltip: true,
    width: 80,
  },
  {
    title: "标题",
    dataIndex: "title",
    align: "center",
    ellipsis: true,
    tooltip: true,
    width: 100,
  },
  {
    title: "题目内容",
    dataIndex: "content",
    align: "center",
    width: 180,
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "标签",
    slotName: "tags",
    ellipsis: true,
    tooltip: true,
    width: 100,
  },
  {
    title: "答案",
    dataIndex: "answer",
    align: "center",
    ellipsis: true,
    tooltip: true,
    width: 150,
  },
  {
    title: "提交数",
    dataIndex: "submitNum",
    align: "center",
    ellipsis: true,
    tooltip: true,
    width: 80,
  },
  {
    title: "通过数",
    dataIndex: "acceptedNum",
    align: "center",
    ellipsis: true,
    tooltip: true,
    width: 80,
  },
  {
    title: "判题用例",
    slotName: "judgeCase",
    align: "center",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "判题配置",
    slotName: "judgeConfig",
    align: "center",
    ellipsis: true,
    tooltip: true,
    width: 150,
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    align: "center",
    ellipsis: true,
    tooltip: true,
    width: 200,
  },
  {
    title: "操作",
    slotName: "optional",
    align: "center",
    ellipsis: true,
    tooltip: true,
    width: 180,
  },
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
 * 确认搜索，重新加载数据
 */
const doSearch = () => {
  message.success({ content: "正在搜索", closable: true });
  // 这里需要重置搜索页号
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
};

/**
 * 跳转到做题页面
 * @param question
 */
const toQuestionPage = (questionId: any) => {
  router.push({
    path: `/question/view/${questionId.questionId}`,
  });
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
#manager-question {
  max-width: 1700px;
  margin: 0 auto;
}
</style>
