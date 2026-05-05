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

import com.example.jobapp.entity.JobApplication;
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

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") String id, Model model) {
        JobApplication application = service.findById(id);
        model.addAttribute("jobApplication", application);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, @Validated @ModelAttribute JobApplication jobApplication, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "edit";
        }
        jobApplication.setId(id);
        service.save(jobApplication);
        return "redirect:/applications";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id) {
        service.deleteById(id);
        return "redirect:/applications";
    }

}
