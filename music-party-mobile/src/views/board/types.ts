export type state = 'TODO' | 'INPROGRESS' | 'TESTING' | 'DONE'
export interface card {
  id: string | number,
  title?: string,
  description: string,
  order: number,
  state: state,
}

