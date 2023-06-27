
//根据指定key对List进行分组
export function groupByKey<T>(array: Array<T>, key: string) {
    let groups: any = {};
    for (let i = 0; i < array.length; i++) {
        const group = array[i][key as keyof T];
        //这里利用对象的key值唯一性的，创建数组
        groups[group] = groups[group] || [];
        groups[group].push(array[i]);
    }
    return groups;
}

