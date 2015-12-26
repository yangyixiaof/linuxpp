package cn.yyx.research.language.JDTHelper;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;

import cn.yyx.research.language.JDTManager.FirstOrderTask;
import cn.yyx.research.language.JDTManager.FirstOrderTaskPool;
import cn.yyx.research.language.JDTManager.GCodeMetaInfo;
import cn.yyx.research.language.JDTManager.NodeCode;
import cn.yyx.research.language.JDTManager.NodeCodeManager;
import cn.yyx.research.language.JDTManager.NodeLevelManager;
import cn.yyx.research.language.JDTManager.NodeTypeLibrary;
import cn.yyx.research.language.JDTManager.OneJavaFileCode;
import cn.yyx.research.language.JDTManager.OperationType;
import cn.yyx.research.language.JDTManager.OtherCodeManager;
import cn.yyx.research.language.Utility.CorpusContentPair;

public class MyCodeGenerateASTVisitor extends ASTVisitor {

	private OtherCodeManager ocm = new OtherCodeManager();
	private NodeCode omc = new NodeCode();
	private OneJavaFileCode ojfc = new OneJavaFileCode();
	private NodeLevelManager nlm = new NodeLevelManager();
	private FirstOrderTaskPool fotp = new FirstOrderTaskPool();

	private NodeCodeManager ncm = null;

	private Integer FirstLevelClass = null;
	
	public MyCodeGenerateASTVisitor(MyCodeGenerateASTVisitor mcgast) {
		ocm = mcgast.getOcm();
		omc = mcgast.getOmc();
		ojfc = mcgast.getOjfc();
		nlm = mcgast.getNlm();
		fotp = mcgast.getFotp();
		ncm = mcgast.getNcm();
		FirstLevelClass = mcgast.getFirstLevelClass();
	}

	public MyCodeGenerateASTVisitor(MyPreProcessASTVisitor mppast) {
		setNcm(mppast.getNcm());
	}

	@Override
	public void preVisit(ASTNode node) {
		getNlm().PreVisit(node);
		getFotp().PostIsBegin(node);
		super.preVisit(node);
	}

	@Override
	public void postVisit(ASTNode node) {
		getNlm().PostVisit(node);
		getFotp().PreIsOver(node);
		super.postVisit(node);
	}

	@Override
	public boolean visit(Block node) {
		// System.out.println("Block:"+node);
		getNlm().Visit(node);
		return super.visit(node);
	}

	@Override
	public void endVisit(Block node) {
		getNlm().EndVisit(node);
	}

	@Override
	public boolean visit(ParenthesizedExpression node) {
		getNlm().Visit(node);
		return super.visit(node);
	}

	@Override
	public void endVisit(ParenthesizedExpression node) {
		// System.out.println("ParenthesizedExpression:"+node);
		// System.out.println("ParenthesizedExpression:"+node.getExpression());
		// GiveLinkBetweenNodes(node, node.getExpression());
		getNlm().EndVisit(node);
	}

	protected boolean isFirstLevelASTNode(ASTNode node) {
		int parenthashcode = node.getParent().hashCode();
		if (parenthashcode == getFirstLevelClass()) {
			return true;
		}
		return false;
	}

	protected void AppendOtherCode(String corpus, String code) {
		getOcm().AppendOtherCode(corpus, code);
	}

	protected void AddFirstOrderTask(FirstOrderTask runtask) {
		getFotp().InfixNodeAddFirstOrderTask(runtask);
	}

	protected void TrulyGenerateOneLineWithAppendingPreType(ASTNode node, ASTNode pre, int level,
			boolean hasContentHolder) {
		String nodecode = GetNodeCode(node) + GCodeMetaInfo.CommonSplitter
				+ OperationType.GetTypeDescriptionId(pre.getClass()) + GCodeMetaInfo.CommonSplitter
				+ OperationType.GetTypeDescriptionId(node.getClass());
		// nodecode += "%" + HandleNodeType(node) + "/";
		getOmc().AddOneLineCode(nodecode, level, hasContentHolder);
	}

	// code has ......#leixing, the rest is %b/ as an example, only need to add
	// info of lines.
	protected void TrulyGenerateOneLine(ASTNode node, int level, boolean hasContentHolder) {
		String nodecode = GetNodeCode(node) + GCodeMetaInfo.CommonSplitter
				+ OperationType.GetTypeDescriptionId(node.getClass());
		// nodecode += "%" + HandleNodeType(node) + "/";
		getOmc().AddOneLineCode(nodecode, level, hasContentHolder);
	}

	protected void TrulyGenerateOneLine(String rawtext, Integer astNodeType, Character relativeNodeType, int level,
			boolean hasContentHolder) {
		String nodecode = rawtext + GCodeMetaInfo.CommonSplitter + astNodeType;
		// nodecode += "%" + relativeNodeType + "/";
		getOmc().AddOneLineCode(nodecode, level, hasContentHolder);
	}

	public ArrayList<CorpusContentPair> GetOtherGeneratedCode() {
		return getOcm().GetOtherGeneratedCode();
	}

	protected void DecreaseLevel() {
		getNlm().DecreaseLevel();
	}

	protected void IncreaseLevel() {
		getNlm().IncreaseLevel();
	}

	protected void OneSentenceEnd() {
		getOjfc().OneSentenceEnd();
	}

	protected void ClearMethodNodeCode() {
		setOmc(null);
	}

	protected void PushMethodNodeCodeToJavaFileCode() {
		getOjfc().AddOneMethodNodeCode(getOmc());
	}

	protected void FlushCode() {
		if (!getOmc().IsEmpty()) {
			PushMethodNodeCodeToJavaFileCode();
			OneSentenceEnd();
		}
		ClearMethodNodeCode();
	}

	protected int GetNodeLevel(ASTNode node) {
		return getNlm().GetNodeLevel(node);
	}

	protected Character HandleNodeType(ASTNode node) {
		if (getNcm().IsNewStatement(node)) {
			getNcm().AddNodeType(node, NodeTypeLibrary.newstart);
		}
		return getNcm().GetNodeType(node);
	}

	public Map<String, String> GetGeneratedCode() {
		Map<String, String> result = new TreeMap<String, String>();
		result.putAll(getOcm().getOtherCodeMap());
		result.put(GCodeMetaInfo.LogicCorpus, getOjfc().toString());
		return result;
	}

	public void RegistFirstNodeAfterDecreasingElement(ASTNode node) {
		getNlm().RegistFirstNodeAfterDecreasingElement(node);
	}

	public void RegistLastNodeBeforeIncreaseingElement(ASTNode node) {
		getNlm().RegistLastNodeBeforeIncreaseingElement(node);
	}

	public Integer getFirstLevelClass() {
		return FirstLevelClass;
	}

	protected String GetNodeCode(ASTNode node) {
		return getNcm().GetAstNodeCode(node);
	}

	protected Boolean GetNodeHasOccupiedOneLine(ASTNode node) {
		return getNcm().GetAstNodeHasOccupiedOneLine(node);
	}

	protected boolean GetNodeHasContentHolder(ASTNode node) {
		return getNcm().GetAstNodeHasContentHolder(node);
	}

	protected boolean GetNodeInMultipleLine(ASTNode node) {
		return getNcm().GetAstNodeInMultipleLine(node);
	}

	protected boolean GetNodeHasUsed(ASTNode node) {
		return getNcm().GetNodeHasUsed(node);
	}

	protected char GetNodeType(ASTNode node) {
		return getNcm().GetNodeType(node);
	}

	protected Integer GetRealNode(ASTNode node) {
		return getNcm().GetRealNode(node);
	}

	protected void AddNodeHasUsed(ASTNode node, boolean used) {
		getNcm().AddNodeHasUsed(node, used);
	}

	protected boolean ShouldExecute(ASTNode node) {
		if (GetNodeHasUsed(node)) {
			return false;
		}
		return true;
	}

	protected boolean GetNodeNeedAppendChildPreNodeType(ASTNode astnode) {
		return getNcm().GetNodeNeedAppendChildPreNodeType(astnode);
	}

	public OtherCodeManager getOcm() {
		return ocm;
	}

	public void setOcm(OtherCodeManager ocm) {
		this.ocm = ocm;
	}

	public NodeCode getOmc() {
		return omc;
	}

	public void setOmc(NodeCode omc) {
		this.omc = omc;
	}

	public OneJavaFileCode getOjfc() {
		return ojfc;
	}

	public void setOjfc(OneJavaFileCode ojfc) {
		this.ojfc = ojfc;
	}

	public NodeLevelManager getNlm() {
		return nlm;
	}

	public void setNlm(NodeLevelManager nlm) {
		this.nlm = nlm;
	}

	public FirstOrderTaskPool getFotp() {
		return fotp;
	}

	public void setFotp(FirstOrderTaskPool fotp) {
		this.fotp = fotp;
	}

	public NodeCodeManager getNcm() {
		return ncm;
	}

	public void setNcm(NodeCodeManager ncm) {
		this.ncm = ncm;
	}

	public void setFirstLevelClass(Integer firstLevelClass) {
		FirstLevelClass = firstLevelClass;
	}

}