import { ObjectDirective } from 'vue'
import store from '@/store'

const permissions: ObjectDirective = {
  inserted(el, binding) {
    const { value, arg = '' } = binding
    const permissions = store.getters['permissionsStore/getPermissions']

    if (!(permissions[arg] || []).includes(value)) {
      el.parentNode?.removeChild(el)
    }
  }
}

export default permissions