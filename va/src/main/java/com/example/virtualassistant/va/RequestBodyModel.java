package com.example.virtualassistant.va;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestBodyModel {
  private int responseId;
  private int id;
  private String intentId;
  private String intentId2;
}
