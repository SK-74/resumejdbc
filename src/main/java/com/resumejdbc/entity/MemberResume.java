package com.resumejdbc.entity;

import java.time.LocalDate;
import java.time.YearMonth;

public class MemberResume {

	/** ID（メンバーID） */
	private Long memberId;
	
	/** 名前 */
	private String name;

	/** ID（経歴ID） */
	private Long resumeId;

	/** 種別（学歴・職歴） */
	private Integer typ;
	
	/** 年月(DB登録用) */
	private LocalDate ym;

	/** 年月(リクエスト用) */
	private YearMonth requestYm;

	/** 経歴 */
	private String content;

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getResumeId() {
		return resumeId;
	}

	public void setResumeId(Long resumeId) {
		this.resumeId = resumeId;
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
	
}
