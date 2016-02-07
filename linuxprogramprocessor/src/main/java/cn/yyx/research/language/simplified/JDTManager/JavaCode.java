package cn.yyx.research.language.simplified.JDTManager;

import cn.yyx.research.language.JDTManager.NodeCode;

public interface JavaCode {
	public void AddOneMethodNodeCode(NodeCode nc);
	public void OneSentenceEnd();
	public boolean IsEmpty();
}
