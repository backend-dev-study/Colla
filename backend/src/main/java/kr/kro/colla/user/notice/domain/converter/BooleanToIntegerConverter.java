package kr.kro.colla.user.notice.domain.converter;

import javax.persistence.AttributeConverter;

public class BooleanToIntegerConverter implements AttributeConverter<Boolean, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? 1 : 0;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer dbData) {
        return dbData == 1;
    }
}
