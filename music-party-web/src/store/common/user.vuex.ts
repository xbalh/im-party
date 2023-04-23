import { Module } from "vuex";

interface UserInfoType {
  username: string;
  age: number;
  sex: number;
}
interface UserStateType {
  userInfo: UserInfoType;
  token: string;
  refreshToken: string;
}

const userStore: Module<UserStateType, any> = {
  namespaced: true,

  state: {
    userInfo: {
      username: "",
      age: 20,
      sex: 1,
    },
    token: "",
    refreshToken: ""
  },

  getters: {
    userInfo: (state) => state.userInfo,
    getToken: (state) => state.token,
    getRefreshToken: (state) => state.refreshToken
  },

  mutations: {
    setToken(state, token) {
      state.token = token;
      localStorage.setItem("token", token);
    },
    setRefreshToken(state, refreshToken) {
      state.refreshToken = refreshToken;
      localStorage.setItem("refreshToken", refreshToken);
    },
    setUserInfo(state, userInfo: UserInfoType) {
      state.userInfo = userInfo;
      localStorage.setItem("userInfo", JSON.stringify(userInfo));
    },
  },
};

export default userStore;
