package com.resumejdbc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.resumejdbc.entity.Member;
import com.resumejdbc.entity.Resume;
import com.resumejdbc.service.MemberService;
import com.resumejdbc.service.ResumeService;

@Controller
public class ResumeController {
	
	private MemberService memberSvc;
	
	private ResumeService resumeSvc;

	public ResumeController(MemberService memberSvc, ResumeService resumeSvc) {
		this.memberSvc = memberSvc;
		this.resumeSvc = resumeSvc;
	}
	
	@GetMapping("/resumeList")
	public String resumeList(@RequestParam(name="id") Integer id, Model model){
		Member member = this.memberSvc.findById(id);
		List<Resume> resumes = new ArrayList<>();
		if(Objects.nonNull(member)) {
			resumes = this.resumeSvc.findByMemberId(member.getId());
		}

		model.addAttribute("member", member);
		model.addAttribute("resumes", resumes);
		
		return "resumeList";
	}

	@GetMapping("/newResume")
	public String newResume(@RequestParam(name="id") Integer id, Model model){
		Member member = this.memberSvc.findById(id);
		
		Resume resume = new Resume();
		resume.setMemberId(id);

		model.addAttribute("member", member);
		model.addAttribute("resume", resume);
		
		return "newResume";
	}

	@PostMapping("/insertResume")
	public String insertResume(@Validated @ModelAttribute("resume") Resume request,
			BindingResult validResult, Model model, RedirectAttributes redirectAttrs) {

		if(! validResult.hasErrors()) {
			resumeSvc.insertResume(request);
		}else {
			//元の画面にメンバー名を表示させるために設定する
			Member member = this.memberSvc.findById(request.getMemberId());
			model.addAttribute("member", member);
			return "newResume";
		}

		redirectAttrs.addAttribute("id", request.getMemberId());
		return "redirect:/resumeList";
	}

	@GetMapping("/editResume")
	public String editResume(@RequestParam(name="id") Integer id, Model model){
		Resume resume = this.resumeSvc.findById(id);

		Member member = this.memberSvc.findById(resume.getMemberId());

		model.addAttribute("member", member);
		model.addAttribute("resume", resume);
		
		return "editResume";
	}

	@PostMapping("/updateResume")
	public String updateResume(@Validated @ModelAttribute("resume") Resume request,
			BindingResult validResult, Model model, RedirectAttributes redirectAttrs) {

		if(! validResult.hasErrors()) {
			resumeSvc.updateResume(request);
		}else {
			//元の画面にメンバー名を表示させるために設定する
			Member member = this.memberSvc.findById(request.getMemberId());
			model.addAttribute("member", member);
			return "editResume";
		}

		redirectAttrs.addAttribute("id", request.getMemberId());
		return "redirect:/resumeList";
	}
	
	@PostMapping("/deleteResume")
	public String deleteResume(@RequestParam(name="id") Integer id, RedirectAttributes redirectAttrs) {
		//redirect時にメンバーIDが必要なので削除前にresumeを取得
		Resume resume = this.resumeSvc.findById(id);
		
		this.resumeSvc.deleteResume(id);

		redirectAttrs.addAttribute("id", resume.getMemberId());
		return "redirect:/resumeList";
	}

}
