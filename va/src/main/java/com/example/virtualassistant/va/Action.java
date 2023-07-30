package com.example.virtualassistant.va;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Action {
    private String name;
    private String intentId;
    List<Rule> rules;
}
