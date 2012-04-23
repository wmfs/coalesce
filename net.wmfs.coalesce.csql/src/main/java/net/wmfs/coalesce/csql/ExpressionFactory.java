package net.wmfs.coalesce.csql;

import net.wmfs.coalesce.aa.exception.ExpressionException;
import net.wmfs.coalesce.csql.antlr.CSQLLexer;
import net.wmfs.coalesce.csql.antlr.CSQLParser;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

public class ExpressionFactory {

	public Expression createCondition(String condition)
			throws ExpressionException {
		return create(condition, true);
	}

	public Expression createExpression(String expression)
			throws ExpressionException {
		return create(expression, false);
	}

	private Expression create(String expression, boolean isCondition)
			throws ExpressionException {
		CSQLLexer lex = new CSQLLexer(new CaseInsensitiveStream("(" + expression + ")"));
		CommonTokenStream tokens = new CommonTokenStream(lex);
		CSQLParser parser = new CSQLParser(tokens);

		try {
			CommonTree tree = isCondition ? (CommonTree) parser.condition()
					.getTree() : (CommonTree) parser.expression().getTree();
			Expression expr = Expression.create(tree);

			return expr;
		} catch (RecognitionException e) {
			throw new ExpressionException("couldn't parse " + expression, e);
		}
	}

	/*
	 * UTILITY CLASS
	 */
	private static class CaseInsensitiveStream extends ANTLRStringStream {
		public CaseInsensitiveStream(String string) {
			super(string);
		}

		@Override
		public int LA(int i) {
			if (i == 0) {
				return 0; // undefined
			}
			if (i < 0) {
				i++; // e.g., translate LA(-1) to use offset 0
			}
			if ((p + i - 1) >= n) {
				return CharStream.EOF;
			}
			return Character.toUpperCase(data[p + i - 1]);
		}
	}
}
