package com.taskflow.backend.board;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BoardResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}