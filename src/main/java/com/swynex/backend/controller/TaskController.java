package com.swynex.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.swynex.backend.dto.TaskRequest;
import com.swynex.backend.dto.TaskUpdateRequest;
import com.swynex.backend.model.Priority;
import com.swynex.backend.model.Status;
import com.swynex.backend.model.Task;
import com.swynex.backend.service.SampleDataService;
import com.swynex.backend.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api")
@Validated
@Tag(name = "tasks")
public class TaskController {

    private final TaskService service;
    private final SampleDataService sampleDataService;

    public TaskController(TaskService service, SampleDataService sampleDataService) {
        this.service = service;
        this.sampleDataService = sampleDataService;
    }

    @Operation(summary = "List tasks (filter by status, priority, title search; paginated)")
    @GetMapping("/tasks")
    public List<Task> list(@RequestParam(required = false) Status status,
                           @RequestParam(required = false) Priority priority,
                           @RequestParam(required = false) String q,
                           @RequestParam(defaultValue = "0") @Min(0) int page,
                           @RequestParam(defaultValue = "50") @Min(1) @Max(200) int size) {
        return service.search(status, priority, q, page, size);
    }

    @Operation(summary = "Get one task")
    @GetMapping("/tasks/{id}")
    public Task get(@PathVariable Long id) {
        return service.get(id);
    }

    @Operation(summary = "Create a task")
    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@Valid @RequestBody TaskRequest request) {
        return service.create(request);
    }

    @Operation(summary = "Update a task (partial update)")
    @PutMapping("/tasks/{id}")
    public Task update(@PathVariable Long id, @Valid @RequestBody TaskUpdateRequest request) {
        return service.update(id, request);
    }

    @Operation(summary = "Delete a task")
    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @Operation(summary = "Task counts by status and priority")
    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return service.stats();
    }

    @Operation(summary = "Reset the table to the sample data")
    @PostMapping("/seed")
    public Map<String, Integer> seed() {
        return Map.of("seeded", sampleDataService.seed(true));
    }
}
