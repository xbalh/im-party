<template>
  <div>
    <el-form ref="ruleForm" :model="ruleForm" status-icon :rules="rules">
      <p>{{ $t('login.username') }}<br />
        <!-- <el-input v-model="loginData.ruleForm.username" placeholder="请输入用户名"></el-input> -->
        <el-form-item label="用户名：" prop="username">
          <el-input v-model="ruleForm.username" autocomplete="off" />
        </el-form-item>
      </p>
      <p>{{ $t('login.password') }}<br />
        <!-- <el-input v-model="loginData.ruleForm.password" placeholder="请输入密码"></el-input> -->
        <el-form-item label="密码：" prop="password">
          <el-input v-model="ruleForm.password" type="password" autocomplete="off" />
        </el-form-item>
      </p>
      <p>
        <input id="remember" type="checkbox" /><label for="smtxt">记住密码</label>
      </p>
      <p>
        <el-button @click="submitForm('ruleForm')">{{ $t('login.signin') }}</el-button>
        <el-button class="login-btn" @click="resetForm">重置</el-button>
      </p>
      <p class="smtxt">没有账号？
        <router-link to="/register">点这里注册</router-link>
      </p>
    </el-form>
  </div>
</template>

<script lang="ts">
import { reactive, toRefs, ref } from "vue"
import { Vue, Component } from "vue-property-decorator"
import Request from '@/utils/requestInstance'
import { namespace } from "vuex-class"
import { AxiosResponse } from "axios"
import { LoginData, LoginForm } from "@/types/login"
import { UserInfoType } from "@/types/user"
import { Form } from "element-ui"
import { encodeRSA } from '@/utils/common'
import defaultPageApi from "@/api/default-page";

const permissionsStore = namespace('permissionsStore')
const userStore = namespace('userStore')

@Component
export default class Login extends Vue {
  @permissionsStore.Mutation('setPermissions') setPermissions!: Function
  @userStore.Mutation('setToken') setToken!: Function
  @userStore.Mutation('setRefreshToken') setRefreshToken!: Function
  @userStore.Mutation('setUserInfo') setUserInfo!: Function

  loginData: LoginData = reactive(new LoginData())
  ruleForm: LoginForm = this.loginData.ruleForm

  rules = {
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' },
      { min: 1, max: 16, message: '账号的长度在1-16之间', trigger: 'blur' },
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { min: 1, max: 20, message: '密码的长度在1-20之间', trigger: 'blur' },
    ],
  }


  submitForm = (formName: string) => {
    if (!this.$refs[formName]) return;
    (this.$refs[formName] as Form).validate((valid: boolean) => {
      if (valid) {
        this.handleLogin()
        // this.getCurrentUserInfo()
      } else {
        console.log('error submit!')
        return false
      }
    })
  }

  //重置
  resetForm = () => {
    this.ruleForm.username = "";
    this.ruleForm.password = "";
  }

  async handleLogin() {
    const enPassword = encodeRSA(this.ruleForm.password, '')
    const formData = new FormData();
    formData.append("username", this.ruleForm.username)
    formData.append("password", enPassword as string)
    // this.$message.error('账号或密码错误！');
    try {
      const res: AxiosResponse = await Request.post(
        '/login',
        {
          data: {
            username: this.ruleForm.username,
            password: enPassword
          }
        }
      )
      const result = res.data
      if (result.code !== 200 || !result.data.accessToken || result.data.accessToken == '') {
        this.$message.error('账号或密码错误！');
        return
      }

      this.setToken(result.data.accessToken)
      this.setRefreshToken(result.data.refreshToken)

    } catch (error) {
      console.log(error, "login error")
    }

    const path = (this.$route.query.redirectUrl || '/') as string | undefined
    this.$router.replace({ path })


  }

  async getCurrentUserInfo() {
    try {
      const res = await Request.get(defaultPageApi.user.info, {}, { isNeedToken: true })
      const userInfo: UserInfoType = {
        username: res.data.msg! as string,
        age: 24,
        sex: 0
      }
      this.setUserInfo(userInfo)
    } catch (error) {
      console.error(error, '获取用户信息出错了')
    }
  }

}
</script>

<style>
body {
  /* background: url('../assets/images/登录界面背景图.jpg'); */
  /* background-repeat: repeat-x; */
  /* background-attachment: fixed; */
}

form {
  /*设置form大小*/
  width: 350px;
  height: 250px;
  /*背景*/
  /*background-image: url(CSS/images/登录界面背景图.jpg);*/
  background-color: #E1E9EF;
  /*设置透明度*/
  opacity: 70%;
  /*设置定位置*/
  text-align: center;
  padding: 120px 100px;
  /*文本大小*/
  font-size: 18px;
  /*设置圆角边框*/
  border-radius: 10px;
  /*增加内边距*/
  margin: 120px auto;
}

.textinput {
  /*设置宽高*/
  height: 40px;
  width: 300px;
  /*内边距*/
  padding: 0 35px;
  /*去除边框*/
  border: none;
  /*设置背景颜色*/
  background-color: #F8F9F9;
  /*字体大小*/
  font-size: 15px;
  /*给文本框加上阴影*/
  box-shadow: 0px 1px 1px rgba(255, 255, 255, 0.7), inset 0px 2px 5px cornflowerblue;
  /*加上圆角*/
  border-radius: 5px;
  /*输入文本颜色*/
  color: saddlebrown;
}

/*筛选input标签中 type 为“submit”的 进行渲染*/

input[type="submit"] {
  /*设置高*/
  width: 110px;
  height: 40px;
  /*内部文本居中*/
  text-align: center;
  /*圆角边框*/
  border-radius: 5px;
  /*设置字体*/
  font: 16px "黑体";
  /*设置背景颜色*/
  background-color: #C0C6CB;
}
</style>