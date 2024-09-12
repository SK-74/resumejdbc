package com.resumejdbc.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.resumejdbc.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {

	//ビジネスロジック用
	private MemberService memberSvc;

	//HTTPセッション用
	private HttpSession session;
	
	public MemberController(MemberService memberSvc, HttpSession session) {
		this.memberSvc = memberSvc;
		this.session = session;
	}

	@GetMapping("/")
	public String memberList(Model model) {
		
		session.removeAttribute("dateFrom");
		session.removeAttribute("dateTo");

		return "memberList";
	}

	@GetMapping("/searchMember")
	public String searchList(
			@RequestParam(name = "dateFrom", required = false) LocalDate from, 
			@RequestParam(name = "dateTo", required = false) LocalDate to, 
			Model model) {
		//検索条件をHTTPセッションに保持する
		session.setAttribute("dateFrom", from);
		session.setAttribute("dateTo", to);
		
		List<Member> members = this.memberSvc.findByBirth(from, to);

		model.addAttribute("dateFrom", from);
		model.addAttribute("dateTo", to);
		model.addAttribute("members", members);
		
		return "memberList";
	}

	@GetMapping("/newMember")
	public String newMember(@ModelAttribute("member") Member request, Model model) {
		
		return "newMember";
	}
	
	@PostMapping("/insertMember")
	public String insertMember(@Validated @ModelAttribute("member") Member request, 
			BindingResult validResult, Model model) {

		if(! validResult.hasErrors()) {
			memberSvc.insertMember(request);
		}else {
			return "newMember";
		}
		
		return "redirect:/backToMemberList";
	}
	
	@GetMapping("/editMember")
	public String editMember(@RequestParam("id") Integer id, Model model) {
		Member member = this.memberSvc.findById(id);
		
		//初期表示するためにModelにmemberを設定する
		model.addAttribute("member", member);
		
		return "editMember";
	}

	@PostMapping("/updateMember")
	public String updateMember(@Validated @ModelAttribute("member") Member request, 
			BindingResult validResult, Model model) {

		if(! validResult.hasErrors()) {
			memberSvc.updateMember(request);
		}else {
			return "newMember";
		}

		return "redirect:/backToMemberList";
	}
	
	@PostMapping("/deleteMember")
	public String deleteMember(@RequestParam("id") Integer id, RedirectAttributes redirectAttrs) {

		memberSvc.deleteMember(id);

		return "redirect:/backToMemberList";
	}

	@GetMapping("/backToMemberList")
	public String backToMemberList(RedirectAttributes redirectAttributes) {
		//HTTPセッションに保存していた検索条件を取り出す
		LocalDate from = (LocalDate) session.getAttribute("dateFrom");
		LocalDate to = (LocalDate) session.getAttribute("dateTo");

		//クエリパラメータに関連付けするにあたって指定なしなら空の文字列に変換すること
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String strFrom = "";
		if(Objects.nonNull(from)) {
			strFrom = from.format(formatter);
		}
		String strTo = "";
		if(Objects.nonNull(to)) {
			strTo = to.format(formatter);
		}

		//取り出した検索条件をクエリパラメータに関連付けする
		redirectAttributes.addAttribute("dateFrom", strFrom);
		redirectAttributes.addAttribute("dateTo", strTo);
		
		//クエリパラメータ付きのURLでリダイレクトする
		return "redirect:/searchMember?dateFrom={dateFrom}&dateTo={dateTo}";
	}
	
}
