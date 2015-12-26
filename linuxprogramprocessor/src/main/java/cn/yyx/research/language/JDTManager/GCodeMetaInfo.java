package cn.yyx.research.language.JDTManager;

public class GCodeMetaInfo {
	
	public static final String ArrayInitial = "@ARI";
	
	public static String ArrayCreation = "@ARC";
	
	public static String StringHolder = "@STR";
	public static String NumberHolder = "@NUB";
	public static String CharHolder = "@Chr";
	
	public static String NoStatement = "nu#";
	public static String NullLiteral = "@NUL";
	public static String CommonSplitter = "#";
	
	public static String NoDeclaredType = "@NT";
	
	public static String CodeHole = "@HO";
	
	// The white space in code is replaced with '#'.
	public static String WhiteSpaceReplacer = "#";
	
	public static String OffsetSpiliter = "?";
	
	public static String ContentHolder = "<!%CH!>";
	
	public static String DataRefIndicator = "$";
	
	public static String HackedNoType = "<$NT$>";
	
	public static int OutofScopeVarOrObject = -10;
	public static String FirstDeclaredData = "F";
	//public static String OutofScopeDesc = "$INF";
	
	//for field only which means data only.
	//public static int IsField = -9;
	//public static String IsFieldDesc = "$FREF#";
	
	// corpus name
	public static String EnumCorpus = "BigEnumDetail";
	public static String NumberCorpus = "BigNumberDetail";
	public static String StringCorpus = "BigStringDetail";
	public static String CharCorpus = "BigCharDetail";
	public static String LogicCorpus = "BigClassDetail";
}
