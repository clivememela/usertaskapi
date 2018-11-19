package za.co.memela.usertask.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import za.co.memela.usertask.api.model.Task;
import za.co.memela.usertask.api.repository.TaskRepository;

@Service
public class TaskService {
	
	@Autowired
	private TaskRepository taskRepository;

	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public Task createTask(Task task) {
		return taskRepository.save(task);
	}

	public Task updateTask(Task task) {
		return taskRepository.save(task);
	}

	public void deleteTask(Long id) {
		taskRepository.deleteById(id);
	}

	public Task getTaskInfo(Long id) {
		return taskRepository.getOne(id);
	}

	public boolean isTaskExist(Task task) {
		return taskRepository.findById(task.getId()) != null;
	}

	public List<Task> listAllTasksFoUser(Long userId) {
		return taskRepository.findByUserId(userId);
	}

}
