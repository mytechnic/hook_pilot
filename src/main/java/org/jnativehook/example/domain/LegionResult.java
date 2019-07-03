package org.jnativehook.example.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class LegionResult {
    private List<String> result;
    private String id;
    private int total;
}
