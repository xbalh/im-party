import {defineStore} from 'pinia'
import {Base} from 'diagram-js/lib/model'
import Canvas from 'diagram-js/lib/core/Canvas'
import ElementRegistry from 'diagram-js/lib/core/ElementRegistry'

type ModelerStore = {
  activeElement: any
  activeElementId: any
  modeler: any
  moddle: any
  modeling: any
  commandStack: any,
  canvas: Canvas | null
  elementRegistry: ElementRegistry | null
}

const defaultState: ModelerStore = {
  activeElement: undefined,
  activeElementId: undefined,
  modeler: null,
  moddle: null,
  modeling: null,
  canvas: null,
  elementRegistry: null,
  commandStack: null,
}

export const useModelStore = defineStore('modeler', {
  state: () => defaultState,
  getters: {
    getActive: (state) => state.activeElement,
    getActiveId: (state) => state.activeElementId,
    getModeler: (state) => state.modeler,
    getModdle: (state) => state.moddle,
    getModeling: (state) => state.modeling,
    getCommandStack: (state) => state.commandStack,
    getCanvas: (state) => state.canvas,
    getElRegistry: (state) => state.elementRegistry
  },
  actions: {
    setModeler(modeler: any) {
      this.modeler = modeler
    },
    setModules<K extends keyof ModelerStore>(key: K, module: any) {
      this[key] = module
    },
    setElement(element: Base, id: string) {
      this.activeElement = element
      this.activeElementId = id
    }
  }
})
