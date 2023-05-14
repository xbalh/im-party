/** 用户性别 */
export const genderLabels: Record<UserManagement.GenderKey, string> = {
  0: 'female',
  1: 'male',
  2: 'unknown'
};

export const genderOptions: { value: UserManagement.GenderKey; label: string }[] = [
  {value: '0', label: genderLabels['0']},
  {value: '1', label: genderLabels['1']},
  {value: '2', label: genderLabels['2']},
];

/** 用户状态 */
export const userStatusLabels: Record<UserManagement.UserStatusKey, string> = {
  1: 'active',
  2: 'disabled',
  4: 'deleted'
};

export const userStatusOptions: { value: UserManagement.UserStatusKey; label: string }[] = [
  {value: '1', label: userStatusLabels['1']},
  {value: '2', label: userStatusLabels['2']},
  {value: '4', label: userStatusLabels['4']}
];

/** 用户状态 */
export const formStatusLabels: Record<FormManagement.FormStatusKey, string> = {
  1: 'active',
  0: 'disabled',
};

export const formStatusOptions: { value: FormManagement.FormStatusKey; label: string }[] = [
  {value: '1', label: formStatusLabels['1']},
  {value: '0', label: formStatusLabels['0']},
];
