package com.taskflow.backend.task;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskStatsResponse {
    private long total;
    private long todo;
    private long inProgress;
    private long done;
    private long overdue;
}