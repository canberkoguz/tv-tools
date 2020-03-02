package com.elsevier.tvtools.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import javax.annotation.PostConstruct;
import com.elsevier.tvtools.model.TV;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

  private final Map<String, Queue<String>> queueMap = new HashMap<>();

  @PostConstruct
  public void initQueues() {
    TV.NAMES.forEach(tvName -> queueMap.put(tvName, new LinkedList<>()));
  }

  public void sendMessage(String tvName, String text) {
    Queue<String> messageQueue = queueMap.get(tvName);
    messageQueue.add(text);
  }

  public String receiveMessage(String tvName) {
    Queue<String> messageQueue = queueMap.get(tvName);
    return messageQueue.poll();
  }

}
