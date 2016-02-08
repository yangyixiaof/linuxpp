package cn.yyx.research.language.simplified.JDTHelper;

import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import cn.yyx.research.language.JDTManager.GCodeMetaInfo;

public class SimplifiedFieldProcessASTVisitor extends SimplifiedCodeGenerateASTVisitor {
	
	protected Integer CurrentLevelClass = null;
	
	public SimplifiedFieldProcessASTVisitor(SimplifiedCodeGenerateASTVisitor scga) {
		ocm = scga.ocm;
		ojfc = scga.ojfc;
		ojfacc = scga.ojfacc;
		mw = scga.mw;
		fotp = scga.fotp;
		sdm = scga.sdm;
		voorm = scga.voorm;
		cjcs = scga.cjcs;
		ljcs = scga.ljcs;
		berefered = scga.berefered;
		bereferedAlready = scga.bereferedAlready;
		referedcnt = scga.referedcnt;
		referhint = scga.referhint;
		runpermit = scga.runpermit;
		runforbid = scga.runforbid;
		argmutiple = scga.argmutiple;
		omc = scga.omc;
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		return false;
	}
	
	@Override
	public void endVisit(MethodDeclaration node) {
	}
	
	@Override
	public boolean visit(Initializer node) {
		return false;
	}
	
	@Override
	public void endVisit(Initializer node) {
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		return HandleCurrentLevelControl(node.hashCode());
	}
	
	@Override
	public void endVisit(TypeDeclaration node) {
	}
	
	@Override
	public boolean visit(EnumDeclaration node) {
		return false;
	}
	
	@Override
	public void endVisit(EnumDeclaration node) {
	}
	
	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		boolean ifcontinue = HandleCurrentLevelControl(node.hashCode());
		if (ifcontinue)
		{
			jc = ojfacc;
		}		
		return ifcontinue;
	}
	
	@Override
	public void endVisit(AnonymousClassDeclaration node) {
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		// System.out.println("FieldDeclaration:" + node + ";typeclass:" + node.getType().getClass());
		String typecode = TypeCode(node.getType(), true);
		SetVeryRecentDeclaredType(typecode);
		String nodecode = GenerateVariableDeclarationTypeCode(typecode, null);
		GenerateOneLine(nodecode, false, false, false, true, null);
		VeryRecentIsFieldDeclared = true;
		return true;
	}
	
	@Override
	public void endVisit(FieldDeclaration node) {
		SetVeryRecentDeclaredType(null);
		VeryRecentIsFieldDeclared = false;
		AppendEndInfoToLast(GCodeMetaInfo.EndOfAStatement);
	}
	
	protected boolean HandleCurrentLevelControl(int classhashcode)
	{
		if (CurrentLevelClass == null)
		{
			CurrentLevelClass = classhashcode;
			return true;
		}
		else
		{
			return false;
		}
	}
	
}