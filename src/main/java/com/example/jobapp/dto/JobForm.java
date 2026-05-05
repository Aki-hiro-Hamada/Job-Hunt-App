package com.example.jobapp.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JobForm {

    private String id;

    @NotBlank(message = "会社名は必須です")
    private String companyName;

    @NotBlank(message = "ステータスは必須です")
    private String status;

    @PastOrPresent(message = "応募日は未来日を指定できません")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate interviewDate;

    @Pattern(
            regexp = "^$|^(https?://)?([\\w-]+\\.)+[\\w-]{2,}(/[\\w\\-./?%&=+#]*)?$",
            message = "URLの形式が正しくありません")
    private String websiteUrl;

    @Size(max = 1000, message = "メモは1000文字以内で入力してください")
    private String memo;
}
