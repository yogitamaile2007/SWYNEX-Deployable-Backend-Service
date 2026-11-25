package com.swynex.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.swynex.backend.service.SampleDataService;

/** Loads sample data on startup when the database is empty. */
@Component
public class DataLoader implements CommandLineRunner {

    private final SampleDataService sampleDataService;

    public DataLoader(SampleDataService sampleDataService) {
        this.sampleDataService = sampleDataService;
    }

    @Override
    public void run(String... args) {
        sampleDataService.seed(false);
    }
}
