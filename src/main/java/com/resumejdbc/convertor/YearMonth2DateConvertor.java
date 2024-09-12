package com.resumejdbc.convertor;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

import org.springframework.core.convert.converter.Converter;

public class YearMonth2DateConvertor  implements Converter<YearMonth, LocalDate>{

	@Override
	public LocalDate convert(YearMonth source) {
		if(Objects.nonNull(source)) {
			//YearMonthの１日でDateオブジェクトを作成して返却
			return source.atDay(1);
		}
		return null;
	}
}
