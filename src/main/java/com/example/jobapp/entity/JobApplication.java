package com.example.jobapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "job_applications")
@Data
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "会社名は必須です")
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @NotBlank(message = "ステータスは必須です")
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "interview_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate interviewDate;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

}
