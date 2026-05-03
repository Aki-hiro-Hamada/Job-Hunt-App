package com.example.jobapp.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.jobapp.entity.JobApplication;
import com.example.jobapp.repository.JobApplicationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository repository;

    public List<JobApplication> findAll() {
        return repository.findAll(Sort.by(Sort.Order.asc("interviewDate").nullsLast()));
    }

    public JobApplication findById(String id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid application Id:" + id));
    }

    public void save(JobApplication application) {
        repository.save(application);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

}
