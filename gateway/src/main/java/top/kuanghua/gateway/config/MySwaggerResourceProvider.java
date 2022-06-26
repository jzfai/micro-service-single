package top.kuanghua.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MySwaggerResourceProvider implements SwaggerResourcesProvider {

    /**
     * swagger2默认的url后缀
     */
    private static final String SWAGGER2_URL = "/v2/api-docs";

    /**
     * 路由定位器
     */
    private final RouteLocator routeLocator;

    /**
     * 网关应用名称
     */
    @Value("${spring.application.name}")
    private String gatewayName;

    /**
     * 获取 Swagger 资源
     */
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<Map<String, String>> routeInfoList = new ArrayList<Map<String, String>>();
        // 1. 获取路由Uri 中的Host=> 服务注册则为服务名=》app-service001
        routeLocator.getRoutes()
                .filter(route -> route.getUri().getHost() != null)
                .filter(route -> !gatewayName.equals(route.getUri().getHost()))
                .subscribe(route -> {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("serviceId", route.getUri().getHost());
                    hashMap.put("serviceName", route.getMetadata().get("name").toString());
                    routeInfoList.add(hashMap);
                });
        // 2. 创建自定义资源
        routeInfoList.forEach((feMap) -> {
            String serviceUrl = "/micro-service-api/" + feMap.get("serviceId") + SWAGGER2_URL; // 后台访问添加服务名前缀
            SwaggerResource swaggerResource = new SwaggerResource(); // 创建Swagger 资源
            swaggerResource.setUrl(serviceUrl); // 设置访问地址
            swaggerResource.setName(feMap.get("serviceName")); // 设置名称
            swaggerResource.setSwaggerVersion("2.0.9");
            resources.add(swaggerResource);
        });
        return resources;
    }
}

