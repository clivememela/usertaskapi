package za.co.memela.usertask.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.util.UriComponentsBuilder;

import za.co.memela.usertask.api.model.Task;
import za.co.memela.usertask.api.model.User;
import za.co.memela.usertask.api.service.TaskService;
import za.co.memela.usertask.api.service.UserService;
import za.co.memela.usertask.api.utils.CustomErrorType;

@RestController
@RequestMapping("/api/user")
public class UserTaskController {

	public static final Logger logger = LoggerFactory.getLogger(UserTaskController.class);
	
	private UserService userService;
	private TaskService taskService;

	// ----------------------------- Create user --------------------------------------------------------------------- //

	@PostMapping(value = "")
	public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
		logger.info("Creating User : {}", user);

		if (userService.isUserExist(user)) {
			logger.error("Unable to create. A User with name {} already exist", user.getUserName());
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A User with name " + user.getUserName() + " already exist."),
					HttpStatus.CONFLICT);
		}
		userService.create(user);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ----------------------------- Update user ---------------------------------------------------------------- //

	@PutMapping(value = "/{id}")
	public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
		logger.info("Updating User with id {}", id);

		User currentUser = userService.getUserInfo(id);

		if (currentUser == null) {
			logger.error("Unable to update. User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. User with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentUser.setFirstName(user.getFirstName());
		currentUser.setLastName(user.getLastName());

		return new ResponseEntity<User>(userService.updateUser(currentUser), HttpStatus.OK);
	}

	// ----------------------------- List All users ----------------- //

	@GetMapping(value = "")
	public ResponseEntity<List<User>> listAllUsers() {
		List<User> users = userService.listAllUsers();
		if (users.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	// ----------------------------- Get User info ------------------------------------------------------------------//

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
		logger.info("Fetching User with id {}", id);
		User user = userService.getUserInfo(id);
		if (user == null) {
			logger.error("User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("User with id " + id + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	// ----------------------------- Create Task  ----------------------------------------------------------------//

	@PostMapping(value = "/{user_id}/task/{task_id}")
	public ResponseEntity<?> createTask(@RequestBody Task task, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Task : {}", task);

		if (taskService.isTaskExist(task)) {
			logger.error("Unable to create. A Task with name {} already exist", task.getName());
			return new ResponseEntity(
					new CustomErrorType("Unable to create. A Task with name " + task.getName() + " already exist."),
					HttpStatus.CONFLICT);
		}
		taskService.createTask(task);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/user/{id}/task/{task_id}").buildAndExpand(task.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ----------------------------- Update Task -----------------------------------------------------------------//

	@PutMapping(value = "/{user_id}/task/{task_id")
	public ResponseEntity<?> updateTask(@PathVariable("user_id") Long userId, @PathVariable("task_id") Long taskId,
			@RequestBody Task task) {
		logger.info("Updating Task with id {}", taskId);

		Task currentTask = taskService.getTaskInfo(taskId);

		if (currentTask == null) {
			logger.error("Unable to update. Task with id {} not found.", taskId);
			return new ResponseEntity(new CustomErrorType("Unable to upate. Task with id " + taskId + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentTask.setName(task.getName());
		currentTask.setDescription(task.getDescription());
		currentTask.setDateTime(task.getDateTime());
		currentTask.setUser(userService.getUserInfo(userId));

		return new ResponseEntity<Task>(taskService.updateTask(currentTask), HttpStatus.OK);
	}

	// ----------------------------- Delete Task --------------------------------------------------------------------//

	@DeleteMapping(value = "/{user_id}/task/{task_id}")
	public ResponseEntity<?> deleteTask(@PathVariable("user_id") Long userId, @PathVariable("task_id") Long taskId) {
		logger.info("Fetching & Deleting User with id {}", taskId);

		Task task = taskService.getTaskInfo(taskId);
		if (task == null) {
			logger.error("Unable to delete. User with id {} not found.", taskId);
			return new ResponseEntity(new CustomErrorType("Unable to delete. User with id " + taskId + " not found."),
					HttpStatus.NOT_FOUND);
		}
		taskService.deleteTask(taskId);
		return new ResponseEntity<Task>(HttpStatus.NO_CONTENT);
	}

	// ----------------------------- Get Task Info --------------------------------------------------------------------//

	@GetMapping(value = "/{user_id}/task/{task_id}")
	public ResponseEntity<?> getTaskInfo(@PathVariable("user_id") Long userId, @PathVariable("task_id") Long taskId) {
		logger.info("Fetching TaskInfo with id {}", taskId);
		Task task = taskService.getTaskInfo(taskId);
		if (task == null) {
			logger.error("Task with id {} not found.", taskId);
			return new ResponseEntity(new CustomErrorType("Task with id " + taskId + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Task>(task, HttpStatus.OK);
	}

	// ----------------------------- List all tasks for a user --------------------------------//

	@GetMapping(value = "/{user_id}/task")
	public ResponseEntity<List<Task>> listAllTasksFoUser(@PathVariable("user_id") Long userId) {
		List<Task> tasks = taskService.listAllTasksFoUser(userId);
		if (tasks.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
	}
}
