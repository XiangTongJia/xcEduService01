package com.xuecheng.framework.exception;


import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

//控制器增强
@ControllerAdvice
public class ExceptionCatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;
    protected   static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();


    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customeException(CustomException customException){

        //记录日志
        LOGGER.error("exception err{}"+customException.getMessage());

        ResultCode resultCode = customException.getResultCode();
        return  new ResponseResult(resultCode);
    }
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e){

        //记录日志
        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(), e);
        if(EXCEPTIONS == null)
            EXCEPTIONS = builder.build();
        final ResultCode resultCode = EXCEPTIONS.get(e.getClass());
        final ResponseResult responseResult;
        if (resultCode != null) {
            responseResult = new ResponseResult(resultCode);
        } else {
            responseResult = new ResponseResult(CommonCode.SERVER_ERROR);
        }
        return responseResult;

    }
    static{
//在这里加入一些基础的异常类型判断
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALIDPARAM);
    }
}
