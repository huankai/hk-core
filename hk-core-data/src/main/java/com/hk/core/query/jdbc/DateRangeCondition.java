package com.hk.core.query.jdbc;

import com.hk.commons.util.date.DateTimeUtils;

import java.util.Calendar;
import java.util.List;

/**
 * 
 * @author huangkai
 * @date 2017年12月21日上午9:02:49
 */
public class DateRangeCondition implements Condition {



	public enum DateRange {

		/**
		 * today
		 */
//		@EnumDisplay("date.today")
		TD,

		/**
		 * last day
		 */
//		@EnumDisplay("date.yesterday")
		YD,

		/**
		 * this week
		 */
//		@EnumDisplay("date.thisweek")
		TW,

		/**
		 * last week
		 */
//		@EnumDisplay("date.lastweek")
		LW,

		/**
		 * this month
		 */
//		@EnumDisplay("date.thismonth")
		TM,

		/**
		 * last month
		 */
//		@EnumDisplay("date.lastmonth")
		LM,

		/**
		 * custom
		 */
//		@EnumDisplay("date.custom")
		CUSTOM
	}

	private DateRange range;

	private String field;

	private Calendar start;

	private Calendar end;

	/**
	 * @return the range
	 */
	public DateRange getRange() {
		return range;
	}

	/**
	 * @param range
	 *            the range to set
	 */
	public void setRange(DateRange range) {
		this.range = range;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field
	 *            the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @return the start
	 */
	public Calendar getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(Calendar start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public Calendar getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(Calendar end) {
		this.end = end;
	}

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
