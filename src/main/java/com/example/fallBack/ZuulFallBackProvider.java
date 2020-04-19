package com.example.fallBack;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import io.micrometer.core.instrument.util.JsonUtils;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
@Component
public class ZuulFallBackProvider implements FallbackProvider{

    @Override
    public String getRoute() {
        // 表明是为哪个微服务提供回退，*表示为所有微服务提供回退
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        if (cause instanceof HystrixTimeoutException) {
            return response(504);
        } else {
            return this.fallbackResponse();
        }
    }

    public ClientHttpResponse fallbackResponse() {
        return this.response(500);
    }


    private ClientHttpResponse response(final int status) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() {
                return 200;
            }

            @Override
            public String getStatusText() {
                return "OK";
            }

            @Override
            public void close() {
            }

            @Override
            public InputStream getBody() {
                String str;
                if (status == 504) {
                    //str= JsonUtils.toJson(ReturnJson.serviceError().setMsg("当前服务未注册，请稍后再重试"));
                    str=JsonUtils.prettyPrint("当前服务未注册，请稍后再重试");
                }else{
                    //str= JsonUtils.toJson(ReturnJson.serviceError().setMsg("当前服务未启动，请启动再重试"));
                    str=JsonUtils.prettyPrint("当前服务未启动，请启动再重试");
                }
                return new ByteArrayInputStream(str.getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                // headers设定
                HttpHeaders headers = new HttpHeaders();
                //和body中的内容编码一致，否则容易乱码
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }

}
