package org.jnativehook.example.parse;

import lombok.extern.slf4j.Slf4j;
import org.jnativehook.example.ui.Const;
import org.jnativehook.example.domain.PoeItem;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PathOfExileItemParse {
    private String s;


    public PathOfExileItemParse(String s) {
        this.s = s;
    }

    public PoeItem getItem() {

        Const.poeTrade.getItemContent().setText(s);

        PoeItem poeItem = new PoeItem();
        poeItem.setItemType(getItemType(s));
        poeItem.setText(getText(s, poeItem.getItemType()));
        poeItem.setName(getName(s, poeItem.getItemType()));
        poeItem.setType(getType(s, poeItem.getItemType()));
        poeItem.setOptionList(getItemOption(s, poeItem.getItemType()));
        log.debug("poeItem : {}", poeItem);

        return poeItem;
    }

    public String getItemType(String s) {
        if (s == null || s.length() == 0) {
            return null;
        }
        String[] z = s.split("\n");
        if (z.length > 3 && z[2].trim().endsWith("주얼")) {
            return "주얼";
        }

        z = z[0].split(":");
        String itemType = z[1].trim();

        return itemType;
    }

    private String getText(String s, String itemType) {
        if (s == null || s.length() == 0) {
            return null;
        }

        String[] z = s.split("\n");
        if ("점술 카드".equals(itemType)) {
            return escape(z[1]);
        } else if ("고유".equals(itemType)) {
            return escape(z[1]) + escape(z[2]);
        } else {
            return escape(z[1]) + escape(z[2]);
        }
    }

    private String getName(String s, String itemType) {
        if (s == null || s.length() == 0) {
            return null;
        }

        if (!"고유".equals(itemType)) {
            return null;
        }

        String[] z = s.split("\n");
        return escape(z[1]);
    }

    private String getType(String s, String itemType) {
        if (s == null || s.length() == 0) {
            return null;
        }
        String[] z = s.split("\n");

        if (itemType.equals("점술 카드")) {
            return escape(z[1]);
        } else if (itemType.endsWith("화폐")) {
            return escape(z[1]);
        }

        return escape(z[2]);
    }

    private String escape(String s) {
        if (s.startsWith("---")) {
            return "";
        }
        return s;
    }

    private List<String> getItemOption(String s, String itemType) {
        if (s == null || s.length() == 0) {
            return null;
        }
        String[] z = s.split("\n");
        List<String> optionList = new ArrayList<>();

        int startPosition = 0;
        int maxLineCount = 0;
        for (int i = 0; i < z.length; i++) {

            if (z[i].startsWith("아이템 레벨:")) {
                startPosition = i + 2;
                continue;
            }

            if (z[i].startsWith("---")) {
                maxLineCount++;
                continue;
            }

            if (z[i].startsWith("메모:")) {
                maxLineCount--;
                continue;
            }

            if (z[i].endsWith(" 아이템")) {
                maxLineCount--;
                continue;
            }
        }

        int offsetLineCount = 0;
        for (int i = 0; i < z.length; i++) {
            String v = z[i];

            if (v.startsWith("---")) {
                offsetLineCount++;
                continue;
            }

            if ("주얼".equals(itemType)) {
                boolean isStartOption = startPosition <= i;
                if (isStartOption && offsetLineCount < maxLineCount) {
                    optionList.add(v);
                }
            }

            if ("희귀".equals(itemType)) {
                boolean isStartOption = startPosition <= i;
                if (isStartOption && offsetLineCount <= maxLineCount) {
                    optionList.add(v);
                }
            }

            if ("고유".equals(itemType)) {
                boolean isStartOption = startPosition <= i;
                if (isStartOption && offsetLineCount < maxLineCount) {
                    optionList.add(v);
                }
            }
        }

        return optionList;
    }

}
