package com.swynex.backend.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swynex.backend.dto.TaskRequest;
import com.swynex.backend.dto.TaskUpdateRequest;
import com.swynex.backend.exception.TaskNotFoundException;
import com.swynex.backend.model.Priority;
import com.swynex.backend.model.Status;
import com.swynex.backend.model.Task;
import com.swynex.backend.repository.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Task> search(Status status, Priority priority, String q, int page, int size) {
        Specification<Task> spec = (root, query, cb) -> cb.conjunction();
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (priority != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("priority"), priority));
        }
        if (q != null && !q.isBlank()) {
            String like = "%" + q.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("title")), like));
        }
        return repository.findAll(spec, PageRequest.of(page, size, Sort.by("id"))).getContent();
    }

    @Transactional(readOnly = true)
    public Task get(Long id) {
        return repository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public Task create(TaskRequest req) {
        Task task = new Task();
        task.setTitle(req.title().trim());
        if (req.description() != null) task.setDescription(req.description());
        if (req.status() != null) task.setStatus(req.status());
        if (req.priority() != null) task.setPriority(req.priority());
        return repository.save(task);
    }

    @Transactional
    public Task update(Long id, TaskUpdateRequest req) {
        Task task = get(id);
        if (req.title() != null) task.setTitle(req.title().trim());
        if (req.description() != null) task.setDescription(req.description());
        if (req.status() != null) task.setStatus(req.status());
        if (req.priority() != null) task.setPriority(req.priority());
        return repository.save(task);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(get(id));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> stats() {
        List<Task> all = repository.findAll();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", all.size());
        result.put("byStatus", all.stream().collect(
                Collectors.groupingBy(t -> t.getStatus().name(), Collectors.counting())));
        result.put("byPriority", all.stream().collect(
                Collectors.groupingBy(t -> t.getPriority().name(), Collectors.counting())));
        return result;
    }
}
