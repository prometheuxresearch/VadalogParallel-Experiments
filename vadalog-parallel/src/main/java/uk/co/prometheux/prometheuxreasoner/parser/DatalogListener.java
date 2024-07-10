// Generated from Datalog.g4 by ANTLR 4.8
package uk.co.prometheux.prometheuxreasoner.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DatalogParser}.
 */
public interface DatalogListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DatalogParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(DatalogParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(DatalogParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#clause}.
	 * @param ctx the parse tree
	 */
	void enterClause(DatalogParser.ClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#clause}.
	 * @param ctx the parse tree
	 */
	void exitClause(DatalogParser.ClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#fact}.
	 * @param ctx the parse tree
	 */
	void enterFact(DatalogParser.FactContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#fact}.
	 * @param ctx the parse tree
	 */
	void exitFact(DatalogParser.FactContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#annotationBody}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationBody(DatalogParser.AnnotationBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#annotationBody}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationBody(DatalogParser.AnnotationBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation(DatalogParser.AnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation(DatalogParser.AnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#rrule}.
	 * @param ctx the parse tree
	 */
	void enterRrule(DatalogParser.RruleContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#rrule}.
	 * @param ctx the parse tree
	 */
	void exitRrule(DatalogParser.RruleContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#head}.
	 * @param ctx the parse tree
	 */
	void enterHead(DatalogParser.HeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#head}.
	 * @param ctx the parse tree
	 */
	void exitHead(DatalogParser.HeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#falseHead}.
	 * @param ctx the parse tree
	 */
	void enterFalseHead(DatalogParser.FalseHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#falseHead}.
	 * @param ctx the parse tree
	 */
	void exitFalseHead(DatalogParser.FalseHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#egdHead}.
	 * @param ctx the parse tree
	 */
	void enterEgdHead(DatalogParser.EgdHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#egdHead}.
	 * @param ctx the parse tree
	 */
	void exitEgdHead(DatalogParser.EgdHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#body}.
	 * @param ctx the parse tree
	 */
	void enterBody(DatalogParser.BodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#body}.
	 * @param ctx the parse tree
	 */
	void exitBody(DatalogParser.BodyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PosLiteral}
	 * labeled alternative in {@link DatalogParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterPosLiteral(DatalogParser.PosLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PosLiteral}
	 * labeled alternative in {@link DatalogParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitPosLiteral(DatalogParser.PosLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NegLiteral}
	 * labeled alternative in {@link DatalogParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterNegLiteral(DatalogParser.NegLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NegLiteral}
	 * labeled alternative in {@link DatalogParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitNegLiteral(DatalogParser.NegLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code domStar}
	 * labeled alternative in {@link DatalogParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterDomStar(DatalogParser.DomStarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code domStar}
	 * labeled alternative in {@link DatalogParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitDomStar(DatalogParser.DomStarContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(DatalogParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(DatalogParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#gtCondition}.
	 * @param ctx the parse tree
	 */
	void enterGtCondition(DatalogParser.GtConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#gtCondition}.
	 * @param ctx the parse tree
	 */
	void exitGtCondition(DatalogParser.GtConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#ltCondition}.
	 * @param ctx the parse tree
	 */
	void enterLtCondition(DatalogParser.LtConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#ltCondition}.
	 * @param ctx the parse tree
	 */
	void exitLtCondition(DatalogParser.LtConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#geCondition}.
	 * @param ctx the parse tree
	 */
	void enterGeCondition(DatalogParser.GeConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#geCondition}.
	 * @param ctx the parse tree
	 */
	void exitGeCondition(DatalogParser.GeConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#leCondition}.
	 * @param ctx the parse tree
	 */
	void enterLeCondition(DatalogParser.LeConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#leCondition}.
	 * @param ctx the parse tree
	 */
	void exitLeCondition(DatalogParser.LeConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#eqCondition}.
	 * @param ctx the parse tree
	 */
	void enterEqCondition(DatalogParser.EqConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#eqCondition}.
	 * @param ctx the parse tree
	 */
	void exitEqCondition(DatalogParser.EqConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#neqCondition}.
	 * @param ctx the parse tree
	 */
	void enterNeqCondition(DatalogParser.NeqConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#neqCondition}.
	 * @param ctx the parse tree
	 */
	void exitNeqCondition(DatalogParser.NeqConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#inCondition}.
	 * @param ctx the parse tree
	 */
	void enterInCondition(DatalogParser.InConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#inCondition}.
	 * @param ctx the parse tree
	 */
	void exitInCondition(DatalogParser.InConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#notInCondition}.
	 * @param ctx the parse tree
	 */
	void enterNotInCondition(DatalogParser.NotInConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#notInCondition}.
	 * @param ctx the parse tree
	 */
	void exitNotInCondition(DatalogParser.NotInConditionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ltExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLtExpression(DatalogParser.LtExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ltExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLtExpression(DatalogParser.LtExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code gtExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterGtExpression(DatalogParser.GtExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code gtExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitGtExpression(DatalogParser.GtExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code externalExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExternalExpression(DatalogParser.ExternalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code externalExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExternalExpression(DatalogParser.ExternalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intersectionExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterIntersectionExpression(DatalogParser.IntersectionExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intersectionExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitIntersectionExpression(DatalogParser.IntersectionExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code minusExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMinusExpression(DatalogParser.MinusExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code minusExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMinusExpression(DatalogParser.MinusExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code skolemExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSkolemExpression(DatalogParser.SkolemExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code skolemExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSkolemExpression(DatalogParser.SkolemExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code prodExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterProdExpression(DatalogParser.ProdExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code prodExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitProdExpression(DatalogParser.ProdExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code leExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLeExpression(DatalogParser.LeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code leExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLeExpression(DatalogParser.LeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code eqEqExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterEqEqExpression(DatalogParser.EqEqExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code eqEqExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitEqEqExpression(DatalogParser.EqEqExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(DatalogParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(DatalogParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code divExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterDivExpression(DatalogParser.DivExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code divExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitDivExpression(DatalogParser.DivExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code precExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPrecExpression(DatalogParser.PrecExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code precExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPrecExpression(DatalogParser.PrecExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unionExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnionExpression(DatalogParser.UnionExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unionExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnionExpression(DatalogParser.UnionExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringOperatorsExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterStringOperatorsExpression(DatalogParser.StringOperatorsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringOperatorsExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitStringOperatorsExpression(DatalogParser.StringOperatorsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(DatalogParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(DatalogParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unaryMinusExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryMinusExpression(DatalogParser.UnaryMinusExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unaryMinusExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryMinusExpression(DatalogParser.UnaryMinusExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code neqExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNeqExpression(DatalogParser.NeqExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code neqExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNeqExpression(DatalogParser.NeqExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(DatalogParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(DatalogParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code aggrExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAggrExpression(DatalogParser.AggrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code aggrExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAggrExpression(DatalogParser.AggrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code geExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterGeExpression(DatalogParser.GeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code geExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitGeExpression(DatalogParser.GeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code plusExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPlusExpression(DatalogParser.PlusExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code plusExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPlusExpression(DatalogParser.PlusExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code termExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterTermExpression(DatalogParser.TermExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code termExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitTermExpression(DatalogParser.TermExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code msumAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void enterMsumAggExpression(DatalogParser.MsumAggExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code msumAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void exitMsumAggExpression(DatalogParser.MsumAggExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mprodAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void enterMprodAggExpression(DatalogParser.MprodAggExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mprodAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void exitMprodAggExpression(DatalogParser.MprodAggExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mcountAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void enterMcountAggExpression(DatalogParser.McountAggExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mcountAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void exitMcountAggExpression(DatalogParser.McountAggExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code munionAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void enterMunionAggExpression(DatalogParser.MunionAggExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code munionAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void exitMunionAggExpression(DatalogParser.MunionAggExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mmaxAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void enterMmaxAggExpression(DatalogParser.MmaxAggExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mmaxAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void exitMmaxAggExpression(DatalogParser.MmaxAggExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code mminAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void enterMminAggExpression(DatalogParser.MminAggExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mminAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void exitMminAggExpression(DatalogParser.MminAggExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code unionAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void enterUnionAggExpression(DatalogParser.UnionAggExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code unionAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void exitUnionAggExpression(DatalogParser.UnionAggExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code listAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void enterListAggExpression(DatalogParser.ListAggExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code listAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void exitListAggExpression(DatalogParser.ListAggExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code setAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void enterSetAggExpression(DatalogParser.SetAggExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code setAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void exitSetAggExpression(DatalogParser.SetAggExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code minAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void enterMinAggExpression(DatalogParser.MinAggExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code minAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void exitMinAggExpression(DatalogParser.MinAggExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code maxAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void enterMaxAggExpression(DatalogParser.MaxAggExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code maxAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void exitMaxAggExpression(DatalogParser.MaxAggExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sumAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void enterSumAggExpression(DatalogParser.SumAggExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sumAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void exitSumAggExpression(DatalogParser.SumAggExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code prodAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void enterProdAggExpression(DatalogParser.ProdAggExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code prodAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void exitProdAggExpression(DatalogParser.ProdAggExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code avgAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void enterAvgAggExpression(DatalogParser.AvgAggExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code avgAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void exitAvgAggExpression(DatalogParser.AvgAggExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code countAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void enterCountAggExpression(DatalogParser.CountAggExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code countAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 */
	void exitCountAggExpression(DatalogParser.CountAggExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code substringExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 */
	void enterSubstringExpression(DatalogParser.SubstringExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code substringExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 */
	void exitSubstringExpression(DatalogParser.SubstringExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code containsExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 */
	void enterContainsExpression(DatalogParser.ContainsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code containsExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 */
	void exitContainsExpression(DatalogParser.ContainsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code startsWithExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 */
	void enterStartsWithExpression(DatalogParser.StartsWithExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code startsWithExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 */
	void exitStartsWithExpression(DatalogParser.StartsWithExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code endsWithExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 */
	void enterEndsWithExpression(DatalogParser.EndsWithExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code endsWithExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 */
	void exitEndsWithExpression(DatalogParser.EndsWithExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code concatExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 */
	void enterConcatExpression(DatalogParser.ConcatExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code concatExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 */
	void exitConcatExpression(DatalogParser.ConcatExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringLengthExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 */
	void enterStringLengthExpression(DatalogParser.StringLengthExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringLengthExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 */
	void exitStringLengthExpression(DatalogParser.StringLengthExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code indexOfExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 */
	void enterIndexOfExpression(DatalogParser.IndexOfExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code indexOfExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 */
	void exitIndexOfExpression(DatalogParser.IndexOfExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#varList}.
	 * @param ctx the parse tree
	 */
	void enterVarList(DatalogParser.VarListContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#varList}.
	 * @param ctx the parse tree
	 */
	void exitVarList(DatalogParser.VarListContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(DatalogParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(DatalogParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(DatalogParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(DatalogParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#stringConstTerm}.
	 * @param ctx the parse tree
	 */
	void enterStringConstTerm(DatalogParser.StringConstTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#stringConstTerm}.
	 * @param ctx the parse tree
	 */
	void exitStringConstTerm(DatalogParser.StringConstTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#integerConstTerm}.
	 * @param ctx the parse tree
	 */
	void enterIntegerConstTerm(DatalogParser.IntegerConstTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#integerConstTerm}.
	 * @param ctx the parse tree
	 */
	void exitIntegerConstTerm(DatalogParser.IntegerConstTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#doubleConstTerm}.
	 * @param ctx the parse tree
	 */
	void enterDoubleConstTerm(DatalogParser.DoubleConstTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#doubleConstTerm}.
	 * @param ctx the parse tree
	 */
	void exitDoubleConstTerm(DatalogParser.DoubleConstTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#booleanConstTerm}.
	 * @param ctx the parse tree
	 */
	void enterBooleanConstTerm(DatalogParser.BooleanConstTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#booleanConstTerm}.
	 * @param ctx the parse tree
	 */
	void exitBooleanConstTerm(DatalogParser.BooleanConstTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#dateConstTerm}.
	 * @param ctx the parse tree
	 */
	void enterDateConstTerm(DatalogParser.DateConstTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#dateConstTerm}.
	 * @param ctx the parse tree
	 */
	void exitDateConstTerm(DatalogParser.DateConstTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#integerSetTerm}.
	 * @param ctx the parse tree
	 */
	void enterIntegerSetTerm(DatalogParser.IntegerSetTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#integerSetTerm}.
	 * @param ctx the parse tree
	 */
	void exitIntegerSetTerm(DatalogParser.IntegerSetTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#doubleSetTerm}.
	 * @param ctx the parse tree
	 */
	void enterDoubleSetTerm(DatalogParser.DoubleSetTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#doubleSetTerm}.
	 * @param ctx the parse tree
	 */
	void exitDoubleSetTerm(DatalogParser.DoubleSetTermContext ctx);
	/**
	 * Enter a parse tree produced by the {@code list}
	 * labeled alternative in {@link DatalogParser#listTerm}.
	 * @param ctx the parse tree
	 */
	void enterList(DatalogParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code list}
	 * labeled alternative in {@link DatalogParser#listTerm}.
	 * @param ctx the parse tree
	 */
	void exitList(DatalogParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptySet}
	 * labeled alternative in {@link DatalogParser#setConstTerm}.
	 * @param ctx the parse tree
	 */
	void enterEmptySet(DatalogParser.EmptySetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptySet}
	 * labeled alternative in {@link DatalogParser#setConstTerm}.
	 * @param ctx the parse tree
	 */
	void exitEmptySet(DatalogParser.EmptySetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringSet}
	 * labeled alternative in {@link DatalogParser#setConstTerm}.
	 * @param ctx the parse tree
	 */
	void enterStringSet(DatalogParser.StringSetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringSet}
	 * labeled alternative in {@link DatalogParser#setConstTerm}.
	 * @param ctx the parse tree
	 */
	void exitStringSet(DatalogParser.StringSetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code integerSet}
	 * labeled alternative in {@link DatalogParser#setConstTerm}.
	 * @param ctx the parse tree
	 */
	void enterIntegerSet(DatalogParser.IntegerSetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code integerSet}
	 * labeled alternative in {@link DatalogParser#setConstTerm}.
	 * @param ctx the parse tree
	 */
	void exitIntegerSet(DatalogParser.IntegerSetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code doubleSet}
	 * labeled alternative in {@link DatalogParser#setConstTerm}.
	 * @param ctx the parse tree
	 */
	void enterDoubleSet(DatalogParser.DoubleSetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code doubleSet}
	 * labeled alternative in {@link DatalogParser#setConstTerm}.
	 * @param ctx the parse tree
	 */
	void exitDoubleSet(DatalogParser.DoubleSetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code dateSet}
	 * labeled alternative in {@link DatalogParser#setConstTerm}.
	 * @param ctx the parse tree
	 */
	void enterDateSet(DatalogParser.DateSetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code dateSet}
	 * labeled alternative in {@link DatalogParser#setConstTerm}.
	 * @param ctx the parse tree
	 */
	void exitDateSet(DatalogParser.DateSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#varTerm}.
	 * @param ctx the parse tree
	 */
	void enterVarTerm(DatalogParser.VarTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#varTerm}.
	 * @param ctx the parse tree
	 */
	void exitVarTerm(DatalogParser.VarTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#anonTerm}.
	 * @param ctx the parse tree
	 */
	void enterAnonTerm(DatalogParser.AnonTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#anonTerm}.
	 * @param ctx the parse tree
	 */
	void exitAnonTerm(DatalogParser.AnonTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#falseTerm}.
	 * @param ctx the parse tree
	 */
	void enterFalseTerm(DatalogParser.FalseTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#falseTerm}.
	 * @param ctx the parse tree
	 */
	void exitFalseTerm(DatalogParser.FalseTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link DatalogParser#booleanTerm}.
	 * @param ctx the parse tree
	 */
	void enterBooleanTerm(DatalogParser.BooleanTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DatalogParser#booleanTerm}.
	 * @param ctx the parse tree
	 */
	void exitBooleanTerm(DatalogParser.BooleanTermContext ctx);
}