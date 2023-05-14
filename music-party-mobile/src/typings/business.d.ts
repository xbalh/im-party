declare namespace Auth {
  type RoleType = keyof typeof import('@/enum').EnumUserRole;

  interface UserInfo {
    userId: string;
    userName: string;
    userRole: RoleType;
    userAvatar?: string;
  }
}

declare namespace UserManagement {
  interface User extends ApiUserManagement.User {
    role: Auth.RoleType
  }

  /**
   * 用户性别
   * - 0: 女
   * - 1: 男
   */
  type GenderKey = NonNullable<User['gender']>;

  /**
   * 用户状态
   * - 1: 启用
   * - 2: 禁用
   * - 3: 冻结
   * - 4: 软删除
   */
  type UserStatusKey = NonNullable<User['userStatus']>;
}

declare namespace FormManagement {
  interface Form extends ApiForm.Form {

  }
  type FormStatusKey = NonNullable<Form['status']>;
}
