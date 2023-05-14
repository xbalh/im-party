import {Base} from 'diagram-js/lib/model'
import {useModelStore} from '@/store'

const prefix = "camunda"

export function getProcessExecutable(element: Base): boolean {
  return element.businessObject.isExecutable
}

export function setProcessExecutable(element: Base, value: boolean) {
  const store = useModelStore()
  const modeling = store.getModeling

  modeling.updateProperties(element, {
    isExecutable: value
  })
}

export function getProcessVersionTag(element: Base): string | undefined {

  return element.businessObject.get(`${prefix}:versionTag`)
}

export function setProcessVersionTag(element: Base, value: string) {

  const modeling = useModelStore().getModeling

  modeling.updateProperties(element, {
    [`${prefix}:versionTag`]: value
  })
}
