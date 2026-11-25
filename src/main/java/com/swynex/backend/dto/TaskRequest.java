package com.swynex.backend.dto;

import com.swynex.backend.model.Priority;
import com.swynex.backend.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Body for creating a task (also the format of sample_data.json). */
public record TaskRequest(
        @NotBlank(message = "title is required") @Size(max = 120, message = "title must be at most 120 characters") String title,
        @Size(max = 1000, message = "description must be at most 1000 characters") String description,
        Status status,
        Priority priority) {
}
