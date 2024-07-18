package org.hibernate.orm.test.query.convert;

import java.util.Date;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DateConverter implements AttributeConverter<Date, Long> {

	@Override
	public Long convertToDatabaseColumn(Date date) {
		if (null == date) {
			return 0L;
		}
		return date.getTime();
	}

	@Override
	public Date convertToEntityAttribute(Long dbDate) {
		if (null == dbDate || 0L == dbDate) {
			return null;
		}
		return new Date(dbDate);
	}
}
