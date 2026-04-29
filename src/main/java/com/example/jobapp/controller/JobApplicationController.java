package com.example.jobapp.controller;

import com.example.jobapp.entity.JobApplication;
import com.example.jobapp.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("applications", service.findAll());
        return "list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("jobApplication", new JobApplication());
        return "create";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute JobApplication jobApplication, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "create";
        }
        service.save(jobApplication);
        return "redirect:/applications";
    }
}
