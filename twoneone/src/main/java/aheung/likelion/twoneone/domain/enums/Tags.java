package aheung.likelion.twoneone.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Tags {
    FILE("파일", "file"),
    MEMO("메모", "memo"),
    LINK("링크", "link"),
    ETC("기타", "etc");

    private final String ko;
    private final String en;
}
