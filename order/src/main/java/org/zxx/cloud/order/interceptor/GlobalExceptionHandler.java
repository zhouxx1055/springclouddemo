package org.zxx.cloud.order.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.zxx.cloud.commenum.BaseResultError;
import org.zxx.cloud.exception.BizException;
import org.zxx.cloud.exception.JwtException;
import org.zxx.cloud.module.vo.ResultVO;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 自定义异常JwtException
     */
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO<Object> JwtExceptionHandler(JwtException e) {
        log.error("", e);
        return ResultVO.fail(BaseResultError.API_LOGIN_INVALID_TOKEN);
    }

    /**
     * 自定义异常APIException
     */
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO<Object> APIExceptionHandler(BizException e) {
        log.error("业务异常", e);
        return ResultVO.fail(BaseResultError.API_NETWORK_ERROR);
    }

    /**
     * 方法参数错误异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO<Object> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("方法参数错误异常:", e);
        List<String> list = new ArrayList<String>();        // 从异常对象中拿到ObjectError对象
        if (!e.getBindingResult().getAllErrors().isEmpty()) {
            for (ObjectError error : e.getBindingResult().getAllErrors()) {
                list.add(error.getDefaultMessage().toString());
            }
        }
        // 然后提取错误提示信息进行返回
        return ResultVO.fail(BaseResultError.API_INVALID_PARAMETER, list);
    }
}