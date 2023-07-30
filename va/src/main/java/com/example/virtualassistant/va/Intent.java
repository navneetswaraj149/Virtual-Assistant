package com.example.virtualassistant.va;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Intent {
    int id;
    String name;
    List<Action> actions;

    public Intent(int id, String name) {
        this.id=id;
        this.name=name;
    }
}
