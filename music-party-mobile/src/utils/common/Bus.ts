// 先定义一个类型，emit作为发布(传递)，on作为订阅(接收)
// name是事件名称，callback是一个回调函数
type BusClass = {
    emit:(name:string) => void
    on:(name:string, callback:Function) => void
}
// 定义多种参数类型
type PramsKey = string | number | symbol
// 定义一个调用中心,可以接收多个参数
type List = {
   [key:PramsKey]: Array<Function>
}
class Bus implements BusClass {
    list: List
    constructor (){
        // 初始化list
        this.list = {}   
    }
    // 发布事件,...args解构多个参数
    emit (name:string,...args:Array<any>){
        // 获取到list里的数据
        let evnentName:Array<Function> = this.list[name]
        evnentName.forEach(fn =>{
            fn.apply(this, args)
        })
    }
    // 接收事件
    on (name:string, callback:Function){
        // 如果是第一次注册，则为空；如果被多次注册，则采用之前注册的名字
        let fn:Array<Function> = this.list[name] || []
        fn.push(callback)
        this.list[name] = fn
    }
}
// 导出bus
export default new Bus()