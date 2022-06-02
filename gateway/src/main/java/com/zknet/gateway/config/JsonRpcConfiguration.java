package com.zknet.gateway.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.googlecode.jsonrpc4j.ErrorResolver;
import com.googlecode.jsonrpc4j.HttpStatusCodeProvider;
import com.googlecode.jsonrpc4j.InvocationListener;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImplExporter;
import com.zknet.gateway.exception.BizCodeEnum;
import com.zknet.gateway.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.googlecode.jsonrpc4j.ErrorResolver.JsonError.ERROR_NOT_HANDLED;
import static com.googlecode.jsonrpc4j.ErrorResolver.JsonError.PARSE_ERROR;

@Configuration
@Slf4j
public class JsonRpcConfiguration {


    @Bean
    public static AutoJsonRpcServiceImplExporter autoJsonRpcServiceImplExporter() {
        AutoJsonRpcServiceImplExporter exporter = new AutoJsonRpcServiceImplExporter();
        exporter.setHttpStatusCodeProvider(new HttpStatusCodeProvider() {

            @Override
            public int getHttpStatusCode(int resultCode) {
                return 200;
            }

            @Override
            public Integer getJsonRpcCode(int httpStatusCode) {
                if (httpStatusCode == PARSE_ERROR.code) {
                    return PARSE_ERROR.code;
                }
                return null;
            }
        });

        exporter.setErrorResolver(new ErrorResolver() {
            @Override
            public JsonError resolveError(Throwable t, Method method, List<JsonNode> arguments) {
                if (t instanceof BizException) {
//                    log.error("method:{} execute request param:{} exception",method.getName(),arguments,t);
                    return new JsonError(((BizException) t).getBizCode().getCode(), ((BizException) t).getBizCode().getMsg(), null);
                } else if (t.getCause() instanceof BizException) {

                    return new JsonError(((BizException) t.getCause()).getBizCode().getCode(), ((BizException) t.getCause()).getBizCode().getMsg(), null);
                } else if (t.getCause() instanceof UnknownHostException) {

                    return new JsonError(BizCodeEnum.TRY_AGAIN_LATER.getCode(), BizCodeEnum.TRY_AGAIN_LATER.getMsg(), null);
                } else if (t instanceof ConstraintViolationException) {
                    Map<String, String> collect = ((ConstraintViolationException) t).getConstraintViolations().stream()
                            .collect(Collectors.toMap(field -> field.getPropertyPath().toString()
                                            .substring(field.getPropertyPath().toString().lastIndexOf(".") + 1)
                                    , ConstraintViolation::getMessage));

                    log.error("method:{} execute request param:{} exception", method.getName(), arguments, t);
                    return new JsonError(BizCodeEnum.MISSING_REQUIRED_FIELDS.getCode(), BizCodeEnum.MISSING_REQUIRED_FIELDS.getMsg(), null);
                }
                return new JsonError(ERROR_NOT_HANDLED.code, t.getMessage(), null);
            }
        });
        exporter.setShouldLogInvocationErrors(false);
        exporter.setInvocationListener(new InvocationListener() {
            @Override
            public void willInvoke(Method method, List<JsonNode> arguments) {
                log.info("before invoke method:{},param:{}", method.getName(), arguments);
            }

            @Override
            public void didInvoke(Method method, List<JsonNode> arguments, Object result, Throwable t, long duration) {
                if (t instanceof InvocationTargetException && ((InvocationTargetException) t).getTargetException() instanceof BizException) {
                    log.error("BizException end invoke method:{},param:{}，result：{},duration：{}，biz error code:{},biz error msg:{}", method.getName(), arguments, result, duration, ((BizException) ((InvocationTargetException) t).getTargetException()).getBizCode().getCode(), ((BizException) ((InvocationTargetException) t).getTargetException()).getReplaceMsg());
                    return;
                } else if (t != null) {
                    log.error("Unknown Exception end invoke method:{},param:{}，result：{},duration：{}，exception:", method.getName(), arguments, result, duration, t);
                    return;
                }
                log.info("end invoke method:{},param:{}，result：{},duration：{}", method.getName(), arguments, result, duration);
            }
        });
        return exporter;
    }
}
