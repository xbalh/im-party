package com.im.imparty.api.music;

import com.alibaba.fastjson.JSONObject;
import com.im.imparty.api.interceptor.MusicRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FeignClient(value = "login", url = "${NeteaseCloudMusicApi.url}", configuration = {MusicRequestInterceptor.class})
public interface LoginApi {

    @GetMapping("/login/qr/create")
    JSONObject qrCreate(@RequestParam("key") String key, @RequestParam("qrimg") Boolean qrImg);

    @GetMapping("/login/qr/key")
    JSONObject qrKey();

    @GetMapping("/login/qr/check")
    JSONObject qrCheck(@RequestParam(value = "key", required = false) String key);

    @GetMapping(value = "/login/status", headers = {""})
    JSONObject getStatus();


    @GetMapping(value = "/user/account")
    JSONObject getUserCount();
    //5cdd1cc7-7a86-446a-b1b0-9bec2f99043b
    // MUSIC_A_T=1491020046006; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/neapi/clientlog; HTTPOnly;MUSIC_A_T=1491020046006; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/eapi/clientlog; HTTPOnly;MUSIC_A_T=1491020046006; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/eapi/feedback; HTTPOnly;MUSIC_R_T=1491020163452; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/weapi/clientlog; HTTPOnly;MUSIC_R_T=1491020163452; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/neapi/clientlog; HTTPOnly;MUSIC_R_T=1491020163452; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/openapi/clientlog; HTTPOnly;MUSIC_R_T=1491020163452; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/api/clientlog; HTTPOnly;MUSIC_A_T=1491020046006; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/weapi/clientlog; HTTPOnly;MUSIC_A_T=1491020046006; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/neapi/feedback; HTTPOnly;MUSIC_A_T=1491020046006; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/weapi/feedback; HTTPOnly;MUSIC_A_T=1491020046006; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/wapi/clientlog; HTTPOnly;MUSIC_R_T=1491020163452; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/eapi/feedback; HTTPOnly;MUSIC_R_T=1491020163452; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/eapi/clientlog; HTTPOnly;MUSIC_R_T=1491020163452; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/weapi/feedback; HTTPOnly;MUSIC_R_T=1491020163452; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/api/feedback; HTTPOnly;__csrf=10b226b3cd413af72483284f30e704db; Max-Age=1296010; Expires=Wed, 26 Apr 2023 09:49:21 GMT; Path=/;;MUSIC_SNS=; Max-Age=0; Expires=Tue, 11 Apr 2023 09:49:11 GMT; Path=/;MUSIC_R_T=1491020163452; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/wapi/feedback; HTTPOnly;MUSIC_A_T=1491020046006; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/wapi/feedback; HTTPOnly;MUSIC_A_T=1491020046006; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/openapi/clientlog; HTTPOnly;MUSIC_R_T=1491020163452; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/neapi/feedback; HTTPOnly;MUSIC_U=c3ad21ccd9c33f1f6b2abcdfd1b56dfb109f506cf07ef902b789961b19c8b07c8a08bd5bf851808fda0f55ca60824f1e67f08dfd5de80216758ab293c62bea8bfb23af057f26a700a0d2166338885bd7; Max-Age=15552000; Expires=Sun, 08 Oct 2023 09:49:11 GMT; Path=/; HTTPOnly;MUSIC_A_T=1491020046006; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/api/clientlog; HTTPOnly;MUSIC_A_T=1491020046006; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/api/feedback; HTTPOnly;MUSIC_R_T=1491020163452; Max-Age=2147483647; Expires=Sun, 29 Apr 2091 13:03:18 GMT; Path=/wapi/clientlog; HTTPOnly
}
