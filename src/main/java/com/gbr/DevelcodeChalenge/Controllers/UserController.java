package com.gbr.DevelcodeChalenge.Controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gbr.DevelcodeChalenge.Models.User;
import com.gbr.DevelcodeChalenge.Repositories.UserRepository;
import com.gbr.DevelcodeChalenge.Services.FileHandling;


@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public User saveUser(@RequestBody User user,@RequestPart("file") MultipartFile file) {
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        user.setPhoto(fileName);
 
        String uploadDir = "user-photos/" + user.getUsername();
 
        try {
			FileHandling.saveFile(uploadDir, fileName, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userRepository.save(user);
	}
	
	@GetMapping
	public List<User> getAll() {
		return userRepository.findAll();
	}
	
	@GetMapping("/{username}")
	public Optional<User> getById(@PathVariable String username) {
		return userRepository.findByUsername(username);
	}
	
	@PutMapping("/{id}")
	public User updateUser(@RequestBody User user, @PathVariable Long id, @RequestPart("file") MultipartFile file) {
		
		User u = userRepository.getById(id);
		
		if (u == null) {
			return null;
		}
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        user.setPhoto(fileName);
 
        String uploadDir = "user-photos/" + user.getUsername();
 
        try {
			FileHandling.saveFile(uploadDir, fileName, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		u.setUsername(user.getUsername());
		u.setBirthDate(user.getBirthDate());
		
		return userRepository.save(u);
	}
	
	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id) {
		userRepository.deleteById(id);
	}
	
	

}
