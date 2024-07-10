// Generated from Datalog.g4 by ANTLR 4.8
package uk.co.prometheux.prometheuxreasoner.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DatalogParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface DatalogVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link DatalogParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(DatalogParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClause(DatalogParser.ClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#fact}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFact(DatalogParser.FactContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#annotationBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationBody(DatalogParser.AnnotationBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotation(DatalogParser.AnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#rrule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRrule(DatalogParser.RruleContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#head}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHead(DatalogParser.HeadContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#falseHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseHead(DatalogParser.FalseHeadContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#egdHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEgdHead(DatalogParser.EgdHeadContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(DatalogParser.BodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PosLiteral}
	 * labeled alternative in {@link DatalogParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPosLiteral(DatalogParser.PosLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NegLiteral}
	 * labeled alternative in {@link DatalogParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegLiteral(DatalogParser.NegLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code domStar}
	 * labeled alternative in {@link DatalogParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDomStar(DatalogParser.DomStarContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(DatalogParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#gtCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGtCondition(DatalogParser.GtConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#ltCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLtCondition(DatalogParser.LtConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#geCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeCondition(DatalogParser.GeConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#leCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeCondition(DatalogParser.LeConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#eqCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqCondition(DatalogParser.EqConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#neqCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNeqCondition(DatalogParser.NeqConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#inCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInCondition(DatalogParser.InConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#notInCondition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotInCondition(DatalogParser.NotInConditionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ltExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLtExpression(DatalogParser.LtExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code gtExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGtExpression(DatalogParser.GtExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code externalExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExternalExpression(DatalogParser.ExternalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intersectionExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntersectionExpression(DatalogParser.IntersectionExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code minusExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinusExpression(DatalogParser.MinusExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code skolemExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSkolemExpression(DatalogParser.SkolemExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code prodExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProdExpression(DatalogParser.ProdExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code leExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeExpression(DatalogParser.LeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code eqEqExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqEqExpression(DatalogParser.EqEqExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(DatalogParser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code divExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDivExpression(DatalogParser.DivExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code precExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrecExpression(DatalogParser.PrecExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unionExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionExpression(DatalogParser.UnionExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringOperatorsExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringOperatorsExpression(DatalogParser.StringOperatorsExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code orExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(DatalogParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unaryMinusExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryMinusExpression(DatalogParser.UnaryMinusExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code neqExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNeqExpression(DatalogParser.NeqExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code andExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(DatalogParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code aggrExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAggrExpression(DatalogParser.AggrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code geExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGeExpression(DatalogParser.GeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code plusExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlusExpression(DatalogParser.PlusExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code termExpression}
	 * labeled alternative in {@link DatalogParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermExpression(DatalogParser.TermExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code msumAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMsumAggExpression(DatalogParser.MsumAggExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mprodAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMprodAggExpression(DatalogParser.MprodAggExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mcountAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMcountAggExpression(DatalogParser.McountAggExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code munionAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMunionAggExpression(DatalogParser.MunionAggExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mmaxAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMmaxAggExpression(DatalogParser.MmaxAggExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mminAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMminAggExpression(DatalogParser.MminAggExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unionAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionAggExpression(DatalogParser.UnionAggExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code listAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListAggExpression(DatalogParser.ListAggExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code setAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetAggExpression(DatalogParser.SetAggExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code minAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinAggExpression(DatalogParser.MinAggExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code maxAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaxAggExpression(DatalogParser.MaxAggExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sumAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSumAggExpression(DatalogParser.SumAggExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code prodAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProdAggExpression(DatalogParser.ProdAggExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code avgAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAvgAggExpression(DatalogParser.AvgAggExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code countAggExpression}
	 * labeled alternative in {@link DatalogParser#aggregation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCountAggExpression(DatalogParser.CountAggExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code substringExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubstringExpression(DatalogParser.SubstringExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code containsExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainsExpression(DatalogParser.ContainsExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code startsWithExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStartsWithExpression(DatalogParser.StartsWithExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code endsWithExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndsWithExpression(DatalogParser.EndsWithExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code concatExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcatExpression(DatalogParser.ConcatExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringLengthExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLengthExpression(DatalogParser.StringLengthExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code indexOfExpression}
	 * labeled alternative in {@link DatalogParser#stringOperators}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndexOfExpression(DatalogParser.IndexOfExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#varList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarList(DatalogParser.VarListContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(DatalogParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(DatalogParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#stringConstTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringConstTerm(DatalogParser.StringConstTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#integerConstTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerConstTerm(DatalogParser.IntegerConstTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#doubleConstTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleConstTerm(DatalogParser.DoubleConstTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#booleanConstTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanConstTerm(DatalogParser.BooleanConstTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#dateConstTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateConstTerm(DatalogParser.DateConstTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#integerSetTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerSetTerm(DatalogParser.IntegerSetTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#doubleSetTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleSetTerm(DatalogParser.DoubleSetTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code list}
	 * labeled alternative in {@link DatalogParser#listTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(DatalogParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code emptySet}
	 * labeled alternative in {@link DatalogParser#setConstTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptySet(DatalogParser.EmptySetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringSet}
	 * labeled alternative in {@link DatalogParser#setConstTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringSet(DatalogParser.StringSetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code integerSet}
	 * labeled alternative in {@link DatalogParser#setConstTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerSet(DatalogParser.IntegerSetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code doubleSet}
	 * labeled alternative in {@link DatalogParser#setConstTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleSet(DatalogParser.DoubleSetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dateSet}
	 * labeled alternative in {@link DatalogParser#setConstTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateSet(DatalogParser.DateSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#varTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarTerm(DatalogParser.VarTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#anonTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnonTerm(DatalogParser.AnonTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#falseTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseTerm(DatalogParser.FalseTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatalogParser#booleanTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanTerm(DatalogParser.BooleanTermContext ctx);
}