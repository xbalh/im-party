declare namespace StorageInterface {
  /** localStorage的存储数据的类型 */
  interface Session {
    demoKey: string;
  }

  /** localStorage的存储数据的类型 */
  interface Local {
    token: string;
    refreshToken: string;
    userInfo: Auth.UserInfo;
    themeSettings: ThemeConfig.Config;
  }
}
