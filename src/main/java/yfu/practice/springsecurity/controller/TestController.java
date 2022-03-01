package yfu.practice.springsecurity.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import yfu.practice.springsecurity.dto.UserDto;
import yfu.practice.springsecurity.service.impl.JWTService;

@RestController
public class TestController {
	
	@Autowired
	private JWTService theJWTService;

	@PostMapping(path = "/auth")
	public String auth(@RequestHeader(value = "Authentication", required = false) String authentication) {
		return "先生，你打到我了！";
	}
	
	@PostMapping(path = "/auth/{id}")
	public String authWithId(@PathVariable("id") String id) {
		return "先生，你打到" + id + "了！";
	}
	
	@PostMapping(path = "/getToken")
	public String getToken(@Valid @RequestBody UserDto userDto) {
		return theJWTService.genToken(userDto);
	}
	
	@GetMapping(path = "/getToken")
	public String getToken() {
		UserDto userDto = new UserDto("88454", "iamnumber1");
		return theJWTService.genToken(userDto);
	}
}