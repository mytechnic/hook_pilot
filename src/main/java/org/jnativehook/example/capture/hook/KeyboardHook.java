package org.jnativehook.example.capture.hook;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.jnativehook.example.capture.item.Clipboard;
import org.jnativehook.example.domain.LegionRequest;
import org.jnativehook.example.domain.LegionResult;
import org.jnativehook.example.domain.PoeItem;
import org.jnativehook.example.domain.TradeResult;
import org.jnativehook.example.parse.PathOfExileItemParse;
import org.jnativehook.example.util.HttpClient;
import org.jnativehook.example.ui.Const;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class KeyboardHook implements NativeKeyListener {
    private ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL)
            .configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {
        if (event.getKeyCode() != NativeKeyEvent.VC_F2) {
            return;
        }

        Const.poeTrade.getItemPrice().setText("검색 중 ...");

        String s = Clipboard.executeCopy();

        PathOfExileItemParse parse = new PathOfExileItemParse(s);
        PoeItem poeItem = parse.getItem();

        LegionResult legionResult = getLegionSearch(poeItem);
        TradeResult tradeResult = getTradeFetch(legionResult);

        if (tradeResult == null) {
            Const.poeTrade.getItemPrice().setText("가격 정보를 찾을 수 없습니다.");
            return;
        }

        int findIndex = -1;
        for (int i = 0; i < tradeResult.getResult().size(); i++) {
            log.debug("tradeResult.getResult().get(i).getListing().getPrice() = {}", tradeResult.getResult().get(i).getListing().getPrice());
            if (tradeResult.getResult().get(i).getListing().getPrice() != null) {
                findIndex = i;
                break;
            }
        }

        if (findIndex > -1) {
            TradeResult.Price z = tradeResult.getResult().get(findIndex).getListing().getPrice();
            Const.poeTrade.getItemPrice().setText("가격 정보 : " + z.getAmount() + " " + z.getCurrency());
        } else {
            Const.poeTrade.getItemPrice().setText("가격 정보가 없습니다.");
        }
    }


    private TradeResult getTradeFetch(LegionResult legionResult) {

        if (legionResult.getTotal() == 0 || legionResult.getResult().size() == 0) {
            return null;
        }

        String url = "https://poe.game.daum.net/api/trade/fetch/";
        for (int i = 0; i < 10; i++) {
            if (i > 0) {
                url += ",";
            }
            url += legionResult.getResult().get(i);
        }
        url += "?query=" + legionResult.getId();

        try {
            String referer = "https://poe.game.daum.net/trade/search/Legion/" + legionResult.getId();
            String result = HttpClient.get(url, referer);
            return mapper.readValue(result, TradeResult.class);
        } catch (IOException e) {
            log.error("{}", e);
        }

        return null;
    }

    public LegionResult getLegionSearch(PoeItem poeItem) {

        LegionRequest request = new LegionRequest();
        LegionRequest.Query query = new LegionRequest.Query();
        ArrayList<LegionRequest.Stats> statsList = new ArrayList<>();
        LegionRequest.Stats stats = new LegionRequest.Stats();
        stats.setFilters(new ArrayList<>());
        stats.setType("and");
        statsList.add(stats);
        query.setStats(statsList);
        LegionRequest.Status status = new LegionRequest.Status();
        status.setOption("online");
        query.setStatus(status);
        query.setName(poeItem.getName());
        query.setType(poeItem.getType());
        request.setQuery(query);

        LegionRequest.Sort sort = new LegionRequest.Sort();
        sort.setPrice("asc");
        request.setSort(sort);

        try {
            String body = mapper.writeValueAsString(request);
            String result = HttpClient.post("https://poe.game.daum.net/api/trade/search/Legion", body);
            return mapper.readValue(result, LegionResult.class);
        } catch (IOException e) {
            log.error("{}", e);
        }
        return new LegionResult();
    }
}
