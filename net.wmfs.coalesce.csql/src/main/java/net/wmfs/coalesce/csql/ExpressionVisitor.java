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

public interface ExpressionVisitor {

	void visitUnaryPlusExpression(PlusExpression plusExpression)
			throws ExpressionException;

	void visitBinaryPlusExpression(PlusExpression plusExpression)
			throws ExpressionException;

	void visitUnaryMinusExpression(SubtractExpression subtractExpression)
			throws ExpressionException;

	void visitBinaryMinusExpression(SubtractExpression subtractExpression)
			throws ExpressionException;

	void visitMultiplyExpression(MultiplyExpression multiplyExpression)
			throws ExpressionException;

	void visitDivideExpression(DivideExpression divideExpression)
			throws ExpressionException;

	void visitLiteralExpression(LiteralExpression literalExpression)
			throws ExpressionException;

	void visitEqualsExpression(EqualsExpression equalsExpression)
			throws ExpressionException;

	void visitNotEqualsExpression(NotEqualsExpression notEqualsExpression)
			throws ExpressionException;

	void visitLessThanExpression(LessThanExpression lessThanExpression)
			throws ExpressionException;

	void visitLessThanOrEqualsExpression(
			LessThanOrEqualsExpression lessThanOrEqualsExpression)
			throws ExpressionException;

	void visitGreaterThanExpression(GreaterThanExpression greaterThanExpression)
			throws ExpressionException;

	void visitGreaterThanOrEqualsExpression(
			GreaterThanOrEqualsExpression greaterThanOrEqualsExpression)
			throws ExpressionException;

	void visitAndExpression(AndExpression andExpression)
			throws ExpressionException;

	void visitOrExpression(OrExpression orExpression)
			throws ExpressionException;

	void visitIsNullExpression(IsNullExpression isNullExpression)
			throws ExpressionException;

	void visitIsNotNullExpression(IsNotNullExpression isNotNullExpression)
			throws ExpressionException;

	void visitInExpression(InExpression inExpression)
			throws ExpressionException;

	void visitNotInExpression(NotInExpression notInExpression)
			throws ExpressionException;

	void visitListExpression(ListExpression listExpression)
			throws ExpressionException;

	void visitStandardFunction(
			StandardFunctionExpression standardFunctionExpression)
			throws ExpressionException;

	void visitBetweenExpression(BetweenExpression betweenExpression)
			throws ExpressionException;

	void visitCustomItemExpression(CustomItemExpression nodeItemExpression)
			throws ExpressionException;
}
