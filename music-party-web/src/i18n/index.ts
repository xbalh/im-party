import Vue from "vue";
import VueI18n from "vue-i18n";

// @ts-ignore
import enLocale from "element-ui/lib/locale/lang/en";
// @ts-ignore
import zhLocale from "element-ui/lib/locale/lang/zh-CN";
// @ts-ignore
import ElementLocale from "element-ui/lib/locale";
import en from "./lang/en";
import zh from "./lang/zh";

Vue.use(VueI18n)
const messages = {
  en: {
    ...en,
    ...enLocale
  },
  zh: {
    ...zh,
    ...zhLocale,
  },
 
}
 
const i18n = new VueI18n({
  locale: localStorage.getItem('language') || 'zh', // 设置语种
  messages, // 设置全局当地语言包,
  fallbackLocale: 'zh',
  numberFormats:{ //设置 数字本地化
    'en': {
      currency: { //添加 $
        style: 'currency', currency: 'USD'
      }
    },
    'zh': {
      currency: { //添加 ￥
        style: 'currency', currency: 'JPY', currencyDisplay: 'symbol'
      }
    }
  },
    dateTimeFormats:{//设置 日期时间本地化
    'en': {
      short: { 
        year: 'numeric', month: 'short', day: 'numeric'
      },
      long: {
        year: 'numeric', month: 'short', day: 'numeric',
        weekday: 'short', hour: 'numeric', minute: 'numeric'
      }
    },
    'zh': {
      short: {
        year: 'numeric', month: 'short', day: 'numeric'
      },
      long: {
            year: 'numeric', month: 'short', day: 'numeric',
            weekday: 'short', hour: 'numeric', minute: 'numeric'  
      }
    }
  }
})
 
export default i18n