package com.engineering.Application.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import java.util.Arrays;


@Configurable
@EnableWebSecurity
@EnableResourceServer
public class OAuthSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	OAuth2ClientContext oauth2ClientContext;
	@Autowired
	AuthorizationCodeResourceDetails authorizationCodeResourceDetails;
	@Autowired
	ResourceServerProperties resourceServerProperties;
	@Autowired
	Environment environment;
	@Value('${engineering.everything.host}')
	private String servicehost;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
	}

	@Bean
	public AuthenticationSuccessHandler successHandler() {
		String [] profiles = ["prod"]
		println("Active profiles are ${environment.getActiveProfiles()} and profiles are ${profiles}")
		if(environment.getActiveProfiles() == profiles)
			return  new ProdCustomSuccessHandler("http://"+servicehost+"/#/")
		else
			return new DevCustomLoginSuccessHandler("http://"+servicehost+"/#/")
	}

	private OAuth2ClientAuthenticationProcessingFilter filter() {
		OAuth2ClientAuthenticationProcessingFilter oAuth2Filter = new OAuth2ClientAuthenticationProcessingFilter(
				"/google/login");

		OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(authorizationCodeResourceDetails,
				oauth2ClientContext);
		oAuth2Filter.setRestTemplate(oAuth2RestTemplate);

		oAuth2Filter.setTokenServices(new UserInfoTokenServices(resourceServerProperties.getUserInfoUri(),
				resourceServerProperties.getClientId()));

		oAuth2Filter.setAuthenticationSuccessHandler(successHandler());

		return oAuth2Filter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
				.headers()
		          .frameOptions().disable().addHeaderWriter(new StaticHeadersWriter("X-FRAME-OPTIONS", "ALLOW-FROM localhost"))
				  .and()
				.cors().and()
				.authorizeRequests()
				.antMatchers("/html/**.html", "/js/*/*.js", "/js/*/*/*.*","/**.html").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/google/login").permitAll()
				.antMatchers(HttpMethod.GET, "/google/login").permitAll()
				.anyRequest().fullyAuthenticated()//
				.and()
				.formLogin().loginPage("/google/login").successHandler(successHandler())
				.and()
				.logout()
				.logoutSuccessUrl("/users/logout")
				.permitAll()
				.and()
				.addFilterAt(filter(),BasicAuthenticationFilter.class)
				.csrf().disable();
				//.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}

}
