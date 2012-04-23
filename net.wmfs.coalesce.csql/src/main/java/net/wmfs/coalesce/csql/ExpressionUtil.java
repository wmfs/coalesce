package net.wmfs.coalesce.csql;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.wmfs.coalesce.aa.exception.ExpressionException;

public class ExpressionUtil {

	private static int compare(Object o1, Object o2) throws ExpressionException {
		if (o1 instanceof BigDecimal && o2 instanceof BigDecimal) {
			BigDecimal d1 = (BigDecimal)o1;
			BigDecimal d2 = (BigDecimal)o2;
			return d1.compareTo(d2);
		} else if ((o1 instanceof Date && o2 instanceof Date)
				|| (o1 instanceof String && o2 instanceof String)) {
			return ((Comparable) o1).compareTo(o2);
		}

		throw new ExpressionException("can't compare " + o1 + " and " + o2);
	}

	public static Object sqlPlus(Object o1, Object o2)
			throws ExpressionException {
		if (o1 == null) {
			return o2;
		} else if (o2 == null) {
			return o1;
		} else {
			return ((BigDecimal)o1).add((BigDecimal)o2); 			
		}
	}

	public static Boolean sqlLessThan(Object o1, Object o2)
			throws ExpressionException {

		return compare(o1, o2) < 0;
	}

	public static Boolean sqlGreaterThanOrEqual(Object o1, Object o2)
			throws ExpressionException {

		return compare(o1, o2) >= 0;
	}

	public static Boolean sqlGreaterThan(Object o1, Object o2)
			throws ExpressionException {

		return compare(o1, o2) > 0;
	}

	public static Boolean sqlLessThanOrEqual(Object o1, Object o2)
			throws ExpressionException {

		return compare(o1, o2) <= 0;
	}

	public static Object sqlTimes(Object val1, Object val2)
			throws ExpressionException {

		return ((BigDecimal)val1).multiply((BigDecimal)val2); 
	}

	public static Object sqlDivideBy(Object val1, Object val2)
			throws ExpressionException {

		return ((BigDecimal)val1).divide((BigDecimal)val2); 
	}

	public static Boolean sqlIn(Object object, Object object2)
			throws ExpressionException {

		if (!(object2 instanceof List<?>)) {
			if (object == null || object2 == null) {
				return Boolean.FALSE;
			}
			return !object.equals(object2);
		}

		List<?> list = (List<?>) object2;

		if (object == null || list == null) {
			return false;
		} else {
			// TODO: string->long hack
			if (list.contains(object) || list.contains(object.toString())) {
				return true;
			}
			if (list.size() == 1 && list.get(0) instanceof List<?>) {
				return sqlIn(object, list.get(0));
			}
			return false;
		}
	}

	public static Boolean sqlNotIn(Object object, Object object2)
			throws ExpressionException {

		if (!(object2 instanceof List<?>)) {
			if (object == null || object2 == null) {
				return Boolean.FALSE;
			}
			return !object.equals(object2);
		}

		List<?> list = (List<?>) object2;

		if (object == null || list == null || list.contains(null)) {
			return false;
		} else {
			// TODO: string->long hack
			if (list.contains(object) || list.contains(object.toString())) {
				return false;
			} else if (list.size() == 1 && list.get(0) instanceof List<?>) {
				return sqlNotIn(object, list.get(0));
			}
			return true;
		}
	}

	public static Boolean sqlOr(Object o1, Object o2)
			throws ExpressionException {
		return (Boolean) o1 || (Boolean) o2;
	}

	public static Boolean sqlAnd(Object o1, Object o2)
			throws ExpressionException {
		return (Boolean) o1 && (Boolean) o2;
	}

	public static Boolean sqlNot(Object o) throws ExpressionException {
		return !(Boolean) o;
	}

	public static Object sqlMinus(Object o1, Object o2)
			throws ExpressionException {

		if (o1 == null) {
			return o2;
		} else if (o2 == null) {
			return o1;
		} else {
			return ((BigDecimal)o1).subtract((BigDecimal)o2); 
		}
	}

	public static Boolean sqlIsNull(Object o) throws ExpressionException {
		return o == null ? Boolean.TRUE : Boolean.FALSE;
	}

	public static Boolean sqlIsNotNull(Object o) throws ExpressionException {
		return o != null ? Boolean.TRUE : Boolean.FALSE;
	}

	public static Boolean sqlEquals(Object o1, Object o2)
			throws ExpressionException {

		if (o1 == null && o2 == null) {
			return true;
		} else if (o1 == null || o2 == null) {
			return false;
		} else {
			return o1.equals(o2);
		}
	}

	public static Object sqlNotEqualTo(Object o1, Object o2)
			throws ExpressionException {
		return !sqlEquals(o1, o2);
	}

	public static Object sqlUnaryMinus(Object val) throws ExpressionException {
		return ((BigDecimal)val).negate();
	}

	public static Object sqlUnaryPlus(Object o) throws ExpressionException {
		return o;
	}
}
