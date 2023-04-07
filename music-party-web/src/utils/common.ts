// 通用的 js 函数

/**
 * 防抖函数
 * @param cb 回调函数
 * @param time 延迟时间
 */
export const imageStabilization = (cb: Function, delay = 300) => {
  let timeId: NodeJS.Timeout

  return () => {
    if (timeId) clearTimeout(timeId)

    timeId = setTimeout(() => {
      cb()
    }, delay)
  }
}

/**
 * 节流函数
 * @param cb 回调函数
 * @param time 延迟时间
 */
export const theThrottle = (cb: Function, delay = 300) => {
  let preDate = new Date().getTime()

  return () => {
    const nowDate = new Date().getTime()
    if (nowDate - preDate > delay) {
      cb()
      preDate = nowDate
    }
  }
}

/**
 * 判断是否是一个对象
 * @param query 需要进行判断的参数
 * @return {boolean}
 */
export function isObject(query: any): boolean {
  return Object.prototype.toString.call(query) === '[object Object]'
}

/**
 * 将一个对象转换成路由参数
 * @param query 
 * @return {string}
 */
export function outputUrl(query: { [index: string]: string | number }) {
  if (!isObject(query)) {
    return console.warn('query 必须是一个 Object')
  }

  let url = ''

  for (const key in query) {
    if (Object.prototype.hasOwnProperty.call(query, key)) {
      url += `&${key}=${encodeURIComponent(query[key])}`
    }
  }

  return url.slice(1)
}

import JSEncrypt from "jsencrypt";

const defaultPublickKey = `-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAov7q9c1SjnOS4TB3RRB+
b/laKcLzYNSuvupXeZj4VguqKFGLoUFvCHWuKStqdscaDq9JwWh1q1eS+HqbV1G7
S1IUccj3IcFUwQZFaHufCgGg1fT8j/nl6iniefNbDtIbcJk0KTlk5tlCT72xGRrr
08tbnTj6y0uivGMVtrDQTWaduqSlogFs6Rj4Dg0sJ2kTmRr9LFMno/wLlcV475+5
H4uo3UIYckE8RoP8iY+4ZO/8ZxCiKn0e/xmgFhIDqD9Y+yVIEwBV50RY1P+nuNMC
wgEkUG3oNo+XHrzrXJHJL/Z/GIYGrbYwm24uWcJhbeZbdiguc/GjyzfAgzzI3liD
5QIDAQAB
-----END PUBLIC KEY-----
`

export function encodeRSA(word: string, keyStr: string) {

  //这个是公钥,有入参时用入参，没有入参用默认公钥
  keyStr = keyStr ? keyStr : defaultPublickKey;
  //创建对象
  const jsRsa = new JSEncrypt();
  //设置公钥
  jsRsa.setPublicKey(keyStr);
  //返回加密后结果
  return jsRsa.encrypt(word);
}


