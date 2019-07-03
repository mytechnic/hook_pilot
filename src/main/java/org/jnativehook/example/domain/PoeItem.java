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
public class PoeItem {
    private String itemType;
    private String text;
    private String name;
    private String type;
    private List<String> optionList;
}
