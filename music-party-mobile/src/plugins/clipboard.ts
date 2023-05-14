export function clipboard(text: string, toastText = 'Copied to Clipboard') {
  try {
    navigator.clipboard.writeText(text).then(() => {
      window.$snackBar?.info(toastText)
    })
  }catch (e) {
    // In Production navigator clipboard will return undefined due to unsafe http protocol, https will fine
    window.$snackBar?.error("Copied to Clipboard failed!")
  }
}
