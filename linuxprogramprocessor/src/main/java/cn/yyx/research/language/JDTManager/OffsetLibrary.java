package cn.yyx.research.language.JDTManager;

public class OffsetLibrary {
	
	public static final int OffsetBlock = 1;
	
	public static String GetOffsetDescription(Integer offset)
	{
		//"$" + 
		String result = GCodeMetaInfo.FirstDeclaredData;
		if (offset != null)
		{
			result = Math.abs(offset)+"";
		}
		//(offset <= 1 ? "aj#" : (offset/OffsetBlock)+"#")
		return result;
	}
	
	public static String GetAdjacentRefCode(boolean ispositive)
	{
		String pre = "";
		if (ispositive)
		{
			pre = "$+";
		}
		else
		{
			pre = "$-";
		}
		return pre + "aj#";
	}
	
	/*public static String GetOffsetDescription(int dataline, int currline)
	{
		if (dataline == GCodeMetaInfo.OutofScopeVarOrObject)
		{
			return "INF#";
		}
		return GetOffsetDescription(dataline-currline);
	}*/
	
}