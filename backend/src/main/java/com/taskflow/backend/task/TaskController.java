package com.taskflow.backend.task;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/boards/{boardId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> create(@PathVariable Long boardId,
                                               @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.create(boardId, request));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getByBoard(
            @PathVariable Long boardId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority) {
        return ResponseEntity.ok(taskService.getByBoard(boardId, status, priority));
    }

    @GetMapping("/stats")
    public ResponseEntity<TaskStatsResponse> getStats(@PathVariable Long boardId) {
        return ResponseEntity.ok(taskService.getStats(boardId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable Long boardId,
                                               @PathVariable Long id,
                                               @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long boardId, @PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}