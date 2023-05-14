// helper ////////////////////

import {getBusinessObject, is, isAny} from "bpmn-js/lib/util/ModelUtil";
import {find} from "lodash-es";

const CONDITIONAL_SOURCES = [
  'bpmn:Activity',
  'bpmn:ExclusiveGateway',
  'bpmn:InclusiveGateway',
  'bpmn:ComplexGateway'
];


export function isNotConditional(element: any) {
  return (
    !(is(element, 'bpmn:SequenceFlow') && isConditionalSource(element.source)) &&
    !getConditionalEventDefinition(element)
  )
}

function isConditionalSource(element: any) {
  return isAny(element, CONDITIONAL_SOURCES);
}

function getConditionalEventDefinition(element: any) {
  if (!is(element, 'bpmn:Event')) {
    return false;
  }

  return getEventDefinition(element, 'bpmn:ConditionalEventDefinition');
}

function getEventDefinition(element: any, eventType: string) {
  const businessObject = getBusinessObject(element);

  const eventDefinitions = businessObject.get('eventDefinitions') || [];

  return find(eventDefinitions, function (definition) {
    return is(definition, eventType);
  });
}
