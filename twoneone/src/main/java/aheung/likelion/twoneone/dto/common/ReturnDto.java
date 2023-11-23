package aheung.likelion.twoneone.dto.common;

import aheung.likelion.twoneone.domain.enums.Code;
import lombok.Data;

@Data
public class ReturnDto<T> {
    private final Integer code;
    private final String httpStatus;
    private final String message;
    private final T data;

    public static <T> ReturnDto<T> ok(T data) {
        return new ReturnDto<>(Code.OK.getCode(), Code.OK.getHttpStatus(), Code.OK.getMessage(), data);
    }

    public static ReturnDto<Void> ok() {
        return new ReturnDto<>(Code.OK.getCode(), Code.OK.getHttpStatus(), Code.OK.getMessage(), null);
    }

    public static ReturnDto<?> fail(Code code) {
        return new ReturnDto<>(code.getCode(), code.getHttpStatus(), code.getMessage(), null);
    }
}
