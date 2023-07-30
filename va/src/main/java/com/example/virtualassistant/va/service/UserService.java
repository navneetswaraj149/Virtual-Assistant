package com.example.virtualassistant.va.service;

import com.example.virtualassistant.va.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class UserService {
  @Autowired private ObjectMapper objectMapper;
  private static List<Response> responses;
  private static List<GreetingResponse> greetings;

  public UserService() {
    loadOptions();
  }

  private static final String JSON_FILE_PATH =
      "C:\\Users\\navne\\IdeaProjects\\va\\src\\main\\resources\\Db.json";

  private void loadOptions() {

    ObjectMapper objectMapper = new ObjectMapper();

    try {
      File file = new File("C:\\Users\\navne\\IdeaProjects\\va\\src\\main\\resources\\Db.json");
      greetings = objectMapper.readValue(file, new TypeReference<List<GreetingResponse>>() {});

    } catch (IOException e) {
      e.printStackTrace();
    }
    for (GreetingResponse greetingResponse : greetings) {
      responses = greetingResponse.getResponses();
    }
  }

  public static Response getResponseById(int responseId) {
    for (Response response : responses) {
      if (response.getId() == responseId) {
        return response;
      }
    }
    return null;
  }
  public static List<String> getRulesByName(String intentId) {
    List<String> ruleNames = new ArrayList<>();

    for (GreetingResponse greetingResponse : greetings) {
      for (Response response : greetingResponse.getResponses()) {
        for (Intent intent : response.getIntents()) {
          for (Action action : intent.getActions()) {
            if (action.getIntentId().equals(intentId)) {
              for (Rule rule : action.getRules()) {
                ruleNames.add(rule.getName());
              }
            }
          }
        }
      }
    }

    return ruleNames;
  }
  public static List<String> getRulesByName2(String intentId2, String intentId) {
    List<String> ruleNames2 = new ArrayList<>();

    for (GreetingResponse greetingResponse : greetings) {
      for (Response response : greetingResponse.getResponses()) {
        for (Intent intent : response.getIntents()) {
          for (Action action : intent.getActions()) {
            if (action.getIntentId().equals(intentId)) {
              for (Rule rule : action.getRules()) {
                if (rule.getName().equals(intentId2)){
                  ruleNames2.add(rule.getId());
                }
              }
            }
          }
        }
      }
    }

    return ruleNames2;
  }


    public static Intent getIntentById(int responseId, int intentId) {
    Response response = getResponseById(responseId);
    if (response != null) {
      for (Intent intent : response.getIntents()) {
        if (intent.getId() == intentId) {
          return intent;
        }
      }
    }
    return null;
  }

//  public List<String> getActionByQuery(String userQuery) {
//    List<String> actionNames = new ArrayList<>();
//
//    for (Response response : responses) {
//      for (Intent intent : response.getIntents()) {
//        if (isMatchedQuery(userQuery, intent)) {
//          for (Action action : intent.getActions()) {
//            actionNames.add(action.getName());
//          }
//        }
//      }
//    }
//
//    return actionNames;
//  }

  private boolean isMatchedQuery(String userQuery, Intent intent) {
    return userQuery.contains(intent.getName());
  }
}
