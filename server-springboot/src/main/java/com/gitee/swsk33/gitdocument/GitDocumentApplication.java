package com.gitee.swsk33.gitdocument;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GitDocumentApplication {

	/**
	 * http自动跳转https
	 */
	@Bean
	@ConditionalOnProperty(name = {"com.gitee.swsk33.git-doc.enable-https"}, havingValue = "true")
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};
		tomcat.addAdditionalTomcatConnectors(redirectConnector());
		return tomcat;
	}

	private Connector redirectConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(80); // 原http端口
		connector.setSecure(false);
		connector.setRedirectPort(443); // 跳转的https端口，也就是我们配置文件中配置的项目端口
		return connector;
	}

	public static void main(String[] args) {
		System.setProperty("spring.config.location", "file:config/");
		SpringApplication.run(GitDocumentApplication.class, args);
	}

}