// Generated from Datalog.g4 by ANTLR 4.7.2
package uk.co.prometheux.prometheuxreasoner.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DatalogParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		SKOLEM_SYMBOL=32, ID=33, VAR=34, ANON_VAR=35, STRING=36, DATE=37, INTEGER=38, 
		DOUBLE=39, TRUE=40, FALSE=41, WS=42, LINE_COMMENT=43, AT=44, GT=45, LT=46, 
		GE=47, LE=48, EQ=49, IN=50, NOTIN=51, NEQ=52, PLUS=53, MINUS=54, NOT=55, 
		EQEQ=56, AND=57, OR=58, PROD=59, DIV=60, UNION=61, INTERSECTION=62, LBR=63, 
		RBR=64;
	public static final int
		RULE_program = 0, RULE_clause = 1, RULE_fact = 2, RULE_annotationBody = 3, 
		RULE_annotation = 4, RULE_rrule = 5, RULE_head = 6, RULE_falseHead = 7, 
		RULE_egdHead = 8, RULE_body = 9, RULE_literal = 10, RULE_condition = 11, 
		RULE_gtCondition = 12, RULE_ltCondition = 13, RULE_geCondition = 14, RULE_leCondition = 15, 
		RULE_eqCondition = 16, RULE_neqCondition = 17, RULE_inCondition = 18, 
		RULE_notInCondition = 19, RULE_expression = 20, RULE_aggregation = 21, 
		RULE_stringOperators = 22, RULE_varList = 23, RULE_atom = 24, RULE_term = 25, 
		RULE_stringConstTerm = 26, RULE_integerConstTerm = 27, RULE_doubleConstTerm = 28, 
		RULE_booleanConstTerm = 29, RULE_dateConstTerm = 30, RULE_integerSetTerm = 31, 
		RULE_doubleSetTerm = 32, RULE_listTerm = 33, RULE_setConstTerm = 34, RULE_varTerm = 35, 
		RULE_anonTerm = 36, RULE_falseTerm = 37, RULE_booleanTerm = 38;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "clause", "fact", "annotationBody", "annotation", "rrule", 
			"head", "falseHead", "egdHead", "body", "literal", "condition", "gtCondition", 
			"ltCondition", "geCondition", "leCondition", "eqCondition", "neqCondition", 
			"inCondition", "notInCondition", "expression", "aggregation", "stringOperators", 
			"varList", "atom", "term", "stringConstTerm", "integerConstTerm", "doubleConstTerm", 
			"booleanConstTerm", "dateConstTerm", "integerSetTerm", "doubleSetTerm", 
			"listTerm", "setConstTerm", "varTerm", "anonTerm", "falseTerm", "booleanTerm"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "':-'", "','", "'not'", "'dom(*)'", "'msum('", "'mprod('", 
			"'mcount('", "'munion('", "'mmax('", "'mmin('", "'union('", "'list('", 
			"'set('", "'min('", "'max('", "'sum('", "'prod('", "'avg('", "'count('", 
			"'substring('", "'contains('", "'starts_with('", "'ends_with('", "'concat('", 
			"'string_length('", "'index_of('", "'['", "']'", "'{'", "'}'", "'#'", 
			null, null, null, null, null, null, null, "'#T'", "'#F'", null, null, 
			"'@'", "'>'", "'<'", "'>='", "'<='", "'='", "' in '", null, null, "'+'", 
			"'-'", "'!'", "'=='", "'&&'", "'||'", "'*'", "'/'", "'|'", "'&'", "'('", 
			"')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, "SKOLEM_SYMBOL", "ID", 
			"VAR", "ANON_VAR", "STRING", "DATE", "INTEGER", "DOUBLE", "TRUE", "FALSE", 
			"WS", "LINE_COMMENT", "AT", "GT", "LT", "GE", "LE", "EQ", "IN", "NOTIN", 
			"NEQ", "PLUS", "MINUS", "NOT", "EQEQ", "AND", "OR", "PROD", "DIV", "UNION", 
			"INTERSECTION", "LBR", "RBR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Datalog.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public DatalogParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramContext extends ParserRuleContext {
		public List<ClauseContext> clause() {
			return getRuleContexts(ClauseContext.class);
		}
		public ClauseContext clause(int i) {
			return getRuleContext(ClauseContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ID) | (1L << VAR) | (1L << FALSE) | (1L << AT))) != 0)) {
				{
				{
				setState(78);
				clause();
				}
				}
				setState(83);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClauseContext extends ParserRuleContext {
		public AnnotationContext annotation() {
			return getRuleContext(AnnotationContext.class,0);
		}
		public FactContext fact() {
			return getRuleContext(FactContext.class,0);
		}
		public RruleContext rrule() {
			return getRuleContext(RruleContext.class,0);
		}
		public ClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_clause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClauseContext clause() throws RecognitionException {
		ClauseContext _localctx = new ClauseContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_clause);
		try {
			setState(87);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(84);
				annotation();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(85);
				fact();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(86);
				rrule();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FactContext extends ParserRuleContext {
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public List<AnnotationBodyContext> annotationBody() {
			return getRuleContexts(AnnotationBodyContext.class);
		}
		public AnnotationBodyContext annotationBody(int i) {
			return getRuleContext(AnnotationBodyContext.class,i);
		}
		public FactContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fact; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterFact(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitFact(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitFact(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FactContext fact() throws RecognitionException {
		FactContext _localctx = new FactContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_fact);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AT) {
				{
				{
				setState(89);
				annotationBody();
				}
				}
				setState(94);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(95);
			atom();
			setState(96);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationBodyContext extends ParserRuleContext {
		public TerminalNode AT() { return getToken(DatalogParser.AT, 0); }
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public AnnotationBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterAnnotationBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitAnnotationBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitAnnotationBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationBodyContext annotationBody() throws RecognitionException {
		AnnotationBodyContext _localctx = new AnnotationBodyContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_annotationBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			match(AT);
			setState(99);
			atom();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationContext extends ParserRuleContext {
		public AnnotationBodyContext annotationBody() {
			return getRuleContext(AnnotationBodyContext.class,0);
		}
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitAnnotation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationContext annotation() throws RecognitionException {
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_annotation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
			annotationBody();
			setState(102);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RruleContext extends ParserRuleContext {
		public HeadContext head() {
			return getRuleContext(HeadContext.class,0);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public List<AnnotationBodyContext> annotationBody() {
			return getRuleContexts(AnnotationBodyContext.class);
		}
		public AnnotationBodyContext annotationBody(int i) {
			return getRuleContext(AnnotationBodyContext.class,i);
		}
		public RruleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rrule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterRrule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitRrule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitRrule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RruleContext rrule() throws RecognitionException {
		RruleContext _localctx = new RruleContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_rrule);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AT) {
				{
				{
				setState(104);
				annotationBody();
				}
				}
				setState(109);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(110);
			head();
			setState(111);
			match(T__1);
			setState(112);
			body();
			setState(113);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HeadContext extends ParserRuleContext {
		public List<AtomContext> atom() {
			return getRuleContexts(AtomContext.class);
		}
		public AtomContext atom(int i) {
			return getRuleContext(AtomContext.class,i);
		}
		public EgdHeadContext egdHead() {
			return getRuleContext(EgdHeadContext.class,0);
		}
		public FalseHeadContext falseHead() {
			return getRuleContext(FalseHeadContext.class,0);
		}
		public HeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_head; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitHead(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitHead(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HeadContext head() throws RecognitionException {
		HeadContext _localctx = new HeadContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_head);
		int _la;
		try {
			setState(125);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(115);
				atom();
				setState(120);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(116);
					match(T__2);
					setState(117);
					atom();
					}
					}
					setState(122);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case VAR:
				enterOuterAlt(_localctx, 2);
				{
				setState(123);
				egdHead();
				}
				break;
			case FALSE:
				enterOuterAlt(_localctx, 3);
				{
				setState(124);
				falseHead();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FalseHeadContext extends ParserRuleContext {
		public FalseTermContext falseTerm() {
			return getRuleContext(FalseTermContext.class,0);
		}
		public FalseHeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_falseHead; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterFalseHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitFalseHead(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitFalseHead(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FalseHeadContext falseHead() throws RecognitionException {
		FalseHeadContext _localctx = new FalseHeadContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_falseHead);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
			falseTerm();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EgdHeadContext extends ParserRuleContext {
		public List<VarTermContext> varTerm() {
			return getRuleContexts(VarTermContext.class);
		}
		public VarTermContext varTerm(int i) {
			return getRuleContext(VarTermContext.class,i);
		}
		public TerminalNode EQ() { return getToken(DatalogParser.EQ, 0); }
		public EgdHeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_egdHead; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterEgdHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitEgdHead(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitEgdHead(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EgdHeadContext egdHead() throws RecognitionException {
		EgdHeadContext _localctx = new EgdHeadContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_egdHead);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			varTerm();
			setState(130);
			match(EQ);
			setState(131);
			varTerm();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BodyContext extends ParserRuleContext {
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public List<ConditionContext> condition() {
			return getRuleContexts(ConditionContext.class);
		}
		public ConditionContext condition(int i) {
			return getRuleContext(ConditionContext.class,i);
		}
		public BodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BodyContext body() throws RecognitionException {
		BodyContext _localctx = new BodyContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_body);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			literal();
			setState(138);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(134);
					match(T__2);
					setState(135);
					literal();
					}
					} 
				}
				setState(140);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			setState(145);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(141);
				match(T__2);
				setState(142);
				condition();
				}
				}
				setState(147);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
	 
		public LiteralContext() { }
		public void copyFrom(LiteralContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class PosLiteralContext extends LiteralContext {
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public PosLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterPosLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitPosLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitPosLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DomStarContext extends LiteralContext {
		public DomStarContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterDomStar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitDomStar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitDomStar(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NegLiteralContext extends LiteralContext {
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public NegLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterNegLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitNegLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitNegLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_literal);
		try {
			setState(152);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				_localctx = new PosLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(148);
				atom();
				}
				break;
			case T__3:
				_localctx = new NegLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(149);
				match(T__3);
				setState(150);
				atom();
				}
				break;
			case T__4:
				_localctx = new DomStarContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(151);
				match(T__4);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionContext extends ParserRuleContext {
		public GtConditionContext gtCondition() {
			return getRuleContext(GtConditionContext.class,0);
		}
		public LtConditionContext ltCondition() {
			return getRuleContext(LtConditionContext.class,0);
		}
		public GeConditionContext geCondition() {
			return getRuleContext(GeConditionContext.class,0);
		}
		public LeConditionContext leCondition() {
			return getRuleContext(LeConditionContext.class,0);
		}
		public EqConditionContext eqCondition() {
			return getRuleContext(EqConditionContext.class,0);
		}
		public NeqConditionContext neqCondition() {
			return getRuleContext(NeqConditionContext.class,0);
		}
		public InConditionContext inCondition() {
			return getRuleContext(InConditionContext.class,0);
		}
		public NotInConditionContext notInCondition() {
			return getRuleContext(NotInConditionContext.class,0);
		}
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		ConditionContext _localctx = new ConditionContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_condition);
		try {
			setState(162);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(154);
				gtCondition();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(155);
				ltCondition();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(156);
				geCondition();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(157);
				leCondition();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(158);
				eqCondition();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(159);
				neqCondition();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(160);
				inCondition();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(161);
				notInCondition();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GtConditionContext extends ParserRuleContext {
		public VarTermContext varTerm() {
			return getRuleContext(VarTermContext.class,0);
		}
		public TerminalNode GT() { return getToken(DatalogParser.GT, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public GtConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_gtCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterGtCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitGtCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitGtCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GtConditionContext gtCondition() throws RecognitionException {
		GtConditionContext _localctx = new GtConditionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_gtCondition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			varTerm();
			setState(165);
			match(GT);
			setState(166);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LtConditionContext extends ParserRuleContext {
		public VarTermContext varTerm() {
			return getRuleContext(VarTermContext.class,0);
		}
		public TerminalNode LT() { return getToken(DatalogParser.LT, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public LtConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ltCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterLtCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitLtCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitLtCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LtConditionContext ltCondition() throws RecognitionException {
		LtConditionContext _localctx = new LtConditionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_ltCondition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			varTerm();
			setState(169);
			match(LT);
			setState(170);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GeConditionContext extends ParserRuleContext {
		public VarTermContext varTerm() {
			return getRuleContext(VarTermContext.class,0);
		}
		public TerminalNode GE() { return getToken(DatalogParser.GE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public GeConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_geCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterGeCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitGeCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitGeCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GeConditionContext geCondition() throws RecognitionException {
		GeConditionContext _localctx = new GeConditionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_geCondition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			varTerm();
			setState(173);
			match(GE);
			setState(174);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LeConditionContext extends ParserRuleContext {
		public VarTermContext varTerm() {
			return getRuleContext(VarTermContext.class,0);
		}
		public TerminalNode LE() { return getToken(DatalogParser.LE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public LeConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_leCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterLeCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitLeCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitLeCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LeConditionContext leCondition() throws RecognitionException {
		LeConditionContext _localctx = new LeConditionContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_leCondition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(176);
			varTerm();
			setState(177);
			match(LE);
			setState(178);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EqConditionContext extends ParserRuleContext {
		public VarTermContext varTerm() {
			return getRuleContext(VarTermContext.class,0);
		}
		public TerminalNode EQ() { return getToken(DatalogParser.EQ, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public EqConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eqCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterEqCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitEqCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitEqCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EqConditionContext eqCondition() throws RecognitionException {
		EqConditionContext _localctx = new EqConditionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_eqCondition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(180);
			varTerm();
			setState(181);
			match(EQ);
			setState(182);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NeqConditionContext extends ParserRuleContext {
		public VarTermContext varTerm() {
			return getRuleContext(VarTermContext.class,0);
		}
		public TerminalNode NEQ() { return getToken(DatalogParser.NEQ, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public NeqConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_neqCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterNeqCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitNeqCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitNeqCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NeqConditionContext neqCondition() throws RecognitionException {
		NeqConditionContext _localctx = new NeqConditionContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_neqCondition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(184);
			varTerm();
			setState(185);
			match(NEQ);
			setState(186);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InConditionContext extends ParserRuleContext {
		public VarTermContext varTerm() {
			return getRuleContext(VarTermContext.class,0);
		}
		public TerminalNode IN() { return getToken(DatalogParser.IN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public InConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterInCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitInCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitInCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InConditionContext inCondition() throws RecognitionException {
		InConditionContext _localctx = new InConditionContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_inCondition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(188);
			varTerm();
			setState(189);
			match(IN);
			setState(190);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NotInConditionContext extends ParserRuleContext {
		public VarTermContext varTerm() {
			return getRuleContext(VarTermContext.class,0);
		}
		public TerminalNode NOTIN() { return getToken(DatalogParser.NOTIN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public NotInConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notInCondition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterNotInCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitNotInCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitNotInCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NotInConditionContext notInCondition() throws RecognitionException {
		NotInConditionContext _localctx = new NotInConditionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_notInCondition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(192);
			varTerm();
			setState(193);
			match(NOTIN);
			setState(194);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class LtExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode LT() { return getToken(DatalogParser.LT, 0); }
		public LtExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterLtExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitLtExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitLtExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GtExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode GT() { return getToken(DatalogParser.GT, 0); }
		public GtExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterGtExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitGtExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitGtExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExternalExpressionContext extends ExpressionContext {
		public TerminalNode ID() { return getToken(DatalogParser.ID, 0); }
		public TerminalNode LBR() { return getToken(DatalogParser.LBR, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public ExternalExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterExternalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitExternalExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitExternalExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntersectionExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode INTERSECTION() { return getToken(DatalogParser.INTERSECTION, 0); }
		public IntersectionExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterIntersectionExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitIntersectionExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitIntersectionExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MinusExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode MINUS() { return getToken(DatalogParser.MINUS, 0); }
		public MinusExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterMinusExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitMinusExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitMinusExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SkolemExpressionContext extends ExpressionContext {
		public TerminalNode SKOLEM_SYMBOL() { return getToken(DatalogParser.SKOLEM_SYMBOL, 0); }
		public TerminalNode ID() { return getToken(DatalogParser.ID, 0); }
		public TerminalNode LBR() { return getToken(DatalogParser.LBR, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public SkolemExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterSkolemExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitSkolemExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitSkolemExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ProdExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode PROD() { return getToken(DatalogParser.PROD, 0); }
		public ProdExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterProdExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitProdExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitProdExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LeExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode LE() { return getToken(DatalogParser.LE, 0); }
		public LeExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterLeExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitLeExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitLeExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EqEqExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode EQEQ() { return getToken(DatalogParser.EQEQ, 0); }
		public EqEqExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterEqEqExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitEqEqExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitEqEqExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotExpressionContext extends ExpressionContext {
		public TerminalNode NOT() { return getToken(DatalogParser.NOT, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public NotExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterNotExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitNotExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitNotExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DivExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode DIV() { return getToken(DatalogParser.DIV, 0); }
		public DivExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterDivExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitDivExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitDivExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrecExpressionContext extends ExpressionContext {
		public TerminalNode LBR() { return getToken(DatalogParser.LBR, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public PrecExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterPrecExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitPrecExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitPrecExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnionExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode UNION() { return getToken(DatalogParser.UNION, 0); }
		public UnionExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterUnionExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitUnionExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitUnionExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringOperatorsExpressionContext extends ExpressionContext {
		public StringOperatorsContext stringOperators() {
			return getRuleContext(StringOperatorsContext.class,0);
		}
		public StringOperatorsExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterStringOperatorsExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitStringOperatorsExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitStringOperatorsExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode OR() { return getToken(DatalogParser.OR, 0); }
		public OrExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitOrExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitOrExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryMinusExpressionContext extends ExpressionContext {
		public TerminalNode MINUS() { return getToken(DatalogParser.MINUS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnaryMinusExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterUnaryMinusExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitUnaryMinusExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitUnaryMinusExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NeqExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode NEQ() { return getToken(DatalogParser.NEQ, 0); }
		public NeqExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterNeqExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitNeqExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitNeqExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AndExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode AND() { return getToken(DatalogParser.AND, 0); }
		public AndExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitAndExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitAndExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AggrExpressionContext extends ExpressionContext {
		public AggregationContext aggregation() {
			return getRuleContext(AggregationContext.class,0);
		}
		public AggrExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterAggrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitAggrExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitAggrExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GeExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode GE() { return getToken(DatalogParser.GE, 0); }
		public GeExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterGeExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitGeExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitGeExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PlusExpressionContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(DatalogParser.PLUS, 0); }
		public PlusExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterPlusExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitPlusExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitPlusExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TermExpressionContext extends ExpressionContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TermExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterTermExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitTermExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitTermExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 40;
		enterRecursionRule(_localctx, 40, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(233);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				{
				_localctx = new PrecExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(197);
				match(LBR);
				setState(198);
				expression(0);
				setState(199);
				match(RBR);
				}
				break;
			case 2:
				{
				_localctx = new UnaryMinusExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(201);
				match(MINUS);
				setState(202);
				expression(19);
				}
				break;
			case 3:
				{
				_localctx = new NotExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(203);
				match(NOT);
				setState(204);
				expression(14);
				}
				break;
			case 4:
				{
				_localctx = new AggrExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(205);
				aggregation();
				}
				break;
			case 5:
				{
				_localctx = new StringOperatorsExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(206);
				stringOperators();
				}
				break;
			case 6:
				{
				_localctx = new SkolemExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(207);
				match(SKOLEM_SYMBOL);
				setState(208);
				match(ID);
				{
				setState(209);
				match(LBR);
				setState(210);
				expression(0);
				setState(215);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(211);
					match(T__2);
					setState(212);
					expression(0);
					}
					}
					setState(217);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(218);
				match(RBR);
				}
				}
				break;
			case 7:
				{
				_localctx = new ExternalExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(220);
				match(ID);
				{
				setState(221);
				match(LBR);
				setState(222);
				expression(0);
				setState(227);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(223);
					match(T__2);
					setState(224);
					expression(0);
					}
					}
					setState(229);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(230);
				match(RBR);
				}
				}
				break;
			case 8:
				{
				_localctx = new TermExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(232);
				term();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(279);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(277);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
					case 1:
						{
						_localctx = new ProdExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(235);
						if (!(precpred(_ctx, 21))) throw new FailedPredicateException(this, "precpred(_ctx, 21)");
						setState(236);
						match(PROD);
						setState(237);
						expression(22);
						}
						break;
					case 2:
						{
						_localctx = new DivExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(238);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(239);
						match(DIV);
						setState(240);
						expression(21);
						}
						break;
					case 3:
						{
						_localctx = new PlusExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(241);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(242);
						match(PLUS);
						setState(243);
						expression(19);
						}
						break;
					case 4:
						{
						_localctx = new UnionExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(244);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(245);
						match(UNION);
						setState(246);
						expression(18);
						}
						break;
					case 5:
						{
						_localctx = new IntersectionExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(247);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(248);
						match(INTERSECTION);
						setState(249);
						expression(17);
						}
						break;
					case 6:
						{
						_localctx = new MinusExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(250);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(251);
						match(MINUS);
						setState(252);
						expression(16);
						}
						break;
					case 7:
						{
						_localctx = new LtExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(253);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(254);
						match(LT);
						setState(255);
						expression(14);
						}
						break;
					case 8:
						{
						_localctx = new LeExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(256);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(257);
						match(LE);
						setState(258);
						expression(13);
						}
						break;
					case 9:
						{
						_localctx = new GtExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(259);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(260);
						match(GT);
						setState(261);
						expression(12);
						}
						break;
					case 10:
						{
						_localctx = new GeExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(262);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(263);
						match(GE);
						setState(264);
						expression(11);
						}
						break;
					case 11:
						{
						_localctx = new EqEqExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(265);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(266);
						match(EQEQ);
						setState(267);
						expression(10);
						}
						break;
					case 12:
						{
						_localctx = new NeqExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(268);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(269);
						match(NEQ);
						setState(270);
						expression(9);
						}
						break;
					case 13:
						{
						_localctx = new AndExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(271);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(272);
						match(AND);
						setState(273);
						expression(8);
						}
						break;
					case 14:
						{
						_localctx = new OrExpressionContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(274);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(275);
						match(OR);
						setState(276);
						expression(7);
						}
						break;
					}
					} 
				}
				setState(281);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class AggregationContext extends ParserRuleContext {
		public AggregationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aggregation; }
	 
		public AggregationContext() { }
		public void copyFrom(AggregationContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SumAggExpressionContext extends AggregationContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public SumAggExpressionContext(AggregationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterSumAggExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitSumAggExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitSumAggExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ListAggExpressionContext extends AggregationContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public VarListContext varList() {
			return getRuleContext(VarListContext.class,0);
		}
		public ListAggExpressionContext(AggregationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterListAggExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitListAggExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitListAggExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class McountAggExpressionContext extends AggregationContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public VarListContext varList() {
			return getRuleContext(VarListContext.class,0);
		}
		public McountAggExpressionContext(AggregationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterMcountAggExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitMcountAggExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitMcountAggExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MprodAggExpressionContext extends AggregationContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public VarListContext varList() {
			return getRuleContext(VarListContext.class,0);
		}
		public MprodAggExpressionContext(AggregationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterMprodAggExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitMprodAggExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitMprodAggExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SetAggExpressionContext extends AggregationContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public VarListContext varList() {
			return getRuleContext(VarListContext.class,0);
		}
		public SetAggExpressionContext(AggregationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterSetAggExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitSetAggExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitSetAggExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MinAggExpressionContext extends AggregationContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public MinAggExpressionContext(AggregationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterMinAggExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitMinAggExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitMinAggExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MaxAggExpressionContext extends AggregationContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public MaxAggExpressionContext(AggregationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterMaxAggExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitMaxAggExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitMaxAggExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AvgAggExpressionContext extends AggregationContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public AvgAggExpressionContext(AggregationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterAvgAggExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitAvgAggExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitAvgAggExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnionAggExpressionContext extends AggregationContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public VarListContext varList() {
			return getRuleContext(VarListContext.class,0);
		}
		public UnionAggExpressionContext(AggregationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterUnionAggExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitUnionAggExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitUnionAggExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CountAggExpressionContext extends AggregationContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public VarListContext varList() {
			return getRuleContext(VarListContext.class,0);
		}
		public CountAggExpressionContext(AggregationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterCountAggExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitCountAggExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitCountAggExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ProdAggExpressionContext extends AggregationContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public ProdAggExpressionContext(AggregationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterProdAggExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitProdAggExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitProdAggExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MmaxAggExpressionContext extends AggregationContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public MmaxAggExpressionContext(AggregationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterMmaxAggExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitMmaxAggExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitMmaxAggExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MsumAggExpressionContext extends AggregationContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public VarListContext varList() {
			return getRuleContext(VarListContext.class,0);
		}
		public MsumAggExpressionContext(AggregationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterMsumAggExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitMsumAggExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitMsumAggExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MminAggExpressionContext extends AggregationContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public MminAggExpressionContext(AggregationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterMminAggExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitMminAggExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitMminAggExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MunionAggExpressionContext extends AggregationContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public VarListContext varList() {
			return getRuleContext(VarListContext.class,0);
		}
		public MunionAggExpressionContext(AggregationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterMunionAggExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitMunionAggExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitMunionAggExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AggregationContext aggregation() throws RecognitionException {
		AggregationContext _localctx = new AggregationContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_aggregation);
		int _la;
		try {
			setState(374);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__5:
				_localctx = new MsumAggExpressionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(282);
				match(T__5);
				setState(283);
				expression(0);
				setState(286);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(284);
					match(T__2);
					setState(285);
					varList();
					}
				}

				setState(288);
				match(RBR);
				}
				break;
			case T__6:
				_localctx = new MprodAggExpressionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(290);
				match(T__6);
				setState(291);
				expression(0);
				setState(294);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(292);
					match(T__2);
					setState(293);
					varList();
					}
				}

				setState(296);
				match(RBR);
				}
				break;
			case T__7:
				_localctx = new McountAggExpressionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(298);
				match(T__7);
				setState(299);
				expression(0);
				setState(302);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(300);
					match(T__2);
					setState(301);
					varList();
					}
				}

				setState(304);
				match(RBR);
				}
				break;
			case T__8:
				_localctx = new MunionAggExpressionContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(306);
				match(T__8);
				setState(307);
				expression(0);
				setState(310);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(308);
					match(T__2);
					setState(309);
					varList();
					}
				}

				setState(312);
				match(RBR);
				}
				break;
			case T__9:
				_localctx = new MmaxAggExpressionContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(314);
				match(T__9);
				setState(315);
				expression(0);
				setState(316);
				match(RBR);
				}
				break;
			case T__10:
				_localctx = new MminAggExpressionContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(318);
				match(T__10);
				setState(319);
				expression(0);
				setState(320);
				match(RBR);
				}
				break;
			case T__11:
				_localctx = new UnionAggExpressionContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(322);
				match(T__11);
				setState(323);
				expression(0);
				setState(326);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(324);
					match(T__2);
					setState(325);
					varList();
					}
				}

				setState(328);
				match(RBR);
				}
				break;
			case T__12:
				_localctx = new ListAggExpressionContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(330);
				match(T__12);
				setState(331);
				expression(0);
				setState(334);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(332);
					match(T__2);
					setState(333);
					varList();
					}
				}

				setState(336);
				match(RBR);
				}
				break;
			case T__13:
				_localctx = new SetAggExpressionContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(338);
				match(T__13);
				setState(339);
				expression(0);
				setState(342);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(340);
					match(T__2);
					setState(341);
					varList();
					}
				}

				setState(344);
				match(RBR);
				}
				break;
			case T__14:
				_localctx = new MinAggExpressionContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(346);
				match(T__14);
				setState(347);
				expression(0);
				setState(348);
				match(RBR);
				}
				break;
			case T__15:
				_localctx = new MaxAggExpressionContext(_localctx);
				enterOuterAlt(_localctx, 11);
				{
				setState(350);
				match(T__15);
				setState(351);
				expression(0);
				setState(352);
				match(RBR);
				}
				break;
			case T__16:
				_localctx = new SumAggExpressionContext(_localctx);
				enterOuterAlt(_localctx, 12);
				{
				setState(354);
				match(T__16);
				setState(355);
				expression(0);
				setState(356);
				match(RBR);
				}
				break;
			case T__17:
				_localctx = new ProdAggExpressionContext(_localctx);
				enterOuterAlt(_localctx, 13);
				{
				setState(358);
				match(T__17);
				setState(359);
				expression(0);
				setState(360);
				match(RBR);
				}
				break;
			case T__18:
				_localctx = new AvgAggExpressionContext(_localctx);
				enterOuterAlt(_localctx, 14);
				{
				setState(362);
				match(T__18);
				setState(363);
				expression(0);
				setState(364);
				match(RBR);
				}
				break;
			case T__19:
				_localctx = new CountAggExpressionContext(_localctx);
				enterOuterAlt(_localctx, 15);
				{
				setState(366);
				match(T__19);
				setState(367);
				expression(0);
				setState(370);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(368);
					match(T__2);
					setState(369);
					varList();
					}
				}

				setState(372);
				match(RBR);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringOperatorsContext extends ParserRuleContext {
		public StringOperatorsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringOperators; }
	 
		public StringOperatorsContext() { }
		public void copyFrom(StringOperatorsContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class StartsWithExpressionContext extends StringOperatorsContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public StartsWithExpressionContext(StringOperatorsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterStartsWithExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitStartsWithExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitStartsWithExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ContainsExpressionContext extends StringOperatorsContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public ContainsExpressionContext(StringOperatorsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterContainsExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitContainsExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitContainsExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IndexOfExpressionContext extends StringOperatorsContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public IndexOfExpressionContext(StringOperatorsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterIndexOfExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitIndexOfExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitIndexOfExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringLengthExpressionContext extends StringOperatorsContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public StringLengthExpressionContext(StringOperatorsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterStringLengthExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitStringLengthExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitStringLengthExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SubstringExpressionContext extends StringOperatorsContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public SubstringExpressionContext(StringOperatorsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterSubstringExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitSubstringExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitSubstringExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConcatExpressionContext extends StringOperatorsContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public ConcatExpressionContext(StringOperatorsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterConcatExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitConcatExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitConcatExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EndsWithExpressionContext extends StringOperatorsContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public EndsWithExpressionContext(StringOperatorsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterEndsWithExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitEndsWithExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitEndsWithExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringOperatorsContext stringOperators() throws RecognitionException {
		StringOperatorsContext _localctx = new StringOperatorsContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_stringOperators);
		try {
			setState(418);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__20:
				_localctx = new SubstringExpressionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(376);
				match(T__20);
				setState(377);
				expression(0);
				setState(378);
				match(T__2);
				setState(379);
				expression(0);
				setState(380);
				match(T__2);
				setState(381);
				expression(0);
				setState(382);
				match(RBR);
				}
				break;
			case T__21:
				_localctx = new ContainsExpressionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(384);
				match(T__21);
				setState(385);
				expression(0);
				setState(386);
				match(T__2);
				setState(387);
				expression(0);
				setState(388);
				match(RBR);
				}
				break;
			case T__22:
				_localctx = new StartsWithExpressionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(390);
				match(T__22);
				setState(391);
				expression(0);
				setState(392);
				match(T__2);
				setState(393);
				expression(0);
				setState(394);
				match(RBR);
				}
				break;
			case T__23:
				_localctx = new EndsWithExpressionContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(396);
				match(T__23);
				setState(397);
				expression(0);
				setState(398);
				match(T__2);
				setState(399);
				expression(0);
				setState(400);
				match(RBR);
				}
				break;
			case T__24:
				_localctx = new ConcatExpressionContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(402);
				match(T__24);
				setState(403);
				expression(0);
				setState(404);
				match(T__2);
				setState(405);
				expression(0);
				setState(406);
				match(RBR);
				}
				break;
			case T__25:
				_localctx = new StringLengthExpressionContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(408);
				match(T__25);
				setState(409);
				expression(0);
				setState(410);
				match(RBR);
				}
				break;
			case T__26:
				_localctx = new IndexOfExpressionContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(412);
				match(T__26);
				setState(413);
				expression(0);
				setState(414);
				match(T__2);
				setState(415);
				expression(0);
				setState(416);
				match(RBR);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarListContext extends ParserRuleContext {
		public TerminalNode LT() { return getToken(DatalogParser.LT, 0); }
		public List<TerminalNode> VAR() { return getTokens(DatalogParser.VAR); }
		public TerminalNode VAR(int i) {
			return getToken(DatalogParser.VAR, i);
		}
		public TerminalNode GT() { return getToken(DatalogParser.GT, 0); }
		public VarListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterVarList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitVarList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitVarList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarListContext varList() throws RecognitionException {
		VarListContext _localctx = new VarListContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_varList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(420);
			match(LT);
			setState(421);
			match(VAR);
			setState(426);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(422);
				match(T__2);
				setState(423);
				match(VAR);
				}
				}
				setState(428);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(429);
			match(GT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(DatalogParser.ID, 0); }
		public TerminalNode LBR() { return getToken(DatalogParser.LBR, 0); }
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public TerminalNode RBR() { return getToken(DatalogParser.RBR, 0); }
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitAtom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitAtom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_atom);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(431);
			match(ID);
			setState(443);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LBR) {
				{
				setState(432);
				match(LBR);
				setState(433);
				term();
				setState(438);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(434);
					match(T__2);
					setState(435);
					term();
					}
					}
					setState(440);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(441);
				match(RBR);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public BooleanConstTermContext booleanConstTerm() {
			return getRuleContext(BooleanConstTermContext.class,0);
		}
		public StringConstTermContext stringConstTerm() {
			return getRuleContext(StringConstTermContext.class,0);
		}
		public IntegerConstTermContext integerConstTerm() {
			return getRuleContext(IntegerConstTermContext.class,0);
		}
		public DoubleConstTermContext doubleConstTerm() {
			return getRuleContext(DoubleConstTermContext.class,0);
		}
		public DateConstTermContext dateConstTerm() {
			return getRuleContext(DateConstTermContext.class,0);
		}
		public SetConstTermContext setConstTerm() {
			return getRuleContext(SetConstTermContext.class,0);
		}
		public ListTermContext listTerm() {
			return getRuleContext(ListTermContext.class,0);
		}
		public VarTermContext varTerm() {
			return getRuleContext(VarTermContext.class,0);
		}
		public AnonTermContext anonTerm() {
			return getRuleContext(AnonTermContext.class,0);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_term);
		try {
			setState(454);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(445);
				booleanConstTerm();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(446);
				stringConstTerm();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(447);
				integerConstTerm();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(448);
				doubleConstTerm();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(449);
				dateConstTerm();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(450);
				setConstTerm();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(451);
				listTerm();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(452);
				varTerm();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(453);
				anonTerm();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringConstTermContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(DatalogParser.STRING, 0); }
		public StringConstTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringConstTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterStringConstTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitStringConstTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitStringConstTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringConstTermContext stringConstTerm() throws RecognitionException {
		StringConstTermContext _localctx = new StringConstTermContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_stringConstTerm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(456);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntegerConstTermContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(DatalogParser.INTEGER, 0); }
		public TerminalNode MINUS() { return getToken(DatalogParser.MINUS, 0); }
		public IntegerConstTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integerConstTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterIntegerConstTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitIntegerConstTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitIntegerConstTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntegerConstTermContext integerConstTerm() throws RecognitionException {
		IntegerConstTermContext _localctx = new IntegerConstTermContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_integerConstTerm);
		try {
			setState(461);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTEGER:
				enterOuterAlt(_localctx, 1);
				{
				setState(458);
				match(INTEGER);
				}
				break;
			case MINUS:
				enterOuterAlt(_localctx, 2);
				{
				setState(459);
				match(MINUS);
				setState(460);
				match(INTEGER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DoubleConstTermContext extends ParserRuleContext {
		public TerminalNode DOUBLE() { return getToken(DatalogParser.DOUBLE, 0); }
		public TerminalNode MINUS() { return getToken(DatalogParser.MINUS, 0); }
		public DoubleConstTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_doubleConstTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterDoubleConstTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitDoubleConstTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitDoubleConstTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DoubleConstTermContext doubleConstTerm() throws RecognitionException {
		DoubleConstTermContext _localctx = new DoubleConstTermContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_doubleConstTerm);
		try {
			setState(466);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DOUBLE:
				enterOuterAlt(_localctx, 1);
				{
				setState(463);
				match(DOUBLE);
				}
				break;
			case MINUS:
				enterOuterAlt(_localctx, 2);
				{
				setState(464);
				match(MINUS);
				setState(465);
				match(DOUBLE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanConstTermContext extends ParserRuleContext {
		public BooleanTermContext booleanTerm() {
			return getRuleContext(BooleanTermContext.class,0);
		}
		public BooleanConstTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanConstTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterBooleanConstTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitBooleanConstTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitBooleanConstTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanConstTermContext booleanConstTerm() throws RecognitionException {
		BooleanConstTermContext _localctx = new BooleanConstTermContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_booleanConstTerm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(468);
			booleanTerm();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DateConstTermContext extends ParserRuleContext {
		public TerminalNode DATE() { return getToken(DatalogParser.DATE, 0); }
		public DateConstTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dateConstTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterDateConstTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitDateConstTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitDateConstTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DateConstTermContext dateConstTerm() throws RecognitionException {
		DateConstTermContext _localctx = new DateConstTermContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_dateConstTerm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(470);
			match(DATE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntegerSetTermContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(DatalogParser.INTEGER, 0); }
		public TerminalNode MINUS() { return getToken(DatalogParser.MINUS, 0); }
		public IntegerSetTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integerSetTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterIntegerSetTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitIntegerSetTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitIntegerSetTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntegerSetTermContext integerSetTerm() throws RecognitionException {
		IntegerSetTermContext _localctx = new IntegerSetTermContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_integerSetTerm);
		try {
			setState(475);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTEGER:
				enterOuterAlt(_localctx, 1);
				{
				setState(472);
				match(INTEGER);
				}
				break;
			case MINUS:
				enterOuterAlt(_localctx, 2);
				{
				setState(473);
				match(MINUS);
				setState(474);
				match(INTEGER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DoubleSetTermContext extends ParserRuleContext {
		public TerminalNode DOUBLE() { return getToken(DatalogParser.DOUBLE, 0); }
		public TerminalNode MINUS() { return getToken(DatalogParser.MINUS, 0); }
		public DoubleSetTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_doubleSetTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterDoubleSetTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitDoubleSetTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitDoubleSetTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DoubleSetTermContext doubleSetTerm() throws RecognitionException {
		DoubleSetTermContext _localctx = new DoubleSetTermContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_doubleSetTerm);
		try {
			setState(480);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DOUBLE:
				enterOuterAlt(_localctx, 1);
				{
				setState(477);
				match(DOUBLE);
				}
				break;
			case MINUS:
				enterOuterAlt(_localctx, 2);
				{
				setState(478);
				match(MINUS);
				setState(479);
				match(DOUBLE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ListTermContext extends ParserRuleContext {
		public ListTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_listTerm; }
	 
		public ListTermContext() { }
		public void copyFrom(ListTermContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ListContext extends ListTermContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ListContext(ListTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListTermContext listTerm() throws RecognitionException {
		ListTermContext _localctx = new ListTermContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_listTerm);
		int _la;
		try {
			setState(495);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
			case 1:
				_localctx = new ListContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(482);
				match(T__27);
				setState(483);
				match(T__28);
				}
				break;
			case 2:
				_localctx = new ListContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(484);
				match(T__27);
				setState(485);
				expression(0);
				setState(490);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(486);
					match(T__2);
					setState(487);
					expression(0);
					}
					}
					setState(492);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(493);
				match(T__28);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SetConstTermContext extends ParserRuleContext {
		public SetConstTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setConstTerm; }
	 
		public SetConstTermContext() { }
		public void copyFrom(SetConstTermContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class EmptySetContext extends SetConstTermContext {
		public EmptySetContext(SetConstTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterEmptySet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitEmptySet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitEmptySet(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DoubleSetContext extends SetConstTermContext {
		public List<DoubleSetTermContext> doubleSetTerm() {
			return getRuleContexts(DoubleSetTermContext.class);
		}
		public DoubleSetTermContext doubleSetTerm(int i) {
			return getRuleContext(DoubleSetTermContext.class,i);
		}
		public DoubleSetContext(SetConstTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterDoubleSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitDoubleSet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitDoubleSet(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DateSetContext extends SetConstTermContext {
		public List<TerminalNode> DATE() { return getTokens(DatalogParser.DATE); }
		public TerminalNode DATE(int i) {
			return getToken(DatalogParser.DATE, i);
		}
		public DateSetContext(SetConstTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterDateSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitDateSet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitDateSet(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringSetContext extends SetConstTermContext {
		public List<TerminalNode> STRING() { return getTokens(DatalogParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(DatalogParser.STRING, i);
		}
		public StringSetContext(SetConstTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterStringSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitStringSet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitStringSet(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntegerSetContext extends SetConstTermContext {
		public List<IntegerSetTermContext> integerSetTerm() {
			return getRuleContexts(IntegerSetTermContext.class);
		}
		public IntegerSetTermContext integerSetTerm(int i) {
			return getRuleContext(IntegerSetTermContext.class,i);
		}
		public IntegerSetContext(SetConstTermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterIntegerSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitIntegerSet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitIntegerSet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetConstTermContext setConstTerm() throws RecognitionException {
		SetConstTermContext _localctx = new SetConstTermContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_setConstTerm);
		int _la;
		try {
			setState(541);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				_localctx = new EmptySetContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(497);
				match(T__29);
				setState(498);
				match(T__30);
				}
				break;
			case 2:
				_localctx = new StringSetContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(499);
				match(T__29);
				setState(500);
				match(STRING);
				setState(505);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(501);
					match(T__2);
					setState(502);
					match(STRING);
					}
					}
					setState(507);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(508);
				match(T__30);
				}
				break;
			case 3:
				_localctx = new IntegerSetContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(509);
				match(T__29);
				setState(510);
				integerSetTerm();
				setState(515);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(511);
					match(T__2);
					setState(512);
					integerSetTerm();
					}
					}
					setState(517);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(518);
				match(T__30);
				}
				break;
			case 4:
				_localctx = new DoubleSetContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(520);
				match(T__29);
				setState(521);
				doubleSetTerm();
				setState(526);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(522);
					match(T__2);
					setState(523);
					doubleSetTerm();
					}
					}
					setState(528);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(529);
				match(T__30);
				}
				break;
			case 5:
				_localctx = new DateSetContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(531);
				match(T__29);
				setState(532);
				match(DATE);
				setState(537);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(533);
					match(T__2);
					setState(534);
					match(DATE);
					}
					}
					setState(539);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(540);
				match(T__30);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarTermContext extends ParserRuleContext {
		public TerminalNode VAR() { return getToken(DatalogParser.VAR, 0); }
		public VarTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterVarTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitVarTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitVarTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarTermContext varTerm() throws RecognitionException {
		VarTermContext _localctx = new VarTermContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_varTerm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(543);
			match(VAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnonTermContext extends ParserRuleContext {
		public TerminalNode ANON_VAR() { return getToken(DatalogParser.ANON_VAR, 0); }
		public AnonTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anonTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterAnonTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitAnonTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitAnonTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnonTermContext anonTerm() throws RecognitionException {
		AnonTermContext _localctx = new AnonTermContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_anonTerm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(545);
			match(ANON_VAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FalseTermContext extends ParserRuleContext {
		public TerminalNode FALSE() { return getToken(DatalogParser.FALSE, 0); }
		public FalseTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_falseTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterFalseTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitFalseTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitFalseTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FalseTermContext falseTerm() throws RecognitionException {
		FalseTermContext _localctx = new FalseTermContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_falseTerm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(547);
			match(FALSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooleanTermContext extends ParserRuleContext {
		public TerminalNode TRUE() { return getToken(DatalogParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(DatalogParser.FALSE, 0); }
		public BooleanTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).enterBooleanTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DatalogListener ) ((DatalogListener)listener).exitBooleanTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DatalogVisitor ) return ((DatalogVisitor<? extends T>)visitor).visitBooleanTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanTermContext booleanTerm() throws RecognitionException {
		BooleanTermContext _localctx = new BooleanTermContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_booleanTerm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(549);
			_la = _input.LA(1);
			if ( !(_la==TRUE || _la==FALSE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 20:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 21);
		case 1:
			return precpred(_ctx, 20);
		case 2:
			return precpred(_ctx, 18);
		case 3:
			return precpred(_ctx, 17);
		case 4:
			return precpred(_ctx, 16);
		case 5:
			return precpred(_ctx, 15);
		case 6:
			return precpred(_ctx, 13);
		case 7:
			return precpred(_ctx, 12);
		case 8:
			return precpred(_ctx, 11);
		case 9:
			return precpred(_ctx, 10);
		case 10:
			return precpred(_ctx, 9);
		case 11:
			return precpred(_ctx, 8);
		case 12:
			return precpred(_ctx, 7);
		case 13:
			return precpred(_ctx, 6);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3B\u022a\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\3\2\7\2R\n\2\f\2\16"+
		"\2U\13\2\3\3\3\3\3\3\5\3Z\n\3\3\4\7\4]\n\4\f\4\16\4`\13\4\3\4\3\4\3\4"+
		"\3\5\3\5\3\5\3\6\3\6\3\6\3\7\7\7l\n\7\f\7\16\7o\13\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\b\3\b\3\b\7\by\n\b\f\b\16\b|\13\b\3\b\3\b\5\b\u0080\n\b\3\t\3\t\3"+
		"\n\3\n\3\n\3\n\3\13\3\13\3\13\7\13\u008b\n\13\f\13\16\13\u008e\13\13\3"+
		"\13\3\13\7\13\u0092\n\13\f\13\16\13\u0095\13\13\3\f\3\f\3\f\3\f\5\f\u009b"+
		"\n\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u00a5\n\r\3\16\3\16\3\16\3\16"+
		"\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22"+
		"\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\7\26\u00d8\n\26\f\26\16\26\u00db\13\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\7\26\u00e4\n\26\f\26\16\26\u00e7\13\26\3\26\3\26"+
		"\3\26\5\26\u00ec\n\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\7\26\u0118\n\26\f\26\16\26\u011b\13\26\3\27\3\27"+
		"\3\27\3\27\5\27\u0121\n\27\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u0129\n"+
		"\27\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u0131\n\27\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\5\27\u0139\n\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\3\27\3\27\3\27\5\27\u0149\n\27\3\27\3\27\3\27\3\27\3\27\3\27"+
		"\5\27\u0151\n\27\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u0159\n\27\3\27\3"+
		"\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3"+
		"\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u0175\n\27"+
		"\3\27\3\27\5\27\u0179\n\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\30\3\30\5\30\u01a5\n\30\3\31\3\31\3\31\3\31\7\31\u01ab"+
		"\n\31\f\31\16\31\u01ae\13\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\7\32\u01b7"+
		"\n\32\f\32\16\32\u01ba\13\32\3\32\3\32\5\32\u01be\n\32\3\33\3\33\3\33"+
		"\3\33\3\33\3\33\3\33\3\33\3\33\5\33\u01c9\n\33\3\34\3\34\3\35\3\35\3\35"+
		"\5\35\u01d0\n\35\3\36\3\36\3\36\5\36\u01d5\n\36\3\37\3\37\3 \3 \3!\3!"+
		"\3!\5!\u01de\n!\3\"\3\"\3\"\5\"\u01e3\n\"\3#\3#\3#\3#\3#\3#\7#\u01eb\n"+
		"#\f#\16#\u01ee\13#\3#\3#\5#\u01f2\n#\3$\3$\3$\3$\3$\3$\7$\u01fa\n$\f$"+
		"\16$\u01fd\13$\3$\3$\3$\3$\3$\7$\u0204\n$\f$\16$\u0207\13$\3$\3$\3$\3"+
		"$\3$\3$\7$\u020f\n$\f$\16$\u0212\13$\3$\3$\3$\3$\3$\3$\7$\u021a\n$\f$"+
		"\16$\u021d\13$\3$\5$\u0220\n$\3%\3%\3&\3&\3\'\3\'\3(\3(\3(\2\3*)\2\4\6"+
		"\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLN\2\3"+
		"\3\2*+\2\u0261\2S\3\2\2\2\4Y\3\2\2\2\6^\3\2\2\2\bd\3\2\2\2\ng\3\2\2\2"+
		"\fm\3\2\2\2\16\177\3\2\2\2\20\u0081\3\2\2\2\22\u0083\3\2\2\2\24\u0087"+
		"\3\2\2\2\26\u009a\3\2\2\2\30\u00a4\3\2\2\2\32\u00a6\3\2\2\2\34\u00aa\3"+
		"\2\2\2\36\u00ae\3\2\2\2 \u00b2\3\2\2\2\"\u00b6\3\2\2\2$\u00ba\3\2\2\2"+
		"&\u00be\3\2\2\2(\u00c2\3\2\2\2*\u00eb\3\2\2\2,\u0178\3\2\2\2.\u01a4\3"+
		"\2\2\2\60\u01a6\3\2\2\2\62\u01b1\3\2\2\2\64\u01c8\3\2\2\2\66\u01ca\3\2"+
		"\2\28\u01cf\3\2\2\2:\u01d4\3\2\2\2<\u01d6\3\2\2\2>\u01d8\3\2\2\2@\u01dd"+
		"\3\2\2\2B\u01e2\3\2\2\2D\u01f1\3\2\2\2F\u021f\3\2\2\2H\u0221\3\2\2\2J"+
		"\u0223\3\2\2\2L\u0225\3\2\2\2N\u0227\3\2\2\2PR\5\4\3\2QP\3\2\2\2RU\3\2"+
		"\2\2SQ\3\2\2\2ST\3\2\2\2T\3\3\2\2\2US\3\2\2\2VZ\5\n\6\2WZ\5\6\4\2XZ\5"+
		"\f\7\2YV\3\2\2\2YW\3\2\2\2YX\3\2\2\2Z\5\3\2\2\2[]\5\b\5\2\\[\3\2\2\2]"+
		"`\3\2\2\2^\\\3\2\2\2^_\3\2\2\2_a\3\2\2\2`^\3\2\2\2ab\5\62\32\2bc\7\3\2"+
		"\2c\7\3\2\2\2de\7.\2\2ef\5\62\32\2f\t\3\2\2\2gh\5\b\5\2hi\7\3\2\2i\13"+
		"\3\2\2\2jl\5\b\5\2kj\3\2\2\2lo\3\2\2\2mk\3\2\2\2mn\3\2\2\2np\3\2\2\2o"+
		"m\3\2\2\2pq\5\16\b\2qr\7\4\2\2rs\5\24\13\2st\7\3\2\2t\r\3\2\2\2uz\5\62"+
		"\32\2vw\7\5\2\2wy\5\62\32\2xv\3\2\2\2y|\3\2\2\2zx\3\2\2\2z{\3\2\2\2{\u0080"+
		"\3\2\2\2|z\3\2\2\2}\u0080\5\22\n\2~\u0080\5\20\t\2\177u\3\2\2\2\177}\3"+
		"\2\2\2\177~\3\2\2\2\u0080\17\3\2\2\2\u0081\u0082\5L\'\2\u0082\21\3\2\2"+
		"\2\u0083\u0084\5H%\2\u0084\u0085\7\63\2\2\u0085\u0086\5H%\2\u0086\23\3"+
		"\2\2\2\u0087\u008c\5\26\f\2\u0088\u0089\7\5\2\2\u0089\u008b\5\26\f\2\u008a"+
		"\u0088\3\2\2\2\u008b\u008e\3\2\2\2\u008c\u008a\3\2\2\2\u008c\u008d\3\2"+
		"\2\2\u008d\u0093\3\2\2\2\u008e\u008c\3\2\2\2\u008f\u0090\7\5\2\2\u0090"+
		"\u0092\5\30\r\2\u0091\u008f\3\2\2\2\u0092\u0095\3\2\2\2\u0093\u0091\3"+
		"\2\2\2\u0093\u0094\3\2\2\2\u0094\25\3\2\2\2\u0095\u0093\3\2\2\2\u0096"+
		"\u009b\5\62\32\2\u0097\u0098\7\6\2\2\u0098\u009b\5\62\32\2\u0099\u009b"+
		"\7\7\2\2\u009a\u0096\3\2\2\2\u009a\u0097\3\2\2\2\u009a\u0099\3\2\2\2\u009b"+
		"\27\3\2\2\2\u009c\u00a5\5\32\16\2\u009d\u00a5\5\34\17\2\u009e\u00a5\5"+
		"\36\20\2\u009f\u00a5\5 \21\2\u00a0\u00a5\5\"\22\2\u00a1\u00a5\5$\23\2"+
		"\u00a2\u00a5\5&\24\2\u00a3\u00a5\5(\25\2\u00a4\u009c\3\2\2\2\u00a4\u009d"+
		"\3\2\2\2\u00a4\u009e\3\2\2\2\u00a4\u009f\3\2\2\2\u00a4\u00a0\3\2\2\2\u00a4"+
		"\u00a1\3\2\2\2\u00a4\u00a2\3\2\2\2\u00a4\u00a3\3\2\2\2\u00a5\31\3\2\2"+
		"\2\u00a6\u00a7\5H%\2\u00a7\u00a8\7/\2\2\u00a8\u00a9\5*\26\2\u00a9\33\3"+
		"\2\2\2\u00aa\u00ab\5H%\2\u00ab\u00ac\7\60\2\2\u00ac\u00ad\5*\26\2\u00ad"+
		"\35\3\2\2\2\u00ae\u00af\5H%\2\u00af\u00b0\7\61\2\2\u00b0\u00b1\5*\26\2"+
		"\u00b1\37\3\2\2\2\u00b2\u00b3\5H%\2\u00b3\u00b4\7\62\2\2\u00b4\u00b5\5"+
		"*\26\2\u00b5!\3\2\2\2\u00b6\u00b7\5H%\2\u00b7\u00b8\7\63\2\2\u00b8\u00b9"+
		"\5*\26\2\u00b9#\3\2\2\2\u00ba\u00bb\5H%\2\u00bb\u00bc\7\66\2\2\u00bc\u00bd"+
		"\5*\26\2\u00bd%\3\2\2\2\u00be\u00bf\5H%\2\u00bf\u00c0\7\64\2\2\u00c0\u00c1"+
		"\5*\26\2\u00c1\'\3\2\2\2\u00c2\u00c3\5H%\2\u00c3\u00c4\7\65\2\2\u00c4"+
		"\u00c5\5*\26\2\u00c5)\3\2\2\2\u00c6\u00c7\b\26\1\2\u00c7\u00c8\7A\2\2"+
		"\u00c8\u00c9\5*\26\2\u00c9\u00ca\7B\2\2\u00ca\u00ec\3\2\2\2\u00cb\u00cc"+
		"\78\2\2\u00cc\u00ec\5*\26\25\u00cd\u00ce\79\2\2\u00ce\u00ec\5*\26\20\u00cf"+
		"\u00ec\5,\27\2\u00d0\u00ec\5.\30\2\u00d1\u00d2\7\"\2\2\u00d2\u00d3\7#"+
		"\2\2\u00d3\u00d4\7A\2\2\u00d4\u00d9\5*\26\2\u00d5\u00d6\7\5\2\2\u00d6"+
		"\u00d8\5*\26\2\u00d7\u00d5\3\2\2\2\u00d8\u00db\3\2\2\2\u00d9\u00d7\3\2"+
		"\2\2\u00d9\u00da\3\2\2\2\u00da\u00dc\3\2\2\2\u00db\u00d9\3\2\2\2\u00dc"+
		"\u00dd\7B\2\2\u00dd\u00ec\3\2\2\2\u00de\u00df\7#\2\2\u00df\u00e0\7A\2"+
		"\2\u00e0\u00e5\5*\26\2\u00e1\u00e2\7\5\2\2\u00e2\u00e4\5*\26\2\u00e3\u00e1"+
		"\3\2\2\2\u00e4\u00e7\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e5\u00e6\3\2\2\2\u00e6"+
		"\u00e8\3\2\2\2\u00e7\u00e5\3\2\2\2\u00e8\u00e9\7B\2\2\u00e9\u00ec\3\2"+
		"\2\2\u00ea\u00ec\5\64\33\2\u00eb\u00c6\3\2\2\2\u00eb\u00cb\3\2\2\2\u00eb"+
		"\u00cd\3\2\2\2\u00eb\u00cf\3\2\2\2\u00eb\u00d0\3\2\2\2\u00eb\u00d1\3\2"+
		"\2\2\u00eb\u00de\3\2\2\2\u00eb\u00ea\3\2\2\2\u00ec\u0119\3\2\2\2\u00ed"+
		"\u00ee\f\27\2\2\u00ee\u00ef\7=\2\2\u00ef\u0118\5*\26\30\u00f0\u00f1\f"+
		"\26\2\2\u00f1\u00f2\7>\2\2\u00f2\u0118\5*\26\27\u00f3\u00f4\f\24\2\2\u00f4"+
		"\u00f5\7\67\2\2\u00f5\u0118\5*\26\25\u00f6\u00f7\f\23\2\2\u00f7\u00f8"+
		"\7?\2\2\u00f8\u0118\5*\26\24\u00f9\u00fa\f\22\2\2\u00fa\u00fb\7@\2\2\u00fb"+
		"\u0118\5*\26\23\u00fc\u00fd\f\21\2\2\u00fd\u00fe\78\2\2\u00fe\u0118\5"+
		"*\26\22\u00ff\u0100\f\17\2\2\u0100\u0101\7\60\2\2\u0101\u0118\5*\26\20"+
		"\u0102\u0103\f\16\2\2\u0103\u0104\7\62\2\2\u0104\u0118\5*\26\17\u0105"+
		"\u0106\f\r\2\2\u0106\u0107\7/\2\2\u0107\u0118\5*\26\16\u0108\u0109\f\f"+
		"\2\2\u0109\u010a\7\61\2\2\u010a\u0118\5*\26\r\u010b\u010c\f\13\2\2\u010c"+
		"\u010d\7:\2\2\u010d\u0118\5*\26\f\u010e\u010f\f\n\2\2\u010f\u0110\7\66"+
		"\2\2\u0110\u0118\5*\26\13\u0111\u0112\f\t\2\2\u0112\u0113\7;\2\2\u0113"+
		"\u0118\5*\26\n\u0114\u0115\f\b\2\2\u0115\u0116\7<\2\2\u0116\u0118\5*\26"+
		"\t\u0117\u00ed\3\2\2\2\u0117\u00f0\3\2\2\2\u0117\u00f3\3\2\2\2\u0117\u00f6"+
		"\3\2\2\2\u0117\u00f9\3\2\2\2\u0117\u00fc\3\2\2\2\u0117\u00ff\3\2\2\2\u0117"+
		"\u0102\3\2\2\2\u0117\u0105\3\2\2\2\u0117\u0108\3\2\2\2\u0117\u010b\3\2"+
		"\2\2\u0117\u010e\3\2\2\2\u0117\u0111\3\2\2\2\u0117\u0114\3\2\2\2\u0118"+
		"\u011b\3\2\2\2\u0119\u0117\3\2\2\2\u0119\u011a\3\2\2\2\u011a+\3\2\2\2"+
		"\u011b\u0119\3\2\2\2\u011c\u011d\7\b\2\2\u011d\u0120\5*\26\2\u011e\u011f"+
		"\7\5\2\2\u011f\u0121\5\60\31\2\u0120\u011e\3\2\2\2\u0120\u0121\3\2\2\2"+
		"\u0121\u0122\3\2\2\2\u0122\u0123\7B\2\2\u0123\u0179\3\2\2\2\u0124\u0125"+
		"\7\t\2\2\u0125\u0128\5*\26\2\u0126\u0127\7\5\2\2\u0127\u0129\5\60\31\2"+
		"\u0128\u0126\3\2\2\2\u0128\u0129\3\2\2\2\u0129\u012a\3\2\2\2\u012a\u012b"+
		"\7B\2\2\u012b\u0179\3\2\2\2\u012c\u012d\7\n\2\2\u012d\u0130\5*\26\2\u012e"+
		"\u012f\7\5\2\2\u012f\u0131\5\60\31\2\u0130\u012e\3\2\2\2\u0130\u0131\3"+
		"\2\2\2\u0131\u0132\3\2\2\2\u0132\u0133\7B\2\2\u0133\u0179\3\2\2\2\u0134"+
		"\u0135\7\13\2\2\u0135\u0138\5*\26\2\u0136\u0137\7\5\2\2\u0137\u0139\5"+
		"\60\31\2\u0138\u0136\3\2\2\2\u0138\u0139\3\2\2\2\u0139\u013a\3\2\2\2\u013a"+
		"\u013b\7B\2\2\u013b\u0179\3\2\2\2\u013c\u013d\7\f\2\2\u013d\u013e\5*\26"+
		"\2\u013e\u013f\7B\2\2\u013f\u0179\3\2\2\2\u0140\u0141\7\r\2\2\u0141\u0142"+
		"\5*\26\2\u0142\u0143\7B\2\2\u0143\u0179\3\2\2\2\u0144\u0145\7\16\2\2\u0145"+
		"\u0148\5*\26\2\u0146\u0147\7\5\2\2\u0147\u0149\5\60\31\2\u0148\u0146\3"+
		"\2\2\2\u0148\u0149\3\2\2\2\u0149\u014a\3\2\2\2\u014a\u014b\7B\2\2\u014b"+
		"\u0179\3\2\2\2\u014c\u014d\7\17\2\2\u014d\u0150\5*\26\2\u014e\u014f\7"+
		"\5\2\2\u014f\u0151\5\60\31\2\u0150\u014e\3\2\2\2\u0150\u0151\3\2\2\2\u0151"+
		"\u0152\3\2\2\2\u0152\u0153\7B\2\2\u0153\u0179\3\2\2\2\u0154\u0155\7\20"+
		"\2\2\u0155\u0158\5*\26\2\u0156\u0157\7\5\2\2\u0157\u0159\5\60\31\2\u0158"+
		"\u0156\3\2\2\2\u0158\u0159\3\2\2\2\u0159\u015a\3\2\2\2\u015a\u015b\7B"+
		"\2\2\u015b\u0179\3\2\2\2\u015c\u015d\7\21\2\2\u015d\u015e\5*\26\2\u015e"+
		"\u015f\7B\2\2\u015f\u0179\3\2\2\2\u0160\u0161\7\22\2\2\u0161\u0162\5*"+
		"\26\2\u0162\u0163\7B\2\2\u0163\u0179\3\2\2\2\u0164\u0165\7\23\2\2\u0165"+
		"\u0166\5*\26\2\u0166\u0167\7B\2\2\u0167\u0179\3\2\2\2\u0168\u0169\7\24"+
		"\2\2\u0169\u016a\5*\26\2\u016a\u016b\7B\2\2\u016b\u0179\3\2\2\2\u016c"+
		"\u016d\7\25\2\2\u016d\u016e\5*\26\2\u016e\u016f\7B\2\2\u016f\u0179\3\2"+
		"\2\2\u0170\u0171\7\26\2\2\u0171\u0174\5*\26\2\u0172\u0173\7\5\2\2\u0173"+
		"\u0175\5\60\31\2\u0174\u0172\3\2\2\2\u0174\u0175\3\2\2\2\u0175\u0176\3"+
		"\2\2\2\u0176\u0177\7B\2\2\u0177\u0179\3\2\2\2\u0178\u011c\3\2\2\2\u0178"+
		"\u0124\3\2\2\2\u0178\u012c\3\2\2\2\u0178\u0134\3\2\2\2\u0178\u013c\3\2"+
		"\2\2\u0178\u0140\3\2\2\2\u0178\u0144\3\2\2\2\u0178\u014c\3\2\2\2\u0178"+
		"\u0154\3\2\2\2\u0178\u015c\3\2\2\2\u0178\u0160\3\2\2\2\u0178\u0164\3\2"+
		"\2\2\u0178\u0168\3\2\2\2\u0178\u016c\3\2\2\2\u0178\u0170\3\2\2\2\u0179"+
		"-\3\2\2\2\u017a\u017b\7\27\2\2\u017b\u017c\5*\26\2\u017c\u017d\7\5\2\2"+
		"\u017d\u017e\5*\26\2\u017e\u017f\7\5\2\2\u017f\u0180\5*\26\2\u0180\u0181"+
		"\7B\2\2\u0181\u01a5\3\2\2\2\u0182\u0183\7\30\2\2\u0183\u0184\5*\26\2\u0184"+
		"\u0185\7\5\2\2\u0185\u0186\5*\26\2\u0186\u0187\7B\2\2\u0187\u01a5\3\2"+
		"\2\2\u0188\u0189\7\31\2\2\u0189\u018a\5*\26\2\u018a\u018b\7\5\2\2\u018b"+
		"\u018c\5*\26\2\u018c\u018d\7B\2\2\u018d\u01a5\3\2\2\2\u018e\u018f\7\32"+
		"\2\2\u018f\u0190\5*\26\2\u0190\u0191\7\5\2\2\u0191\u0192\5*\26\2\u0192"+
		"\u0193\7B\2\2\u0193\u01a5\3\2\2\2\u0194\u0195\7\33\2\2\u0195\u0196\5*"+
		"\26\2\u0196\u0197\7\5\2\2\u0197\u0198\5*\26\2\u0198\u0199\7B\2\2\u0199"+
		"\u01a5\3\2\2\2\u019a\u019b\7\34\2\2\u019b\u019c\5*\26\2\u019c\u019d\7"+
		"B\2\2\u019d\u01a5\3\2\2\2\u019e\u019f\7\35\2\2\u019f\u01a0\5*\26\2\u01a0"+
		"\u01a1\7\5\2\2\u01a1\u01a2\5*\26\2\u01a2\u01a3\7B\2\2\u01a3\u01a5\3\2"+
		"\2\2\u01a4\u017a\3\2\2\2\u01a4\u0182\3\2\2\2\u01a4\u0188\3\2\2\2\u01a4"+
		"\u018e\3\2\2\2\u01a4\u0194\3\2\2\2\u01a4\u019a\3\2\2\2\u01a4\u019e\3\2"+
		"\2\2\u01a5/\3\2\2\2\u01a6\u01a7\7\60\2\2\u01a7\u01ac\7$\2\2\u01a8\u01a9"+
		"\7\5\2\2\u01a9\u01ab\7$\2\2\u01aa\u01a8\3\2\2\2\u01ab\u01ae\3\2\2\2\u01ac"+
		"\u01aa\3\2\2\2\u01ac\u01ad\3\2\2\2\u01ad\u01af\3\2\2\2\u01ae\u01ac\3\2"+
		"\2\2\u01af\u01b0\7/\2\2\u01b0\61\3\2\2\2\u01b1\u01bd\7#\2\2\u01b2\u01b3"+
		"\7A\2\2\u01b3\u01b8\5\64\33\2\u01b4\u01b5\7\5\2\2\u01b5\u01b7\5\64\33"+
		"\2\u01b6\u01b4\3\2\2\2\u01b7\u01ba\3\2\2\2\u01b8\u01b6\3\2\2\2\u01b8\u01b9"+
		"\3\2\2\2\u01b9\u01bb\3\2\2\2\u01ba\u01b8\3\2\2\2\u01bb\u01bc\7B\2\2\u01bc"+
		"\u01be\3\2\2\2\u01bd\u01b2\3\2\2\2\u01bd\u01be\3\2\2\2\u01be\63\3\2\2"+
		"\2\u01bf\u01c9\5<\37\2\u01c0\u01c9\5\66\34\2\u01c1\u01c9\58\35\2\u01c2"+
		"\u01c9\5:\36\2\u01c3\u01c9\5> \2\u01c4\u01c9\5F$\2\u01c5\u01c9\5D#\2\u01c6"+
		"\u01c9\5H%\2\u01c7\u01c9\5J&\2\u01c8\u01bf\3\2\2\2\u01c8\u01c0\3\2\2\2"+
		"\u01c8\u01c1\3\2\2\2\u01c8\u01c2\3\2\2\2\u01c8\u01c3\3\2\2\2\u01c8\u01c4"+
		"\3\2\2\2\u01c8\u01c5\3\2\2\2\u01c8\u01c6\3\2\2\2\u01c8\u01c7\3\2\2\2\u01c9"+
		"\65\3\2\2\2\u01ca\u01cb\7&\2\2\u01cb\67\3\2\2\2\u01cc\u01d0\7(\2\2\u01cd"+
		"\u01ce\78\2\2\u01ce\u01d0\7(\2\2\u01cf\u01cc\3\2\2\2\u01cf\u01cd\3\2\2"+
		"\2\u01d09\3\2\2\2\u01d1\u01d5\7)\2\2\u01d2\u01d3\78\2\2\u01d3\u01d5\7"+
		")\2\2\u01d4\u01d1\3\2\2\2\u01d4\u01d2\3\2\2\2\u01d5;\3\2\2\2\u01d6\u01d7"+
		"\5N(\2\u01d7=\3\2\2\2\u01d8\u01d9\7\'\2\2\u01d9?\3\2\2\2\u01da\u01de\7"+
		"(\2\2\u01db\u01dc\78\2\2\u01dc\u01de\7(\2\2\u01dd\u01da\3\2\2\2\u01dd"+
		"\u01db\3\2\2\2\u01deA\3\2\2\2\u01df\u01e3\7)\2\2\u01e0\u01e1\78\2\2\u01e1"+
		"\u01e3\7)\2\2\u01e2\u01df\3\2\2\2\u01e2\u01e0\3\2\2\2\u01e3C\3\2\2\2\u01e4"+
		"\u01e5\7\36\2\2\u01e5\u01f2\7\37\2\2\u01e6\u01e7\7\36\2\2\u01e7\u01ec"+
		"\5*\26\2\u01e8\u01e9\7\5\2\2\u01e9\u01eb\5*\26\2\u01ea\u01e8\3\2\2\2\u01eb"+
		"\u01ee\3\2\2\2\u01ec\u01ea\3\2\2\2\u01ec\u01ed\3\2\2\2\u01ed\u01ef\3\2"+
		"\2\2\u01ee\u01ec\3\2\2\2\u01ef\u01f0\7\37\2\2\u01f0\u01f2\3\2\2\2\u01f1"+
		"\u01e4\3\2\2\2\u01f1\u01e6\3\2\2\2\u01f2E\3\2\2\2\u01f3\u01f4\7 \2\2\u01f4"+
		"\u0220\7!\2\2\u01f5\u01f6\7 \2\2\u01f6\u01fb\7&\2\2\u01f7\u01f8\7\5\2"+
		"\2\u01f8\u01fa\7&\2\2\u01f9\u01f7\3\2\2\2\u01fa\u01fd\3\2\2\2\u01fb\u01f9"+
		"\3\2\2\2\u01fb\u01fc\3\2\2\2\u01fc\u01fe\3\2\2\2\u01fd\u01fb\3\2\2\2\u01fe"+
		"\u0220\7!\2\2\u01ff\u0200\7 \2\2\u0200\u0205\5@!\2\u0201\u0202\7\5\2\2"+
		"\u0202\u0204\5@!\2\u0203\u0201\3\2\2\2\u0204\u0207\3\2\2\2\u0205\u0203"+
		"\3\2\2\2\u0205\u0206\3\2\2\2\u0206\u0208\3\2\2\2\u0207\u0205\3\2\2\2\u0208"+
		"\u0209\7!\2\2\u0209\u0220\3\2\2\2\u020a\u020b\7 \2\2\u020b\u0210\5B\""+
		"\2\u020c\u020d\7\5\2\2\u020d\u020f\5B\"\2\u020e\u020c\3\2\2\2\u020f\u0212"+
		"\3\2\2\2\u0210\u020e\3\2\2\2\u0210\u0211\3\2\2\2\u0211\u0213\3\2\2\2\u0212"+
		"\u0210\3\2\2\2\u0213\u0214\7!\2\2\u0214\u0220\3\2\2\2\u0215\u0216\7 \2"+
		"\2\u0216\u021b\7\'\2\2\u0217\u0218\7\5\2\2\u0218\u021a\7\'\2\2\u0219\u0217"+
		"\3\2\2\2\u021a\u021d\3\2\2\2\u021b\u0219\3\2\2\2\u021b\u021c\3\2\2\2\u021c"+
		"\u021e\3\2\2\2\u021d\u021b\3\2\2\2\u021e\u0220\7!\2\2\u021f\u01f3\3\2"+
		"\2\2\u021f\u01f5\3\2\2\2\u021f\u01ff\3\2\2\2\u021f\u020a\3\2\2\2\u021f"+
		"\u0215\3\2\2\2\u0220G\3\2\2\2\u0221\u0222\7$\2\2\u0222I\3\2\2\2\u0223"+
		"\u0224\7%\2\2\u0224K\3\2\2\2\u0225\u0226\7+\2\2\u0226M\3\2\2\2\u0227\u0228"+
		"\t\2\2\2\u0228O\3\2\2\2*SY^mz\177\u008c\u0093\u009a\u00a4\u00d9\u00e5"+
		"\u00eb\u0117\u0119\u0120\u0128\u0130\u0138\u0148\u0150\u0158\u0174\u0178"+
		"\u01a4\u01ac\u01b8\u01bd\u01c8\u01cf\u01d4\u01dd\u01e2\u01ec\u01f1\u01fb"+
		"\u0205\u0210\u021b\u021f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}