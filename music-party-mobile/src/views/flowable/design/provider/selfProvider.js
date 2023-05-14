import spellProps from './parts/SpellProps';

import {is} from 'bpmn-js/lib/util/ModelUtil';
import {FormProps} from "@/views/flowable/design/provider/parts/FormProps";

const LOW_PRIORITY = 500;


export default function SelfProvider(propertiesPanel, translate) {

  this.getGroups = function (element) {

    return function (groups) {
      deleteById(groups, "CamundaPlatform__FormData")
      const form = findGroup(groups, 'CamundaPlatform__Form')

      if (form) {
        form.entries = [
          ...FormProps({element})
        ]
      }
      return groups;
    }
  };
  propertiesPanel.registerProvider(LOW_PRIORITY, this);
}

SelfProvider.$inject = ['translate'];

// Create the custom magic group
function createMagicGroup(element, translate) {

  // create a group called "Magic components".
  const magicGroup = {
    id: 'magic',
    label: translate('Magic components'),
    entries: spellProps(element)
  };

  return magicGroup
}

function findGroup(groups, id) {
  return groups.find(g => g.id === id);
}

function findEntry(entries, id) {
  return entries.find(g => g.id === id);
}

function deleteById(array, id) {
  const index = array.findIndex(g => g.id === id)
  if (index > -1) {
    array.splice(index, 1)
  }
}
