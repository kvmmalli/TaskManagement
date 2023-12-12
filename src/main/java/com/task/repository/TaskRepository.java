package com.task.repository;

import com.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Task entities.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByDueDateBeforeAndStatusNot(LocalDate currentDate, String status);
    List<Task> findByProjectIdAndStatus(Long projectId, String status);

    List<Task> findByProjectIdAndStatusAndDueDateAfter(Long projectId, String status, LocalDate dueDate);

    List<Task> findByProjectId(Long projectId);


}
