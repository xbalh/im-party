package com.im.imparty.music.api;

import com.alibaba.fastjson.JSONObject;
import com.im.imparty.music.interceptor.MusicRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@FeignClient(value = "music", url = "${NeteaseCloudMusicApi.url}", configuration = {MusicRequestInterceptor.class})
public interface MusicApi {

    @GetMapping("/song/url/v1")
    JSONObject getSong(@RequestParam("id") List<String> ids, @RequestParam("level") String level);

    @GetMapping("/song/url")
    JSONObject getSongOld(@RequestParam("id") List<String> ids);

    @GetMapping("/search")
    JSONObject search(@RequestParam("keywords") String keywords, @RequestParam(value = "type", required = false) Integer type,
                      @RequestParam(value = "offset", required = false) Integer offset, @RequestParam(value = "limit", required = false) Integer limit);

}
