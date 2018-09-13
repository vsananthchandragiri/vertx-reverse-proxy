package com.sirius;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sirius.verticle.HttpProxyVerticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

@SpringBootApplication
public class Application {

  @Autowired
  private HttpProxyVerticle proxyVerticle;

	  @Value("${application.verticleInstances}")
	  private static Integer verticleInstances;
	  
	  @Value("${application.workerPoolSize}")
	  private static Integer workerPoolSize;

	  @Value("${application.workerPoolName}")
	  private static String workerPoolName;
	
  public static void main(String[] args) {

    SpringApplication.run(Application.class, args);
     
  }
  
  @PostConstruct
  public void deployVerticle() {
	    /*Vertx.vertx().deployVerticle(new HttpProxyVerticle() , new DeploymentOptions()
	            .setInstances(verticleInstances).setWorkerPoolSize(workerPoolSize).setWorkerPoolName(workerPoolName));*/
	  Vertx.vertx().deployVerticle(proxyVerticle);
	  
  }
  
}

