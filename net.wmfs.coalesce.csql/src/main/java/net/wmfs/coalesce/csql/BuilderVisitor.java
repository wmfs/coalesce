package net.wmfs.coalesce.csql;

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

public class BuilderVisitor implements ExpressionVisitor {

	String result = null;

	public String getResult() {
		return result;
	}

	public void visitAndExpression(AndExpression expr)
			throws ExpressionException {

		expr.arg(0).accept(this);
		String o1 = result;
		expr.arg(1).accept(this);
		String o2 = result;

		result = "(" + o1 + " AND " + o2 + ")";
	}

	public void visitBetweenExpression(BetweenExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		String s1 = result;
		expr.arg(1).accept(this);
		String s2 = result;
		expr.arg(2).accept(this);
		String s3 = result;

		result = "(" + s1 + " BETWEEN " + s2 + " AND " + s3 + ")";
	}

	public void visitBinaryMinusExpression(SubtractExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		String s1 = result;
		expr.arg(1).accept(this);
		String s2 = result;

		result = "(" + s1 + "-" + s2 + ")";
	}

	public void visitBinaryPlusExpression(PlusExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		String s1 = result;
		expr.arg(1).accept(this);
		String s2 = result;

		result = "(" + s1 + "+" + s2 + ")";
	}

	public void visitDivideExpression(DivideExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		String s1 = result;
		expr.arg(1).accept(this);
		String s2 = result;

		result = "(" + s1 + "/" + s2 + ")";

	}

	public void visitEqualsExpression(EqualsExpression expr)
			throws ExpressionException {

		expr.arg(0).accept(this);
		String s1 = result;
		expr.arg(1).accept(this);
		String s2 = result;

		result = "(" + s1 + "=" + s2 + ")";
	}

	public void visitGreaterThanExpression(GreaterThanExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		String s1 = result;
		expr.arg(1).accept(this);
		String s2 = result;

		result = "(" + s1 + ">" + s2 + ")";
	}

	public void visitGreaterThanOrEqualsExpression(
			GreaterThanOrEqualsExpression expr) throws ExpressionException {
		expr.arg(0).accept(this);
		String s1 = result;
		expr.arg(1).accept(this);
		String s2 = result;

		result = "(" + s1 + ">=" + s2 + ")";
	}

	public void visitInExpression(InExpression expr) throws ExpressionException {
		expr.arg(0).accept(this);
		String s1 = result;
		expr.arg(1).accept(this);
		String s2 = result;

		result = "(" + s1 + " IN " + s2 + ")";
	}

	public void visitIsNotNullExpression(IsNotNullExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		String s1 = result;

		result = "(" + s1 + " IS NOT NULL)";
	}

	public void visitIsNullExpression(IsNullExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		String s1 = result;

		result = "(" + s1 + " IS NULL)";
	}

	public void visitLessThanExpression(LessThanExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		String s1 = result;
		expr.arg(1).accept(this);
		String s2 = result;

		result = "(" + s1 + "<" + s2 + ")";
	}

	public void visitLessThanOrEqualsExpression(LessThanOrEqualsExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		String s1 = result;
		expr.arg(1).accept(this);
		String s2 = result;

		result = "(" + s1 + "<=" + s2 + ")";
	}

	public void visitListExpression(ListExpression expr)
			throws ExpressionException {
		String list = "";
		for (Expression e : expr.getArguments()) {
			e.accept(this);
			if (list.equals(""))
				list += ",";
			list += result;
		}
		result = "(" + list + ")";
	}

	public void visitLiteralExpression(LiteralExpression expr)
			throws ExpressionException {

		result = expr.getValue().toString();

		if (expr.getValue() instanceof String) {
			result = "'" + result + "'";
		}
	}

	public void visitMultiplyExpression(MultiplyExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		String s1 = result;
		expr.arg(1).accept(this);
		String s2 = result;

		result = "(" + s1 + "*" + s2 + ")";
	}

	public void visitNotEqualsExpression(NotEqualsExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		String s1 = result;
		expr.arg(1).accept(this);
		String s2 = result;

		result = "(" + s1 + "<>" + s2 + ")";
	}

	public void visitNotInExpression(NotInExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		String s1 = result;
		expr.arg(1).accept(this);
		String s2 = result;

		result = "(" + s1 + " IN " + s2 + ")";
	}

	public void visitOrExpression(OrExpression expr) throws ExpressionException {
		expr.arg(0).accept(this);
		String o1 = result;
		expr.arg(1).accept(this);
		String o2 = result;

		result = "(" + o1 + " OR " + o2 + ")";
	}

	public void visitStandardFunction(StandardFunctionExpression expr)
			throws ExpressionException {

		String list = "";
		for (Expression e : expr.getArguments()) {
			e.accept(this);
			if (list.equals(""))
				list += ",";
			list += result;
		}
		result = expr.getName() + "(" + list + ")";
	}

	public void visitUnaryMinusExpression(SubtractExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		String s1 = result;

		result = "(-" + s1 + ")";
	}

	public void visitUnaryPlusExpression(PlusExpression expr)
			throws ExpressionException {
		expr.arg(0).accept(this);
		String s1 = result;

		result = "(+" + s1 + ")";
	}

	public void visitCustomItemExpression(
			CustomItemExpression nodeItemExpression) throws ExpressionException {
		throw new ExpressionException("Custom expressions are not implemented");
	}

}
