package com.resumejdbc.request;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.validation.constraints.AssertTrue;

public class MemberSearchCriteria {

	/** 誕生日From*/
	private LocalDate dateFrom;

	/** 誕生日To*/
	private LocalDate dateTo;

	/** 日付有効チェック結果*/
	private boolean validDate;

	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(LocalDate dateFrom) {
		this.dateFrom = dateFrom;
	}

	public LocalDate getDateTo() {
		return dateTo;
	}

	public void setDateTo(LocalDate dateTo) {
		this.dateTo = dateTo;
	}

	public void setValidDate(boolean validDate) {
		this.validDate = validDate;
	}

	@AssertTrue(message = "{err.msg.validfromdate}")
	public boolean isvalidDate() {
		this.validDate = true;
		if(Objects.nonNull(dateFrom) && Objects.nonNull(dateTo)) {
			this.validDate = dateTo.isAfter(dateFrom);
		}
		return this.validDate;
	}
}
