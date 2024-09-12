package com.resumejdbc.convertor;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

import org.springframework.core.convert.converter.Converter;

public class LocalDate2YearMonthConvertor implements Converter<LocalDate, YearMonth> {

	@Override
	public YearMonth convert(LocalDate source) {
		if(Objects.nonNull(source)) {
			return YearMonth.from(source);
		}
		return null;
	}

}
