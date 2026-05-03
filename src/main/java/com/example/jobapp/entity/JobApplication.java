package com.example.jobapp.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Document(collection = "job_applications")
@Data
public class JobApplication {

    @Id
    private String id;

    @NotBlank(message = "会社名は必須です")
    private String companyName;

    @NotBlank(message = "ステータスは必須です")
    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate interviewDate;

    private String memo;

}
