import en from '../translations/en'
import zh from '../translations/zh'

const supported = ['en', 'zh']
let locale = 'en'

try {
  const {0: browserLang} = navigator.language.split('-')
  if (supported.includes(browserLang)) locale = browserLang
} catch (e) {
  console.log(e)
}

export default {
  locale,

  fallbackLocale: 'en',

  availableLocales: [{
    code: 'en',
    flag: 'us',
    label: 'English',
    messages: en
  }, {
    code: 'zh',
    flag: 'cn',
    label: '中文',
    messages: zh
  }]
}
