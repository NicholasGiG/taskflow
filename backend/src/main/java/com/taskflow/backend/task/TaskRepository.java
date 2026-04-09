package com.taskflow.backend.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByBoardId(Long boardId);
    List<Task> findByBoardIdAndStatus(Long boardId, TaskStatus status);
    List<Task> findByBoardIdAndPriority(Long boardId, TaskPriority priority);
    List<Task> findByBoardIdAndStatusAndPriority(Long boardId, TaskStatus status, TaskPriority priority);

    @Query("SELECT t FROM Task t WHERE t.board.id = :boardId AND t.dueDate < :today AND t.status != 'DONE'")
    List<Task> findOverdue(@Param("boardId") Long boardId, @Param("today") LocalDate today);

    long countByBoardIdAndStatus(Long boardId, TaskStatus status);
    long countByBoardId(Long boardId);
}