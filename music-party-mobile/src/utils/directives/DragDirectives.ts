// 拖拽的指令
const drag = {
    beforeMount(el: any, binding: any) {
      // 自定义属性，判断是否可拖拽
      if (!binding.value) return
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
   
      el.onmousedown = (e: any) => {
        // 鼠标按下，计算当前元素距离可视区的距离
        const disX = e.clientX - el.offsetLeft
        const disY = e.clientY - el.offsetTop
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
   
        document.onmousemove = function (e) {
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
   
        document.onmouseup = function (e: any) {
          document.onmousemove = null
          document.onmouseup = null
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