package yfu.practice.springsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import yfu.practice.springsecurity.filter.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				/*
				 * /auth/*: 單層，不包含/auth
				 * /auth/**: 多層，包含/auth
				 * /auth/?: 單一字元
				 */
//				.antMatchers("/auth").hasAnyAuthority(RoleEnum.ADMIN.name())	// 順序有差
//				.antMatchers("/auth/*").authenticated()
//				.antMatchers("/", "/home").permitAll()
				.anyRequest().authenticated()
				.and()
			.csrf().disable()	// 關閉「跨站請求偽造」防護，讓外部可直接對API發出請求，否則會403
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//			.formLogin();		// 預設的登入畫面
	}
	
	@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(new BCryptPasswordEncoder());
    }
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}