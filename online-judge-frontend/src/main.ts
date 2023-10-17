import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import '@/plugins/axios'
import '@/access/index'

// bytemd 的样式文件
import 'bytemd/dist/index.css'
import 'bytemd/dist/index.css'
import 'katex/dist/katex.css'
import 'highlight.js/styles/atom-one-dark-reasonable.css'
import 'gemoji'

// 额外引入图标库
import ArcoVueIcon from '@arco-design/web-vue/es/icon'

// 引入 moment.js
moment.locale('zh-cn') //设置语言 或 moment.lang('zh-cn');

// 引入 Arco-Design 组件
import ArcoVue from '@arco-design/web-vue'
import '@arco-design/web-vue/dist/arco.css'
import moment from 'moment'
import 'bytemd/dist/index.css'

const app = createApp(App)
app.use(store).use(router).use(ArcoVue).use(ArcoVueIcon).mount('#app')
app.config.globalProperties.$moment = moment
