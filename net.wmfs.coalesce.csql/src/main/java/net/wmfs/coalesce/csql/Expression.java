package net.wmfs.coalesce.csql;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.wmfs.coalesce.csql.antlr.CSQLParser;

import org.antlr.runtime.tree.CommonTree;

public abstract class Expression {

	private List<Expression> arguments;

	private Expression() {
	}

	private Expression(Expression expr1) {
		addArgument(expr1);
	}

	private Expression(Expression expr1, Expression expr2) {
		addArgument(expr1);
		addArgument(expr2);
	}

	private Expression(List<Expression> args) {
		this.arguments = args;
	}

	public abstract void accept(ExpressionVisitor visitor)
			throws ExpressionException;

	public void acceptArguments(ExpressionVisitor visitor)
			throws ExpressionException {
		if (arguments != null) {
			for (Expression arg : arguments) {
				if (arg != null) {
					arg.accept(visitor);
				}
			}
		}
	}

	public void addArgument(Expression argument) {
		if (arguments == null) {
			arguments = new ArrayList<Expression>();
		}
		arguments.add(argument);
	}

	public List<Expression> getArguments() {
		return arguments;
	}

	public Expression arg(int n) {
		return arguments == null ? null : arguments.get(n);
	}

	protected static Expression create(CommonTree tree) {

		Expression expr = null;

		if (tree == null) {
			return null;
		}

		switch (tree.getType()) {
		case CSQLParser.QUOTED_STRING:
			return new LiteralExpression(tree.getText().substring(1,
					tree.getText().length() - 1).replaceAll("''", "'"));
		case CSQLParser.NUMBER:
			return new LiteralExpression(new BigDecimal(tree.getText()));
		case CSQLParser.TRUE:
			return new LiteralExpression(true);
		case CSQLParser.FALSE:
			return new LiteralExpression(false);
		case CSQLParser.SET:
			return new ListExpression(getChildExpressions(tree));
		case CSQLParser.PLUS:
			return new PlusExpression(create((CommonTree) tree.getChild(0)),
					create((CommonTree) tree.getChild(1)));
		case CSQLParser.MINUS:
			return new SubtractExpression(
					create((CommonTree) tree.getChild(0)),
					create((CommonTree) tree.getChild(1)));
		case CSQLParser.MULT:
			return new MultiplyExpression(
					create((CommonTree) tree.getChild(0)),
					create((CommonTree) tree.getChild(1)));
		case CSQLParser.DIV:
			return new DivideExpression(create((CommonTree) tree.getChild(0)),
					create((CommonTree) tree.getChild(1)));
		case CSQLParser.EQ:
			return new EqualsExpression(create((CommonTree) tree.getChild(0)),
					create((CommonTree) tree.getChild(1)));
		case CSQLParser.NE:
			return new NotEqualsExpression(
					create((CommonTree) tree.getChild(0)),
					create((CommonTree) tree.getChild(1)));
		case CSQLParser.LT:
			return new LessThanExpression(
					create((CommonTree) tree.getChild(0)),
					create((CommonTree) tree.getChild(1)));
		case CSQLParser.LE:
			return new LessThanOrEqualsExpression(create((CommonTree) tree
					.getChild(0)), create((CommonTree) tree.getChild(1)));
		case CSQLParser.GT:
			return new GreaterThanExpression(create((CommonTree) tree
					.getChild(0)), create((CommonTree) tree.getChild(1)));
		case CSQLParser.GE:
			return new GreaterThanOrEqualsExpression(create((CommonTree) tree
					.getChild(0)), create((CommonTree) tree.getChild(1)));
		case CSQLParser.AND:
			return new AndExpression(create((CommonTree) tree.getChild(0)),
					create((CommonTree) tree.getChild(1)));
		case CSQLParser.OR:
			return new OrExpression(create((CommonTree) tree.getChild(0)),
					create((CommonTree) tree.getChild(1)));
		case CSQLParser.ISNULL:
			return new IsNullExpression(create((CommonTree) tree.getChild(0)));
		case CSQLParser.ISNOTNULL:
			return new IsNotNullExpression(
					create((CommonTree) tree.getChild(0)));
		case CSQLParser.IN:
			return new InExpression(create((CommonTree) tree.getChild(0)),
					create((CommonTree) tree.getChild(1)));
		case CSQLParser.NOTIN:
			return new NotInExpression(create((CommonTree) tree.getChild(0)),
					create((CommonTree) tree.getChild(1)));
		case CSQLParser.BETWEEN:
			return new BetweenExpression(create((CommonTree) tree.getChild(0)),
					create((CommonTree) tree.getChild(1)),
					create((CommonTree) tree.getChild(2)));
		case CSQLParser.STD_FUNCTION:
			String name = tree.getToken().getText();
			return new StandardFunctionExpression(name,
					getChildExpressions(tree));
		case CSQLParser.CUSTOM_ITEM:
			return new CustomItemExpression(tree.getText().toUpperCase(),
					getChildExpressions(tree));

		default:
			System.err.println(tree.getType() + " '" + tree.getText() + "'");
		}

		return expr;
	}

	private static List<Expression> getChildExpressions(CommonTree tree) {
		List<Expression> args = new ArrayList<Expression>();
		for (int i = 0; i < tree.getChildCount(); i++) {
			args.add(create((CommonTree) tree.getChild(i)));
		}
		return args;
	}

	public static class LiteralExpression extends Expression {
		private Object value;


		public LiteralExpression(String value) {
			this.value = value;
		}

		public LiteralExpression(BigDecimal value) {
			this.value = value;
		}

		public LiteralExpression(boolean value) {
			this.value = Boolean.valueOf(value);
		}

		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitLiteralExpression(this);
		}

		public Object getValue() {
			return value;
		}

	}

	public static class PlusExpression extends Expression {
		PlusExpression(Expression expr1, Expression expr2) {
			super(expr1, expr2);
		}

		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			if (arg(1) == null) {
				// unary plus
				visitor.visitUnaryPlusExpression(this);
			} else {
				// binary plus
				visitor.visitBinaryPlusExpression(this);
			}
		}
	}

	public static class SubtractExpression extends Expression {
		SubtractExpression(Expression expr1, Expression expr2) {
			super(expr1, expr2);
		}

		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			if (arg(1) == null) {
				// unary plus
				visitor.visitUnaryMinusExpression(this);
			} else {
				// binary plus
				visitor.visitBinaryMinusExpression(this);
			}

		}
	}

	public static class MultiplyExpression extends Expression {
		MultiplyExpression(Expression expr1, Expression expr2) {
			super(expr1, expr2);
		}

		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitMultiplyExpression(this);
		}
	}

	public static class DivideExpression extends Expression {
		DivideExpression(Expression expr1, Expression expr2) {
			super(expr1, expr2);
		}

		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitDivideExpression(this);
		}
	}

	public static class EqualsExpression extends Expression {

		public EqualsExpression(Expression create, Expression create2) {
			super(create, create2);
		}

		@Override
		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitEqualsExpression(this);
		}
	}

	public static class NotEqualsExpression extends Expression {

		public NotEqualsExpression(Expression create, Expression create2) {
			super(create, create2);
		}

		@Override
		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitNotEqualsExpression(this);
		}
	}

	public static class LessThanExpression extends Expression {

		public LessThanExpression(Expression create, Expression create2) {
			super(create, create2);
		}

		@Override
		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitLessThanExpression(this);
		}
	}

	public static class LessThanOrEqualsExpression extends Expression {

		public LessThanOrEqualsExpression(Expression create, Expression create2) {
			super(create, create2);
		}

		@Override
		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitLessThanOrEqualsExpression(this);
		}
	}

	public static class GreaterThanExpression extends Expression {

		public GreaterThanExpression(Expression create, Expression create2) {
			super(create, create2);
		}

		@Override
		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitGreaterThanExpression(this);
		}
	}

	public static class GreaterThanOrEqualsExpression extends Expression {

		public GreaterThanOrEqualsExpression(Expression create,
				Expression create2) {
			super(create, create2);
		}

		@Override
		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitGreaterThanOrEqualsExpression(this);
		}
	}

	public static class AndExpression extends Expression {
		public AndExpression(Expression create, Expression create2) {
			super(create, create2);
		}

		@Override
		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitAndExpression(this);
		}
	}

	public static class OrExpression extends Expression {
		public OrExpression(Expression create, Expression create2) {
			super(create, create2);
		}

		@Override
		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitOrExpression(this);
		}
	}

	public static class IsNullExpression extends Expression {
		public IsNullExpression(Expression create) {
			super(create);
		}

		@Override
		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitIsNullExpression(this);
		}
	}

	public static class IsNotNullExpression extends Expression {
		public IsNotNullExpression(Expression create) {
			super(create);
		}

		@Override
		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitIsNotNullExpression(this);
		}
	}

	public static class StandardFunctionExpression extends Expression {
		private String name;

		public StandardFunctionExpression(String name, List<Expression> args) {
			super(args);
			this.name = name.toUpperCase();
		}

		@Override
		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitStandardFunction(this);
		}

		public String getName() {
			return name;
		}

	}

	public static class InExpression extends Expression {
		public InExpression(Expression arg1, Expression arg2) {
			super(arg1, arg2);
		}

		@Override
		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitInExpression(this);
		}
	}

	public static class NotInExpression extends Expression {
		public NotInExpression(Expression arg1, Expression arg2) {
			super(arg1, arg2);
		}

		@Override
		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitNotInExpression(this);
		}
	}

	public static class ListExpression extends Expression {
		public ListExpression(List<Expression> args) {
			super(args);
		}

		@Override
		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitListExpression(this);
		}
	}

	public static class BetweenExpression extends Expression {
		public BetweenExpression(Expression arg1, Expression arg2,
				Expression arg3) {
			super(Arrays.asList(new Expression[] { arg1, arg2, arg3 }));
		}

		@Override
		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitBetweenExpression(this);
		}
	}

	public static class CustomItemExpression extends Expression {
		private String name;

		public CustomItemExpression(String name, List<Expression> args) {
			super(args);
			this.name = name;
		}

		@Override
		public void accept(ExpressionVisitor visitor)
				throws ExpressionException {
			visitor.visitCustomItemExpression(this);
		}

		public String getName() {
			return name;
		}
	}
}
