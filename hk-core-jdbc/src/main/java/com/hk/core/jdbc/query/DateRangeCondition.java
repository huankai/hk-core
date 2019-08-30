package com.hk.core.jdbc.query;

import com.hk.commons.annotations.EnumDisplay;
import com.hk.commons.util.date.DateTimeUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * @author kevin
 * @date 2018-09-19 11:05
 */
@NoArgsConstructor
public class DateRangeCondition implements Condition {

    public enum DateRange {
        /**
         * today
         */
        @EnumDisplay("今天")
        TD,

        /**
         * last day
         */
        @EnumDisplay("昨天")
        YD,

        /**
         * this week
         */
        @EnumDisplay("本周")
        TW,

        /**
         * last week
         */
        @EnumDisplay("上周")
        LW,

        /**
         * this month
         */
        @EnumDisplay("本月")
        TM,

        /**
         * last month
         */
        @EnumDisplay("上月")
        LM,

        /**
         * custom
         */
        @EnumDisplay("自定义")
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
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = DateTimeUtils.getLocalDateTimeStart(now), // 当天开始时间
                end = DateTimeUtils.getLocalDateTimeEnd(now); //当天结束时间
        switch (range) {
            case TD:
                break;
            case YD:
                start = start.minusDays(1);
                end = end.minusDays(1);
                break;
            case TW:
                start = start.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MIN); //周日凌晨
                break;
            case LW:
                start = start.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MIN).minusDays(7); // 上周日凌晨
                end = end.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)).with(LocalTime.MAX).minusDays(7);// 上周六凌晨
                break;
            case TM:
                start = start.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN); // 本月开始时间
                break;
            case LM:
                LocalDateTime minusMonths = start.minusMonths(1);
                start = minusMonths.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN); // 上月开始时间
                end = minusMonths.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);  // 上月结束时间
                break;
            case CUSTOM:
            default:
                start = this.start;
                end = this.end;
                break;
        }
        RangeCondition<LocalDateTime> condition = new RangeCondition<>(field, start, end, true, false);
        return condition.toSqlString(parameters);
    }
}
