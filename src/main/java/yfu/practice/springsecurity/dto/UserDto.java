package yfu.practice.springsecurity.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

	@NotBlank(message = "account不得為空")
	private String account;
	
	@NotBlank(message = "password不得為空")
	private String password;
}