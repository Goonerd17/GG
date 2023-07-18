package teameight.gg.global.exception;

import teameight.gg.global.stringCode.ErrorCodeEnum;

public class InvalidConditionException extends IllegalArgumentException{

    ErrorCodeEnum errorCodeEnum;

    public InvalidConditionException(ErrorCodeEnum errorCodeEnum) {
        this.errorCodeEnum = errorCodeEnum;
    }
}
