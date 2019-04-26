package com.example.qidai.config;


import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpsConfig {

   @Bean
   public Connector connector(){
       Connector connector=new Connector("org.apache.coyote.http11.Http11NioProtocol");
       connector.setScheme("http");
       //Connector监听的http的端口号
       connector.setPort(80);
       connector.setSecure(false);
       //监听到http的端口号后转向到的https的端口号
       connector.setRedirectPort(443);
       return connector;
   }

   @Bean
   public TomcatServletWebServerFactory tomcatServletWebServerFactory(Connector connector){
       TomcatServletWebServerFactory tomcat=new TomcatServletWebServerFactory(){
           @Override
           protected void postProcessContext(Context context) {
               SecurityConstraint securityConstraint=new SecurityConstraint();
               securityConstraint.setUserConstraint("CONFIDENTIAL");
               SecurityCollection collection=new SecurityCollection();
               collection.addPattern("/*");
               securityConstraint.addCollection(collection);
               context.addConstraint(securityConstraint);
           }
       };
       tomcat.addAdditionalTomcatConnectors(connector);
       return tomcat;
   }

}