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
public class LegionRequest {

    private Query query;
    private Sort sort;

    @NoArgsConstructor
    @Setter
    @Getter
    @ToString
    public static class Query {
        private List<Stats> stats;
        private Status status;
        private String name;
        private String type;
    }

    @NoArgsConstructor
    @Setter
    @Getter
    @ToString
    public static class Stats {
        private List<String> filters;
        private String type;
    }

    @NoArgsConstructor
    @Setter
    @Getter
    @ToString
    public static class Status {
        private String option;
    }

    @NoArgsConstructor
    @Setter
    @Getter
    @ToString
    public static class Sort {
        private String price;
    }
}
