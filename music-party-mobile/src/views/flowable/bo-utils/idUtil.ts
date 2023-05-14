import {Base} from 'diagram-js/lib/model'
import {useModelStore} from '@/store'
import {isIdValid} from "@/views/flowable/utils/BpmnValidator";

export function getIdValue(element: Base): string {
  return element.businessObject.id
}

export function setIdValue(element: Base, value: string) {
  const errorMsg = isIdValid(element.businessObject, value)

  if (errorMsg && errorMsg.length) {
    return window.$snackBar?.warning(errorMsg)
  }

  const store = useModelStore()
  const modeling = store.getModeling

  modeling.updateProperties(element, {
    id: value
  })
}
