package com.sdjxd.common.report;

/**
 * ��Ԫ������
 * 
 * @author zhen
 * 
 */
public class ReportCellCoordinate {
	private int X;// X����
	private int Y;// Y����

	/**
	 * ���������޲���
	 */
	public ReportCellCoordinate() {

	}

	/**
	 * �������������
	 * 
	 * @param x
	 * @param y
	 */
	public ReportCellCoordinate(int x, int y) {
		this.X = x;
		this.Y = y;
	}

	/**
	 * ��������
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
