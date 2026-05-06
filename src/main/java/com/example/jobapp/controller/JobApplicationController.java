package com.example.jobapp.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") String id, Model model) {
        JobApplication application = service.findById(id);
        model.addAttribute("jobApplication", application);
        return "detail";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("jobApplication", new JobApplication());
        return "create";
    }

    /**
     * 旧URL互換（デプロイ差分やブックマーク対策）。
     * /applications/new を /applications/create に寄せます。
     */
    @GetMapping("/new")
    public String newFormCompat() {
        return "redirect:/applications/create";
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

    /**
     * 削除は GET で副作用を起こさない（リンクを踏んだだけで消える事故を防ぐ）。
     */
    @GetMapping("/delete/{id}")
    public String deleteGetCompat(@PathVariable("id") String id) {
        return "redirect:/applications";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id) {
        service.deleteById(id);
        return "redirect:/applications";
    }

    @PostMapping("/{id}/history")
    public String addHistory(@PathVariable("id") String id, @ModelAttribute JobHistory newHistory) {
        service.addHistory(id, newHistory);
        return "redirect:/applications/edit/" + id;
    }

    /**
     * モバイル等で「自動更新（ポーリング）」するためのAPI。
     * キャッシュを効かせないよう no-store を付与します。
     */
    @GetMapping("/api/applications")
    @ResponseBody
    public ResponseEntity<List<JobApplication>> apiApplications() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(service.findAll());
    }

    /**
     * 応募先詳細の最新データ取得（自動更新用）。
     */
    @GetMapping("/api/applications/{id}")
    @ResponseBody
    public ResponseEntity<JobApplication> apiApplication(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.noStore())
                    .body(service.findById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound()
                    .cacheControl(CacheControl.noStore())
                    .build();
        }
    }
}
