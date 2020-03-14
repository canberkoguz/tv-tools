package com.elsevier.tvtools.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@EnableScheduling
public class MessageService {

  private static final SseEmitter.SseEventBuilder KEEP_ALIVE_EVENT = SseEmitter.event().comment("keep alive");
  private final Map<String, List<SseEmitter>> emitterMap = new HashMap<>();

  public void addEmitter(final String tvName, final SseEmitter emitter) {
    final List<SseEmitter> emitters = emitterMap.getOrDefault(tvName, new CopyOnWriteArrayList<>());
    emitters.add(emitter);
    emitterMap.put(tvName, emitters);
  }

  public void removeEmitter(final String tvName, final SseEmitter emitter) {
    final List<SseEmitter> emitters = emitterMap.get(tvName);
    if (!CollectionUtils.isEmpty(emitters)) {
      emitters.remove(emitter);
    }
  }

  @Async
  public void sendMessage(final String tvName, final String text) {
    final List<SseEmitter> emitters = emitterMap.getOrDefault(tvName, Collections.emptyList());
    notifyEmitters(emitters, SseEmitter.event().data(text));
  }

  @Async
  @Scheduled(fixedRate = 15000)
  public void keepALive() {
    emitterMap.values().forEach(emitters -> notifyEmitters(emitters, KEEP_ALIVE_EVENT));
  }

  private void notifyEmitters(List<SseEmitter> emitters, SseEmitter.SseEventBuilder keepAliveEvent) {
    List<SseEmitter> deadEmitters = new ArrayList<>();
    emitters.forEach(emitter -> {
      try {
        emitter.send(keepAliveEvent);
      } catch (Exception e) {
        deadEmitters.add(emitter);
      }
    });
    emitters.removeAll(deadEmitters);
  }

}