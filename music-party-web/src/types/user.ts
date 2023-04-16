export interface UserInfoType {
    username: string;
    age: number;
    sex: number;
}
export interface UserStateType {
    userInfo: UserInfoType;
    token: string;
    refreshToken: string;
}