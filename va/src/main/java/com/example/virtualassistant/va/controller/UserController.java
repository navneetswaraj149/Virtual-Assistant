package com.example.virtualassistant.va.controller;

import com.example.virtualassistant.va.*;
import com.example.virtualassistant.va.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
public class UserController {
  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/greet")
  public ResponseEntity<?> getActionNamesByResponseAndIntentId(
      @RequestBody RequestBodyModel requestBody) {
    int responseId = requestBody.getResponseId();
    int id = requestBody.getId();
    String intentId = requestBody.getIntentId();
    String intentId2 = requestBody.getIntentId2();

    if (id == 0 && responseId == 0 && intentId == null && intentId2 == null) {

      ObjectMapper objectMapper = new ObjectMapper();
      File file = new File("C:\\Users\\navne\\IdeaProjects\\va\\src\\main\\resources\\Db.json");

      try {
        List<GreetingResponse> greetingResponses =
            objectMapper.readValue(file, new TypeReference<List<GreetingResponse>>() {});
        if (!greetingResponses.isEmpty()) {
          GreetingResponse firstGreetingResponse = greetingResponses.get(0);
          List<Response> responses = firstGreetingResponse.getResponses();
          List<ObjectNode> modifiedResponses = new ArrayList<>();

          for (Response response : responses) {
            ObjectNode intentNode = JsonNodeFactory.instance.objectNode();
            intentNode.put("id", response.getId());
            intentNode.put("message", response.getMessage());
            modifiedResponses.add(intentNode);
          }

          ObjectNode responseNode = JsonNodeFactory.instance.objectNode();
          responseNode.put("greeting", firstGreetingResponse.getGreeting());
          responseNode.put("responses", objectMapper.valueToTree(modifiedResponses));

          HttpHeaders headers = new HttpHeaders();
          headers.setContentType(MediaType.APPLICATION_JSON);
          return new ResponseEntity<>(responseNode, headers, HttpStatus.OK);
        } else {
          return ResponseEntity.notFound().build();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
    } else if (id == 0 && intentId == null && intentId2 == null) {

      Response response = UserService.getResponseById(responseId);
      if (response == null) {
        return ResponseEntity.notFound().build();
      }

      List<Intent> intents = response.getIntents();

      List<ObjectNode> modifiedIntents = new ArrayList<>();
      for (Intent intent : intents) {
        ObjectNode intentNode = JsonNodeFactory.instance.objectNode();
        intentNode.put("id", intent.getId());
        intentNode.put("name", intent.getName());
        modifiedIntents.add(intentNode);
      }

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      return new ResponseEntity<>(modifiedIntents, headers, HttpStatus.OK);
    } else if (intentId == null && intentId2 == null) {

      Intent intent = UserService.getIntentById(responseId, id);
      if (intent == null) {
        return ResponseEntity.notFound().build();
      }
      List<Action> actions = intent.getActions();

      List<ObjectNode> modifiedAction = new ArrayList<>();
      for (Action action : actions) {
        ObjectNode actionNode = JsonNodeFactory.instance.objectNode();
        actionNode.put("id", action.getIntentId());
        actionNode.put("name", action.getName());
        modifiedAction.add(actionNode);
      }
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      return new ResponseEntity<>(modifiedAction, headers, HttpStatus.OK);
    } else if (intentId2 == null) {
      List<String> ruleNames = UserService.getRulesByName(intentId.toLowerCase(Locale.ROOT));

      if (!ruleNames.isEmpty()) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(ruleNames, headers, HttpStatus.OK);
      } else {
        return ResponseEntity.notFound().build();
      }
    } else if (responseId != 0 && id != 0 && intentId != null && intentId2 != null) {

      List<String> ruleNames2 =
          UserService.getRulesByName2(
              intentId2.toLowerCase(Locale.ROOT), intentId.toLowerCase(Locale.ROOT));

      if (!ruleNames2.isEmpty()) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(ruleNames2, headers, HttpStatus.OK);
      } else {
        return ResponseEntity.notFound().build();
      }
    }

    return null;
  }

  //  @PostMapping("/greet/3")
  //  public ResponseEntity<List<String>> getActionByQuery(@RequestBody String userQuery) {
  //    List<String> actionNames = userService.getActionByQuery(userQuery);
  //
  //    if (actionNames != null) {
  //      HttpHeaders headers = new HttpHeaders();
  //      headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
  //
  //      return new ResponseEntity<>(actionNames, headers, HttpStatus.OK);
  //    } else {
  //      return ResponseEntity.ok().build();
  //    }
  //  }

  // Rest of the code...

  private static final String JSON_FILE_PATH =
      "C:\\Users\\navne\\IdeaProjects\\va\\src\\main\\resources\\Db.json";

  @Autowired private ObjectMapper objectMapper;

  //  @PostMapping("/append-response")
  //  public ResponseEntity<String> appendResponse(@RequestBody Response newResponse) {
  //
  //    ObjectMapper objectMapper = new ObjectMapper();
  //    File file = new File(JSON_FILE_PATH);
  //    try {
  //
  //      List<GreetingResponse> greetingResponses =
  //          objectMapper.readValue(
  //              file,
  //              objectMapper
  //                  .getTypeFactory()
  //                  .constructCollectionType(List.class, GreetingResponse.class));
  //
  //      if (greetingResponses == null) {
  //        greetingResponses = new ArrayList<>();
  //      }
  //
  //      for (GreetingResponse greetingResponse : greetingResponses) {
  //        List<Response> responses = greetingResponse.getResponses();
  //        responses.add(newResponse);
  //      }
  //
  //      objectMapper.writeValue(file, greetingResponses);
  //
  //      return ResponseEntity.status(HttpStatus.CREATED).body("Response added successfully.");
  //    } catch (IOException e) {
  //      e.printStackTrace();
  //      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
  //          .body("Failed to add response.");
  //    }
  //  }
  @PostMapping("/append-response")
  public ResponseEntity<String> appendResponse(@RequestBody Response newResponse) {

    ObjectMapper objectMapper = new ObjectMapper();
    File file = new File(JSON_FILE_PATH);
    try {
      List<GreetingResponse> greetingResponses =
          objectMapper.readValue(
              file,
              objectMapper
                  .getTypeFactory()
                  .constructCollectionType(List.class, GreetingResponse.class));

      if (greetingResponses == null) {
        greetingResponses = new ArrayList<>();
      }

      for (GreetingResponse greetingResponse : greetingResponses) {
        List<Response> responses = greetingResponse.getResponses();
        int insertIndex = 0;
        for (int i = 0; i < responses.size(); i++) {
          if (newResponse.getId() <= responses.get(i).getId()) {
            insertIndex = i;
            break;
          } else {
            insertIndex = i + 1;
          }
        }
        responses.add(insertIndex, newResponse);
      }

      objectMapper.writeValue(file, greetingResponses);

      return ResponseEntity.status(HttpStatus.CREATED).body("Response added successfully.");
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Failed to add response.");
    }
  }

  private List<Response> getAllResponses() throws IOException {
    List<GreetingResponse> greetingResponses =
        objectMapper.readValue(
            new File(JSON_FILE_PATH), new TypeReference<List<GreetingResponse>>() {});
    List<Response> responses = new ArrayList<>();

    for (GreetingResponse greetingResponse : greetingResponses) {
      responses.addAll(greetingResponse.getResponses());
    }

    return responses;
  }
}
