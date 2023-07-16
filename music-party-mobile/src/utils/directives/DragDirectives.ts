import Bus from "@/utils/common/Bus";

// 拖拽的指令
const drag = {
  beforeMount(el: any, binding: any) {
    // 自定义属性，判断是否可拖拽
    if (!binding.value) return
    el.onmousemove = null
    el.onmousedown = null
    el.ontouchstart = null
    el.ontouchmove = null
    el.style.cssText += ';cursor:move;'
    // dragDom.style.cssText += ';bottom:0px;'

    // 获取原有属性 ie dom元素.currentStyle 火狐谷歌 window.getComputedStyle(dom元素, null);
    const sty = (function () {
      if ((document.body as any).currentStyle) {
        // 在ie下兼容写法
        return (dom: any, attr: any) => dom.currentStyle[attr]
      }
      return (dom: any, attr: any) => getComputedStyle(dom, null)[attr]
    })()

    el.ontouchstart = (e: any) => {
      const firstTime = new Date().getTime();
      let isMove = false;
      // 鼠标按下，计算当前元素距离可视区的距离
      const disX = e.changedTouches[0].clientX
      const disY = e.changedTouches[0].clientY
      const screenWidth = document.body.clientWidth // body当前宽度
      const screenHeight = document.documentElement.clientHeight // 可见区域高度(应为body高度，可某些环境下无法获取)

      const dragDomWidth = el.offsetWidth // 对话框宽度
      const dragDomheight = el.offsetHeight // 对话框高度

      const minDragDomLeft = el.offsetLeft
      const maxDragDomLeft = screenWidth - el.offsetLeft - dragDomWidth

      const minDragDomTop = el.offsetTop
      const maxDragDomTop = screenHeight - el.offsetTop - dragDomheight

      // 获取到的值带px 正则匹配替换
      let styL = sty(el, 'left')
      // 为兼容ie
      if (styL === 'auto') styL = '0px'
      let styT = sty(el, 'top')

      // console.log(styL)
      // 注意在ie中 第一次获取到的值为组件自带50% 移动之后赋值为px
      if (styL.includes('%')) {
        styL = +document.body.clientWidth * (+styL.replace(/%/g, '') / 100)
        styT = +document.body.clientHeight * (+styT.replace(/%/g, '') / 100)
      } else {
        styL = +styL.replace(/px/g, '')
        styT = +styT.replace(/px/g, '')
      }

      el.ontouchmove = function (e) {
        isMove = true
        // 通过事件委托，计算移动的距离
        let left = e.changedTouches[0].clientX - disX
        let top = e.changedTouches[0].clientY - disY
        // 边界处理
        if (-(left) > minDragDomLeft) {
          left = -(minDragDomLeft)
        } else if (left > maxDragDomLeft) {
          left = maxDragDomLeft
        }

        if (-(top) > minDragDomTop) {
          top = -(minDragDomTop)
        } else if (top > maxDragDomTop) {
          top = maxDragDomTop
        }

        // 移动当前元素
        el.style.cssText += `;left:${left + styL}px;top:${top + styT}px;`
      }

      el.ontouchend = function (e: any) {
        const lastTime = new Date().getTime();
        document.ontouchstart = null
        document.ontouchmove = null
        if (!isMove) {
          console.log("这次是点击")
          binding.value()
        }
        let left = e.changedTouches[0].clientX - disX
        if (left > maxDragDomLeft) {
          console.log("贴住右边了")
          Bus.emit('dragDotRight', true)
        }
      }
      return false
    }
    el.onmousedown = (e: any) => {
      let isMove = false
      const firstTime = new Date().getTime();
      // 鼠标按下，计算当前元素距离可视区的距离
      const disX = e.clientX
      const disY = e.clientY
      const screenWidth = document.body.clientWidth // body当前宽度
      const screenHeight = document.documentElement.clientHeight // 可见区域高度(应为body高度，可某些环境下无法获取)

      const dragDomWidth = el.offsetWidth // 对话框宽度
      const dragDomheight = el.offsetHeight // 对话框高度

      const minDragDomLeft = el.offsetLeft
      const maxDragDomLeft = screenWidth - el.offsetLeft - dragDomWidth

      const minDragDomTop = el.offsetTop
      const maxDragDomTop = screenHeight - el.offsetTop - dragDomheight

      // 获取到的值带px 正则匹配替换
      let styL = sty(el, 'left')
      // 为兼容ie
      if (styL === 'auto') styL = '0px'
      let styT = sty(el, 'top')

      // console.log(styL)
      // 注意在ie中 第一次获取到的值为组件自带50% 移动之后赋值为px
      if (styL.includes('%')) {
        styL = +document.body.clientWidth * (+styL.replace(/%/g, '') / 100)
        styT = +document.body.clientHeight * (+styT.replace(/%/g, '') / 100)
      } else {
        styL = +styL.replace(/px/g, '')
        styT = +styT.replace(/px/g, '')
      }

      el.onmousemove = function (e) {
        isMove = true
        // 通过事件委托，计算移动的距离
        let left = e.clientX - disX
        let top = e.clientY - disY
        // 边界处理
        if (-(left) > minDragDomLeft) {
          left = -(minDragDomLeft)
        } else if (left > maxDragDomLeft) {
          left = maxDragDomLeft
        }

        if (-(top) > minDragDomTop) {
          top = -(minDragDomTop)
        } else if (top > maxDragDomTop) {
          top = maxDragDomTop
        }

        // 移动当前元素
        el.style.cssText += `;left:${left + styL}px;top:${top + styT}px;`
      }

      el.onmouseup = function (e: any) {
        const lastTime = new Date().getTime();
        document.onmousemove = null
        document.onmousedown = null
        if (!isMove) {
          console.log("这次是点击")
          binding.value()
        }
        if (e.clientX >= disX && e.clientY >= disY) {
          console.log("贴住右边了")
          Bus.emit('dragDotRight', true)
        }
      }
      return false
    }
  }
}
// 挂载，注册
const DragDirectives = {
  install: function (app: any) {
    app.directive('dialogDrag', drag)
  }
};
export default DragDirectives;