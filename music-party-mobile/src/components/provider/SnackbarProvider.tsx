import {defineComponent, provide, VNodeChild, ref, reactive, h} from 'vue'
import {VSnackbar} from 'vuetify/components'
import {createId} from 'seemly'
import {render} from '@/utils'


type ContentType = string | (() => VNodeChild)

export const SnackBarInjectKey = "SnackBarInjectKey "

interface SnackBarReactive {
  key: string,
  content: ContentType
  modelValue: boolean,
  options?: Snackbar,
  onClose: (key: string) => void
}


export interface SnackBarApiInjection {
  info: (content: ContentType, options?: Snackbar) => SnackBarReactive
  success: (content: ContentType, options?: Snackbar) => SnackBarReactive
  warning: (content: ContentType, options?: Snackbar) => SnackBarReactive
  error: (content: ContentType, options?: Snackbar) => SnackBarReactive
}

export default defineComponent({
  name: "snackbarProvider",
  setup() {
    const snackBarListRef = ref<SnackBarReactive[]>([])
    const snackBarRefs = ref<Record<string, SnackBarReactive>>({})

    const api: SnackBarApiInjection = {
      info(content: ContentType, options?: Snackbar): SnackBarReactive {
        return create(content, {...options, color: "info", timeout: 1000})
      },
      success(content: ContentType, options?: Snackbar): SnackBarReactive {
        return create(content, {...options, color: "success", timeout: 1000})
      },
      error(content: ContentType, options?: Snackbar): SnackBarReactive {
        return create(content, {...options, color: "error", timeout: 2000})
      },
      warning(content: ContentType, options?: Snackbar): SnackBarReactive {
        return create(content, {...options, color: "warning", timeout: 1000})
      },
    }
    provide(SnackBarInjectKey, api)

    function create(content: ContentType, options?: Snackbar): SnackBarReactive {
      const snackBarReactive = reactive<SnackBarReactive>({
        key: createId(),
        content,
        options: {
          timeout: 2000,
          ...options,
        },
        modelValue: true,
        onClose: (key: string) => {
          snackBarListRef.value.splice(
            snackBarListRef.value.findIndex((message) => message.key === key),
            1
          )
          delete snackBarRefs.value[key]
        }
      })
      snackBarListRef.value.push(snackBarReactive)
      // @ts-ignore
      return snackBarReactive
    }

    return Object.assign(
      {
        snackBarRefs,
        snackBarList: snackBarListRef,
      },
      api
    )
  },
  render() {
    return (
      <>
        {this.$slots.default?.()}
        {this.snackBarList.length > 0 ? (
          <>
            {this.snackBarList.map((msg: SnackBarReactive) => {
              // @ts-ignore
              return (
                <VSnackbar
                  ref={
                    ((inst: SnackBarReactive) => {
                      if (inst) {
                        this.snackBarRefs[msg.key] = inst
                      }
                    }) as () => void
                  }
                  v-model={msg.modelValue}
                  {...msg.options}
                  {...{
                    'onUpdate:modelValue': (val: boolean) => {
                      if (!val) {
                        setTimeout(() => {
                          msg.onClose(msg.key)
                        }, msg.options ? Number(msg.options.timeout) + 200 : 1000)
                      }
                    }
                  }}

                >
                  {render(msg.content)}
                </VSnackbar>
              )
            })}
          </>
        ) : null}
      </>
    )
  }
})

export function useSnackBar(): SnackBarApiInjection {
  const api = inject(SnackBarInjectKey, null)
  if (api === null) {
    throw new Error('not outer <snackbar-provider> found')
  }
  return api
}
