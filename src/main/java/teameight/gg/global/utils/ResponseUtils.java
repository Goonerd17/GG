package teameight.gg.global.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import teameight.gg.global.exception.ErrorCodeEnum;
import teameight.gg.global.responseDto.ApiResponse;
import teameight.gg.global.responseDto.ErrorResponse;

@Getter
@NoArgsConstructor
public class ResponseUtils {

    public static <T> ApiResponse<T> ok(T response) {
        return new ApiResponse<>(true,response,null);
    }

    public static ApiResponse<?> error(String message, int status) {
        return new ApiResponse<>(false, null, new ErrorResponse(message, status));
    }

    public static ApiResponse<?> tokenError(ErrorCodeEnum errorCodeEnum) {
        return new ApiResponse<>(false, null, new ErrorResponse(errorCodeEnum));
    }
}
