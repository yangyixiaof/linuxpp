package cn.yyx.research.language.JDTManager;

import java.util.Map;
import java.util.TreeMap;

public class NodeHelpManager<T> {
	
	Map<Integer, T> helps = new TreeMap<Integer, T>();
	
	public void AddNodeHelp(Integer key, T value)
	{
		helps.put(key, value);
	}
	
	public T DeleteNodeHelp(Integer key)
	{
		return helps.remove(key);
	}
	
	public T GetNodeHelp(Integer key)
	{
		return helps.get(key);
	}
	
}
