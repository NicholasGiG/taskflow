package com.taskflow.backend.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BoardRequest {
    @NotBlank
    private String name;
    private String description;
}
