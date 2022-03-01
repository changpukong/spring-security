package yfu.practice.springsecurity.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import yfu.practice.springsecurity.enums.RoleEnum;
import yfu.practice.springsecurity.vo.UserVO;

@Service
public class UserDetailsSvcImpl implements UserDetailsService {
	
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	/** 測試用帳號 */
	private Map<String, UserVO> userMap = new HashMap<>();
	
	public UserDetailsSvcImpl() {
		UserVO user = new UserVO();
		user.setUsername("88454");
		user.setPassword("iamnumber1");
		user.setRoles(Arrays.asList(RoleEnum.ADMIN.name(), RoleEnum.USER.name()));
		userMap.put(user.getUsername(), user);
		
		UserVO user2 = new UserVO();
		user2.setUsername("99999");
		user2.setPassword("iamnumber2");
		user2.setRoles(Arrays.asList(RoleEnum.USER.name()));
		userMap.put(user2.getUsername(), user2);
	}

	/*
	 * 點擊預設登入頁面的Sign in後會被呼叫
	 * Spring Security進行驗證時是用UserDetails作為使用者資訊的載體
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserVO user = userMap.get(username);
		if (user == null) {
			throw new UsernameNotFoundException("帳號或密碼錯誤");
		}
		
		List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		
		return new User(username, encoder.encode(user.getPassword()), authorities);
	}
}