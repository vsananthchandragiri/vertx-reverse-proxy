package com.sirius.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sirius.entity.ProxyRoute;
import com.sirius.routes.RouterConfigurer;

import io.vertx.core.Vertx;

@Service
public class RouteServiceImpl implements RouteService {
	
	@Autowired
	private RouterConfigurer routeConfigurer;

	@Override
	public String getRequest(ProxyRoute proxyRoute) {
		        
        proxyRoute.configure(Vertx.vertx(), routeConfigurer.getRouter());
        
        String result = null;
        
		        switch (proxyRoute.getHttpMethod().toUpperCase()) {
		        case "GET":
		        	result = routeConfigurer.routetoGetMethod(proxyRoute);
		            break;
		        case "POST":
		        	result =  routeConfigurer.routetoPostMethod(proxyRoute);
		            break;
		        case "PUT":
		        	result = routeConfigurer.routetoPutMethod(proxyRoute);
		            break;
		        case "DELETE":
		        	result = routeConfigurer.routetoDeleteMethod(proxyRoute);
		            break;                
		        default:
		            return  new RuntimeException("Unsupported HTTP method on proxy route ").getMessage();
		        }
		  
		return result;
		
	}
	
	
	
	
}
