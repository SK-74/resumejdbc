package com.resumejdbc.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.resumejdbc.request.MemberSearchCriteria;
import com.resumejdbc.result.Result;
import com.resumejdbc.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {

	//業務ロジック用
	private MemberService memberSvc;

	//HTTPセッション用
	private HttpSession session;
	
	/**
	 * コンストラクタ
	 * @param memberSvc 業務ロジック（会員サービス）
	 * @param session HTTPセッション
	 */
	public MemberController(MemberService memberSvc, HttpSession session) {
		this.memberSvc = memberSvc;
		this.session = session;
	}

	/**
	 * 初期画面
	 * @param criteria 検索条件
	 * @param model モデル
	 * @return
	 */
	@GetMapping("/")
	public String memberList(@ModelAttribute("criteria") MemberSearchCriteria criteria, Model model) {
		
		//HTTPセッションに保存していた情報を削除する
		this.session.removeAttribute("dateFrom");
		this.session.removeAttribute("dateTo");
		this.session.removeAttribute("isSearch");

		return "memberList";
	}

	/**
	 * 会員検索の要求
	 * @param criteria
	 * @param validResult
	 * @param model
	 * @return
	 */
	@GetMapping("/searchMember")
	public String searchList(@Validated @ModelAttribute("criteria") MemberSearchCriteria criteria, 
			BindingResult validResult, Model model) {

		if(! validResult.hasErrors()) {
			//検索条件をHTTPセッションに保持する
			this.session.setAttribute("dateFrom", criteria.getDateFrom());
			this.session.setAttribute("dateTo", criteria.getDateTo());
			this.session.setAttribute("isSearch", true);

			//検索条件を画面に再表示するためにmodelに設定する
			model.addAttribute("dateFrom", criteria.getDateFrom());
			model.addAttribute("dateTo", criteria.getDateTo());
			
			List<Member> members = new ArrayList<>();
			try {
				//データ検索
				members = this.memberSvc.findByBirth(criteria);
				model.addAttribute("members", members);
			}catch(Exception ex) {
				ex.printStackTrace();
				//メッセージを画面表示する
				model.addAttribute("errmsg", ex.getMessage());
				//空の情報を渡す
				model.addAttribute("members", new ArrayList<Member>());
			}
		}
		
		return "memberList";
	}

	/**
	 * 会員登録画面
	 * @param request　リクエスト
	 * @param model
	 * @return
	 */
	@GetMapping("/newMember")
	public String newMember(@ModelAttribute("member") Member request, Model model) {
		
		return "newMember";
	}
	
	/**
	 * 会員登録の要求
	 * @param request リクエスト
	 * @param validResult バリデーション結果
	 * @param model モデル
	 * @return
	 */
	@PostMapping("/insertMember")
	public String insertMember(@Validated @ModelAttribute("member") Member request, 
			BindingResult validResult, Model model, RedirectAttributes redirectAttributes) {

		if(! validResult.hasErrors()) {
			try {
				//データ挿入
				Result result = memberSvc.insertMember(request);
				if(! result.isOk()) {
					//業務チェックのNG
					model.addAttribute("errmsg", result.getErrMsg());
					return "newMember";
				}
			}catch(Exception ex) {
				ex.printStackTrace();
				//メッセージを画面表示する
				model.addAttribute("errmsg", ex.getMessage());
				return "newMember";
			}
		}else {
			return "newMember";
		}

		//セッションに保存していた情報を取り出す
		String strFrom = this.getSearchParamFromSession("dateFrom");
		String strTo = this.getSearchParamFromSession("dateTo");
		//取り出した検索条件をクエリパラメータに関連付けする
		redirectAttributes.addAttribute("dateFrom", strFrom);
		redirectAttributes.addAttribute("dateTo", strTo);
		
		return "redirect:/backToMemberList";
	}
	
	/**
	 * 会員編集画面
	 * @param id ID
	 * @param model モデル
	 * @return
	 */
	@GetMapping("/editMember")
	public String editMember(@RequestParam("id") Long id, Model model) {
		try {
			Member member = this.memberSvc.findById(id);
			
			//初期表示するためにModelにmemberを設定する
			model.addAttribute("member", member);
		}catch(Exception ex) {
			ex.printStackTrace();
			//メッセージを画面表示する
			model.addAttribute("errmsg", ex.getMessage());
			//空の情報を渡す
			model.addAttribute("member", new Member());
		}
		
		return "editMember";
	}

	/**
	 * 会員編集の要求
	 * @param request リクエスト
	 * @param validResult バリデーション結果
	 * @param model モデル
	 * @return
	 */
	@PostMapping("/updateMember")
	public String updateMember(@Validated @ModelAttribute("member") Member request, 
			BindingResult validResult, Model model, RedirectAttributes redirectAttributes) {

		if(! validResult.hasErrors()) {
			try {
				Result result = memberSvc.updateMember(request);
				if(! result.isOk()) {
					//業務チェックのNG
					model.addAttribute("errmsg", result.getErrMsg());
					return "newMember";
				}
			}catch(Exception ex) {
				ex.printStackTrace();
				//メッセージを画面表示する
				model.addAttribute("errmsg", ex.getMessage());
				return "newMember";
			}
		}else {
			return "newMember";
		}

		//セッションに保存していた情報を取り出す
		String strFrom = this.getSearchParamFromSession("dateFrom");
		String strTo = this.getSearchParamFromSession("dateTo");
		//取り出した検索条件をクエリパラメータに関連付けする
		redirectAttributes.addAttribute("dateFrom", strFrom);
		redirectAttributes.addAttribute("dateTo", strTo);
		
		return "redirect:/searchMember";
	}
	
	/**
	 * 会員削除の要求
	 * @param id ID
	 * @param model モデル
	 * @param redirectAttributes リダイレクト用モデル
	 * @return
	 */
	@PostMapping("/deleteMember")
	public String deleteMember(@RequestParam("id") Long id, Model model, 
			RedirectAttributes redirectAttributes) {

		try {
			memberSvc.deleteMember(id);
		}catch(Exception ex) {
			ex.printStackTrace();
			//メッセージを画面表示する(リダイレクト先のModelに情報を引き継ぐ)
			redirectAttributes.addFlashAttribute("errmsg", ex.getMessage());
		}

		//会員一覧の再読み込みを実施
		//セッションに保存していた情報を取り出す
		String strFrom = this.getSearchParamFromSession("dateFrom");
		String strTo = this.getSearchParamFromSession("dateTo");
		//取り出した検索条件をクエリパラメータに関連付けする
		redirectAttributes.addAttribute("dateFrom", strFrom);
		redirectAttributes.addAttribute("dateTo", strTo);
		
		return "redirect:/searchMember";
	}

	/**
	 * 会員一覧へのリダイレクト
	 * @param model モデル
	 * @param redirectAttributes リダイレクト用モデル
	 * @return
	 */
	@GetMapping("/backToMemberList")
	public String backToMemberList(Model model, RedirectAttributes redirectAttributes) {
		
		Boolean isSearch = (Boolean) session.getAttribute("isSearch");
		if(Objects.isNull(isSearch) || !isSearch) {
			//検索が実施されていない状況なら初期画面に遷移する
			return "redirect:/";
		}

		//セッションに保存していた情報を取り出す
		String strFrom = this.getSearchParamFromSession("dateFrom");
		String strTo = this.getSearchParamFromSession("dateTo");

		//取り出した検索条件をクエリパラメータに関連付けする
		redirectAttributes.addAttribute("dateFrom", strFrom);
		redirectAttributes.addAttribute("dateTo", strTo);
		
		//クエリパラメータ付きのURLでリダイレクトする
		return "redirect:/searchMember";
	}
	
	/**
	 * セッションに保存している検索条件を取り出す
	 * @param key キー
	 * @return
	 */
	private String getSearchParamFromSession(String key) {
		//HTTPセッションに保存していた検索条件を取り出す
		LocalDate item = (LocalDate) this.getSessionAttr(key);
		//クエリパラメータに関連付けするにあたってフォーマット
		return this.formatDate("yyyy-MM-dd", item);
	}
	
	/**
	 * セッションに保存している情報を取得する
	 * @param key キー
	 * @return
	 */
	private Object getSessionAttr(String key) {
		return this.session.getAttribute(key);
	}
	
	/**
	 * 日時を指定された文字形式で返す
	 * @param pattern 形式
	 * @param dt 日時
	 * @return
	 */
	private String formatDate(String pattern, LocalDate dt) {
		String str = "";
		if(Objects.nonNull(dt)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			str = dt.format(formatter);
		}		
		return str;
	}
}
