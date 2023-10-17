<template>
  <div id="solve-question">
    <!--一行两列-->
    <a-row :md="12" :gutter="[24, 24]">
      <a-col :md="12" :xs="24">
        <a-tabs default-active-key="question">
          <a-tab-pane key="question" title="题目">
            <a-card v-if="question" :title="question.title">
              <a-descriptions
                title="判题条件:"
                :column="{ xs: 1, md: 2, lg: 3 }"
              >
                <a-descriptions-item label="时间限制（ms）：">
                  {{ question.judgeConfig.timeLimit ?? 0 }}
                </a-descriptions-item>
                <a-descriptions-item label="内存限制（KB）：">
                  {{ question.judgeConfig.memoryLimit ?? 0 }}
                </a-descriptions-item>
                <a-descriptions-item label="堆栈限制（KB）：">
                  {{ question.judgeConfig.stackLimit ?? 0 }}
                </a-descriptions-item>
              </a-descriptions>
              <MdEViewer :value="question.content || ''" />
              <template #extra>
                <a-space wrap>
                  <a-tag
                    v-for="(tag, index) of question.tags"
                    :key="index"
                    color="green"
                    >{{ tag }}
                  </a-tag>
                </a-space>
              </template>
            </a-card>
          </a-tab-pane>
          <a-tab-pane key="comment" title="评论" disabled> 评论区</a-tab-pane>
          <a-tab-pane key="answer" title="答案" style="text-align: center">
            <h3>提交后方可查看答案</h3>
          </a-tab-pane>
        </a-tabs>
      </a-col>
      <a-col :md="12" :xs="24">
        <a-form :model="form" layout="inline">
          <a-form-item
            field="submitLanguage"
            label="编程语言："
            style="min-width: 240px"
          >
            <a-select
              v-model="form.language"
              :style="{ width: '320px' }"
              placeholder="选择编程语言"
            >
              <a-option :key="language" v-for="language in codeLanguages">
                {{ language }}
              </a-option>
            </a-select>
          </a-form-item>
        </a-form>
        <CodeEditor
          language="form.language"
          :value="form.code"
          :handle-change="changeCode"
        />
        <a-divider size="0" />
        <a-button
          shape="round"
          type="primary"
          style="min-width: 200px; margin-left: 280px"
          size="large"
          @click="doSubmit"
        >
          提交代码
        </a-button>
      </a-col>
    </a-row>
  </div>
<!--  <a-modal-->
<!--    v-model:visible="visible"-->
<!--    @ok="handleOk"-->
<!--    @cancel="handleCancel"-->
<!--    draggable-->
<!--    :esc-to-close="false"-->
<!--    :mask-closable="false"-->
<!--  >-->
<!--    <template #title> {{ modalTitle }}</template>-->
<!--    <div v-if="judgeTime">耗时: {{ judgeTime }}</div>-->
<!--  </a-modal>-->
</template>

<script setup lang="ts">
import { defineProps, onMounted, ref, withDefaults } from "vue";
import {
  QuestionControllerService,
  QuestionSubmitAddRequest,
  QuestionVO,
} from "../../../backapi";
import message from "@arco-design/web-vue/es/message";
import CodeEditor from "@/components/CodeEditor.vue";
import MdEViewer from "@/components/MdEViewer.vue";
import { useRouter } from "vue-router";

const question = ref<QuestionVO>();
const visible = ref(false);
const modalTitle = ref("判题中...");
const judgeTime = ref<string>();
let timer: ReturnType<typeof setInterval>;
let elapsedTime = 0;

const router = useRouter();

const codeLanguages = ref(["java"]);

interface Props {
  id: string;
}

/**
 * 获取到动态路由 id
 */
const props = withDefaults(defineProps<Props>(), {
  id: () => "",
});

// const startTimer = (id: number) => {
//   timer = setInterval(async () => {
//     elapsedTime++;
//     // 10 分钟，60 秒 * 10
//     if (elapsedTime >= 600) {
//       clearInterval(timer);
//     }
//     const res = await QuestionControllerService.getJudgeResultUsingGet(id);
//     console.log(res.data);
//     if (res.code === 0) {
//       clearInterval(timer);
//       modalTitle.value = res.data.message;
//       judgeTime.value = res.data.time;
//     }
//   }, 1000);
// };

// const handleOk = () => {
//   visible.value = false;
//   if (timer) {
//     clearInterval(timer);
//     modalTitle.value = "判题中...";
//     judgeTime.value = undefined;
//   }
//   router.push({ path: "/question_submit" });
// };

// const handleCancel = () => {
//   visible.value = false;
//   if (timer) {
//     clearInterval(timer);
//   }
//   modalTitle.value = "判题中...";
//   judgeTime.value = undefined;
// };

const loadData = async () => {
  const res = await QuestionControllerService.getQuestionVoByIdUsingGet(
    props.id as any
  );
  if (res.code === 0) {
    question.value = res.data;
  } else {
    message.error("加载失败，" + res.message);
  }
};

/**
 * 不同语言的默认程序
 */
const codeDefaultValue = ref(
  "import java.util.Scanner;\n" +
    "public class Main{\n" +
    "    public static void main(String[] args){\n" +
    "        Scanner sc = new Scanner(System.in);\n" +
    "        int a = sc.nextInt();\n" +
    "        int b = sc.nextInt();\n" +
    "        System.out.println(a + b);\n" +
    "    }\n" +
    "}"
);

const form = ref<QuestionSubmitAddRequest>({
  language: "java",
  code: codeDefaultValue as unknown as string,
});

/**
 * 提交代码
 */
const doSubmit = async () => {
  if (!question.value?.id) {
    return;
  }
  const res = await QuestionControllerService.doQuestionSubmitUsingPost({
    ...form.value,
    questionId: question.value.id,
  });
  if (res.code === 0) {
    message.success("提交成功!");
  } else {
    message.error("提交失败," + res.message);
  }
  // visible.value = true;
  // console.log(res.data);
  // startTimer(res.data);
};
/**
 * 页面加载时，请求数据
 */
onMounted(async () => {
  await loadData();

  const res = await QuestionControllerService.getCodeLanguageUsingGet();
  if (res.code === 0 && res.data) {
    codeLanguages.value = res.data;
  }
});

const changeCode = (value: string) => {
  form.value.code = value;
};
</script>

<style scoped>
#solve-question {
  max-width: 1600px;
  margin: 0 auto;
  box-shadow: 0px 0px 10px rgba(35, 7, 7, 0.21);
  border-radius: 10px;
}

#solve-question .arco-space-horizontal .arco-space-item {
  margin-bottom: 0 !important;
}
</style>
