package cn.yyx.research.language.JDTManager;

public class ReferenceHint
{
	private byte DataType = -1;
	private byte WayUse = -1;
	
	public ReferenceHint(byte dataType, byte wayUse) {
		DataType = dataType;
		WayUse = wayUse;
	}
	
	public int GetOverAllHint()
	{
		return DataType | WayUse;
	}
	
	public byte getDataType() {
		return DataType;
	}
	public void setDataType(byte dataType) {
		DataType = dataType;
	}
	public byte getWayUse() {
		return WayUse;
	}
	public void setWayUse(byte wayUse) {
		WayUse = wayUse;
	}
}