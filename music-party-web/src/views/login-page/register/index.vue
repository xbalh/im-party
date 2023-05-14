<template>
  <div>
    <form>
			<p>用户名<br />
				<input type="text" class="textinput" placeholder="请输入用户名" />
			</p>
			<p>密码<br />
				<input type="password" class="textinput" placeholder="请输入密码捏~" />
			</p>
			<p>再次输入密码<br />
				<input type="password" class="textinput" placeholder="请再输入一次密码~" />
			</p>
			<div class="chakra-modal_body css-79z5gx" id="chakra-modal--body:r1:">
				<div class="css-k008qs">
					<input placeholder="你的网易云名字" class="chakra-input css-10uavu1" value/>
					<button type="button" class="chakra-button css-1poo3jr">搜索</button>
				</div>
			</div>
			<p>
				<a href="登录界面.html">注册好了再点这里哦~</a>
			</p>
		</form>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator"
import Request from '@/utils/requestInstance'
import { namespace } from "vuex-class"

const permissionsStore = namespace('permissionsStore')
const userStore = namespace('userStore')

@Component
export default class Login extends Vue {
  @permissionsStore.Mutation('setPermissions') setPermissions!: Function
  @userStore.Mutation('setToken') setToken!: Function

  async handleLogin() {
    const res = await Request.post('/login')
    const path = (this.$route.query.redirectUrl || '/') as string | undefined

    this.setToken(res.data.token)
    this.$router.replace({ path })

    try {
      const res = await Request.get('/promise')
      this.setPermissions(res.data)
    } catch (error) {
      console.error(error, '获取权限出错了')
    }
  }
}
</script>

<style>
		body {
			/* background: url('../../../assets/images/背景.jpg');
			background-repeat: repeat-x;
			background-attachment: fixed; */
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
	<style data-emotion="css k008qs" data-s>
		.css-k008qs {
			display: -webkit-box;
			display: -webkit-flex;
			display: -ms-flexbox;
			display: flex;
		}
	</style>
	<style data-emotion="css 10uavu1" data-s>
		.css-10uavu1 {
			width: 100%;
			min-width: 0px;
			outline: 2px solid transparent;
			outline-offset: 2px;
			position: relative;
			-webkit-appearance: none;
			-moz-appearance: none;
			-ms-appearance: none;
			appearance: none;
			transition-property: var(--chakra-transition-property-common);
			transition-duration: var(--chakra-transition-duration-normal);
			font-size: var(--chakra-fontSizes-md);
			-webkit-padding-start: var(--chakra-space-4);
			padding-inline-start: var(--chakra-space-4);
			-webkit-padding-end: var(--chakra-space-4);
			padding-inline-end: var(--chakra-space-4);
			height: var(--chakra-sizes-10);
			border-radius: var(--chakra-radii-md);
			border: 1px solid;
			border-color: inherit;
			background: inherit;
			-webkit-flex: 1;
			-ms-flex: 1;
			flex: 1;
		}
		
		.css-10uavu1:disabled,
		.css-10uavu1[disabled],
		.css-10uavu1[aria-disabled=true],
		.css-10uavu1[data-disabled] {
			opacity: 0.4;
			cursor: not-allowed;
		}
		
		.css-10uavu1:hover,
		.css-10uavu1[data-hover] {
			border-color: var(--chakra-colors-gray-300);
		}
		
		.css-10uavu1[aria-readonly=true],
		.css-10uavu1[readonly],
		.css-10uavu1[data-readonly] {
			box-shadow: var(--chakra-shadows-none)!important;
			-webkit-user-select: all;
			-moz-user-select: all;
			-ms-user-select: all;
			user-select: all;
		}
		
		.css-10uavu1[aria-invalid=true],
		.css-10uavu1[data-invalid] {
			border-color: #E53E3E;
			box-shadow: 0 0 0 1px #E53E3E;
		}
		
		.css-10uavu1:focus-visible,
		.css-10uavu1[data-focus-visible] {
			z-index: 1;
			border-color: #3182ce;
			box-shadow: 0 0 0 1px #3182ce;
		}
	</style>
	<style data-emotion="css 1poo3jr" data-s>
		.css-1poo3jr {
			display: -webkit-inline-box;
			display: -webkit-inline-flex;
			display: -ms-inline-flexbox;
			display: inline-flex;
			-webkit-appearance: none;
			-moz-appearance: none;
			-ms-appearance: none;
			appearance: none;
			-webkit-align-items: center;
			-webkit-box-align: center;
			-ms-flex-align: center;
			align-items: center;
			-webkit-box-pack: center;
			-ms-flex-pack: center;
			-webkit-justify-content: center;
			justify-content: center;
			-webkit-user-select: none;
			-moz-user-select: none;
			-ms-user-select: none;
			user-select: none;
			position: relative;
			white-space: nowrap;
			vertical-align: middle;
			outline: 2px solid transparent;
			outline-offset: 2px;
			line-height: 1.2;
			border-radius: var(--chakra-radii-md);
			font-weight: var(--chakra-fontWeights-semibold);
			transition-property: var(--chakra-transition-property-common);
			transition-duration: var(--chakra-transition-duration-normal);
			height: var(--chakra-sizes-10);
			min-width: var(--chakra-sizes-10);
			font-size: var(--chakra-fontSizes-md);
			-webkit-padding-start: var(--chakra-space-4);
			padding-inline-start: var(--chakra-space-4);
			-webkit-padding-end: var(--chakra-space-4);
			padding-inline-end: var(--chakra-space-4);
			background: var(--chakra-colors-gray-100);
			margin-left: var(--chakra-space-2);
		}
		
		.css-1poo3jr:focus-visible,
		.css-1poo3jr[data-focus-visible] {
			box-shadow: var(--chakra-shadows-outline);
		}
		
		.css-1poo3jr:disabled,
		.css-1poo3jr[disabled],
		.css-1poo3jr[aria-disabled=true],
		.css-1poo3jr[data-disabled] {
			opacity: 0.4;
			cursor: not-allowed;
			box-shadow: var(--chakra-shadows-none);
		}
		
		.css-1poo3jr:hover,
		.css-1poo3jr[data-hover] {
			background: var(--chakra-colors-gray-200);
		}
		
		.css-1poo3jr:hover:disabled,
		.css-1poo3jr[data-hover]:disabled,
		.css-1poo3jr:hover[disabled],
		.css-1poo3jr[data-hover][disabled],
		.css-1poo3jr:hover[aria-disabled=true],
		.css-1poo3jr[data-hover][aria-disabled=true],
		.css-1poo3jr:hover[data-disabled],
		.css-1poo3jr[data-hover][data-disabled] {
			background: var(--chakra-colors-gray-100);
		}
		
		.css-1poo3jr:active,
		.css-1poo3jr[data-active] {
			background: var(--chakra-colors-gray-300);
		}
	</style>