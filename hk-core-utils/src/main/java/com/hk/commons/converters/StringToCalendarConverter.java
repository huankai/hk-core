package com.hk.commons.converters;

import com.hk.commons.util.date.DateTimeUtils;

import java.util.Calendar;

public class StringToCalendarConverter extends StringGenericConverter<Calendar> {

    public StringToCalendarConverter() {
        super(Calendar.class);
    }

    @Override
    protected Calendar doConvert(String source) {
        var date = DateTimeUtils.stringToDate(source);
        return (null == date) ? null : DateTimeUtils.dateToCalendar(date);
    }

}
