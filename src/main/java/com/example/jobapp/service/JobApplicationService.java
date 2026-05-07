package com.example.jobapp.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.jobapp.entity.JobApplication;
import com.example.jobapp.entity.JobHistory;
import com.example.jobapp.repository.JobApplicationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository repository;

    public List<JobApplication> findAll(String ownerUserId) {
        return repository.findAllByOwnerUserId(ownerUserId, Sort.by(Sort.Order.asc("interviewDate").nullsLast()));
    }

    public JobApplication findById(String ownerUserId, String id) {
        return repository.findByIdAndOwnerUserId(id, ownerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application Id:" + id));
    }

    public void save(String ownerUserId, JobApplication application) {
        application.setOwnerUserId(ownerUserId);

        if (application.getId() != null && application.getJobHistories() == null) {
            repository.findByIdAndOwnerUserId(application.getId(), ownerUserId)
                    .ifPresent(existing -> application.setJobHistories(existing.getJobHistories()));
        }
        repository.save(application);
    }

    public void deleteById(String ownerUserId, String id) {
        repository.deleteByIdAndOwnerUserId(id, ownerUserId);
    }

    public long getCountByStatus(String ownerUserId, String status) {
        return repository.countByOwnerUserIdAndStatus(ownerUserId, status);
    }

    public void addHistory(String ownerUserId, String jobId, JobHistory newHistory) {
        JobApplication job = repository.findByIdAndOwnerUserId(jobId, ownerUserId)
                .orElseThrow(() -> new RuntimeException("応募先が見つかりません"));
        if (job.getJobHistories() == null) {
            job.setJobHistories(new ArrayList<>());
        }
        job.getJobHistories().add(newHistory);
        repository.save(job);
    }

}
