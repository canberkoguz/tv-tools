package com.elsevier.tvtools.repository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.annotation.PostConstruct;
import com.elsevier.tvtools.model.TV;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository {

  private final Map<String, LinkedList<String>> queueMap = new HashMap<>();

  @PostConstruct
  public void initQueues() {
    TV.NAMES.forEach(tvName -> queueMap.put(tvName, new LinkedList<>()));
  }

  public void saveMessage(String tvName, String text) {
    LinkedList<String> messageQueue = queueMap.get(tvName);
    messageQueue.add(text);
  }

  public String getMessage(String tvName) {
    LinkedList<String> messageQueue = queueMap.get(tvName);
    return messageQueue.poll();
  }

}
