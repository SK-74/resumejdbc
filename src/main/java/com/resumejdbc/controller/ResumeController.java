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
import com.resumejdbc.entity.MemberResume;
import com.resumejdbc.entity.Resume;
import com.resumejdbc.service.MemberService;
import com.resumejdbc.service.ResumeService;

@Controller
public class ResumeController {

	//業務ロジック用
	private MemberService memberSvc;

	//業務ロジック用
	private ResumeService resumeSvc;

	/**
	 * コンストラクタ
	 * @param memberSvc 業務ロジック（会員サービス）
	 * @param resumeSvc 業務ロジック（経歴サービス）
	 */
	public ResumeController(MemberService memberSvc, ResumeService resumeSvc) {
		this.memberSvc = memberSvc;
		this.resumeSvc = resumeSvc;
	}
	
	/**
	 * 経歴一覧
	 * @param id ID
	 * @param model モデル
	 * @return
	 */
	@GetMapping("/resumeList")
	public String resumeList(@RequestParam(name="id") Long id, Model model){
		try {
			Member member = this.memberSvc.findById(id);
			List<Resume> resumes = new ArrayList<>();
			if(Objects.nonNull(member)) {
				resumes = this.resumeSvc.findByMemberId(member.getId());
			}
	
			model.addAttribute("member", member);
			model.addAttribute("resumes", resumes);
			model.addAttribute("age", this.memberSvc.getAge(member.getId()));
		}catch(Exception ex) {
			ex.printStackTrace();
			//メッセージを画面表示する
			model.addAttribute("errmsg", ex.getMessage());
			//空の情報を渡す
			model.addAttribute("member", new Member());
			model.addAttribute("resumes", new ArrayList<Resume>());
		}
		
		return "resumeList";
	}

	/**
	 * 経歴追加画面
	 * @param id ID
	 * @param model モデル
	 * @return
	 */
	@GetMapping("/newResume")
	public String newResume(@RequestParam(name="id") Long id, Model model){
		//初期表示用（hidden項目）の会員IDを設定する
		Resume resume = new Resume();
		resume.setMemberId(id);
		
		try {
			Member member = this.memberSvc.findById(id);
	
			model.addAttribute("member", member);
			model.addAttribute("resume", resume);
		}catch(Exception ex) {
			ex.printStackTrace();
			//メッセージを画面表示する
			model.addAttribute("errmsg", ex.getMessage());
			//空の情報を渡す
			model.addAttribute("member", new Member());
			model.addAttribute("resume", new Resume());
		}
		
		return "newResume";
	}

	/**
	 * 経歴追加の要求
	 * @param request リクエスト
	 * @param validResult バリデーション結果
	 * @param model モデル
	 * @param redirectAttrs リダイレクト用モデル
	 * @return
	 */
	@PostMapping("/insertResume")
	public String insertResume(@Validated @ModelAttribute("resume") Resume request,
			BindingResult validResult, Model model, RedirectAttributes redirectAttrs) {

		try {
			if(! validResult.hasErrors()) {
				resumeSvc.insertResume(request);
			}else {
				//元の画面にメンバー名を表示させるために設定する
				Member member = this.memberSvc.findById(request.getMemberId());
				model.addAttribute("member", member);
				return "newResume";
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			//メッセージを画面表示する
			model.addAttribute("errmsg", ex.getMessage());
			//空の情報を渡す
			model.addAttribute("member", new Member());
			return "newResume";
		}

		redirectAttrs.addAttribute("id", request.getMemberId());
		return "redirect:/resumeList";
	}

	/**
	 * 経歴編集画面
	 * @param id ID
	 * @param model モデル
	 * @return
	 */
	@GetMapping("/editResume")
	public String editResume(@RequestParam(name="id") Long id, Model model){
		try {
			MemberResume memberResume = this.resumeSvc.findWithMemberById(id);
	
			model.addAttribute("memberResume", memberResume);
		}catch(Exception ex) {
			ex.printStackTrace();
			//メッセージを画面表示する
			model.addAttribute("errmsg", ex.getMessage());
			//空の情報を渡す
			model.addAttribute("memberResume", new MemberResume());
		}
		
		return "editResume";
	}

	/**
	 * 経歴更新の要求
	 * @param request リクエスト
	 * @param validResult バリデーション結果
	 * @param model モデル
	 * @param redirectAttrs リダイレクト用モデル
	 * @return
	 */
	@PostMapping("/updateResume")
	public String updateResume(@Validated @ModelAttribute("memberResume") MemberResume request,
			BindingResult validResult, Model model, RedirectAttributes redirectAttrs) {

		try {
			if(! validResult.hasErrors()) {
				resumeSvc.updateResume(request);
			}else {
				//元の画面にメンバー名を表示させるために設定する
				model.addAttribute("memberResume", request);
				return "editResume";
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			//メッセージを画面表示する
			model.addAttribute("errmsg", ex.getMessage());
			//空の情報を渡す
			model.addAttribute("memberResume", new MemberResume());
			return "editResume";
		}

		redirectAttrs.addAttribute("id", request.getMemberId());
		return "redirect:/resumeList";
	}
	
	/**
	 * 経歴削除の要求
	 * @param id ID
	 * @param model モデル
	 * @param redirectAttrs リダイレクト用モデル
	 * @return
	 */
	@PostMapping("/deleteResume")
	public String deleteResume(@RequestParam(name="id") Long id, Model model, RedirectAttributes redirectAttrs) {
		try {
			//redirect時にメンバーIDが必要なので削除前にresumeを取得
			Resume resume = this.resumeSvc.findById(id);
			
			this.resumeSvc.deleteResume(id);
	
			redirectAttrs.addAttribute("id", resume.getMemberId());
		}catch(Exception ex) {
			ex.printStackTrace();
			//メッセージを画面表示する
			model.addAttribute("errmsg", ex.getMessage());
		}
		return "redirect:/resumeList";
	}

}
