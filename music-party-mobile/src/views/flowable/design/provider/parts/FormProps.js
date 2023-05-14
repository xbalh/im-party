import {SelectEntry, isSelectEntryEdited} from '@bpmn-io/properties-panel';
import {useService} from 'bpmn-js-properties-panel'
import {getBusinessObject} from 'bpmn-js/lib/util/ModelUtil';

export function FormProps(props) {
  return [
    {
      id: "form",
      component: Form,
      isEdited: isSelectEntryEdited,
    }
  ]
}

function Form(props) {
  const {element} = props;
  const translate = useService('translate');
  const bpmnFactory = useService('bpmnFactory');
  const businessObject = getBusinessObject(element);
  const commandStack = useService('commandStack');

  let extensionElements = businessObject.get('extensionElements');

  const getOptions = () => {
    return [
      {value: '', label: translate('<none>')},
      {value: 'formRef', label: translate('Camunda Forms')},
      {value: 'formKey', label: translate('Embedded or External Task Forms')},
      {value: 'formData', label: translate('Generated Task Forms')},
      {value: 'fuck', label: translate('Generated Task Forms')}
    ];
  }

  const setValue = (value) => {
    console.log(value)
  }
  const getValue = () => {
    return 'fuck'
  }

  return SelectEntry({
    element,
    id: 'form',
    label: translate('FormRef'),
    getOptions,
    setValue,
    getValue
  })
}
