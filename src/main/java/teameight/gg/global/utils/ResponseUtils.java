package teameight.gg.global.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import teameight.gg.global.stringCode.ErrorCodeEnum;
import teameight.gg.global.responseDto.ApiResponse;
import teameight.gg.global.responseDto.ErrorResponse;
import teameight.gg.global.stringCode.SuccessCodeEnum;

@Getter
@NoArgsConstructor
public class ResponseUtils {

    public static <T> ApiResponse<T> successWithData(T response) {
        return new ApiResponse<>(true, response, null);
    }

    public static ApiResponse<?> success(SuccessCodeEnum successCodeEnum) {
        return new ApiResponse<>(true, successCodeEnum.toString(), null);
    }
    public static ApiResponse<?> error(String message, int status) {
        return new ApiResponse<>(false, null, new ErrorResponse(message, status));
    }

    public static ApiResponse<?> customError(ErrorCodeEnum errorCodeEnum) {
        return new ApiResponse<>(false, null, new ErrorResponse(errorCodeEnum));
    }
}
