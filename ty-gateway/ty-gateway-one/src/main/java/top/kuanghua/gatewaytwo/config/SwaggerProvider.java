package top.kuanghua.gatewaytwo.config;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @introduce: SwaggerProvider
 * @author: kuanghua
 * @date: 2020/6/4
 **/
@Component
@Primary
@AllArgsConstructor
public class SwaggerProvider implements SwaggerResourcesProvider {

    public static final String API_URI = "/v2/api-docs";
    private final RouteLocator routeLocator;
    private final GatewayProperties gatewayProperties;
    /**
     * @return
     */
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        //List<String> routes = new ArrayList<>();
        routeLocator.getRoutes().subscribe(route -> {
//            System.out.println("routeLocator");
//            System.out.println(route);
        });
        gatewayProperties.getRoutes().stream().forEach(item -> {
//            System.out.println("gatewayProperties");
//            System.out.println(item);
        });
        /*设置swagger服务路径*/
        resources.add(createResource("用户模块", "ty-user", "2.9"));
        return resources;
    }

    private SwaggerResource createResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation("/api/" + location + "/v2/api-docs");
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
