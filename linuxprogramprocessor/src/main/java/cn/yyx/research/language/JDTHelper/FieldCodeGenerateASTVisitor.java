package cn.yyx.research.language.JDTHelper;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import cn.yyx.research.language.JDTManager.FirstOrderTask;
import cn.yyx.research.language.JDTManager.GCodeMetaInfo;
import cn.yyx.research.language.JDTManager.NodeTypeLibrary;
import cn.yyx.research.language.JDTManager.OperationType;

public class FieldCodeGenerateASTVisitor extends MyCodeGenerateASTVisitor {
	
	public FieldCodeGenerateASTVisitor(MyCodeGenerateASTVisitor mcgast) {
		super(mcgast);
	}
	
	@Override
	public boolean visit(LambdaExpression node) {
		if (ShouldExecute(node)) {

			RegistFirstNodeAfterDecreasingElement(node.getBody());
			RegistLastNodeBeforeIncreaseingElement(node.getBody());

			TrulyGenerateOneLine(node, GetNodeLevel(node), GetNodeHasContentHolder(node));
		}
		return super.visit(node);
	}

	@Override
	public void endVisit(ExpressionMethodReference node) {
		if (ShouldExecute(node)) {
			TrulyGenerateOneLine(node, GetNodeLevel(node), GetNodeHasContentHolder(node));
		}
	}

	@Override
	public boolean visit(CastExpression node) {
		if (ShouldExecute(node)) {
			TrulyGenerateOneLine(node, GetNodeLevel(node), GetNodeHasContentHolder(node));
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(Assignment node) {
		if (ShouldExecute(node)) {
			ASTNode expr = node.getLeftHandSide();
			if (GetNodeHasOccupiedOneLine(expr))
			{
				AddFirstOrderTask(new FirstOrderTask(expr, node.getRightHandSide(), node, false) {
					@Override
					public void run() {
						TrulyGenerateOneLine(node, GetNodeLevel(node), GetNodeHasContentHolder(node));
					}
				});
			}
			else
			{
				TrulyGenerateOneLine(node, GetNodeLevel(node), GetNodeHasContentHolder(node));
			}
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		// System.out.println("Node Type:"+node.getType());
		// System.out.println("Body:"+node.getAnonymousClassDeclaration());
		if (ShouldExecute(node)) {
			TrulyGenerateOneLine(node, GetNodeLevel(node), GetNodeHasContentHolder(node));
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(ConditionalExpression node) {
		if (ShouldExecute(node)) {
			TrulyGenerateOneLine(node, GetNodeLevel(node), GetNodeHasContentHolder(node));
		}
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ArrayAccess node) {
		// do nothing.
		return super.visit(node);
	};

	@Override
	public boolean visit(FieldAccess node) {
		// do nothing.
		if (ShouldExecute(node))
		{
			ASTNode expr = node.getExpression();
			if (GetNodeInMultipleLine(expr))
			{
				AddFirstOrderTask(new FirstOrderTask(expr, node.getName(), node, false) {
					@Override
					public void run() {
						TrulyGenerateOneLine(node, GetNodeLevel(node), false);
					}
				});
			}
			else
			{
				TrulyGenerateOneLine(node, GetNodeLevel(node), false);
			}
		}
		return super.visit(node);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(MethodInvocation node) {
		if (ShouldExecute(node)) {
			List<ASTNode> args = node.arguments();
			ASTNode postnode = null;
			boolean afterprerun = false;
			if (args.size() > 0) {
				postnode = args.get(0);
				RegistFirstNodeAfterDecreasingElement(args.get(0));
				RegistLastNodeBeforeIncreaseingElement(args.get(args.size() - 1));
			}
			else
			{
				afterprerun = true;
			}
			ASTNode expr = node.getExpression();
			if (expr != null)
			{
				AddFirstOrderTask(new FirstOrderTask(expr, postnode, node, afterprerun) {
					@Override
					public void run() {
						TrulyGenerateOneLine(node, GetNodeLevel(node), GetNodeHasContentHolder(node));
					}
				});
			}
			else
			{
				TrulyGenerateOneLine(node, GetNodeLevel(node), GetNodeHasContentHolder(node));
			}
		}
		return super.visit(node);
	}
	
	@Override
	public boolean visit(VariableDeclarationExpression node) {
		if (ShouldExecute(node)) {
			TrulyGenerateOneLine(node, GetNodeLevel(node), GetNodeHasContentHolder(node));
		}
		return super.visit(node);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean visit(InfixExpression node) {
		int nodelevel = GetNodeLevel(node);
		ASTNode left = node.getLeftOperand();
		ASTNode right = node.getRightOperand();
		if (ShouldExecute(node)) {
			AddFirstOrderTask(new FirstOrderTask(left, right, node, false) {
				@Override
				public void run() {
					TrulyGenerateOneLine(node, nodelevel, GetNodeHasContentHolder(node));
				}
			});
		}
		ASTNode pre = right;
		String operatorcode = node.getOperator().toString();
		List<ASTNode> extendops = node.extendedOperands();
		for (ASTNode op : extendops) {
			AddFirstOrderTask(new FirstOrderTask(pre, op, node, false) {
				@Override
				public void run() {
					boolean hasContentHolder = false;
					String postcode = GetNodeCode(getPost());
					if (GetNodeHasOccupiedOneLine(getPost()))
					{
						postcode = GCodeMetaInfo.ContentHolder;
						hasContentHolder = true;
					}
					String code = operatorcode + postcode;
					TrulyGenerateOneLine(code, OperationType.InfixExpression, NodeTypeLibrary.adjacent, nodelevel,
							hasContentHolder);
				}
			});
			pre = op;
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(InstanceofExpression node) {
		if (ShouldExecute(node)) {
			AddFirstOrderTask(new FirstOrderTask(node.getLeftOperand(), null, node, true) {
				@Override
				public void run() {
					TrulyGenerateOneLine(node, GetNodeLevel(node), GetNodeHasContentHolder(node));
				}
			});
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(PostfixExpression node) {
		if (ShouldExecute(node)) {
			AddFirstOrderTask(new FirstOrderTask(node.getOperand(), null, node, true) {
				@Override
				public void run() {
					if (GetNodeNeedAppendChildPreNodeType(node))
					{
						ASTNode expr = node.getOperand();
						TrulyGenerateOneLineWithAppendingPreType(node, expr, GetNodeLevel(node), GetNodeHasContentHolder(node));
					}
					else
					{
						TrulyGenerateOneLine(node, GetNodeLevel(node), GetNodeHasContentHolder(node));
					}
				}
			});
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(PrefixExpression node) {
		if (ShouldExecute(node)) {
			TrulyGenerateOneLine(node, GetNodeLevel(node), GetNodeHasContentHolder(node));
		}
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SingleVariableDeclaration node) {
		if (ShouldExecute(node)) {
			TrulyGenerateOneLine(node, GetNodeLevel(node), GetNodeHasContentHolder(node));
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(FieldDeclaration node) {
		if (ShouldExecute(node)) {
			TrulyGenerateOneLine(node, GetNodeLevel(node), GetNodeHasContentHolder(node));
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		if (ShouldExecute(node)) {
			TrulyGenerateOneLine(node, GetNodeLevel(node), GetNodeHasContentHolder(node));
		}
		return super.visit(node);
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		return false;
	}
	
	@Override
	public boolean visit(Initializer node) {
		return false;
	}
	
}