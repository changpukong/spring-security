package yfu.practice.springsecurity.vo;

import java.util.List;

import lombok.Data;

@Data
public class UserVO {

	private String username;
	
	private String password;
	
	private List<String> roles;
}