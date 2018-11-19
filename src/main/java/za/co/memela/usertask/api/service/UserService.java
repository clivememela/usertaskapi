package za.co.memela.usertask.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import za.co.memela.usertask.api.model.User;
import za.co.memela.usertask.api.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

	public User create(User user) {
		userRepository.save(user);
		return user;
	}
	
	public User updateUser (User user) {
		return userRepository.save(user);
	}
	
	public List<User> listAllUsers(){
		return userRepository.findAll();
	}
	
	public User getUserInfo(Long userId) {
		return userRepository.getOne(userId);
	}
	
	public boolean isUserExist(User user) {
		return userRepository.findByUserName(user.getUserName())!=null;
	}
	
}
