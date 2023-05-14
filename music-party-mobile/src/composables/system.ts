import UAParser from 'ua-parser-js';
import {useAuthStore} from '@/store';
import {isArray, isString} from '@/utils';
import pkg from '~/package.json';

interface AppInfo {
  name: string;
  title: string;
  desc: string;
  version: string,
}

interface Package {
  name: string;
  version: string;
}

const pkgWithType = pkg as Package;

export function useAppInfo(): AppInfo {
  const {VITE_APP_NAME: name, VITE_APP_TITLE: title, VITE_APP_DESC: desc} = import.meta.env;

  return {
    name,
    title,
    desc,
    version: pkgWithType.version
  };
}

/** 获取设备信息 */
export function useDeviceInfo() {
  const parser = new UAParser();
  const result = parser.getResult();
  return result;
}

/** 权限判断 */
export function usePermission() {
  const auth = useAuthStore();

  function hasPermission(permission: Auth.RoleType | Auth.RoleType[]) {
    const {userRole} = auth.userInfo;

    let has = userRole === 'admin';
    if (!has) {
      if (isArray(permission)) {
        has = (permission as Auth.RoleType[]).includes(userRole);
      }
      if (isString(permission)) {
        has = (permission as Auth.RoleType) === userRole;
      }
    }
    return has;
  }

  return {
    hasPermission
  };
}
