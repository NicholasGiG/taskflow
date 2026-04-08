package com.taskflow.backend.task;

import com.taskflow.backend.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public List<TaskResponse> getByBoard(Long boardId) {
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

    private TaskResponse toResponse(Task t) {
        return new TaskResponse(t.getId(), t.getTitle(), t.getDescription(),
                t.getStatus(), t.getPriority(), t.getDueDate(),
                t.getBoard().getId(), t.getCreatedAt());
    }
}