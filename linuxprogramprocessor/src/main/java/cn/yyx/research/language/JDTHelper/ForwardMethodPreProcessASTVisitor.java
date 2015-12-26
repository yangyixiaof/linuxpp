package cn.yyx.research.language.JDTHelper;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.CreationReference;
import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.IntersectionType;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodReference;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeMethodReference;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import cn.yyx.research.language.JDTManager.GCodeMetaInfo;
import cn.yyx.research.language.JDTManager.NodeTypeLibrary;
import cn.yyx.research.language.JDTManager.ReferenceHint;
import cn.yyx.research.language.JDTManager.ReferenceHintLibrary;

public class ForwardMethodPreProcessASTVisitor extends MyPreProcessASTVisitor {
	
	// (later half part not solved.) method invocation not need to set parameters into the generated code but parameters need to have some prefix to indicate this is a parameter of a method.
	// not only method invocation, switch while if for such all need prefix. judgment, update, no-prefix block, and others all need to be prefixed.
	// = + such operations need to change to first order.
	// out of scope variables are thought as First declared.
	// offset still the assignment but method invoke of an object is taken as assignment.
	// record only assignments in a pool. reference offset is the offset to the index of assignment in the poll at reverse order.
	// first used element is also in this pool and taken as first declared or first assignment.
	// In pool, scope is taken into consideration including field. offset must be as this kind: -1/-2#. first part means -1 level, second part means -2 offset in assignment.
	// the offset must record which kind of sub pool it is being offset. sub pool include: final(constant), class(hidden, generated by me), var or obj, or others?.
	// AddEquivalentScope(node, node.get);
	
	// if nothing found in data pool, should return first declared. Solved.
	// check the call of DataNewlyUsed function and the call of AddDataReferenceHint.
	
	// set node type map in NodeCodeManager. Solved.
	// set node used map in NodeCodeManager. Solved.
	
	// if node used, the itself and the children of it should be all not visited in MyCodeGenerateASTVisitor. Solved.
	
	// in MyCodeGenerateASTVisitor some in visit but some in endVisitor. Solved.
	
	// For one visit, there are two tasks: 
	// task 1. raw code. generated raw node code which is stored in NodeCodeManager.
	// task 2. operation & line type. generate codes which have OperationType and line type but not have line offset info.
	
	// For one visit, there are two hidden tasks:
	// 1. set node level and its child's node level indication.
	// 2. for code not put in this line, set HoleManager.
	
	// one line code could offer only one content holder.
	// type a is set, when in first order, the already handled has occupied a line.
	// type c(hole) is set, when the node has occupied a line.
	
	public ForwardMethodPreProcessASTVisitor() {
	}
	
	@Override
	public boolean visit(CreationReference node) {
		System.out.println("CreationReference:" + node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(Dimension node) {
		// []
		// System.out.println("Dimension:" + node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(IntersectionType node) {
		System.out.println("IntersectionType:" + node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(UnionType node) {
		System.out.println("UnionType:" + node);
		return super.visit(node);
	}	
	
	@Override
	public boolean visit(TypeDeclarationStatement node) {
		// Do not know what it is now.
		 System.out.println("TypeDeclarationStatement:"+node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SuperMethodReference node) {
		// TODO Auto-generated method stub
		 System.out.println("SuperMethodReference:"+node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TypeMethodReference node) {
		// TODO Auto-generated method stub
		 System.out.println("TypeMethodReference:"+node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodRef node) {
		// TODO Auto-generated method stub
		 System.out.println("MethodRef:"+node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodRefParameter node) {
		// TODO Auto-generated method stub
		 System.out.println("MethodRefParameter:"+node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TypeLiteral node) {
		AddNodeCode(node, node.toString());
		return false;
	}
	
	@Override
	public boolean visit(ImportDeclaration node) {
		return false;
	}
	
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		return false;
	}
	
	@Override
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		return false;
	}
	
	@Override
	public boolean visit(PackageDeclaration node) {
		return false;
	}
	
	@Override
	public boolean visit(SingleMemberAnnotation node) {
		return false;
	}
	
	@Override
	public boolean visit(NormalAnnotation node) {
		return false;
	}
	
	@Override
	public boolean visit(MarkerAnnotation node) {
		return false;
	}
	
	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		// System.out.println("AnonymousClassDeclaration Begin");
		// System.out.println(node);
		// System.out.println("AnonymousClassDeclaration End");
		EnterBlock(node);
		FieldPreProcessASTVisitor fppast = new FieldPreProcessASTVisitor(this);
		node.accept(fppast);
		return super.visit(node);
	}

	@Override
	public void endVisit(AnonymousClassDeclaration node) {
		ExitBlock();
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		// Do nothing now.
		// System.out.println("TypeDeclaration Begin");
		// System.out.println(node.hashCode());
		// System.out.println("TypeDeclaration End");
		// classstack.push(node.hashCode());
		// blockstack.push(node.hashCode());
		EnterBlock(node);
		FieldPreProcessASTVisitor fppast = new FieldPreProcessASTVisitor(this);
		node.accept(fppast);
		NoVisit(node.getName());
		return super.visit(node);
	}

	@Override
	public void endVisit(TypeDeclaration node) {
		ExitBlock();
	}
	
	@Override
	public boolean visit(CharacterLiteral node) {
		AddNodeCode(node, GCodeMetaInfo.CharHolder);
		return super.visit(node);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void endVisit(Block node) {
		List<ASTNode> statements = node.statements();
		if (statements.size() > 1)
		{
			AddNodeInMultipleLine(node, true);
		}
		if (statements != null && statements.size() > 0)
		{
			DirectLinkCode(node, statements.get(0));
		}
		super.endVisit(node);
	}
	
	@Override
	public boolean visit(VariableDeclarationFragment node)
	{
		Integer hint = GetReferenceUpdateHint(node);
		if (hint != ReferenceHintLibrary.NoHint)
		{
			AddReferenceUpdateHint(node.getName(), hint);
			Expression iniliazer = node.getInitializer();
			if (iniliazer != null)
			{
				AddReferenceUpdateHint(iniliazer, ReferenceHintLibrary.DataUse);
			}
		}
		return super.visit(node);
	}
	
	@Override
	public void endVisit(VariableDeclarationFragment node)
	{
		Integer hint = GetReferenceUpdateHint(node);
		if (hint != ReferenceHintLibrary.NoHint)
		{
			DeleteReferenceUpdateHint(node.getName());
			Expression iniliazer = node.getInitializer();
			if (iniliazer != null)
			{
				DeleteReferenceUpdateHint(iniliazer);
			}
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(LambdaExpression node) {
		// System.out.println("LambdaExpressionBegin:");
		// System.out.println("LambdaExpression:"+node);
		// System.out.println("LambdaExpressionHasParenthese:"+node.hasParentheses());
		// System.out.println("LambdaExpressionParameters:"+node.parameters());
		// System.out.println("LambdaExpressionBody:"+node.getBody());
		// System.out.println("LambdaExpressionEnd.");
		List<ASTNode> params = node.parameters();
		if (params!=null && params.size() > 0)
		{
			Iterator<ASTNode> itr = params.iterator();
			while (itr.hasNext())
			{
				ASTNode para = itr.next();
				AddNodeHasUsed(para, true);
				String str = para.toString();
				String[] decs = str.split(" ");
				if (decs.length == 1)
				{
					SetVeryRecentDeclaredType(GCodeMetaInfo.NoDeclaredType);
				}
				AddReferenceUpdateHint(para, ReferenceHintLibrary.DataDeclare);
			}
		}
		return super.visit(node);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void endVisit(LambdaExpression node) {
		String pre = "";
		String post = "";
		if (node.hasParentheses())
		{
			pre = "(";
			post = ")";
		}
		StringBuilder cnt = new StringBuilder("");
		List<ASTNode> params = node.parameters();
		if (params!=null && params.size() > 0)
		{
			Iterator<ASTNode> itr = params.iterator();
			while (itr.hasNext())
			{
				ASTNode para = itr.next();
				String str = para.toString();
				String[] decs = str.split(" ");
				if (decs.length == 1)
				{
					SetVeryRecentDeclaredType(null);
					cnt.append(GCodeMetaInfo.NoDeclaredType).append(",");
				}
				else if (decs.length == 2)
				{
					cnt.append(decs[0]).append(",");
				}
				else
				{
					System.err.println("What Lambda Param? Serious error: over two modules in a param. The program will exit.");
					System.exit(1);
				}
				
				DeleteReferenceUpdateHint(para);
			}
			cnt.deleteCharAt(cnt.length()-1);
		}
		String code = pre + cnt.toString() + post + "->";
		ASTNode body = node.getBody();
		if (!GetNodeHasOccupiedOneLine(body))
		{
			code += GetNodeCode(body);
			AddNodeHasUsed(body, true);
		}
		else
		{
			if (GetNodeInMultipleLine(body))
			{
				AddNodeInMultipleLine(node, true);
				AddNodeType(body, NodeTypeLibrary.adjacent);
			}
			else
			{
				code = PushBackContentHolder(code, node);
			}
		}
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
	}
	
	@Override
	public boolean visit(ExpressionMethodReference node) {
		// System.out.println("ExpressionMethodReference:"+node);
		// System.out.println("ExpressionMethodReferenceExpr:"+);
		// System.out.println("ExpressionMethodReferenceName:"+node.getName());
		/*AddFirstOrderTask(new FirstOrderTask(node.getExpression(), node.getName(), node, true) {
			@Override
			public void run() {
				int line = VisitLineOccupy(node);
				String exprcode = LineOccupied(node.getExpression()) ? "" : GetRefCode(node.getExpression(), line);
				String code = OperationType.ExpressionMethodReference + "#" + this.getPost().toString() + "#" + exprcode;
				EndVisitReplaceLineOccupyWithRealContent(line, node, code);
			}
		});*/
		AddNodeType(node, NodeTypeLibrary.adjacent);
		AddNodeCode(node, "::"+node.getName());
		NoVisit(node.getName());
		AddNodeHasOccupiedOneLine(node, true);
		return super.visit(node);
	}
	
	// CLASS offset is special, relate to code offset. Solved.
	// Only 'assignment' records the line position of one variable. Solved.

	// Initializers in for is null. Solved.
	// Array initializer is null and there are many other nulls. Solved.
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(ArrayInitializer node) {
		// System.out.println("ArrayInitializer:"+node);
		List<Expression> inilist = node.expressions();
		for (Expression ini : inilist)
		{
			AddReferenceUpdateHint(ini, ReferenceHintLibrary.DataUse);
			// System.out.println("ArrayCreation's Initializer:" + ini);
		}
		AddNodeCode(node, GCodeMetaInfo.ArrayInitial);
		return super.visit(node);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void endVisit(ArrayInitializer node) {
		List<Expression> inilist = node.expressions();
		for (Expression ini : inilist)
		{
			DeleteReferenceUpdateHint(ini);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(ArrayCreation node) {
		// System.out.println("ArrayCreation:"+node);
		List<Expression> dimenlist = node.dimensions();
		for (Expression dimen : dimenlist)
		{
			AddReferenceUpdateHint(dimen, ReferenceHintLibrary.DataUse);
			// System.out.println("ArrayCreation's Dimension:" + dimen);
		}
		AddNodeCode(node, GCodeMetaInfo.ArrayCreation);
		return super.visit(node);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void endVisit(ArrayCreation node) {
		List<Expression> dimenlist = node.dimensions();
		for (Expression dimen : dimenlist)
		{
			DeleteReferenceUpdateHint(dimen);
		}
	}
	
	@Override
	public boolean visit(CastExpression node) {
		ASTNode expr = node.getExpression();
		AddReferenceUpdateHint(expr, ReferenceHintLibrary.DataUse);
		return super.visit(node);
	}
	
	@Override
	public void endVisit(CastExpression node) {
		String code = "(" + GetNodeCode(node.getType()) + ")";
		ASTNode expr = node.getExpression();
		if (!GetNodeHasOccupiedOneLine(expr))
		{
			code += GetNodeCode(expr);
			AddNodeHasUsed(expr, true);
		}
		else
		{
			if (GetNodeInMultipleLine(expr))
			{
				code += GCodeMetaInfo.CodeHole;
				AddNodeInMultipleLine(node, true);
			}
			else
			{
				code = PushBackContentHolder(code, node);
			}
		}
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
		
		DeleteReferenceUpdateHint(expr);
	}
	
	@Override
	public boolean visit(ArrayAccess node) {
		AddReferenceUpdateHint(node.getArray(), ReferenceHintLibrary.DataUpdate);
		AddReferenceUpdateHint(node.getIndex(), ReferenceHintLibrary.DataUse);
		return super.visit(node);
	};
	
	@Override
	public void endVisit(ArrayAccess node) {
		String code = GetNodeCode(node.getArray());
		ASTNode idx = node.getIndex();
		if (GetNodeHasOccupiedOneLine(idx))
		{
			code+="[" + GCodeMetaInfo.CodeHole + "]";
			AddNodeType(idx, NodeTypeLibrary.comphole);
		}
		else
		{
			code+="[" + GetNodeCode(idx) + "]";
		}
		AddNodeCode(node, code);
		
		DeleteReferenceUpdateHint(node.getArray());
		DeleteReferenceUpdateHint(node.getIndex());
	}
	
	@Override
	public boolean visit(Assignment node) {
		ASTNode left = node.getLeftHandSide();
		ASTNode right = node.getRightHandSide();
		AddReferenceUpdateHint(left, ReferenceHintLibrary.DataUpdate);
		AddReferenceUpdateHint(right, ReferenceHintLibrary.DataUse);
		return super.visit(node);
	}
	
	@Override
	public void endVisit(Assignment node) {
		String code = "";
		ASTNode left = node.getLeftHandSide();
		ASTNode right = node.getRightHandSide();
		if (!GetNodeHasOccupiedOneLine(left))
		{
			code = GetNodeCode(left)+node.getOperator().toString();
			AddNodeHasUsed(left, true);
		}
		else
		{
			AddNodeType(node, NodeTypeLibrary.adjacent);
			code = node.getOperator().toString();
			AddNodeInMultipleLine(node, true);
		}
		if (!GetNodeHasOccupiedOneLine(right))
		{
			code += GetNodeCode(right);
		}
		else
		{
			code = PushBackContentHolder(code, node);
			AddNodeInMultipleLine(node, true);
		}
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
		
		DeleteReferenceUpdateHint(left);
	}

	@Override
	public void endVisit(BooleanLiteral node) {
		// System.out.println("BooleanLiteral:"+node);
		AddNodeCode(node, node.toString());
	}
	
	@Override
	public void endVisit(BreakStatement node) {
		// System.out.println("BreakStatement:"+node);
		// System.out.println(node.getLabel());
		ASTNode label = node.getLabel();
		String labelcode = "";
		if (label != null)
		{
			String labelstr = label.toString();
			labelcode = GetLabelOffset(labelstr);
			getLjcs().PushNewlyAssignedData(labelstr, GCodeMetaInfo.HackedNoType);
		}
		String code = "break" + (label == null ? "" : GCodeMetaInfo.WhiteSpaceReplacer + labelcode);
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
	}
	
	@Override
	public void endVisit(ContinueStatement node) {
		ASTNode label = node.getLabel();
		String labelcode = "";
		if (label != null)
		{
			String labelstr = label.toString();
			labelcode = GetLabelOffset(labelstr);
			getLjcs().PushNewlyAssignedData(labelstr, GCodeMetaInfo.HackedNoType);
		}
		String code = "continue" + (label == null ? "" : GCodeMetaInfo.WhiteSpaceReplacer + labelcode);
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(ClassInstanceCreation node) {
		MethodInvocationAddHint(node.arguments());
		return super.visit(node);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void endVisit(ClassInstanceCreation node) {
		// System.out.println("Node Type:"+node.getType());
		// System.out.println("Body:"+node.getAnonymousClassDeclaration());
		// System.out.println("ClassInstanceCreation:"+node);
		StringBuilder code = new StringBuilder("new" + GCodeMetaInfo.WhiteSpaceReplacer);
		MethodInvocationCode(node.getType().toString(), node.arguments(), code);
		AddNodeCode(node, code.toString());
		AddNodeHasOccupiedOneLine(node, true);
		/*if (node.getAnonymousClassDeclaration() != null)
		{
			AddNodeInMultipleLine(node, true);
		}*/
		MethodInvocationDeleteHint(node.arguments());
	}
	
	@Override
	public void endVisit(ConditionalExpression node) {
		String thenexprcode = GCodeMetaInfo.CodeHole;
		ASTNode thenexpr = node.getThenExpression();
		if (!GetNodeHasOccupiedOneLine(thenexpr))
		{
			thenexprcode = GetNodeCode(thenexpr);
			AddNodeHasUsed(thenexpr, true);
		}
		else
		{
			AddNodeType(thenexpr, NodeTypeLibrary.comphole);
		}
		String elseexprcode = GCodeMetaInfo.CodeHole;
		ASTNode elseexpr = node.getElseExpression();
		if (!GetNodeHasOccupiedOneLine(elseexpr))
		{
			elseexprcode = GetNodeCode(elseexpr);
			AddNodeHasUsed(elseexpr, true);
		}
		else
		{
			AddNodeType(elseexpr, NodeTypeLibrary.comphole);
		}
		String code = "?"+thenexprcode+":"+elseexprcode;
		ASTNode judge = node.getExpression();
		if (!GetNodeInMultipleLine(judge))
		{
			code = GetNodeCode(judge)+code;
			AddNodeHasUsed(judge, true);
		}
		else
		{
			AddNodeType(node, NodeTypeLibrary.adjacent);
		}
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
		AddNodeInMultipleLine(node, true);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(ConstructorInvocation node) {
		MethodInvocationAddHint(node.arguments());
		return super.visit(node);
	}
		
	@Override
	@SuppressWarnings("unchecked")
	public void endVisit(ConstructorInvocation node) {
		// Do nothing now.
		// System.out.println("ConstructorInvocation:" + node);
		StringBuilder code = new StringBuilder("");
		boolean isInOneLine = MethodInvocationCode("this", node.arguments(), code);
		AddNodeCode(node, code.toString());
		AddNodeHasOccupiedOneLine(node, true);
		if (!isInOneLine)
		{
			AddNodeInMultipleLine(node, true);
		}
		MethodInvocationDeleteHint(node.arguments());
	}

	@Override
	public boolean visit(DoStatement node) {
		// System.out.println("Do statement begin:");
		// System.out.println("DoStatement:"+node);
		// System.out.println("Do statement end.");
		
		// Remember to add "do" in code line.
		return super.visit(node);
	}

	@Override
	public void endVisit(DoStatement node) {
		String exprcode = "";
		ASTNode expr = node.getExpression();
		if (GetNodeInMultipleLine(expr))
		{
			exprcode = GCodeMetaInfo.ContentHolder;
			AddNodeType(expr, NodeTypeLibrary.adjacent);
			AddNodeInMultipleLine(node, true);
			AddNodeHasContentHolder(node, true);
		}
		else
		{
			if (GetNodeHasOccupiedOneLine(expr))
			{
				exprcode = GCodeMetaInfo.ContentHolder;
				AddNodeHasContentHolder(node, true);
			}
			else
			{
				exprcode = GetNodeCode(expr);
				AddNodeHasUsed(expr, true);
			}
		}
		String code = "while"+ GCodeMetaInfo.WhiteSpaceReplacer + exprcode;
		// this expr needs to register level.
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
	}
	
	@Override
	public boolean visit(EnhancedForStatement node) {
		// System.out.println("EnhancedForStatementParameter:"+node.getParameter());
		// System.out.println("EnhancedForStatementExpr:"+node.getExpression());
		// System.out.println("EnhancedForStatementBody:"+node.getBody());
		ASTNode expr = node.getExpression();
		AddReferenceUpdateHint(expr, ReferenceHintLibrary.DataUse);
		return super.visit(node);
	}
	
	@Override
	public void endVisit(EnhancedForStatement node) {
		String exprcode = GCodeMetaInfo.CodeHole;
		ASTNode expr = node.getExpression();
		if (!GetNodeInMultipleLine(expr))
		{
			exprcode = GetNodeCode(expr);
			AddNodeHasUsed(expr, true);
		}
		else
		{
			AddNodeInMultipleLine(node, true);
			AddNodeType(expr, NodeTypeLibrary.comphole);
		}
		String code = "for" + GCodeMetaInfo.WhiteSpaceReplacer + node.getParameter().getType().toString() + ":" + exprcode;
		AddNodeHasUsed(node.getParameter(), true);
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
	}

	@Override
	public void endVisit(ExpressionStatement node) {
		// This method is too high level, almost useless.
		// System.out.println("ExpressionStatement:"+node);
		DirectLinkCode(node, node.getExpression());
	}

	@Override
	public boolean visit(FieldAccess node) {
		// System.out.println("FieldAccess:"+node);
		// System.out.println("FieldAccessName:"+node.getName());
		// System.out.println("FieldAccessExpr:"+node.getExpression());
		ASTNode expr = node.getExpression();
		if (expr != null)
		{
			String exprstr = expr.toString();
			int overAllHint = GetReferenceUpdateHint(node);
			ReferenceHint rh = null;
			if (overAllHint != ReferenceHintLibrary.NoHint)
			{
				rh = ReferenceHintLibrary.ParseReferenceHint(overAllHint);
			}
			if (exprstr.equals("this"))
			{
				AddReferenceUpdateHint(node.getName(), rh != null ? ReferenceHintLibrary.Field | rh.getWayUse() : ReferenceHintLibrary.DataUpdate);
			}
			else
			{
				// System.err.println("This should never happen. node string is:" + node);
				// new Exception().printStackTrace();
				// System.exit(1);
				NoVisit(node.getName());
				// AddReferenceUpdateHint(node.getName(), rh != null ? rh.GetOverAllHint() : ReferenceHintLibrary.DataUpdate);
			}
		}
		else
		{
			System.err.println("This should never happen. node string is:" + node);
			new Exception().printStackTrace();
			System.exit(1);
		}
		return super.visit(node);
	}

	@Override
	public void endVisit(FieldAccess node) {
		ASTNode expr = node.getExpression();
		String exprstr = expr.toString();
		String code = "";
		if (exprstr.equals("this"))
		{
			code = GetNodeCode(node.getName());
		}
		else
		{
			if (GetNodeInMultipleLine(expr))
			{
				AddNodeType(node, NodeTypeLibrary.adjacent);
				code = "."+node.getName().toString();
				AddNodeInMultipleLine(node, true);
				AddNodeHasOccupiedOneLine(node, true);
			}
			else
			{
				AddNodeHasUsed(expr, true);
				code = GetNodeCode(expr)+"."+node.getName().toString();
				if (GetNodeHasOccupiedOneLine(expr))
				{
					// AddNodeType(node, NodeTypeLibrary.adjacent);
					// code = "."+node.getName().toString();
					AddNodeHasOccupiedOneLine(node, true);
				}
			}
		}
		AddNodeCode(node, code);
		
		DeleteReferenceUpdateHint(node.getName());
	}

	@Override
	public boolean visit(SuperFieldAccess node) {
		// System.out.println("SuperFieldAccess:"+node);
		NoVisit(node.getName());
		AddNodeCode(node, node.toString());
		return super.visit(node);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(SuperMethodInvocation node) {
		MethodInvocationAddHint(node.arguments());
		NoVisit(node.getName());
		return super.visit(node);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void endVisit(SuperMethodInvocation node) {
		StringBuilder code = new StringBuilder("");
		boolean isInOneLine = MethodInvocationCode(node.getName().toString(), node.arguments(), code);
		AddNodeCode(node, "super."+code.toString());
		AddNodeHasOccupiedOneLine(node, true);
		if (!isInOneLine)
		{
			AddNodeInMultipleLine(node, true);
		}
		MethodInvocationDeleteHint(node.arguments());
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(MethodInvocation node) {
		MethodInvocationAddHint(node.arguments());
		ASTNode expr = node.getExpression();
		if (expr != null)
		{
			AddReferenceUpdateHint(expr, ReferenceHintLibrary.DataUpdate);
		}
		NoVisit(node.getName());
		return super.visit(node);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void endVisit(MethodInvocation node) {
		StringBuilder code = new StringBuilder("");
		ASTNode expr = node.getExpression();
		String exprcode = "";
		if (expr != null)
		{
			if (GetNodeHasOccupiedOneLine(expr))
			{
				exprcode = ".";
				AddNodeType(node, NodeTypeLibrary.adjacent);
				AddNodeInMultipleLine(node, true);
			}
			else
			{
				exprcode = GetNodeCode(expr) + ".";
				AddNodeHasUsed(expr, true);
			}
		}
		boolean isInOneLine = MethodInvocationCode(node.getName().toString(), node.arguments(), code);
		AddNodeCode(node, exprcode + code.toString());
		AddNodeHasOccupiedOneLine(node, true);
		if (!isInOneLine)
		{
			AddNodeInMultipleLine(node, true);
		}
		
		if (expr != null)
		{
			DeleteReferenceUpdateHint(expr);
		}
		
		MethodInvocationDeleteHint(node.arguments());
	}
	
	@Override
	//@SuppressWarnings("unchecked")
	public boolean visit(ForStatement node) {
		/*List<ASTNode> inis = node.initializers();
		for (ASTNode ininode : inis)
		{
			AddReferenceUpdateHint(ininode, ReferenceHintLibrary.DataUpdate);
		}
		List<ASTNode> ups = node.updaters();
		for (ASTNode upnode : ups)
		{
			AddReferenceUpdateHint(upnode, ReferenceHintLibrary.DataUpdate);
		}*/
		return super.visit(node);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void endVisit(ForStatement node) {
		StringBuilder code = new StringBuilder("for");
		List<ASTNode> inis = node.initializers();
		List<ASTNode> ups = node.updaters();
		
		AddNodeCode(node, code.toString());
		AddNodeHasOccupiedOneLine(node, true);
		AddNodeInMultipleLine(node, true);
		
		for (ASTNode ininode : inis)
		{
			DeleteReferenceUpdateHint(ininode);
		}
		for (ASTNode upnode : ups)
		{
			DeleteReferenceUpdateHint(upnode);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(VariableDeclarationExpression node) {
		// System.out.println("VariableDeclarationExpression:" + node);
		// for (int i=0,j=0;...) 's int i=0,j=0
		SetVeryRecentDeclaredType(node.getType().toString());
		SetVeryRecentDeclaredFinal(node.modifiers());
		
		ReferenceHint rh = ReferenceHintLibrary.ParseReferenceHint(GetReferenceUpdateHint(node));
		VariableDeclarationReferenceHint(node.fragments(), rh != null ? rh.getDataType() | ReferenceHintLibrary.Declare : ReferenceHintLibrary.DataDeclare);
		return super.visit(node);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void endVisit(VariableDeclarationExpression node) {
		SetVeryRecentDeclaredType(null);
		SetVeryRecentDeclaredFinal(null);
		
		String type = node.getType().toString();
		List<VariableDeclarationFragment> fs = node.fragments();
		VariableDeclarationCode(node, fs, type);
	}
	
	@Override
	public boolean visit(IfStatement node) {
		// System.out.println("IfStatement:"+node);
		// System.out.println("IfStatementExpr:"+node.getExpression());
		// System.out.println("IfStatementThen:"+node.getThenStatement());
		// System.out.println("IfStatementElse:"+node.getThenStatement());
		AddReferenceUpdateHint(node.getExpression(), ReferenceHintLibrary.DataUse);
		
		return super.visit(node);
	}

	@Override
	public void endVisit(IfStatement node) {
		// blockstack.pop();
		StringBuilder code = new StringBuilder("if");
		ASTNode expr = node.getExpression();
		if (!GetNodeHasOccupiedOneLine(expr))
		{
			code.append(GCodeMetaInfo.CommonSplitter + GetNodeCode(expr));
			AddNodeHasUsed(expr, true);
		}
		
		AddNodeCode(node, code.toString());
		AddNodeHasOccupiedOneLine(node, true);
		AddNodeInMultipleLine(node, true);
		
		DeleteReferenceUpdateHint(node.getExpression());
		
		/*int line = GetOccupiedLine(node);
		String thencode = node.getThenStatement() == null ? GCodeMetaInfo.NoStatement
				: GetRefCode(node.getThenStatement(), line);
		String elsecode = node.getElseStatement() == null ? GCodeMetaInfo.NoStatement
				: GetRefCode(node.getElseStatement(), line);
		String code = OperationType.IfStatement + "#" + GetRefCode(node.getExpression(), line) + thencode + elsecode;*/
	}
	
	@SuppressWarnings("unchecked")
	public boolean visit(InfixExpression node) {
		
		// System.out.println("InfixExpression:" + node);
		
		ASTNode left = node.getLeftOperand();
		AddReferenceUpdateHint(left, ReferenceHintLibrary.DataUse);
		ASTNode right = node.getRightOperand();
		AddReferenceUpdateHint(right, ReferenceHintLibrary.DataUse);
		
		// System.out.println("left:" + left);
		// System.out.println("right:" + right);
		
		List<ASTNode> ops = node.extendedOperands();
		for (ASTNode op : ops)
		{
			// System.out.println("extend op:" + op);
			
			AddReferenceUpdateHint(op, ReferenceHintLibrary.DataUse);
		}
		return super.visit(node);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void endVisit(InfixExpression node) {
		boolean inMultipleLine = false;
		String leftcode = "";
		ASTNode left = node.getLeftOperand();
		if (!GetNodeHasOccupiedOneLine(left))
		{
			leftcode = GetNodeCode(left);
			AddNodeHasUsed(left, true);
		}
		else
		{
			inMultipleLine = true;
		}
		String rightcode = "";
		ASTNode right = node.getRightOperand();
		if (!GetNodeHasOccupiedOneLine(right))
		{
			rightcode = GetNodeCode(right);
			AddNodeHasUsed(right, true);
		}
		else
		{
			if (GetNodeInMultipleLine(right))
			{
				inMultipleLine = true;
			}
			rightcode = PushBackContentHolder(rightcode, node);
		}
		String code = leftcode + node.getOperator().toString() + rightcode;
		List<Expression> ops = node.extendedOperands();
		if (!inMultipleLine && ops.size() > 0)
		{
			inMultipleLine = true;
		}
		
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
		if (inMultipleLine)
		{
			AddNodeInMultipleLine(node, true);
		}
		
		DeleteReferenceUpdateHint(left);
		DeleteReferenceUpdateHint(right);
		for (ASTNode op : ops)
		{
			DeleteReferenceUpdateHint(op);
		}
		/*
		 * System.out.println("=========传说中的分割线==开始==========");
		 * System.out.println("InfixExpression:"+node);
		 * System.out.println(node.getOperator());
		 * System.out.println(node.getLeftOperand());
		 * System.out.println(node.getRightOperand());
		 * System.out.println("=========传说中的分割线==中间==========");
		 * List<Expression> opso = node.extendedOperands(); for (Expression op :
		 * opso) { System.out.println(op); }
		 * System.out.println("=========传说中的分割线==结束==========");
		 */
	}
	
	@Override
	public boolean visit(Initializer node) {
		// Do nothing now.
		// System.out.println("Initializer:"+node);
		if (isFirstLevelASTNode(node))
		{
			ResetDLM();
		}
		// PrintLevelInfo();
		return super.visit(node);
	}
	
	@Override
	public boolean visit(InstanceofExpression node) {
		// System.out.println("InstanceofExpression:"+node);
		// System.out.println("InstanceofExpressionLeft:"+node.getLeftOperand());
		// System.out.println("InstanceofExpressionRight:"+node.getRightOperand());
		ASTNode left = node.getLeftOperand();
		AddReferenceUpdateHint(left, ReferenceHintLibrary.DataUse);
		return super.visit(node);
	}
	
	@Override
	public void endVisit(InstanceofExpression node) {
		ASTNode typenode = node.getRightOperand();
		String typecode = GetNodeCode(typenode);
		ASTNode left = node.getLeftOperand();
		String leftcode = "";
		if (!GetNodeInMultipleLine(left))
		{
			leftcode = GetNodeCode(left);
			AddNodeHasUsed(left, true);
		}
		else
		{
			// AddNodeType(left, NodeTypeLibrary.comphole);
			AddNodeInMultipleLine(node, true);
		}
		String code = leftcode + GCodeMetaInfo.WhiteSpaceReplacer + "instanceof" + GCodeMetaInfo.WhiteSpaceReplacer + typecode;
		
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
		
		DeleteReferenceUpdateHint(left);
	}
	
	@Override
	public boolean visit(LabeledStatement node) {
		// System.out.println("LabeledStatement:"+node);
		ASTNode label = node.getLabel();
		String labelstr = label.toString();
		LabelNewlyAssigned(labelstr);
		
		AddNodeCode(node, labelstr);
		AddNodeHasOccupiedOneLine(node, true);
		
		AddReferenceUpdateHint(node.getLabel(), ReferenceHintLibrary.LabelDeclare);
		return super.visit(node);
	}
	
	@Override
	public void endVisit(LabeledStatement node) {
		DeleteReferenceUpdateHint(node.getLabel());
	}
	
	@Override
	public void endVisit(ParenthesizedExpression node) {
		// System.out.println("ParenthesizedExpression:"+node);
		// System.out.println("ParenthesizedExpression:"+node.getExpression());
		DirectLinkCode(node, node.getExpression());
	}
	
	@Override
	public boolean visit(NullLiteral node) {
		// System.out.println("NullLiteral:"+node);
		AddNodeCode(node, GCodeMetaInfo.NullLiteral);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(PostfixExpression node) {
		ASTNode expr = node.getOperand();
		AddReferenceUpdateHint(expr, ReferenceHintLibrary.DataUpdate);
		return super.visit(node);
	}
	
	@Override
	public void endVisit(PostfixExpression node) {
		// System.out.println("PostfixExpression:"+node);
		// System.out.println("PostfixExpressionOperator:"+node.getOperator());
		// System.out.println("PostfixExpressionOperand:"+node.getOperand());
		String exprcode = "";
		ASTNode expr = node.getOperand();
		
		if (GetNodeInMultipleLine(expr))
		{
			// exprcode = GCodeMetaInfo.CodeHole;
			// AddNodeType(expr, NodeTypeLibrary.comphole);
			AddNodeInMultipleLine(node, true);
		}
		else
		{
			if (GetNodeHasOccupiedOneLine(expr))
			{
				AddNodeNeedAppendChildPreNodeType(node, true);
			}
			AddNodeHasUsed(expr, true);
			exprcode = GetNodeCode(expr);
		}
		String code = exprcode + node.getOperator();
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
		
		DeleteReferenceUpdateHint(expr);
	}
	
	@Override
	public boolean visit(PrefixExpression node) {
		ASTNode expr = node.getOperand();
		AddReferenceUpdateHint(expr, ReferenceHintLibrary.DataUpdate);
		return super.visit(node);
	}
	
	@Override
	public void endVisit(PrefixExpression node) {
		// System.out.println("PrefixExpression:"+node);
		// System.out.println("PrefixExpressionOperator:"+node.getOperator());
		// System.out.println("PrefixExpressionOperand:"+node.getOperand());
		String exprcode = "";
		ASTNode expr = node.getOperand();
		
		if (GetNodeInMultipleLine(expr))
		{
			exprcode = GCodeMetaInfo.ContentHolder;
			AddNodeInMultipleLine(node, true);
			AddNodeHasContentHolder(node, true);
		}
		else
		{
			exprcode = GetNodeCode(expr);
			AddNodeHasUsed(expr, true);
		}
		String code = node.getOperator() + exprcode;
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
		
		DeleteReferenceUpdateHint(expr);
	}
	
	@Override
	public boolean visit(ReturnStatement node) {
		ASTNode expr = node.getExpression();
		if (expr != null)
		{
			AddReferenceUpdateHint(expr, ReferenceHintLibrary.DataUse);	
		}
		return super.visit(node);
	}
	
	@Override
	public void endVisit(ReturnStatement node) {
		ASTNode expr = node.getExpression();
		String exprcode = "";
		if (expr != null)
		{
			if (GetNodeInMultipleLine(expr))
			{
				exprcode = GCodeMetaInfo.ContentHolder;
				AddNodeHasContentHolder(node, true);
				AddNodeInMultipleLine(node, true);
			}
			else
			{
				if (GetNodeHasOccupiedOneLine(expr))
				{
					exprcode = GCodeMetaInfo.ContentHolder;
					AddNodeHasContentHolder(node, true);
				}
				else
				{
					exprcode = GetNodeCode(expr);
					AddNodeHasUsed(expr, true);
				}
			}
		}
		String code = "return" + GCodeMetaInfo.WhiteSpaceReplacer + exprcode;
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
		
		if (expr != null)
		{
			DeleteReferenceUpdateHint(expr);
		}
	}
	
	@Override
	public boolean visit(PrimitiveType node) {
		// System.out.println("PrimitiveType:" + node);
		AddNodeCode(node, node.toString());
		return false;
	}
	
	@Override
	public boolean visit(SimpleType node) {
		// System.out.println("SimpleType:" + node);
		TypeCode(node);
		return false;
	}
	
	@Override
	public boolean visit(QualifiedType node) {
		// System.out.println("QualifiedType:"+node);
		TypeCode(node);
		return false;
	}
	
	@Override
	public boolean visit(ArrayType node) {
		// System.out.println("ArrayType:"+node);
		TypeCode(node);
		return false;
	}
	
	@Override
	public boolean visit(ParameterizedType node) {
		// System.out.println("ParameterizedType:"+node);
		TypeCode(node);
		return false;
	}
	
	@Override
	public boolean visit(NameQualifiedType node) {
		// System.out.println("NameQualifiedType:"+node);
		TypeCode(node);
		return false;
	}
	
	private void TypeCode(Type node)
	{
		String type = node.toString();
		AddNodeCode(node, GetClassOffset(type));
		ClassNewlyAssigned(type);
	}
	
	@Override
	public boolean visit(StringLiteral node) {
		// System.out.println("StringLiteral:"+node);
		AddNodeCode(node, GCodeMetaInfo.StringHolder);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(NumberLiteral node) {
		// System.out.println("NumberLiteral:"+node);
		AddNodeCode(node, GCodeMetaInfo.NumberHolder);
		return super.visit(node);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(SuperConstructorInvocation node) {
		MethodInvocationAddHint(node.arguments());
		return super.visit(node);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void endVisit(SuperConstructorInvocation node) {
		StringBuilder code = new StringBuilder("");
		boolean isInOneLine = MethodInvocationCode("super", node.arguments(), code);
		AddNodeCode(node, code.toString());
		AddNodeHasOccupiedOneLine(node, true);
		if (!isInOneLine)
		{
			AddNodeInMultipleLine(node, true);
		}
		MethodInvocationDeleteHint(node.arguments());
	}
	
	@Override
	public boolean visit(SwitchStatement node) {
		// System.out.println("SwitchStatement:"+node);
		// System.out.println("SwitchStatementExpr:"+node.getExpression());
		// blockstack.push(node.hashCode());
		ASTNode expr = node.getExpression();
		AddReferenceUpdateHint(expr, ReferenceHintLibrary.DataUse);
		return super.visit(node);
	}
	
	@Override
	public void endVisit(SwitchStatement node) {
		ASTNode expr = node.getExpression();
		String exprcode = GetNodeCode(expr);
		if (GetNodeInMultipleLine(expr))
		{
			exprcode = GCodeMetaInfo.ContentHolder;
			AddNodeHasContentHolder(node, true);
		}
		String code = "switch" + GCodeMetaInfo.WhiteSpaceReplacer + exprcode;
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
		AddNodeInMultipleLine(node, true);
		
		DeleteReferenceUpdateHint(expr);
	}
	
	@Override
	public void endVisit(SwitchCase node) {
		ASTNode expr = node.getExpression();
		String code = "default";
		if (expr != null)
		{
			code = "case" + GCodeMetaInfo.WhiteSpaceReplacer;
			if (GetNodeInMultipleLine(expr))
			{
				code += GCodeMetaInfo.ContentHolder;
				AddNodeHasContentHolder(node, true);
			}
			else
			{
				code += GetNodeCode(expr);
			}
		}
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
		AddNodeInMultipleLine(node, true);
	}
	
	@Override
	public void endVisit(SynchronizedStatement node) {
		String exprcode = "";
		ASTNode expr = node.getExpression();
		if (GetNodeInMultipleLine(expr))
		{
			exprcode = GCodeMetaInfo.CodeHole;
			AddNodeType(expr, NodeTypeLibrary.comphole);
		}
		else
		{
			if (GetNodeHasOccupiedOneLine(expr))
			{
				exprcode = GCodeMetaInfo.ContentHolder;
				AddNodeHasContentHolder(node, true);
			}
			else
			{
				exprcode = GetNodeCode(expr);
				AddNodeHasUsed(expr, true);
			}
		}
		String code = "synchronized" + GCodeMetaInfo.WhiteSpaceReplacer + exprcode;
		
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
		AddNodeInMultipleLine(node, true);
	}
	
	@Override
	public boolean visit(ThisExpression node) {
		// System.out.println("ThisExpression:"+node);
		AddNodeCode(node, node.toString());
		return super.visit(node);
	}
	
	@Override
	public void endVisit(TryStatement node) {
		// System.out.println("TryStatement:"+node);
		// System.out.println("TryStatementBody:"+node.getBody());
		DirectLinkCode(node, node.getBody());
	}
	
	@Override
	public void endVisit(ThrowStatement node) {
		// System.out.println("ThrowStatement:"+node);
		ASTNode expr = node.getExpression();
		String exprcode = GetNodeCode(expr);
		if (GetNodeInMultipleLine(expr))
		{
			exprcode = GCodeMetaInfo.ContentHolder;
			AddNodeHasContentHolder(node, true);
			AddNodeInMultipleLine(node, true);
		}
		else
		{
			if (GetNodeHasOccupiedOneLine(expr))
			{
				exprcode = GCodeMetaInfo.ContentHolder;
				AddNodeHasContentHolder(node, true);
			}
			else
			{
				AddNodeHasUsed(expr, true);
			}
		}
		String code = "throw" + GCodeMetaInfo.WhiteSpaceReplacer + exprcode;
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
	}
	
	@Override
	public void endVisit(CatchClause node) {
		// System.out.println("CatchClause:"+node);
		ASTNode expr = node.getException();
		String exprcode = GetNodeCode(expr);
		if (GetNodeInMultipleLine(expr))
		{
			exprcode = GCodeMetaInfo.ContentHolder;
			AddNodeHasContentHolder(node, true);
			AddNodeInMultipleLine(node, true);
		}
		else
		{
			AddNodeHasUsed(expr, true);
		}
		String code = "catch" + GCodeMetaInfo.WhiteSpaceReplacer + exprcode;
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(SingleVariableDeclaration node) {
		// System.out.println("SingleVariableDeclaration:"+node);
		// System.out.println("SingleVariableDeclarationType:"+node.getType());
		SetVeryRecentDeclaredType(node.getType().toString());
		SetVeryRecentDeclaredFinal(node.modifiers());
		
		AddReferenceUpdateHint(node.getName(), ReferenceHintLibrary.DataDeclare);
		return super.visit(node);
	}
	
	@Override
	public void endVisit(SingleVariableDeclaration node) {
		SetVeryRecentDeclaredType(null);
		SetVeryRecentDeclaredFinal(null);
		
		String code = node.getType().toString();
		AddNodeCode(node, code);
		
		DeleteReferenceUpdateHint(node.getName());
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		// System.out.println("FieldDeclaration:"+node);
		// System.out.println("FieldDeclarationParentHashcode:"+node.getParent().hashCode());
		/*List<VariableDeclarationFragment> fs = node.fragments();
		for (VariableDeclarationFragment f : fs) {
			// System.out.println("FieldDeclarationFragmentName:"+f.getName());
			dlm.AddDataLineInfo(f.getName().toString(), GCodeMetaInfo.IsField, true, false);
		}*/
		// SetVeryRecentDeclaredType(node.getType().toString());
		// SetVeryRecentDeclaredFinal(node.modifiers());
		
		// VariableDeclarationReferenceHint(node.fragments(), ReferenceHintLibrary.FieldDeclare);
		return false;
	}
	
	@Override
	public void endVisit(FieldDeclaration node) {
		// SetVeryRecentDeclaredType(null);
		// SetVeryRecentDeclaredFinal(null);
		
		// String type = node.getType().toString();
		// List<VariableDeclarationFragment> fs = node.fragments();
		// VariableDeclarationCode(node, fs, type);
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		// System.out.println("MethodDeclarationParent:"+node.getParent().hashCode());
		// There is no need to add hint to MethodDeclaration.
		if (isFirstLevelASTNode(node))
		{
			ResetDLM();
		}
		ClearClassAndLabelInfo();
		NoVisit(node.getName());
		return super.visit(node);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void endVisit(MethodDeclaration node) {
		List<ASTNode> types = node.typeParameters();
		Iterator<ASTNode> itr = types.iterator();
		String code = node.getName().toString();
		StringBuilder typedec = new StringBuilder("");
		while (itr.hasNext())
		{
			String type = itr.next().toString();
			typedec.append(type);
			typedec.append(",");
		}
		if (types.size() > 0)
		{
			typedec.deleteCharAt(typedec.length()-1);
		}
		code += GCodeMetaInfo.WhiteSpaceReplacer + typedec.toString();
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
		AddNodeInMultipleLine(node, true);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(VariableDeclarationStatement node) {
		// System.out.println("VariableDeclarationStatement:"+node);
		// System.out.println("VariableDeclarationStatementType:"+node.getType());
		SetVeryRecentDeclaredType(node.getType().toString());
		SetVeryRecentDeclaredFinal(node.modifiers());
		
		ReferenceHint rh = ReferenceHintLibrary.ParseReferenceHint(GetReferenceUpdateHint(node));
		VariableDeclarationReferenceHint(node.fragments(), rh != null ? rh.getDataType() | ReferenceHintLibrary.Declare : ReferenceHintLibrary.DataDeclare);
		return super.visit(node);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void endVisit(VariableDeclarationStatement node) {
		SetVeryRecentDeclaredType(null);
		SetVeryRecentDeclaredFinal(null);
		
		String type = node.getType().toString();
		List<VariableDeclarationFragment> fs = node.fragments();
		VariableDeclarationCode(node, fs, type);
	}
	
	@Override
	public boolean visit(QualifiedName node) {
		//check
		if (GetReferenceUpdateHint(node) == null)
		{
			System.err.println("Wrong Situation. No Hint for QualifiedName.");
			System.exit(1);
		}
		
		// System.out.println("qualifier:"+node.getQualifier() + "; hint:" + GetReferenceUpdateHint(node));
		
		AddReferenceUpdateHint(node.getQualifier(), GetReferenceUpdateHint(node));
		SimpleName name = node.getName();
		NoVisit(name);
		return super.visit(node);
	}
	
	@Override
	public void endVisit(QualifiedName node) {
		SimpleName name = node.getName();
		Name qualifier = node.getQualifier();
		String qualifiercode = GetNodeCode(qualifier);
		AddNodeCode(node, qualifiercode + "." + name.toString());
		
		DeleteReferenceUpdateHint(qualifier);
	}
	
	@Override
	public boolean visit(WhileStatement node) {
		// System.out.println("WhileStatement:"+node);
		// System.out.println("WhileStatementExpr:"+node.getExpression());
		// System.out.println("WhileStatementBody:"+node.getBody());
		
		// this should register nlm.
		return super.visit(node);
	}
	
	@Override
	public void endVisit(WhileStatement node) {
		ASTNode expr = node.getExpression();
		String exprcode = GetNodeCode(expr);
		if (GetNodeInMultipleLine(expr) || GetNodeHasOccupiedOneLine(expr))
		{
			exprcode = GCodeMetaInfo.ContentHolder;
			AddNodeHasContentHolder(node, true);
		}
		String code = "while" + GCodeMetaInfo.WhiteSpaceReplacer + exprcode;
		AddNodeCode(node, code);
		AddNodeHasOccupiedOneLine(node, true);
		AddNodeInMultipleLine(node, true);
	}
	
	@Override
	public void endVisit(SimpleName node) {
		if (!ShouldVisit(node))
		{
			DeleteNoVisit(node);
			return;
		}
		boolean isfield = false;
		Integer hint = GetReferenceUpdateHint(node);
		if (hint != ReferenceHintLibrary.NoHint)
		{
			String code = null;
			boolean hasCorrespond = false;
			String data = node.toString();
			switch (hint) {
			case ReferenceHintLibrary.DataUse:
				/*if (data.equals("a"))
				{
					System.out.println("DataUse");
				}*/
				code = GetDataOffset(data, false, false);
				break;
			case ReferenceHintLibrary.FieldUse:
				/*if (data.equals("a"))
				{
					System.out.println("FieldUse");
				}*/
				isfield = true;
				code = GetDataOffset(data, true, false);
				break;
			case ReferenceHintLibrary.DataUpdate:
				/*if (data.equals("a"))
				{
					System.out.println("DataUpdate");
				}*/
				code = GetDataOffset(data, false, false);
				DataNewlyUsed(data, null, GetVeryRecentDeclaredFinal(), false, false, false, false);
				break;
			case ReferenceHintLibrary.FieldUpdate:
				/*if (data.equals("a"))
				{
					System.out.println("FieldUpdate");
				}*/
				isfield = true;
				code = GetDataOffset(data, true, false);
				DataNewlyUsed(data, null, GetVeryRecentDeclaredFinal(), false, false, true, false);
				break;
			case ReferenceHintLibrary.DataDeclare:
				/*if (data.equals("a"))
				{
					System.out.println("DataDeclare");
				}*/
				String declaredtype = GetVeryRecentDeclaredType();
				// System.out.println("common declaredtype:" + declaredtype + "; and data is:" + data + "; and is final:" + GetVeryRecentDeclaredFinal());
				CheckVeryRecentDeclaredTypeMustNotNull(declaredtype);
				// System.err.println("data is:" + data + " declaredtype:"+declaredtype);
				DataNewlyUsed(data, declaredtype, GetVeryRecentDeclaredFinal(), false, true, false, false);
				hasCorrespond = true;
				return;
			case ReferenceHintLibrary.FieldDeclare:
				/*if (data.equals("a"))
				{
					System.out.println("FieldDeclare");
				}*/
				isfield = true;
				String declaredtype2 = GetVeryRecentDeclaredType();
				CheckVeryRecentDeclaredTypeMustNotNull(declaredtype2);
				DataNewlyUsed(data, declaredtype2, GetVeryRecentDeclaredFinal(), true, false, false, false);
				hasCorrespond = true;
				return;
			default:
				break;
			}
			if (code != null)
			{
				AddNodeCode(node, code);
			}
			else
			{
				if (!hasCorrespond)
				{
					String nodestr = node.toString();
					String pre = (isfield ? "this." : "");
					AddNodeCode(node, pre + nodestr);
					if (Character.isLowerCase(nodestr.charAt(0))==true)
					{
						System.err.println("Debugging Data: " + node + "; No corresponding data offset. Maybe data use or others.");
					}
				}
			}
		}
		else
		{
			String nodestr = node.toString();
			AddNodeCode(node, nodestr);
			if (Character.isLowerCase(nodestr.charAt(0))==true)
			{
				System.err.println("Warning Data: " + node + "; just for debugging and testing. The simple name does not have hint.");
			}
		}
	}
	
}