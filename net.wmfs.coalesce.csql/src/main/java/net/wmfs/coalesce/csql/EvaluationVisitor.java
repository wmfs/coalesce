package net.wmfs.coalesce.csql;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import net.wmfs.coalesce.csql.Expression.AndExpression;
import net.wmfs.coalesce.csql.Expression.BetweenExpression;
import net.wmfs.coalesce.csql.Expression.CustomItemExpression;
import net.wmfs.coalesce.csql.Expression.DivideExpression;
import net.wmfs.coalesce.csql.Expression.EqualsExpression;
import net.wmfs.coalesce.csql.Expression.GreaterThanExpression;
import net.wmfs.coalesce.csql.Expression.GreaterThanOrEqualsExpression;
import net.wmfs.coalesce.csql.Expression.InExpression;
import net.wmfs.coalesce.csql.Expression.IsNotNullExpression;
import net.wmfs.coalesce.csql.Expression.IsNullExpression;
import net.wmfs.coalesce.csql.Expression.LessThanExpression;
import net.wmfs.coalesce.csql.Expression.LessThanOrEqualsExpression;
import net.wmfs.coalesce.csql.Expression.ListExpression;
import net.wmfs.coalesce.csql.Expression.LiteralExpression;
import net.wmfs.coalesce.csql.Expression.MultiplyExpression;
import net.wmfs.coalesce.csql.Expression.NotEqualsExpression;
import net.wmfs.coalesce.csql.Expression.NotInExpression;
import net.wmfs.coalesce.csql.Expression.OrExpression;
import net.wmfs.coalesce.csql.Expression.PlusExpression;
import net.wmfs.coalesce.csql.Expression.StandardFunctionExpression;
import net.wmfs.coalesce.csql.Expression.SubtractExpression;

public class EvaluationVisitor implements ExpressionVisitor {

	public Object result;

	private Expression expression;

	public boolean evaluateCondition() throws ExpressionException {
		return ((Boolean) evaluateExpression()).booleanValue();
	}

	public Object evaluateExpression() throws ExpressionException {
		expression.accept(this);
		return result;
	}

	/*
	 * SETTERS
	 */
	
	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	/*
	 * VISITOR METHODS
	 */

	public void visitBinaryMinusExpression(SubtractExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		Object o1 = result;
		expr.arg(1).accept(this);
		Object o2 = result;

		result = ExpressionUtil.sqlMinus(o1, o2);
	}

	public void visitBinaryPlusExpression(PlusExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		Object o1 = result;
		expr.arg(1).accept(this);
		Object o2 = result;

		result = ExpressionUtil.sqlPlus(o1, o2);
	}

	public void visitDivideExpression(DivideExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		Object o1 = result;
		expr.arg(1).accept(this);
		Object o2 = result;

		result = ExpressionUtil.sqlDivideBy(o1, o2);
	}

	public void visitEqualsExpression(EqualsExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		Object o1 = result;
		expr.arg(1).accept(this);
		Object o2 = result;

		result = ExpressionUtil.sqlEquals(o1, o2);
	}

	public void visitLiteralExpression(LiteralExpression expr) {
		result = expr.getValue();
	}

	public void visitMultiplyExpression(MultiplyExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		Object o1 = result;
		expr.arg(1).accept(this);
		Object o2 = result;

		result = ExpressionUtil.sqlTimes(o1, o2);
	}

	public void visitUnaryMinusExpression(SubtractExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		Object o = result;

		result = ExpressionUtil.sqlUnaryMinus(o);
	}

	public void visitUnaryPlusExpression(PlusExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		Object o = result;

		result = ExpressionUtil.sqlUnaryPlus(o);
	}

	public void visitAndExpression(AndExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		Object o1 = result;
		if (((Boolean) o1) == false) {
			result = false;
			return;
		}

		expr.arg(1).accept(this);
		Object o2 = result;

		result = ExpressionUtil.sqlAnd(o1, o2);
	}

	public void visitGreaterThanExpression(GreaterThanExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		Object o1 = result;
		expr.arg(1).accept(this);
		Object o2 = result;

		result = ExpressionUtil.sqlGreaterThan(o1, o2);
	}

	public void visitGreaterThanOrEqualsExpression(
			GreaterThanOrEqualsExpression expr) throws ExpressionException {
		expr.arg(0).accept(this);
		Object o1 = result;
		expr.arg(1).accept(this);
		Object o2 = result;

		result = ExpressionUtil.sqlGreaterThanOrEqual(o1, o2);

	}

	public void visitInExpression(InExpression expr) throws ExpressionException {
		expr.arg(0).accept(this);
		Object o1 = result;
		expr.arg(1).accept(this);
		Object o2 = result;

		result = ExpressionUtil.sqlIn(o1, o2);
	}

	public void visitIsNotNullExpression(IsNotNullExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		Object o = result;

		result = ExpressionUtil.sqlIsNotNull(o);
	}

	public void visitIsNullExpression(IsNullExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		Object o = result;

		result = ExpressionUtil.sqlIsNull(o);
	}

	public void visitLessThanExpression(LessThanExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		Object o1 = result;
		expr.arg(1).accept(this);
		Object o2 = result;

		result = ExpressionUtil.sqlLessThan(o1, o2);
	}

	public void visitLessThanOrEqualsExpression(LessThanOrEqualsExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		Object o1 = result;
		expr.arg(1).accept(this);
		Object o2 = result;

		result = ExpressionUtil.sqlLessThanOrEqual(o1, o2);
	}

	public void visitListExpression(ListExpression expr)
			throws ExpressionException {
		List<Object> data = new ArrayList<Object>();
		for (Expression e : expr.getArguments()) {
			e.accept(this);
			data.add(result);
		}
		result = data;
	}

	public void visitNotEqualsExpression(NotEqualsExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		Object o1 = result;
		expr.arg(1).accept(this);
		Object o2 = result;

		result = ExpressionUtil.sqlNotEqualTo(o1, o2);
	}

	public void visitNotInExpression(NotInExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		Object o1 = result;
		expr.arg(1).accept(this);
		Object o2 = result;

		result = ExpressionUtil.sqlNotIn(o1, o2);
	}

	public void visitOrExpression(OrExpression expr) throws ExpressionException {
		expr.arg(0).accept(this);
		Object o1 = result;
		if (((Boolean) o1) == true) {
			result = true;
			return;
		}

		expr.arg(1).accept(this);
		Object o2 = result;

		result = ExpressionUtil.sqlOr(o1, o2);
	}

	public void visitStandardFunction(StandardFunctionExpression expr)
			throws ExpressionException {
		if ("NVL".equals(expr.getName())) {
			expr.arg(0).accept(this);
			Object o1 = result;
			expr.arg(1).accept(this);
			Object o2 = result;

			result = ExpressionUtil.sqlIsNull(o1) ? o2 : o1;
		} else if ("TO_NUMBER".equals(expr.getName())) {
			expr.arg(0).accept(this);
			Object o = result;

			if (o == null) {
				result = null;
			} else {
				try {
					result = new BigDecimal((String) o);
				} catch (RuntimeException e) {
					throw new ExpressionException(
							"Can't convert TO_NUMBER argument to number", e);
				}
			}
		} else {
			throw new ExpressionException("Invalid standard function: "
					+ expr.getName());
		}
	}

	public void visitBetweenExpression(BetweenExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		Object o1 = result;
		expr.arg(1).accept(this);
		Object o2 = result;
		expr.arg(1).accept(this);
		Object o3 = result;

		result = Boolean.valueOf(ExpressionUtil.sqlGreaterThanOrEqual(o1, o2)
				&& ExpressionUtil.sqlLessThanOrEqual(o1, o3));
	}

	public void visitCustomItemExpression(
			CustomItemExpression nodeItemExpression) throws ExpressionException {
		throw new ExpressionException("Custom expressions are not implemented");
	}

}