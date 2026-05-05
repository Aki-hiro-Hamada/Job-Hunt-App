package com.example.jobapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.jobapp.dto.JobForm;
import com.example.jobapp.entity.JobApplication;
import com.example.jobapp.entity.JobHistory;
import com.example.jobapp.service.JobApplicationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("applications", service.findAll());
        model.addAttribute("ingCount", service.getCountByStatus("書類選考中"));
        return "list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("jobForm", new JobForm());
        return "create";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute("jobForm") JobForm jobForm, BindingResult result) {
        if (result.hasErrors()) {
            return "create";
        }
        service.save(toEntity(jobForm));
        return "redirect:/applications";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") String id, Model model) {
        JobApplication application = service.findById(id);
        JobForm jobForm = new JobForm();
        jobForm.setId(application.getId());
        jobForm.setCompanyName(application.getCompanyName());
        jobForm.setStatus(application.getStatus());
        jobForm.setInterviewDate(application.getInterviewDate());
        jobForm.setWebsiteUrl(application.getWebsiteUrl());
        jobForm.setMemo(application.getMemo());
        model.addAttribute("jobForm", jobForm);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, @Validated @ModelAttribute("jobForm") JobForm jobForm,
            BindingResult result) {
        if (result.hasErrors()) {
            return "edit";
        }
        JobApplication jobApplication = toEntity(jobForm);
        jobApplication.setId(id);
        service.save(jobApplication);
        return "redirect:/applications";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id) {
        service.deleteById(id);
        return "redirect:/applications";
    }

    @PostMapping("/{id}/history")
    public String addHistory(@PathVariable String id, @ModelAttribute JobHistory newHistory) {
        service.addHistory(id, newHistory);
        return "redirect:/applications";
    }

    private JobApplication toEntity(JobForm form) {
        JobApplication application = new JobApplication();
        application.setId(form.getId());
        application.setCompanyName(form.getCompanyName());
        application.setStatus(form.getStatus());
        application.setInterviewDate(form.getInterviewDate());
        application.setWebsiteUrl(form.getWebsiteUrl());
        application.setMemo(form.getMemo());
        return application;
    }

}
