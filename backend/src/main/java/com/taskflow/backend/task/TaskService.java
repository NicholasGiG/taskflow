package com.taskflow.backend.task;

import com.taskflow.backend.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final BoardRepository boardRepository;

    public TaskResponse create(Long boardId, TaskRequest request) {
        var board = boardRepository.findById(boardId).orElseThrow();
        var task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO)
                .priority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM)
                .dueDate(request.getDueDate())
                .board(board)
                .build();
        return toResponse(taskRepository.save(task));
    }

    public List<TaskResponse> getByBoard(Long boardId, TaskStatus status, TaskPriority priority) {
        if (status != null && priority != null) {
            return taskRepository.findByBoardIdAndStatusAndPriority(boardId, status, priority)
                    .stream().map(this::toResponse).toList();
        } else if (status != null) {
            return taskRepository.findByBoardIdAndStatus(boardId, status)
                    .stream().map(this::toResponse).toList();
        } else if (priority != null) {
            return taskRepository.findByBoardIdAndPriority(boardId, priority)
                    .stream().map(this::toResponse).toList();
        }
        return taskRepository.findByBoardId(boardId)
                .stream().map(this::toResponse).toList();
    }

    public TaskResponse update(Long id, TaskRequest request) {
        var task = taskRepository.findById(id).orElseThrow();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        return toResponse(taskRepository.save(task));
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public TaskStatsResponse getStats(Long boardId) {
        long total = taskRepository.countByBoardId(boardId);
        long todo = taskRepository.countByBoardIdAndStatus(boardId, TaskStatus.TODO);
        long inProgress = taskRepository.countByBoardIdAndStatus(boardId, TaskStatus.IN_PROGRESS);
        long done = taskRepository.countByBoardIdAndStatus(boardId, TaskStatus.DONE);
        long overdue = taskRepository.findOverdue(boardId, LocalDate.now()).size();
        return new TaskStatsResponse(total, todo, inProgress, done, overdue);
    }

    private TaskResponse toResponse(Task t) {
        return new TaskResponse(t.getId(), t.getTitle(), t.getDescription(),
                t.getStatus(), t.getPriority(), t.getDueDate(),
                t.getBoard().getId(), t.getCreatedAt());
    }
}