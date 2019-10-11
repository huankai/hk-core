package com.hk.core.data.jpa.type;

import org.hibernate.type.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kevin
 * @date 2019-8-30 15:59
 */
public final class TypeFactory {

    private static final Map<Class<?>, Type> typeMap;

    static {
        typeMap = new HashMap<>();
        typeMap.put(Byte.class, ByteType.INSTANCE);
        typeMap.put(Short.class, ShortType.INSTANCE);
        typeMap.put(Float.class, FloatType.INSTANCE);
        typeMap.put(Integer.class, IntegerType.INSTANCE);
        typeMap.put(Long.class, LongType.INSTANCE);
        typeMap.put(Double.class, DoubleType.INSTANCE);
        typeMap.put(String.class, StringType.INSTANCE);
        typeMap.put(Boolean.class, BooleanType.INSTANCE);
        typeMap.put(BigDecimal.class, BigDecimalType.INSTANCE);
        typeMap.put(Calendar.class, CalendarType.INSTANCE);
        typeMap.put(Date.class, DateType.INSTANCE);
        typeMap.put(LocalDateTime.class, LocalDateTimeType.INSTANCE);
        typeMap.put(LocalDate.class, LocalDateType.INSTANCE);
        typeMap.put(LocalTime.class, LocalTimeType.INSTANCE);
    }

    public static Type getType(Class<?> clazz) {
        return typeMap.get(clazz);
    }

    public static Type getType(Object value) {
        return typeMap.get(value.getClass());
    }
}
