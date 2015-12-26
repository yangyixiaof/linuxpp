package cn.yyx.research.language.JDTHelper;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import cn.yyx.research.language.JDTManager.DebugNodeCorrespondingCode;
import cn.yyx.research.language.JDTManager.FirstOrderTask;
import cn.yyx.research.language.JDTManager.FirstOrderTaskPool;
import cn.yyx.research.language.JDTManager.GCodeMetaInfo;
import cn.yyx.research.language.JDTManager.JCScope;
import cn.yyx.research.language.JDTManager.NoVisitNodeManager;
import cn.yyx.research.language.JDTManager.NodeCodeManager;
import cn.yyx.research.language.JDTManager.NodeTypeLibrary;
import cn.yyx.research.language.JDTManager.OffsetLibrary;
import cn.yyx.research.language.JDTManager.ReferenceHintLibrary;
import cn.yyx.research.language.JDTManager.ScopeDataManager;
import cn.yyx.research.language.JDTManager.VarOrObjReferenceManager;

public class MyPreProcessASTVisitor extends ASTVisitor{
	
	// private EnteredLambdaParamFlag lambdaparamstack = new EnteredLambdaParamFlag();
	// private NodeLineManager nlm = new NodeLineManager();
	// private LineManager lm = new LineManager();
	// private LineCodeManager lcm = new LineCodeManager();
	private NodeCodeManager ncm = new NodeCodeManager();
	private ScopeDataManager sdm = new ScopeDataManager();
	private FirstOrderTaskPool fotp = new FirstOrderTaskPool();
	private VarOrObjReferenceManager voorm = new VarOrObjReferenceManager();
	private NoVisitNodeManager nvnm = new NoVisitNodeManager();
	// private Map<Integer, ASTNode> nodelink = new TreeMap<Integer, ASTNode>();
	// a node is only equivalent to one node.
	// private Map<Integer, Integer> equivalentScope = new TreeMap<Integer, Integer>();
	private JCScope cjcs = new JCScope();
	private JCScope ljcs = new JCScope();
	
	private String VeryRecentDeclaredType = null;
	private boolean VeryRecentDeclaredFinal = false;
	
	public MyPreProcessASTVisitor(MyPreProcessASTVisitor mppast)
	{
		ncm = mppast.getNcm();
		sdm = mppast.getSdm();
		fotp = mppast.getFotp();
		voorm = mppast.getVoorm();
		nvnm = mppast.getNvnm();
		cjcs = mppast.getCjcs();
		ljcs = mppast.getLjcs();
		VeryRecentDeclaredFinal = mppast.GetVeryRecentDeclaredFinal();
		VeryRecentDeclaredType = mppast.GetVeryRecentDeclaredType();
	}
	
	public MyPreProcessASTVisitor() {
	}
	
	@Override
	public void postVisit(ASTNode node) {
		getFotp().PreIsOver(node);
		super.postVisit(node);
	}
	
	@Override
	public void preVisit(ASTNode node) {
		// debuging
		DebugNodeCorrespondingCode.AddIdNodePair(node);
		
		getFotp().PostIsBegin(node);
		super.preVisit(node);
	}
	
	@Override
	public boolean visit(ParenthesizedExpression node) {
		return super.visit(node);
	}
	
	@Override
	public void endVisit(ParenthesizedExpression node) {
		// System.out.println("ParenthesizedExpression:"+node);
		// System.out.println("ParenthesizedExpression:"+node.getExpression());
		// GiveLinkBetweenNodes(node, node.getExpression());
	}
	
	// If doesn't know the kind, just set one as random. The one must be the big kind you want.
	protected String GetDataOffset(String data, boolean isFieldUseOrUpdate, boolean isCommonUseOrUpdate) {
		String code = getSdm().GetDataOffsetInfo(data, isFieldUseOrUpdate, isCommonUseOrUpdate);
		return code;
	}
	
	protected void ClassNewlyAssigned(String type)
	{
		getCjcs().PushNewlyAssignedData(type, GCodeMetaInfo.HackedNoType);
	}
	
	protected String GetClassOffset(String type)
	{
		return "$K" + 0 + GCodeMetaInfo.OffsetSpiliter + OffsetLibrary.GetOffsetDescription(getCjcs().GetExactOffset(type, GCodeMetaInfo.HackedNoType));
	}
	
	protected void LabelNewlyAssigned(String label)
	{
		getLjcs().PushNewlyAssignedData(label, GCodeMetaInfo.HackedNoType);
	}
	
	protected String GetLabelOffset(String label)
	{
		return "$L" + 0 + GCodeMetaInfo.OffsetSpiliter + OffsetLibrary.GetOffsetDescription(getLjcs().GetExactOffset(label, GCodeMetaInfo.HackedNoType));
	}
	
	protected void ResetDLM() {
		getSdm().ResetCurrentClassField();
	}
	
	protected boolean isFirstLevelASTNode(ASTNode node) {
		int classhashcode = getSdm().GetFirstClassId();
		int parenthashcode = node.getParent().hashCode();
		if (parenthashcode == classhashcode) {
			return true;
		}
		return false;
	}
	
	protected void EnterBlock(ASTNode node) {
		// System.out.println("Hashcode:"+node.hashCode()+";node:"+node);
		getSdm().EnterBlock(node.hashCode());
	}
	
	protected void ExitBlock() {
		getSdm().ExitBlock();
	}
	
	protected boolean ContainsScope(Integer equid) {
		return getSdm().ContainsScope(equid);
	}
	
	/*protected void EnterLambdaParam(LambdaExpression lambda)
	{
		lambdaparamstack.push(lambda.hashCode());
	}
	
	protected void ExitLambdaParam(LambdaExpression lambda)
	{
		lambdaparamstack.pop();
	}*/
	
	/*protected void AddLinkBetweenNodes(ASTNode linkingnode, ASTNode nodetobelinked) {
		nodelink.put(linkingnode.hashCode(), nodetobelinked);
	}*/
	
	protected void AddFirstOrderTask(FirstOrderTask runtask)
	{
		fotp.InfixNodeAddFirstOrderTask(runtask);
	}

	public NodeCodeManager getNcm() {
		return ncm;
	}

	public void setNcm(NodeCodeManager ncm) {
		this.ncm = ncm;
	}

	public ScopeDataManager getSdm() {
		return sdm;
	}

	public void setSdm(ScopeDataManager sdm) {
		this.sdm = sdm;
	}
	
	protected void DataNewlyUsed(String data, String type, boolean isfianl, boolean isFieldDeclare, boolean isCommonDeclare, boolean isFieldUseOrDeclare, boolean isCommonUseOrDeclare)
	{
		sdm.AddDataNewlyUsed(data, type, isfianl, isFieldDeclare, isCommonDeclare, isFieldUseOrDeclare, isCommonUseOrDeclare);
	}
	
	protected void AddNodeCode(ASTNode node, String code)
	{
		ncm.AddASTNodeCode(node, code);
	}
	
	protected String GetNodeCode(ASTNode node)
	{
		return ncm.GetAstNodeCode(node);
	}
	
	protected void AddNodeHasOccupiedOneLine(ASTNode node, Boolean HasOccupiedOneLine)
	{
		ncm.AddASTNodeHasOccupiedOneLine(node, HasOccupiedOneLine);
	}
	
	protected Boolean GetNodeHasOccupiedOneLine(ASTNode node)
	{
		return ncm.GetAstNodeHasOccupiedOneLine(node);
	}
	
	protected void AddNodeHasContentHolder(ASTNode node, Boolean ifHasContentHolder)
	{
		ncm.AddASTNodeIfHasContentHolder(node, ifHasContentHolder);
	}
	
	protected boolean GetNodeHasContentHolder(ASTNode node)
	{
		return ncm.GetAstNodeHasContentHolder(node);
	}
	
	protected void AddNodeInMultipleLine(ASTNode node, Boolean inMultipleLine)
	{
		ncm.AddASTNodeInMultipleLine(node, inMultipleLine);
	}
	
	protected boolean GetNodeInMultipleLine(ASTNode node)
	{
		return ncm.GetAstNodeInMultipleLine(node);
	}
	
	public void AddNodeHasUsed(ASTNode node, boolean used)
	{
		ncm.AddNodeHasUsed(node, used);
	}
	
	public boolean GetNodeHasUsed(ASTNode node)
	{
		return ncm.GetNodeHasUsed(node);
	}
	
	public void AddNodeType(ASTNode node, char type)
	{
		ncm.AddNodeType(node, type);
	}
	
	public char GetNodeType(ASTNode node)
	{
		return ncm.GetNodeType(node);
	}
	
	protected String PushBackContentHolder(String code, ASTNode node)
	{
		AddNodeHasContentHolder(node, true);
		return code + GCodeMetaInfo.ContentHolder;
	}
	
	protected void AddReferenceUpdateHint(ASTNode node, Integer hint)
	{
		getVoorm().AddReferenceUpdateHint(node, hint);
	}
	
	protected void DeleteReferenceUpdateHint(ASTNode node)
	{
		getVoorm().DeleteReferenceUpdateHint(node);
	}
	
	protected Integer GetReferenceUpdateHint(ASTNode node)
	{
		return getVoorm().GetReferenceUpdateHint(node);
	}
	
	// v means virtual, r means real.
	protected void DirectLinkCode(ASTNode vnode, ASTNode rnode)
	{
		ncm.AddNodeLink(vnode, rnode);
		/*AddNodeCode(vnode, GetNodeCode(rnode));
		AddNodeHasUsed(rnode, true);
		if (GetNodeHasContentHolder(rnode))
		{
			AddNodeHasContentHolder(rnode, true);
		}
		if (GetNodeHasOccupiedOneLine(rnode))
		{
			AddNodeHasOccupiedOneLine(rnode, true);
		}
		if (GetNodeInMultipleLine(rnode))
		{
			AddNodeInMultipleLine(rnode, true);
		}*/
	}
	
	protected void MethodInvocationAddHint(List<ASTNode> args)
	{
		for (ASTNode arg : args) {
			AddReferenceUpdateHint(arg, ReferenceHintLibrary.DataUse);
		}
	}
	
	protected void MethodInvocationDeleteHint(List<ASTNode> args)
	{
		for (ASTNode arg : args) {
			DeleteReferenceUpdateHint(arg);
		}
	}
	
	protected Boolean MethodInvocationCode(String methodName, List<ASTNode> args, StringBuilder code)
	{
		// "new" + GCodeMetaInfo.WhiteSpaceReplacer + 
		code.append(methodName);
		String pre = "(";
		String post = ")";
		code.append(pre);
		boolean inOneLine = true;
		for (ASTNode arg : args) {
			if (GetNodeHasOccupiedOneLine(arg))
			{
				code.append(GCodeMetaInfo.CodeHole);
				AddNodeType(arg, NodeTypeLibrary.comphole);
				inOneLine = false;
			}
			else
			{
				code.append(GetNodeCode(arg));
				AddNodeHasUsed(arg, true);
			}
			code.append(",");
		}
		if (args.size() > 0)
		{
			code.deleteCharAt(code.length()-1);
		}
		code.append(post);
		return inOneLine;
	}
	
	protected void VariableDeclarationReferenceHint(List<VariableDeclarationFragment> fs, int overAllHint)
	{
		for (VariableDeclarationFragment vdf : fs) {
			AddReferenceUpdateHint(vdf.getName(), overAllHint);
			Expression iniliazer = vdf.getInitializer();
			if (iniliazer != null)
			{
				AddReferenceUpdateHint(iniliazer, ReferenceHintLibrary.DataUse);
			}
		}
	}
	
	private void VariableDeclarationReferenceHintDeletion(List<VariableDeclarationFragment> fs)
	{
		for (VariableDeclarationFragment vdf : fs) {
			DeleteReferenceUpdateHint(vdf.getName());
			Expression iniliazer = vdf.getInitializer();
			if (iniliazer != null)
			{
				DeleteReferenceUpdateHint(iniliazer);
			}
		}
	}
	
	protected void VariableDeclarationCode(ASTNode node, List<VariableDeclarationFragment> fs, String type)
	{
		StringBuilder code = new StringBuilder("");
		if (fs != null && fs.size() > 0)
		{
			for (VariableDeclarationFragment vdf : fs) {
				code.append(type);
				Expression iniexpr = vdf.getInitializer();
				if (iniexpr != null) {
					code.append("=");
					if (!GetNodeHasOccupiedOneLine(iniexpr))
					{
						code.append(GetNodeCode(iniexpr));
						AddNodeHasUsed(iniexpr, true);
					}
					else
					{
						if (GetNodeInMultipleLine(iniexpr) || fs.size() > 1)
						{
							code.append(GCodeMetaInfo.CodeHole);
							AddNodeType(iniexpr, NodeTypeLibrary.comphole);
						}
						else
						{
							code.append(GCodeMetaInfo.ContentHolder);
							AddNodeHasContentHolder(node, true);
						}
					}
				}
				code.append(",");
			}
			code.deleteCharAt(code.length()-1);
		}
		AddNodeCode(node, code.toString());
		
		VariableDeclarationReferenceHintDeletion(fs);
	}
	
	protected void NoVisit(ASTNode node)
	{
		getNvnm().AddNoVisitNode(node.hashCode());
	}
	
	protected boolean ShouldVisit(ASTNode node)
	{
		return getNvnm().NeedVisit(node.hashCode());
	}
	
	protected void DeleteNoVisit(ASTNode node)
	{
		getNvnm().DeleteNoVisit(node.hashCode());
	}

	public String GetVeryRecentDeclaredType() {
		return VeryRecentDeclaredType;
	}

	public void SetVeryRecentDeclaredType(String veryRecentDeclaredType) {
		VeryRecentDeclaredType = veryRecentDeclaredType;
	}

	public boolean GetVeryRecentDeclaredFinal() {
		return VeryRecentDeclaredFinal;
	}

	public void SetVeryRecentDeclaredFinal(List<ASTNode> modifiers) {
		VeryRecentDeclaredFinal = false;
		if (modifiers == null || modifiers.size() == 0)
		{
			return;
		}
		for (ASTNode modifier : modifiers)
		{
			if (modifier.toString().equals("this"))
			{
				VeryRecentDeclaredFinal = true;
				return;
			}
			// System.out.println("modifier:" + modifier);
		}
		// VeryRecentDeclaredFinal = veryRecentDeclaredFinal;
	}
	
	protected void CheckVeryRecentDeclaredTypeMustNotNull(String declaredtype)
	{
		if (declaredtype == null)
		{
			System.err.println("No Declared Type? The system will exit.");
			System.exit(1);
		}
	}
	
	protected void ClearClassAndLabelInfo()
	{
		getCjcs().ClearAll();
		getLjcs().ClearAll();
	}
	
	protected void AddNodeNeedAppendChildPreNodeType(ASTNode astnode, boolean ifneed)
	{
		ncm.AddNodeNeedAppendChildPreNodeType(astnode, ifneed);
	}

	public VarOrObjReferenceManager getVoorm() {
		return voorm;
	}

	public void setVoorm(VarOrObjReferenceManager voorm) {
		this.voorm = voorm;
	}

	public NoVisitNodeManager getNvnm() {
		return nvnm;
	}

	public void setNvnm(NoVisitNodeManager nvnm) {
		this.nvnm = nvnm;
	}

	public JCScope getCjcs() {
		return cjcs;
	}

	public void setCjcs(JCScope cjcs) {
		this.cjcs = cjcs;
	}

	public JCScope getLjcs() {
		return ljcs;
	}

	public void setLjcs(JCScope ljcs) {
		this.ljcs = ljcs;
	}

	public FirstOrderTaskPool getFotp() {
		return fotp;
	}

	public void setFotp(FirstOrderTaskPool fotp) {
		this.fotp = fotp;
	}
	
}