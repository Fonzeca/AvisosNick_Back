package com.mindia.avisosnick.security;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mindia.avisosnick.persistence.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static int secondsUntilExpires = 3600;
	public static String secretKey = "huffm4an123";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
		.authorizeRequests()
		//TODO: mejorar mappings de coso xD
		.antMatchers(HttpMethod.POST, "/login").permitAll()
		.antMatchers(HttpMethod.POST, "/loginWithGoogle").permitAll()
		.antMatchers(HttpMethod.POST, "/register").permitAll()
		.antMatchers(HttpMethod.POST, "/validateToken").permitAll()
		.anyRequest().authenticated();
	}


	public static String getJWTToken(User user) {
		String authorithies = "";
		
		for (String rol : user.getRoles()) {
			authorithies+= rol + ",";
		}
		
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(authorithies);
		
		String token = Jwts
				.builder()
				.setId("mindiaJWT")
				.setSubject(user.getEmail())
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + (secondsUntilExpires * 1000)))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return token;
	}
	
	public static String getJWTTokenWithOAuth(User user) {
		String authorithies = "";
		
		for (String rol : user.getRoles()) {
			authorithies+= rol + ",";
		}
		
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(authorithies);
		
		String token = Jwts
				.builder()
				.setId("mindiaJWT")
				.setSubject(user.getEmail())
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(user.getAuth().getExpirationLastIdToken() * 1000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return token;
	}
}