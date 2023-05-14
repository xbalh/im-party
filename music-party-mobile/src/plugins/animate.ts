export function animate(node: HTMLElement, animationName: string, callBack?: () => void) {

  node.classList.add('animate__animated', `animate__${animationName}`)

  function handleAnimationEnd() {
    node.classList.remove('animate__animated', `animate__${animationName}`)
    node.removeEventListener('animationend', handleAnimationEnd)
    if (callBack) {
      callBack()
    }
  }

  node.addEventListener('animationend', handleAnimationEnd)
}
