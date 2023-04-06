package com.shyiko.coursework.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.hibernate.query.sqm.IntervalType;

import java.time.Duration;


@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, Long> {
    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        return attribute.getSeconds();
    }

    @Override
    public Duration convertToEntityAttribute(Long dbData) {
        return Duration.ofSeconds(dbData);
    }
}