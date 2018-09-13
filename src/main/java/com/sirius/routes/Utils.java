package com.sirius.routes;


import com.sirius.entity.ProxyRoute;

import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;

public class Utils {

    public static HttpRequest setRequestHeaders(HttpRequest request, RoutingContext routingContext, ProxyRoute proxyRoute) {
        routingContext.request().headers().entries().forEach((entry) -> {
            request.putHeader(entry.getKey(), entry.getValue());
        });
        request.putHeader("Host", proxyRoute.getTargetBaseUrl());

        return request;
    }

    public static void setResponseHeaders(HttpResponse response, RoutingContext routingContext) {
        response.headers().entries().forEach((entry) -> {
            routingContext.response().putHeader(entry.getKey(), entry.getValue());
        });
    }

}
