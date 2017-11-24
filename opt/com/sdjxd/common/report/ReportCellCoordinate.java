package com.sdjxd.common.report;

/**
 * 单元格坐标
 * 
 * @author zhen
 * 
 */
public class ReportCellCoordinate {
	private int X;// X坐标
	private int Y;// Y坐标

	/**
	 * 创建坐标无参数
	 */
	public ReportCellCoordinate() {

	}

	/**
	 * 创建坐标带参数
	 * 
	 * @param x
	 * @param y
	 */
	public ReportCellCoordinate(int x, int y) {
		this.X = x;
		this.Y = y;
	}

	/**
	 * 设置坐标
	 * 
	 * @param x
	 * @param y
	 */
	public void setCoordinate(int x, int y) {
		this.X = x;
		this.Y = y;
	}

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

}
