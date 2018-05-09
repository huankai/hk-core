package com.hk.core.query.jdbc;

import com.hk.commons.annotations.EnumDisplay;
import com.hk.commons.util.date.DateTimeUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * @author huangkai
 * @date 2017年12月21日上午9:02:49
 */
public class DateRangeCondition implements Condition {


    public enum DateRange {

        /**
         * today
         */
        @EnumDisplay("date.today")
        TD,

        /**
         * last day
         */
        @EnumDisplay("date.yesterday")
        YD,

        /**
         * this week,本周
         */
        @EnumDisplay("date.thisweek")
        TW,

        /**
         * last week,上周
         */
        @EnumDisplay("date.lastweek")
        LW,

        /**
         * this month,本月
         */
        @EnumDisplay("date.thismonth")
        TM,

        /**
         * last month,上月
         */
        @EnumDisplay("date.lastmonth")
        LM,

        /**
         * custom,自定义
         */
        @EnumDisplay("date.custom")
        CUSTOM
    }

    @Getter
    @Setter
    private DateRange range;

    @Getter
    @Setter
    private String field;

    @Getter
    @Setter
    private LocalDateTime start;

    @Getter
    @Setter
    private LocalDateTime end;

    @Override
    public String toSqlString(List<Object> parameters) {
        if (range == null) {
            return null;
        }
        LocalDateTime end = LocalDateTime.now(), start = DateTimeUtils.getLocalDateTimeStart(end);
        switch (range) {
            case TD:
                end = DateTimeUtils.getLocalDateTimeEnd(end);
                break;
            case YD:
                start = start.minusDays(1);
                end = DateTimeUtils.getLocalDateTimeEnd(end.minusDays(1));
                break;
            case TW:
                start = end.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).toLocalDate().atTime(LocalTime.MIN);
                break;
            case LW:
                end = end.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).toLocalDate().atTime(LocalTime.MAX);
                start = end.with(TemporalAdjusters.previous(DayOfWeek.MONDAY)).toLocalDate().atTime(LocalTime.MIN);
                break;
            case TM:
                start = start.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atTime(LocalTime.MIN);
                break;
            case LM:
                LocalDateTime dateTime = end.minus(1, ChronoUnit.MONTHS);
                start = dateTime.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atTime(LocalTime.MIN);
                end = dateTime.with(TemporalAdjusters.lastDayOfMonth()).toLocalDate().atTime(LocalTime.MAX);
                break;
            case CUSTOM:
            default:
                start = this.start;
                end = this.end;
        }
        RangeCondition<LocalDateTime> condition = new RangeCondition<>(field, start, end, true, false);
        return condition.toSqlString(parameters);
    }

}
