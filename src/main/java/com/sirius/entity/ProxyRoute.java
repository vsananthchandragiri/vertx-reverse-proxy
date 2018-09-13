package com.sirius.entity;

import java.util.Date;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.sirius.routes.Utils;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

@Component
public class ProxyRoute {
    private static final String START_TIME_KEY = "proxyHandlerStartTime";
    private JSONObject body;
    private WebClient client;
    private String httpMethod;
    private String pathVariable;
    private Integer port;
    private String route;  
    private String targetBaseUrl;
    private Long timeout;
    
    private Map<String, ?> urlVariables;

	public Router configure(Vertx vertx, Router router) {
        WebClientOptions clientOptions = new WebClientOptions().setLogActivity(true).setMaxPoolSize(100);
        this.client = WebClient.create(vertx, clientOptions);

        switch (httpMethod.toUpperCase()) {
            case "GET":
                router.get(route).handler(this::doProxyGet);
                break;
            case "POST":
                router.post(route).handler(this::doProxyPost);
                break;
            case "PUT":
                router.put(route).handler(this::doProxyPut);
                break;
            case "DELETE":
                router.put(route).handler(this::doProxyDelete);
                break;                
            default:
                throw new RuntimeException("Unsupported HTTP method on proxy route " + route);
        }

        return router;
    }

	public void doProxyDelete(RoutingContext routingContext) {

        long timeoutToUse = this.timeout == null ? 1000L : this.timeout;
        int portToUse = (this.port == null) ? 80 : this.port;

        routingContext.put(START_TIME_KEY, new Date().getTime());
        // Blind passthrough - only change the host and port part of the incoming request.
        // Sending the request URI will send both the path and the query params onwards
        HttpRequest request = client.get(portToUse, targetBaseUrl, routingContext.request().uri()).timeout(timeoutToUse);
        request = Utils.setRequestHeaders(request, routingContext, this);

        request.sendBuffer(routingContext.getBody(), r -> {
            AsyncResult<HttpResponse<Buffer>> ar = (AsyncResult) r;
            handleResponse(routingContext, ar);
        });
    }

	public void doProxyGet(RoutingContext routingContext) {

        long timeoutToUse = this.timeout == null ? 1000L : this.timeout;
        int portToUse = (this.port == null) ? 80 : this.port;

        routingContext.put(START_TIME_KEY, new Date().getTime());
        // Blind passthrough - only change the host and port part of the incoming request.
        // Sending the request URI will send both the path and the query params onwards
        HttpRequest request = client.get(portToUse, targetBaseUrl, routingContext.request().uri()).timeout(timeoutToUse);
        request = Utils.setRequestHeaders(request, routingContext, this);

        request.send(r -> {
            AsyncResult<HttpResponse<Buffer>> ar = (AsyncResult) r;
            handleResponse(routingContext, ar);
        });
    }

	private void doProxyPost(RoutingContext routingContext) {

        long timeoutToUse = this.timeout == null ? 1000L : this.timeout;
        int portToUse = (this.port == null) ? 80 : this.port;

        routingContext.put(START_TIME_KEY, new Date().getTime());
        // Blind passthrough - only change the host and port part of the incoming request.
        // Sending the request URI will send both the path and the query params onwards
        HttpRequest request = client.post(portToUse, targetBaseUrl, routingContext.request().uri()).timeout(timeoutToUse);
        request = Utils.setRequestHeaders(request, routingContext, this);
      
        request.sendBuffer(routingContext.getBody(), r -> {
            AsyncResult<HttpResponse<Buffer>> ar = (AsyncResult) r;
            handleResponse(routingContext, ar);
        });
    }

	private void doProxyPut(RoutingContext routingContext) {

        long timeoutToUse = this.timeout == null ? 1000L : this.timeout;
        int portToUse = (this.port == null) ? 80 : this.port;

        routingContext.put(START_TIME_KEY, new Date().getTime());
        // Blind passthrough - only change the host and port part of the incoming request.
        // Sending the request URI will send both the path and the query params onwards
        HttpRequest request = client.put(portToUse, targetBaseUrl, routingContext.request().uri()).timeout(timeoutToUse);
        request = Utils.setRequestHeaders(request, routingContext, this);
        
        request.sendBuffer(routingContext.getBody(), r -> {
            AsyncResult<HttpResponse<Buffer>> ar = (AsyncResult) r;
            handleResponse(routingContext, ar);
        });
    }

	public JSONObject getBody() {
		return body;
	}
    
    public String getHttpMethod() {
        return httpMethod;
    }

    public String getPathVariable() {
		return pathVariable;
	}
    
    public Integer getPort() {
        return port;
    }

    public String getRoute() {
        return route;
    }

    public String getTargetBaseUrl() {
        return targetBaseUrl;
    }

    public Long getTimeout() {
        return timeout;
    }

    public Map<String, ?> getUrlVariables() {
		return urlVariables;
	}

    private void handleResponse(RoutingContext routingContext, AsyncResult<HttpResponse<Buffer>> ar) {
        if (ar.succeeded()) {
            HttpResponse<Buffer> response = ar.result();
            long timeTaken = (new Date().getTime()) - ((Long)routingContext.get(START_TIME_KEY));

            Utils.setResponseHeaders(response, routingContext);
            routingContext.response().setStatusCode(response.statusCode());
            routingContext.response().setStatusMessage(response.statusMessage());

            routingContext.response().end(response.bodyAsBuffer());
        } else {
            routingContext.fail(ar.cause());
        }
    }

    public void setBody(JSONObject body) {
		this.body = body;
	}

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setPathVariable(String pathVariable) {
		this.pathVariable = pathVariable;
	}

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void setTargetBaseUrl(String targetBaseUrl) {
        this.targetBaseUrl = targetBaseUrl;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public void setUrlVariables(Map<String, ?> urlVariables) {
		this.urlVariables = urlVariables;
	}
}

