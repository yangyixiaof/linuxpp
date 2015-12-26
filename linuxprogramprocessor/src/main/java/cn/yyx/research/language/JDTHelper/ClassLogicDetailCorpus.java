package cn.yyx.research.language.JDTHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import cn.yyx.research.language.Utility.CorpusContentPair;

public class ClassLogicDetailCorpus {
	
	@SuppressWarnings("unchecked")
	public static ArrayList<CorpusContentPair> GenerateClassDetailCorpus(CompilationUnit compilationUnit)
	{
		List<TypeDeclaration> typeDeclarations = compilationUnit.types();
		Map<String, String> allcodemap = new TreeMap<String, String>();
		for (Object object : typeDeclarations) {
			TypeDeclaration clazzNode = (TypeDeclaration) object;
			
			//testing
			//System.out.println("ClassName:"+clazzNode.getName());
			
			/*MethodDeclaration[] methods = clazzNode.getMethods();
			for (MethodDeclaration method : methods) {
				Block mbody = method.getBody();
				StringBuilder onemethod = new StringBuilder("");
				mbody.accept(new ForwardMethodASTVisitor(onemethod));
				Content.append(onemethod);
			}*/
			ForwardMethodPreProcessASTVisitor fmastv = new ForwardMethodPreProcessASTVisitor();
			ForwardMethodCodeGenerateASTVisitor fmvgastv = new ForwardMethodCodeGenerateASTVisitor(fmastv);
			clazzNode.accept(fmastv);
			clazzNode.accept(fmvgastv);
			Map<String, String> codemap = fmvgastv.GetGeneratedCode();
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
			fmvgastv = null;
			codemap = null;
		}
		//testing
		//System.out.println(Contentstr);
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