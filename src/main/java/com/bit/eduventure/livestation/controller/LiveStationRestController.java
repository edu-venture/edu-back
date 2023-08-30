package com.bit.eduventure.livestation.controller;

import com.bit.eduventure.livestation.service.LiveStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/live")
@RequiredArgsConstructor
@RestController
public class LiveStationRestController {
    private final LiveStationService liveStationService;

    //채널 아이디가 있지만 강의중 것을 확인할 때
    @GetMapping("/info/{channelId}")
    public ResponseEntity<?> getChannelInfo(@PathVariable String channelId) {
        return liveStationService.getChannelInfo(channelId);
    }

    @GetMapping("/url/{channelId}")
    public ResponseEntity<?> getServiceURL(@PathVariable String channelId) {
        return liveStationService.getServiceURL(channelId);
    }

    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<?> deleteChannel(@PathVariable String channelId) {
        return liveStationService.deleteChannel(channelId);
    }

}
