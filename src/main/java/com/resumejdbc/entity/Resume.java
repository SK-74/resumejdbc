package com.resumejdbc.entity;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class Resume {

	/** ID */
	private Long id;
	
	/** 種別（学歴・職歴） */
	private Integer typ;
	
	/** 年月(DB登録用) */
	private LocalDate ym;

	/** 年月(リクエスト用) */
	@NotNull(message="{err.msg.required}")
	private YearMonth requestYm;

	/** 経歴 */
	@NotEmpty(message="{err.msg.required}")
	private String content;

	/** member.id*/
	private Long memberId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTyp() {
		return typ;
	}

	public void setTyp(Integer typ) {
		this.typ = typ;
	}

	public LocalDate getYm() {
		return ym;
	}

	public void setYm(LocalDate ym) {
		this.ym = ym;
	}

	public YearMonth getRequestYm() {
		return requestYm;
	}

	public void setRequestYm(YearMonth requestYm) {
		this.requestYm = requestYm;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	
	public String getTypStr() {
		String ret = "";
		if(this.typ == 1) {
			ret = "学歴";
		}else if(this.typ == 2){
			ret = "職歴";
		}
		return ret;
	}

	public String getDtYM() {
		//resumeList.htmlのtableのカラムに年月だけ表示するために形式を整える
		DateTimeFormatter dtm = DateTimeFormatter.ofPattern("yyyy-MM");
		return ym.format(dtm);
	}
}
