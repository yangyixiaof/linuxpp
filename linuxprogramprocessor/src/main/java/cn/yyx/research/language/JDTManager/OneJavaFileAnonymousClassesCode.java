package cn.yyx.research.language.JDTManager;

import java.util.Iterator;

import cn.yyx.research.language.simplified.JDTManager.JavaCode;

public class OneJavaFileAnonymousClassesCode implements JavaCode{
	
	StringBuilder sb = new StringBuilder("");
	MethodWindow mw = null;
	
	public OneJavaFileAnonymousClassesCode() {
	}
	
	public void AddPreDeclrations(MethodWindow mw)
	{
		this.mw = mw;
	}
	
	public void AddOneMethodNodeCode(NodeCode nc)
	{
		if (mw != null)
		{
			sb.append(mw);
			mw = null;
		}
		Iterator<String> itr = nc.GetCodeIterator();
		while (itr.hasNext())
		{
			sb.append(" " + itr.next());
		}
	}
	
	public void OneSentenceEnd()
	{
		/*System.err.println(sb);
		if (sb.length() == 0 || sb.charAt(sb.length()-1) != '.')
		{
			sb.append(".");
		}*/
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}

	@Override
	public boolean IsEmpty() {
		return sb.length() == 0;
	}
	
}