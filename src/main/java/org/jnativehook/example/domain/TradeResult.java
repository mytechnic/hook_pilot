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
public class TradeResult {

    private List<Result> result;

    @NoArgsConstructor
    @Setter
    @Getter
    @ToString
    public static class Result {
        private String id;
        private Listing listing;
    }

    @NoArgsConstructor
    @Setter
    @Getter
    @ToString
    public static class Listing {
        private Price price;
    }

    @NoArgsConstructor
    @Setter
    @Getter
    @ToString
    public static class Price {
        private String amount;
        private String currency;
        private String type;
    }
}

