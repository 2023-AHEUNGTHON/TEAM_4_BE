package aheung.likelion.twoneone.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    NEWS("시사/뉴스", "news"),
    FOOD("푸드", "food"),
    CULTURE("문화/예술", "culture"),
    ECONOMY("경제/금융", "economy"),
    IT("IT/기술", "it"),
    HEALTH("건강/의학", "health"),
    BUSINESS("비즈니스", "business"),
    ETC("기타", "etc");

    private final String name;
    private final String value;

    @JsonCreator
    public static Category from(String value) {
        for (Category category : Category.values()) {
            if (category.getValue().equals(value)) {
                return category;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
