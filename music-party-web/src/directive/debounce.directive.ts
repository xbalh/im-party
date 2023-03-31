// 防抖指令

import { DirectiveBinding } from "vue/types/options"

const debounce = {
  inserted: function (el: HTMLElement, binding: DirectiveBinding) {
    let timer: NodeJS.Timeout
    el.addEventListener('keyup', () => {
      if (timer) {
        clearTimeout(timer)
      }
      timer = setTimeout(() => {
        binding.value()
      }, 1000)
    })
  },
}

export default debounce