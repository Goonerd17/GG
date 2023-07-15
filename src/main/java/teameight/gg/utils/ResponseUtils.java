package teameight.gg.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import teameight.gg.dto.ApiResponse;
import teameight.gg.dto.ErrorResponse;

@Getter
@NoArgsConstructor
public class ResponseUtils {

    public static <T> ApiResponse<T> ok(T response) {
        return new ApiResponse<>(true,response,null);
    }

    public static ApiResponse<?> error(String message, int status) {
        return new ApiResponse<>(false, null, new ErrorResponse(message, status));
    }
}
