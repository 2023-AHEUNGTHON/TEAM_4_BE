package aheung.likelion.twoneone.domain.enums;

import aheung.likelion.twoneone.util.Parser;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {
    APPLICATION("application"),
    AUDIO("audio"),
    IMAGE("image"),
    VIDEO("video"),
    TEXT("text"),
    FONT("font");

    private final String value;

    public static FileType from(String value) {
        String contentType = Parser.getContentType(value);
        for (FileType type : FileType.values()) {
            if (type.getValue().equals(contentType)) {
                return type;
            }
        }
        return null;
    }
}
