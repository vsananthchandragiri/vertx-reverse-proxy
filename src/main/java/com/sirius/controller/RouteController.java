package com.sirius.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sirius.entity.ProxyRoute;
import com.sirius.service.RouteService;

@RestController
@RequestMapping("/route")
public class RouteController {

	@Autowired
	private RouteService service;
	
	@RequestMapping(value = "/getRoute", method= RequestMethod.POST)
	public String getRoute(@RequestBody ProxyRoute proxyRoute) {
		return service.getRequest(proxyRoute);
	}
	
}
