package com.sirius.routes;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.sirius.entity.ProxyRoute;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ResponseTimeHandler;


@Scope(value = "singleton")
@Component
public class RouterConfigurer { 
	
	@Value("${verticle.port}")
  	private Integer port;

	private RestTemplate restTemplate =  new RestTemplate();
	
	private Router router;

	public Router getRouter() {
		return router;
	}

	public void setRouter(Router router) {
		this.router = router;
	}
	
	
/*	  if(proxyRoute.getHttpMethod().equals("DELETE")) {
		  restTemplate.delete(uri);
		  return "deleted successfully";
		  
	  }
	  
	  else {
		  String result = restTemplate.getForObject(uri, String.class);
		  return result;

	  }
	*/
	
	public String routetoGetMethod(ProxyRoute proxyRoute) {
		
		  String uri = "http://localhost:"+port+proxyRoute.getRoute();
		  try {		  
			  if(proxyRoute.getPathVariable()!=null) {
				  return restTemplate.getForObject(uri, String.class,proxyRoute.getPathVariable());
			  }
			  else if(proxyRoute.getUrlVariables()!=null) {
				  return restTemplate.getForObject(uri, String.class,proxyRoute.getUrlVariables());
			  }
			  else {
				  return restTemplate.getForObject(uri, String.class);
			  }
		  }
		  catch(Exception e) {
			  return e.getMessage();
		  }
	}
	
	public String routetoPostMethod(ProxyRoute proxyRoute) {
		
		  String uri = "http://localhost:"+port+proxyRoute.getRoute();
		  ResponseEntity<?> response = null;	  

			MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		       
   		    Map<String, String> map = new HashMap<String, String>();
		        
	        map.put("Content-Type", "application/json");

	        headers.setAll(map);

	        HttpEntity<?> request = new HttpEntity<>(proxyRoute.getBody(), headers);
	       		        
	        try {		  
				  if(proxyRoute.getPathVariable()!=null) {
					  response =   restTemplate.postForEntity(uri, request, String.class,proxyRoute.getPathVariable());
				  }
				  else if(proxyRoute.getUrlVariables()!=null) {
					  response =  restTemplate.postForEntity(uri,request, String.class,proxyRoute.getUrlVariables());
				  }
				  else {
					  response = restTemplate.postForEntity(uri, request, String.class);
				  }
			  }
			  catch(Exception e) {
				  return e.getMessage();
			  }

		  return response.getBody().toString();	
	}
	
	public String routetoPutMethod(ProxyRoute proxyRoute) {
		  String uri = "http://localhost:"+port+proxyRoute.getRoute();
		  
		  MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	       
 		    Map<String, String> map = new HashMap<String, String>();
		        
	        map.put("Content-Type", "application/json");

	        headers.setAll(map);

	        HttpEntity<?> request = new HttpEntity<>(proxyRoute.getBody(), headers);
	       		        
	        try {		  
				  if(proxyRoute.getPathVariable()!=null) {
					  restTemplate.put(uri, request, String.class,proxyRoute.getPathVariable());
				  }
				  else if(proxyRoute.getUrlVariables()!=null) {
					    restTemplate.put(uri,request, String.class,proxyRoute.getUrlVariables());
				  }
				  else {
					 restTemplate.put(uri, request, String.class);
				  }
			  }
			  catch(Exception e) {
				  return e.getMessage();
			  }

		return "record updated successfully";
	}
	
	public String routetoDeleteMethod(ProxyRoute proxyRoute) {
		  String uri = "http://localhost:"+port+proxyRoute.getRoute();
		  
		  try {		  
			  if(proxyRoute.getPathVariable()!=null) {
				  restTemplate.delete(uri,proxyRoute.getPathVariable());
			  }
			  else if(proxyRoute.getUrlVariables()!=null) {
				  restTemplate.delete(uri, proxyRoute.getUrlVariables());
			  }
			  else {
				  restTemplate.delete(uri);
			  }
		  }
		  catch(Exception e) {
			  return e.getMessage();
		  }
		return "record deleted successfully";
	}
	
}
