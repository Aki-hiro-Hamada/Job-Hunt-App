package com.example.jobapp.service;

import com.example.jobapp.entity.JobApplication;
import com.example.jobapp.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository repository;

    public List<JobApplication> findAll() {
        return repository.findAll(Sort.by(Sort.Order.asc("interviewDate").nullsLast()));
    }

    public JobApplication findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid application Id:" + id));
    }

    @Transactional
    public void save(JobApplication application) {
        repository.save(application);
    }
}
