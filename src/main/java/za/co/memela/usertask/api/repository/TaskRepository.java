package za.co.memela.usertask.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import za.co.memela.usertask.api.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findByUserId(Long userId);

}
