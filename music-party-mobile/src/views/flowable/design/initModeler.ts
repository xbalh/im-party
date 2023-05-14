import {markRaw} from 'vue'
import {useModelStore} from '@/store'
import BpmnModeler from "bpmn-js/lib/Modeler";
import EventEmitter from "@/utils/flow/EventEmitter";

export default function (
  options: any,
  emit: (event: any, ...args: any[]) => void
) {

  const store = useModelStore()

  store.getModeler && store.getModeler.destroy()
  store.setModeler(null)
  const modeler = new BpmnModeler(options)
  store.setModeler(markRaw(modeler))
  store.setModules('moddle', markRaw(modeler.get('moddle')))
  store.setModules('modeling', markRaw(modeler.get('modeling')))
  store.setModules('canvas', markRaw(modeler.get('canvas')))
  store.setModules('commandStack', markRaw(modeler.get('commandStack')))
  store.setModules('elementRegistry', markRaw(modeler.get('elementRegistry')))

  EventEmitter.emit('modeler-init', modeler)

  modeler.on('commandStack.changed', async (event: any) => {
    try {
      const {xml} = await modeler.saveXML({format: true})
      emit('update:xml', xml)
      emit('command-stack-changed', event)
    } catch (error) {
      console.error(error)
    }
  })
}
