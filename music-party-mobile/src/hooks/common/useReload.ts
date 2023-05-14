import { nextTick } from 'vue';
import useBoolean from './useBoolean';

export default function useReload() {
  // 重载的标志
  const { bool: reloadFlag, setTrue, setFalse } = useBoolean(true);

  async function handleReload(duration = 0) {
    setFalse();
    await nextTick();

    if (duration > 0) {
      setTimeout(() => {
        setTrue();
      }, duration);
    }
  }

  return {
    reloadFlag,
    handleReload
  };
}
