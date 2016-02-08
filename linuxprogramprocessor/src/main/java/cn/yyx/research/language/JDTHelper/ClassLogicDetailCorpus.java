package cn.yyx.research.language.JDTHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import cn.yyx.research.language.Utility.CorpusContentPair;
import cn.yyx.research.language.simplified.JDTHelper.SimplifiedCodeGenerateASTVisitor;

public class ClassLogicDetailCorpus {
	
	@SuppressWarnings("unchecked")
	public static ArrayList<CorpusContentPair> GenerateClassDetailCorpus(CompilationUnit compilationUnit)
	{
		List<ASTNode> typeDeclarations = compilationUnit.types();
		Map<String, String> allcodemap = new TreeMap<String, String>();
		for (Object object : typeDeclarations) {
			ASTNode clazzNode = (ASTNode) object;
			SimplifiedCodeGenerateASTVisitor fmastv = new SimplifiedCodeGenerateASTVisitor();
			clazzNode.accept(fmastv);
			Map<String, String> codemap = fmastv.GetGeneratedCode();
			Set<String> keys = codemap.keySet();
			Iterator<String> itr = keys.iterator();
			while (itr.hasNext())
			{
				String corpus = itr.next();
				if (allcodemap.get(corpus) == null)
				{
					allcodemap.put(corpus, "");
				}
				String value = codemap.get(corpus);
				allcodemap.put(corpus, (allcodemap.get(corpus) + value));
			}
			
			fmastv = null;
			codemap = null;
		}
		ArrayList<CorpusContentPair> result = new ArrayList<CorpusContentPair>();
		Set<String> keys = allcodemap.keySet();
		Iterator<String> itr = keys.iterator();
		while (itr.hasNext())
		{
			String corpus = itr.next();
			String content = allcodemap.get(corpus);
			result.add(new CorpusContentPair(corpus, content));
		}
		
		// clear variables
		allcodemap = null;
		return result;
	}
	
}