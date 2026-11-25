package com.swynex.backend.dto;

import com.swynex.backend.model.Priority;
import com.swynex.backend.model.Status;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Body for a partial update: only the fields that are sent are changed. */
public record TaskUpdateRequest(
        @Size(max = 120, message = "title must be at most 120 characters")
        @Pattern(regexp = ".*\\S.*", message = "title must not be blank") String title,
        @Size(max = 1000, message = "description must be at most 1000 characters") String description,
        Status status,
        Priority priority) {
}
