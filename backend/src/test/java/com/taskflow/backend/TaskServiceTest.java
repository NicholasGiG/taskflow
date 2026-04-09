package com.taskflow.backend;

import com.taskflow.backend.board.Board;
import com.taskflow.backend.board.BoardRepository;
import com.taskflow.backend.task.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private TaskService taskService;

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
        board.setId(1L);
        board.setName("Test Board");
    }

    @Test
    void shouldCreateTaskSuccessfully() {
        var request = new TaskRequest();
        request.setTitle("Nuovo task");
        request.setPriority(TaskPriority.HIGH);

        var savedTask = Task.builder()
                .id(1L).title("Nuovo task")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.HIGH)
                .board(board).build();

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(taskRepository.save(any())).thenReturn(savedTask);

        var result = taskService.create(1L, request);

        assertThat(result.getTitle()).isEqualTo("Nuovo task");
        assertThat(result.getPriority()).isEqualTo(TaskPriority.HIGH);
        assertThat(result.getStatus()).isEqualTo(TaskStatus.TODO);
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void shouldReturnFilteredTasksByStatus() {
        var task = Task.builder()
                .id(1L).title("Task done")
                .status(TaskStatus.DONE)
                .priority(TaskPriority.LOW)
                .board(board).build();

        when(taskRepository.findByBoardIdAndStatus(1L, TaskStatus.DONE))
                .thenReturn(List.of(task));

        var result = taskService.getByBoard(1L, TaskStatus.DONE, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void shouldReturnStatsCorrectly() {
        when(taskRepository.countByBoardId(1L)).thenReturn(5L);
        when(taskRepository.countByBoardIdAndStatus(1L, TaskStatus.TODO)).thenReturn(2L);
        when(taskRepository.countByBoardIdAndStatus(1L, TaskStatus.IN_PROGRESS)).thenReturn(1L);
        when(taskRepository.countByBoardIdAndStatus(1L, TaskStatus.DONE)).thenReturn(2L);
        when(taskRepository.findOverdue(any(), any())).thenReturn(List.of());

        var stats = taskService.getStats(1L);

        assertThat(stats.getTotal()).isEqualTo(5);
        assertThat(stats.getDone()).isEqualTo(2);
        assertThat(stats.getOverdue()).isEqualTo(0);
    }
}