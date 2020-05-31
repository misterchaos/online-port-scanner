package cn.hellochaos.portscanner.controller.handler;

import cn.hellochaos.portscanner.entity.dto.ResultBean;
import cn.hellochaos.portscanner.exception.bizException.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 全局异常处理器
 * @date 2019-08-12 19:19
 */
@Slf4j
@RestControllerAdvice
@CrossOrigin
public class GlobalExceptionHandler implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse,
                                         Object o, Exception e) {
        log.info("请求异常" + e.getMessage());
        e.printStackTrace();
        return null;
    }

    @ExceptionHandler(cn.hellochaos.portscanner.exception.bizException.BizException.class)
    public ResultBean<?> bizException(BizException e) {
        return new ResultBean<>(e);
    }


    @ExceptionHandler(NullPointerException.class)
    public ResultBean<?> nullPointerException(NullPointerException e) {
        e.printStackTrace();
        return new ResultBean<>(new BizException("请求中缺少了必要的参数"));
    }


    @ExceptionHandler(
            org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    public ResultBean<?> maxUploadexception(
            org.springframework.web.multipart.MaxUploadSizeExceededException e) {
        e.printStackTrace();
        return new ResultBean<>(new BizException("您上传的文件大小超过限制"));
    }


    @ExceptionHandler(Throwable.class)
    public ResultBean<?> unknownException(Throwable e) {
        e.printStackTrace();
        return new ResultBean<>(new BizException("发生了未知的异常，请告知程序员哥哥前来修复"));
    }

    @ExceptionHandler(
            {org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class,
                    org.springframework.http.converter.HttpMessageNotReadableException.class,
                    org.springframework.web.bind.MissingServletRequestParameterException.class})
    public ResultBean<?> http400Handler(Exception e) {
        e.printStackTrace();
        if (e instanceof org.springframework.web.method.annotation.MethodArgumentTypeMismatchException) {
            return new ResultBean<>(new BizException("请求中的数据类型和服务器声明的类型不匹配"));
        } else if (e.getMessage().contains("Cannot deserialize value of type `java.time.LocalDateTime` from String")) {
            return new ResultBean<>(new BizException("请求的json数据中的时间格式不正确"));
        } else if (e.getMessage().contains("JSON parse error")) {
            return new ResultBean<>(new BizException("请求的json数据格式不正确"));
        } else if (e.getMessage().contains("Required request body is missing")) {
            return new ResultBean<>(new BizException("缺少必要的请求体"));
        } else if (e instanceof org.springframework.web.bind.MissingServletRequestParameterException) {
            return new ResultBean<>(new BizException("缺少必要的请求参数"));
        }
        return new ResultBean<>(new BizException("缺少必要的请求体或请求体格式错误"));
    }

    @ExceptionHandler(
            org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResultBean<?> http405Handler(
            org.springframework.web.HttpRequestMethodNotSupportedException e) {
        e.printStackTrace();
        return new ResultBean<>(new BizException("服务器并不支持您所使用的请求方法"));
    }

    @ExceptionHandler(
            org.springframework.web.HttpMediaTypeNotSupportedException.class)
    public ResultBean<?> http405Handler(
            org.springframework.web.HttpMediaTypeNotSupportedException e) {
        e.printStackTrace();
        return new ResultBean<>(new BizException("您的请求体格式不正确"));
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResultBean<?> http500Handler(IllegalStateException e) {
        e.printStackTrace();
        return new ResultBean<>(new BizException("您的请求体格式不正确"));
    }

}
