declare namespace Todo {
  interface Task{
    id: number|string,
    title: string,
    description: string,
    labels: Array<string>,
    completed: boolean
  }
  interface Label{
    id:string,
    title:string,
    color:string
  }
}
