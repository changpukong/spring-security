package yfu.practice.springsecurity.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import yfu.practice.springsecurity.service.impl.JWTService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JWTService theJWTService;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwtToken = request.getHeader("Authentication");
		if (jwtToken != null) {
			/*
			 * Token過期或被竄改都會拋Exception
			 * 若有Redis可用來確認Token是否由Server發出
			 */
			Claims claims = theJWTService.parseToken(jwtToken);
			UserDetails userDetails = userDetailsService.loadUserByUsername((String) claims.get("userName"));
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);	// 將身分放在SecurityContext供後續使用
		}
		
		filterChain.doFilter(request, response);
	}
}