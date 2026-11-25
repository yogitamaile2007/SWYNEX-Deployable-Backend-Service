package com.swynex.backend.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swynex.backend.dto.TaskRequest;
import com.swynex.backend.repository.TaskRepository;

/** Loads src/main/resources/sample_data.json into the database. */
@Service
public class SampleDataService {

    private final TaskRepository repository;
    private final TaskService taskService;
    private final ObjectMapper mapper;
    private final Resource sampleFile;

    public SampleDataService(TaskRepository repository, TaskService taskService, ObjectMapper mapper,
                             @Value("classpath:sample_data.json") Resource sampleFile) {
        this.repository = repository;
        this.taskService = taskService;
        this.mapper = mapper;
        this.sampleFile = sampleFile;
    }

    /** Seeds an empty table; with force=true the table is cleared first. Returns rows inserted. */
    @Transactional
    public int seed(boolean force) {
        if (repository.count() > 0 && !force) {
            return 0;
        }
        if (force) {
            repository.deleteAll();
        }
        try (InputStream in = sampleFile.getInputStream()) {
            List<TaskRequest> items = mapper.readValue(in, new TypeReference<List<TaskRequest>>() {});
            items.forEach(taskService::create);
            return items.size();
        } catch (IOException e) {
            throw new IllegalStateException("Could not read sample_data.json", e);
        }
    }
}
