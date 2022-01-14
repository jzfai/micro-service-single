package top.kuanghua.gatewaytwo.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.kuanghua.feign.tyauth.feign.TokenFeign;
import top.kuanghua.khcomomon.utils.ObjectUtilsSelf;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @Title: AuthorizeFilter
 * @Description:
 * @Auther: kuanghua
 * @create 2020/9/8 21:26
 */
@Slf4j
@Component
//https://www.cnblogs.com/javastack/archive/2020/10/23/13862164.html
//https://www.jianshu.com/p/f3383e7b1faf
//https://blog.csdn.net/weixin_41187876/article/details/103645102?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1.control
/*
* @Value("#{'${scio.cloud.list}'.split(',')}")
private List<String> list;
@Value("#{${scio.cloud.maps}}")
private Map<String,String> maps;
* */
public class AuthorizeFilterBefore implements GlobalFilter, Ordered {

    private static final String AUTHORIZE_TOKEN = "AUTHORIZE_TOKEN";
    @Value("#{'${filter.allowPaths:}'.empty ? null : '${filter.allowPaths:}'.split(',')}")
    private List<String> allowPaths;

    @Resource
    private TokenFeign tokenFeign;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getURI().getPath();

        //1.白名单放行
        for (String allowPath : allowPaths) {
            if (StringUtils.contains(path, allowPath)) {
                return chain.filter(exchange);
            }
        }
        //2.解析jwt token放行
        String jwtToken = request.getHeaders().getFirst(AUTHORIZE_TOKEN);

        if (ObjectUtils.isEmpty(jwtToken)) {
            JSONObject message = new JSONObject();
            message.put("code", 403);
            message.put("msg", "token为空");
            DataBuffer buffer = getDataBuffer(response, message);
            return response.writeWith(Mono.just(buffer));
        }
        //2.1 将解析到的jwt数据保存到线程中(转发过去的服务都能接收)
        try {
            //调用feign服务进行解析
            Object resResult = tokenFeign.parseToken(jwtToken);

            Map tokenInfo = ObjectUtilsSelf.parseResToData(resResult);
            log.info("解析的token数据", tokenInfo.toString());
            //将信息设置到头部
            ServerHttpRequest httpRequest = exchange.getRequest().mutate().headers(httpHeaders -> {
                String decode = "";
                try {
                    decode = URLEncoder.encode(JSON.toJSONString(tokenInfo), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                httpHeaders.add("TOKEN_INFO", decode);
            }).build();
            //将信息设置到线程中
            ServerWebExchange build = exchange.mutate().request(httpRequest).build();
            return chain.filter(build);
        } catch (Exception e) {
            JSONObject message = new JSONObject();
            message.put("code", 403);
            message.put("msg", "token解析失败" + e.getMessage());
            log.error("token解析失败" + e);
            return response.writeWith(Mono.just(getDataBuffer(response, message)));
        }
    }

    private DataBuffer getDataBuffer(ServerHttpResponse response, JSONObject message) {
        byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return buffer;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
