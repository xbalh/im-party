<template>
    <div class="outer-wrapper">
        <!-- <el-menu class="el-menu-demo" mode="horizontal" @select="handleSelect" background-color="#545c64" text-color="#fff"
            active-text-color="#ffd04b">
            <el-submenu index="2">
                <template slot="title">{{ userName }}</template>
                <el-menu-item index="2-1">设置</el-menu-item>
                <el-menu-item index="2-2">退出登陆</el-menu-item>
            </el-submenu>
        </el-menu> -->
        <div class="wrapper">
            <h3>群聊</h3>

            <div class="message-panel">
                <msg-box v-for="(item, index) of msgList" :key="index + Math.random()" :uname="item.name"
                    :content="item.msg" :isself="item.isSelf"></msg-box>

            </div>
            <div class="input-area">
                <textarea class="input" v-model="msg" @keydown="onKeyDown" @keydown.enter.prevent="handleEnter"></textarea>
                <!-- <button class="send" @click="sendMsg">发送</button> -->
                <el-button class="send-btn" @click="sendMsgHandle">发送</el-button>

            </div>
        </div>
    </div>
</template>
  
<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import msgBox from './msgBox.vue';
import { Msg } from '@/types/chat';
//   import headMenu from '../../components/head-menu.vue';

@Component({
    components: {
        msgBox
    }
})
export default class Chat extends Vue {
    @Prop({ type: Function }) sendMsg!: Function;
    @Prop({ type: String }) userName: string = '';
    content: string = 'hahhahaha';
    msg: string = '';
    msgList: Array<any> = [];

    sendMsgHandle() {
        this.sendMsg(this.msg);
        this.msg = '';
    }
    public receiveMsg(msgInfo: Msg) {
        this.msgList.push({
            name: msgInfo.from === this.userName ? '我' : msgInfo.from,
            msg: msgInfo.msg,
            isSelf: msgInfo.from === this.userName
        })
        console.log(this.msgList)
    }

    onKeyDown(event: any) {
        //如果按下了ctrl+enter
        if (event.keyCode === 13 && event.ctrlKey) {
            // 按下了组合键，插入一个换行符
            const textarea = event.target;
            const start = textarea.selectionStart;
            const end = textarea.selectionEnd;
            const value = textarea.value;
            textarea.value = value.substring(0, start) + "\n" + value.substring(end);
            textarea.selectionStart = textarea.selectionEnd = start + 1;
            this.msg = textarea.value
            event.preventDefault();
            return
        }
    }

    handleEnter(event: any) {
        if (event.ctrlKey) return
        event.preventDefault();
        const reg = /^\s+$/g;
        if (reg.test(this.msg)) return
        this.sendMsgHandle()
    }

}



// export default {
//     name: 'Chat',
//     props: {
//         sendMsg: Function
//     },
//     data() {
//         return {
//             content: 'hahhahaha',
//             userName: '',
//             msg: '',
//             msgList: [
//                 {
//                     name: '用户A',
//                     msg: 'test233',
//                     isSelf: false
//                 },
//                 {
//                     name: '我自己',
//                     msg: 'test666',
//                     isSelf: true
//                 },
//             ],
//         }
//     },
//     components: {
//         msgBox,
//         // headMenu,
//     },
//     mounted() {
//         this.userName = this.$route.query.name;
//         // this.$socket.emit('login', {
//         //     userid: 1,
//         //     username: this.userName
//         // })
//     },
//     methods: {
//         //   handleSelect(key, keyPath) {},
//         sendMsgHandle: function () {
//             const data = {
//                 name: this.userName,
//                 msg: this.msg
//             }
//             this.sendMsg(this.msg);
//             this.msg = '';
//         },
//         receiveMsg: function (msgInfo) {
//             this.msgList.push({
//                 name: msgInfo.from === this.userName ? '我' : msgInfo.from,
//                 msg: msgInfo.msg,
//                 isSelf: msgInfo.from === this.userName
//             })
//         }
//     },

// }
</script>
  
  <!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.outer-wrapper {

    width: 100%;
    height: 100%;
    padding: 0;
    margin: 0;
}

.wrapper {
    /* position: relative; */
    /* width: 650px; */
    background-color: #fff;
    opacity: 0.85;
    /* height: 610px; */
    border-radius: 4px;
    border: 1px #ebebeb solid;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin: 0 auto;
    display: flex;
    top: 80px;
    flex-direction: column;
    align-items: center;
    padding-top: 10px;
    height: 100%;
}

.message-panel {
    width: 85%;
    height: 80%;
    border-top: 1px #ebebeb solid;
    border-bottom: 1px #ebebeb solid;
    overflow: hidden;
    overflow-x: hidden;
    padding: 10px;

}

.input-area {
    width: 85%;
    border-radius: 4px;
    height: 20%;
    margin-top: 20px;
    border: 0 solid black;
    overflow: hidden;
}

.send-btn {
    position: relative;
    float: right;
    bottom: 80px;
}

.input {
    width: 100%;
    height: 100%;
    border: 0px #ebebeb solid;
    border-radius: 4px;
    outline: none;
    padding: 5px;
    resize: vertical;
}

.fix-top {
    position: fixed;
    width: 100%;
    height: 50px;
    top: 0;
}
</style>    
  