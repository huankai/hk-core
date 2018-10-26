package com.hk.core.data.jdbc.query;

import com.hk.commons.annotations.EnumDisplay;
import com.hk.commons.util.date.DateTimeUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.List;

/**
 * @author: sjq-278
 * @date: 2018-09-19 11:05
 */
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
    private Calendar start;

    @Getter
    @Setter
    private Calendar end;

    @Override
    public String toSqlString(List<Object> parameters) {
        if (range == null)
            return null;

        Calendar start, end;
        start = DateTimeUtils.getDateWithOutTime(Calendar.getInstance());
        end = (Calendar) start.clone();
        switch (range) {
            case TD:
                break;
            case YD:
                start.add(Calendar.DATE, -1);
                end.add(Calendar.DATE, -1);
                break;
            case TW:
                int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == Calendar.SUNDAY) {
                    dayOfWeek = 7;
                }
                start.add(Calendar.DATE, 1 - dayOfWeek);
                end = (Calendar) start.clone();
                end.add(Calendar.DATE, 6);
                break;
            case LW:
                dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == Calendar.SUNDAY) {
                    dayOfWeek = 7;
                }
                start.add(Calendar.DATE, 1 - dayOfWeek - 7);
                end = (Calendar) start.clone();
                end.add(Calendar.DATE, 6);
                break;
            case TM:
                start.add(Calendar.DATE, 1 - start.get(Calendar.DATE));
                end = (Calendar) start.clone();
                end.add(Calendar.MONTH, 1);
                end.add(Calendar.DATE, -1);
                break;
            case LM:
                // 本月1号
                start.add(Calendar.DATE, 1 - start.get(Calendar.DATE));
                end = (Calendar) start.clone();
                // 上月1号
                start.add(Calendar.MONTH, -1);
                // 本月1号减一，变成上月最后一天
                end.add(Calendar.DATE, -1);
                break;
            case CUSTOM:
            default:
                start = this.start;
                end = this.end;
                break;
        }
        RangeCondition<Calendar> condition = new RangeCondition<Calendar>(field, start, end, true, false);
        if (end != null) {
            end.add(Calendar.DATE, 1);
            condition.setEnd(end);
        }
        return condition.toSqlString(parameters);
    }
}
