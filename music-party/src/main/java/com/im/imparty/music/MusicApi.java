package com.im.imparty.music;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@FeignClient(value = "music", url = "${NeteaseCloudMusicApi.url}")
public interface MusicApi {

    @GetMapping("/song/url/v1")
    String getSong(@RequestParam("id") List<String> ids, @RequestParam("level") String level);

    @GetMapping("/song/url")
    String getSongOld(@RequestParam("id") List<String> ids);
}
