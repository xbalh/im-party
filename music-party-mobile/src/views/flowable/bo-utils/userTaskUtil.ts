import {is} from "bpmn-js/lib/util/ModelUtil";

export function isUserService(element: any): boolean {
  return is(element, 'bpmn:UserTask')
}

export function isStartEvent(element: any): boolean {
  return is(element, 'bpmn:StartEvent')
}
