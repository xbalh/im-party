import {SelectEntry, isSelectEntryEdited} from '@bpmn-io/properties-panel';
import {useService} from 'bpmn-js-properties-panel'

// import hooks from the vendored preact package
import {useEffect, useState} from '@bpmn-io/properties-panel/preact/hooks';

export default function (element) {

  return [
    {
      id: 'lulu-formType',
      element,
      component: FormType,
      isEdited: isSelectEntryEdited
    }
  ];
}

function FormType(props) {
  const {element, id} = props;

  const modeling = useService('modeling');
  const translate = useService('translate');
  const debounce = useService('debounceInput');


  const getValue = () => {
    return element.businessObject.spell || '';
  }

  const setValue = value => {
    return modeling.updateProperties(element, {
      spell: value
    });
  }

  const [spells, setSpells] = useState([]);

  useEffect(() => {
    function fetchSpells() {
      fetch('http://localhost:1234/spell')
        .then(res => res.json())
        .then(spellbook => setSpells(spellbook))
        .catch(error => console.error(error));
    }

    fetchSpells();
  }, [setSpells]);

  const getOptions = () => {
    return [
      {label: '<none>', value: undefined},
      ...spells.map(spell => ({
        label: spell,
        value: spell
      }))
    ];
  }

  return SelectEntry(
    {
      id,
      element,
      description: translate('Apply a black magic spell'),
      label: translate('FormType'),
      getValue,
      setValue,
      getOptions,
      debounce
    }
  )
}
