package yfu.practice.springsecurity.service.impl;

import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import yfu.practice.springsecurity.dto.UserDto;

@Service
public class JWTService {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	/** 密鑰 */
	private static final String KEY = "ThisIsMyFirstTimeToUseJwtForAuthentication";
	
	private Key secretKey;
	
	/** 解碼器 */
	private JwtParser parser;
	
	@PostConstruct
	public void init() {
		this.secretKey = Keys.hmacShaKeyFor(KEY.getBytes());
		this.parser = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build();
	}
	
	/**
	 * 取得Token
	 * @param userDto
	 * @return
	 */
	public String genToken(UserDto userDto) {
		/*
		 * 將帳密放入Authentication物件
		 * 帳號為principal，代表與伺服器互動者
		 * 密碼為credentials
		 */
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDto.getAccount(), userDto.getPassword());
		
		/*
		 * 驗證回傳該身分完整的資料（含角色）
		 * principal會變成UserDetailsService回傳的物件
		 */
		authentication = authenticationManager.authenticate(authentication);
		UserDetails user = (UserDetails) authentication.getPrincipal();
		
		Claims claims = Jwts.claims()
				.setExpiration(Timestamp.valueOf(LocalDateTime.now().plusMinutes(2)))	// Token有效時間2分鐘
				.setIssuer("yfu");	// Token發行者
		claims.put("userName", user.getUsername());
		
		return genToken(claims);
	}
	
	/**
	 * 取得Token
	 * @param claims
	 * @return
	 */
	public String genToken(Claims claims) {
		return Jwts.builder()
				.setClaims(claims)
				.signWith(secretKey)
				.compact();
	}

	/**
	 * Token轉Claims
	 * @param token
	 * @return
	 */
	public Claims parseToken(String token) {
		return parser.parseClaimsJws(token).getBody();
	}
	
	/**
	 * 驗證Token是否有被竄改
	 * @param token
	 * @return
	 */
	public boolean validateToken(String token) {
		Claims claims = parseToken(token);
		return token.equals(genToken(claims));
	}
}