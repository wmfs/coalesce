package net.wmfs.coalesce.csql;

import java.math.BigDecimal;


import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class EvaluationVisitorTest {

	static ExpressionFactory ef;
	
	@BeforeClass
	public static void init() {
		ef = new ExpressionFactory();
	}
	
	@Test
	public void testEvaluateExpression() throws Exception {
		
		assertEquals("Hi t'here!", eval("'Hi t''here!'"));
		
		assertEquals(new BigDecimal(15), eval("NVL(15, 103)"));
		
		assertEquals(true, eval("nvL('123', 'N') = 'N' oR 123 IN (123, 124)"));
		
		try {
			eval("'fdsfds");
		} catch (ExpressionException e) {
			assertTrue(true);
		}
	}
	
	protected Object eval(String expression) throws ExpressionException {
		EvaluationVisitor visitor = new EvaluationVisitor();
		visitor.setExpression(ef.createExpression(expression));
		return visitor.evaluateExpression();		
	}
}