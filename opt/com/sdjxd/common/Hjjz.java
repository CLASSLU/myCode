package com.sdjxd.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.RowSet;

import com.sdjxd.pms.platform.data.DbOper;

public class Hjjz {
	
	public static String getSghg(String id){
		//List<String> list = new ArrayList<String>();
		String sghg = "";
		try{
			String strSql = "SELECT NAME, VALUE FROM [S15].XMGG_HJJZ_SGHG WHERE VALUE = '" + id + "' ORDER BY SHOWORDER";
			RowSet rs = DbOper.executeQuery(strSql);
			if(rs.next()){
				sghg = rs.getString("NAME") + "|" + rs.getString("VALUE");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return sghg;
	}
	
	public static String[] getSghgxx(String id){
		List<String> list = new ArrayList<String>();
		try{
			String strSql = "SELECT NAME, VALUE FROM [S15].XMGG_HJJZ_SGHGXX WHERE SSHG = '" + id + "' ORDER BY SHOWORDER";
			RowSet rs = DbOper.executeQuery(strSql);
			while(rs.next()){
				list.add(rs.getString("NAME") + "|" + rs.getString("VALUE"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list.toArray(new String[list.size()]);
	}
	
	public static String[] getFsknx(){
		List<String> list = new ArrayList<String>();
		try{
			String strSql = "SELECT NAME, VALUE FROM [S15].XMGG_HJJZ_FSKNX ORDER BY SHOWORDER";
			RowSet rs = DbOper.executeQuery(strSql);
			while(rs.next()){
				list.add(rs.getString("NAME") + "|" + rs.getString("VALUE"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list.toArray(new String[list.size()]);
	}
	
	public static String[] getHgyzdj(){
		List<String> list = new ArrayList<String>();
		try{
			String strSql = "SELECT NAME, VALUE FROM [S15].XMGG_HJJZ_YZDJ ORDER BY SHOWORDER";
			RowSet rs = DbOper.executeQuery(strSql);
			while(rs.next()){
				list.add(rs.getString("NAME") + "|" + rs.getString("VALUE"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list.toArray(new String[list.size()]);
	}

	public static String[] getSghgnr(String hgyzdj, String sghg){
		List<String> list = new ArrayList<String>();
		try{
			String strSql = "SELECT CONTENT, VALUE " +
							  "FROM [S15].XMGG_HJJZ_SGHGNR " +
							 "WHERE SSYZDJ = '" + hgyzdj + "' " +
							   "AND SSSGHGXX IN (SELECT VALUE FROM XMGG_HJJZ_SGHGXX WHERE SSHG = '" + sghg + "') " +
							 "ORDER BY SHOWORDER";
			RowSet rs = DbOper.executeQuery(strSql);
			while(rs.next()){
				list.add(rs.getString("CONTENT") + "|" + rs.getString("VALUE"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list.toArray(new String[list.size()]);
	}

	public static String[] getFxnr(String hgyzdj){
		List<String> list = new ArrayList<String>();
		try{
			String strSql = "SELECT FXD, BCOLOR, B.VALUE, B.SFZDFX FROM [S15].XMGG_HJJZ_FXZ A, [S15].XMGG_HJJZ_FXDJ B WHERE SSHGYZDJ = '" + hgyzdj + "' AND B.VALUE = A.SSFXDJ ORDER BY A.SHOWORDER";
			RowSet rs = DbOper.executeQuery(strSql);
			while(rs.next()){
				list.add(rs.getString("FXD") + "|" + rs.getString("BCOLOR") + "|" + rs.getString("VALUE") + "|" + rs.getString("SFZDFX"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list.toArray(new String[list.size()]);
	}
	
	public static String getFxColor(String fxValue){
		try{
			String strSql = "SELECT BCOLOR FROM [S15].XMGG_HJJZ_FXDJ WHERE VALUE = '" + fxValue + "'";
			RowSet rs = DbOper.executeQuery(strSql);
			if(rs.next()){
				return rs.getString("BCOLOR");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	public static String save(String sheetid, String flfg, String flfg_bk, String zyny, String zyny_bk, String gsxx, String gsxx_bk, String wryx, String wryx_bk, String maxvalue, String zdfx, String bcolor){
		try{
			List<String> list = new ArrayList<String>();
			String strSql = "";
			if(!"0".equals(sheetid)){
				strSql = "DELETE FROM [S15].XMGG_HJJZ_RESULT WHERE SHEETID = '" + sheetid + "'";
				list.add(strSql);
				strSql = "INSERT INTO [S15].XMGG_HJJZ_RESULT (sheetid, flfg, flfg_bkcolor, zyny, zyny_bkcolor, gsxx, gsxx_bkcolor, wryx, wryx_bkcolor, maxvalue, zdfx, bcolor) VALUES ('" + sheetid + "','" + flfg + "','" + flfg_bk + "','" + zyny + "','" + zyny_bk + "','" + gsxx + "','" + gsxx_bk + "','" + wryx + "','" + wryx_bk + "','" + maxvalue + "','" + zdfx + "','" + bcolor + "')";
				list.add(strSql);
			}else{
				sheetid = UUID.randomUUID().toString();
				strSql = "INSERT INTO [S15].XMGG_HJJZ_RESULT (sheetid, flfg, flfg_bkcolor, zyny, zyny_bkcolor, gsxx, gsxx_bkcolor, wryx, wryx_bkcolor, maxvalue, zdfx, bcolor) VALUES ('" + sheetid + "','" + flfg + "','" + flfg_bk + "','" + zyny + "','" + zyny_bk + "','" + gsxx + "','" + gsxx_bk + "','" + wryx + "','" + wryx_bk + "','" + maxvalue + "','" + zdfx + "','" + bcolor + "')";
				list.add(strSql);
			}
			DbOper.executeNonQuery(list);
		}catch(Exception e){
			e.printStackTrace();
		}
		return sheetid;
	}
	
	public static String[] getInfo(String sheetid){
		try{
			String strSql = "SELECT * FROM [S15].XMGG_HJJZ_RESULT WHERE SHEETID = '" + sheetid + "'";
			RowSet rs = DbOper.executeQuery(strSql);
			if(rs.next()){
				return (rs.getString("FLFG") + "@" + rs.getString("FLFG_BKCOLOR") + "@" + rs.getString("ZYNY") + "@" + rs.getString("ZYNY_BKCOLOR") + "@" + rs.getString("GSXX") + "@" + rs.getString("GSXX_BKCOLOR") + "@" + rs.getString("WRYX") + "@" + rs.getString("WRYX_BKCOLOR") + "@" + rs.getString("MAXVALUE") + "@" + rs.getString("BCOLOR")).split("@");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
