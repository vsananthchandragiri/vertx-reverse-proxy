package com.sirius.verticle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sirius.routes.RouterConfigurer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ResponseTimeHandler;

@Component
public class HttpProxyVerticle extends AbstractVerticle {
	
	@Autowired
	private RouterConfigurer routeConfigurer;
	
	@Value("${verticle.port}")
	private Integer port;
	
	 public void start(Future<Void> fut) {

	        // Set up all the routes and their handlers
	   //     Router router = routeConfigurer.setRouteHandlers(vertx);
	        HttpServerOptions options = new HttpServerOptions().setLogActivity(true);
	        
	        Router router = Router.router(vertx);
	    	
		     // Enable response time handlers fr all requests and body handler for all POST and PUT requests
		     router.route().handler(ResponseTimeHandler.create());
		     router.route(HttpMethod.POST, "/*").handler(BodyHandler.create());
		     router.route(HttpMethod.PUT, "/*").handler(BodyHandler.create());
		     router.route(HttpMethod.GET, "/*").handler(BodyHandler.create());
		     router.route(HttpMethod.DELETE, "/*").handler(BodyHandler.create());
		     
		     routeConfigurer.setRouter(router);
        
	       // Router router = routeConfigurer.setRouterConfiguaration(vertx);
	       
	        // Create the HTTP server
	        vertx.createHttpServer(
	        		options).requestHandler(
	        				router::accept).listen(
	        						config().getInteger("http.port", port),
		                    result -> {
		                        if (result.succeeded()) {
		                            fut.complete();
		                        } else {
		                            fut.fail(result.cause());
		                        }
		                    }
		            );
	    }

    

    

}
