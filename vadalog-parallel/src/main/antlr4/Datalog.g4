grammar Datalog;

// TEST: grun uk.co.prometheux.prometheuxreasoner.parser.Datalog clause -gui <path_to_test_file>

program  : ( clause )* ;

clause :  annotation | fact | rrule ;   

fact :  annotationBody* atom '.' ;

annotationBody : AT atom ;
annotation : annotationBody '.' ;

rrule :  annotationBody* head ':-'  body '.'  ;

head :  atom ( ',' atom )* | egdHead | falseHead ;

falseHead : falseTerm ;

egdHead : varTerm '=' varTerm;

body  : literal ( ',' literal )*  ( ',' condition )* ;

literal: atom          #PosLiteral   
         | 'not' atom  #NegLiteral
         | 'dom(*)'    #domStar
         ;

condition : 
	gtCondition
	| ltCondition
	| geCondition
	| leCondition
	| eqCondition
	| neqCondition 
	| inCondition
	| notInCondition ;

gtCondition : varTerm GT expression ;
ltCondition : varTerm LT expression ;
geCondition : varTerm GE expression ;
leCondition : varTerm LE expression ;
eqCondition : varTerm EQ expression ;
neqCondition : varTerm NEQ expression ;
inCondition : varTerm IN expression ;
notInCondition : varTerm NOTIN expression ;

expression : 
			LBR expression RBR # precExpression
			| expression PROD expression # prodExpression
			| expression DIV expression # divExpression
			| MINUS expression # unaryMinusExpression
			| expression PLUS expression # plusExpression
			| expression UNION expression #unionExpression
			| expression INTERSECTION expression #intersectionExpression
			| expression MINUS expression # minusExpression 
			| NOT expression # notExpression
			| expression LT expression # ltExpression
			| expression LE expression # leExpression
			| expression GT expression # gtExpression
			| expression GE expression # geExpression
			| expression EQEQ expression # eqEqExpression
			| expression NEQ expression # neqExpression
			| expression AND expression # andExpression
			| expression OR expression # orExpression
			| aggregation # aggrExpression
			| stringOperators # stringOperatorsExpression
			| SKOLEM_SYMBOL ID ('(' expression ( ',' expression )* ')') # skolemExpression
			| ID ('(' expression ( ',' expression )* ')') # externalExpression
			| term # termExpression ;
			
aggregation : 
			'msum(' expression ( ',' varList )? ')'  # msumAggExpression
			| 'mprod(' expression ( ',' varList )? ')' # mprodAggExpression
			| 'mcount(' expression ( ',' varList )? ')' # mcountAggExpression
			| 'munion(' expression ( ',' varList )? ')' # munionAggExpression
			| 'mmax(' expression ')' # mmaxAggExpression
			| 'mmin(' expression ')' # mminAggExpression
			| 'union(' expression ( ',' varList )? ')' # unionAggExpression
			| 'list(' expression ( ',' varList )? ')' # listAggExpression
			| 'set(' expression ( ',' varList )? ')' # setAggExpression
			| 'min(' expression ')' # minAggExpression
			| 'max(' expression ')' # maxAggExpression
			| 'sum(' expression ')' # sumAggExpression
			| 'prod(' expression ')' # prodAggExpression
			| 'avg(' expression ')' # avgAggExpression
			| 'count(' expression ( ',' varList )? ')' # countAggExpression ;
			
stringOperators :
			'substring(' expression ',' expression ',' expression ')' # substringExpression
			| 'contains(' expression ',' expression ')' #containsExpression
			| 'starts_with(' expression ',' expression ')' #startsWithExpression
			| 'ends_with(' expression ',' expression ')' #endsWithExpression
			| 'concat(' expression ',' expression ')' #concatExpression
			| 'string_length(' expression ')' #stringLengthExpression
			| 'index_of(' expression ',' expression ')' #indexOfExpression;

varList : '<' VAR ( ',' VAR )* '>' ;

SKOLEM_SYMBOL : '#' ;

atom : ID ('(' term ( ',' term )* ')')?;

term : booleanConstTerm | stringConstTerm | integerConstTerm | doubleConstTerm | dateConstTerm | setConstTerm | listTerm | varTerm | anonTerm;

stringConstTerm : STRING;
integerConstTerm : INTEGER | MINUS INTEGER;
doubleConstTerm : DOUBLE | MINUS DOUBLE;
booleanConstTerm : booleanTerm;
dateConstTerm : DATE;

integerSetTerm : INTEGER | MINUS INTEGER;
doubleSetTerm : DOUBLE | MINUS DOUBLE;

listTerm :
        '[' ']' #list
    |   '[' expression (',' expression)* ']' #list
;

setConstTerm :
	    '{' '}' #emptySet
	|   '{' STRING ( ',' STRING )* '}' #stringSet
	|   '{' integerSetTerm ( ',' integerSetTerm )* '}' #integerSet
	|   '{' doubleSetTerm ( ',' doubleSetTerm )* '}' #doubleSet
	|   '{' DATE ( ',' DATE )* '}' #dateSet;
	
varTerm : VAR;
anonTerm : ANON_VAR;

falseTerm : FALSE;

booleanTerm : TRUE | FALSE ;


ID   : ('a'..'z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_'|'<'|'>'|':')* ;

VAR : ('A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')* ;

ANON_VAR : ('_') ;

STRING : '"' (~'"' | '""')* '"' ;

DATE : ('0'..'9')('0'..'9')('0'..'9')('0'..'9') '-' ('0'..'9')('0'..'9') '-' ('0'..'9')('0'..'9') (' ' ('0'..'9')('0'..'9') ':' ('0'..'9')('0'..'9') ':' ('0'..'9')('0'..'9'))?;

INTEGER : ('0'..'9')+ ;

DOUBLE : ('0'..'9')+'.'('0'..'9')+ ;

TRUE : '#T' ; 

FALSE : '#F' ; 

WS : (' '|'\t'|'\r'|'\n') -> skip ;

LINE_COMMENT : '%' .*? '\r'? '\n' -> skip ; 

AT : '@' ;

// operators for conditions
GT : '>' ;
LT : '<' ;
GE : '>=' ;
LE : '<=' ;
EQ : '=' ;
IN : ' in ' ;
NOTIN : WS+ '!in' ;
NEQ : '!=' | '<>' ;
PLUS : '+' ;
MINUS : '-' ;
NOT : '!';
EQEQ : '==';
AND : '&&';
OR : '||';
PROD : '*' ;
DIV : '/' ;
UNION : '|';
INTERSECTION : '&' ;
LBR : '(' ;
RBR : ')' ;