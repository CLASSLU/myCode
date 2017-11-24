package com.sdjxd.common.index;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.sdjxd.pms.platform.base.Global;
import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.form.service.cell.DynamicCell;
import com.sdjxd.pms.platform.tool.Guid;

public class IndexUserCell {
	/**
	 * 首页用户自定义原件--折线图
	 * 统计最近一年发生的安全事故数
	 */
	public static String userDefinedLineChart(DynamicCell cell){
		ArrayList dataSet = null;
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs3;
		int sum;
		int yd;
		int[] a=new int[13]; 
		int year = 0;
		int month = 1;
		String sqlStr="select  sum(fscs) as sum1,yd from TB_HSE_SG_YBDJB a,TB_HSE_SG_YBSGJL b  " +
				"where  a.sheetid=b.dybdh AND SHEETSTATUSID='1A2D2B0F-1519-45ED-AE65-5E5393E867D6' " +
				"and ((nd=(select to_char(sysdate,'yyyy')  from dual) and yd<=(select to_char(sysdate,'mm')  from dual)) " +
				"or (nd=(select to_char(sysdate,'yyyy')-1  from dual) and yd>=(select to_char(sysdate,'mm')+1  from dual))) " +
				"group by yd ";
		  try {

				rs1 = DbOper.executeQuery(sqlStr);
				while(rs1.next()){
					sum=rs1.getInt("sum1");
					yd=Integer.parseInt(rs1.getString("yd"));
					a[yd]=sum;
				}
				
				
			} catch (Exception e) {
				e.getMessage();
				e.printStackTrace();
			}
		String sqlStr1="select   to_char(sysdate,'yyyy') as  year  from   dual ";
		String sqlStr2="select   to_char(sysdate,'mm')  as  month  from   dual";
		 try {

				rs2 = DbOper.executeQuery(sqlStr1);
				while(rs2.next()){
					year=rs2.getInt("year");
				}

				
			} catch (Exception e) {
				e.getMessage();
			}
			 try {

					

					rs3 = DbOper.executeQuery(sqlStr2);
					while(rs3.next()){
						month=rs3.getInt("month");
					}
					
				} catch (Exception e) {
					e.getMessage();
				}
			System.out.println(month);
		String b[]=new String[13];
		for(int i=0,j=month+1;i<12;i++,j++){
			if(j==13){
				j=1;
				
			}
			b[i]="<set name=\""+j+"月\" value=\""+a[j]+"\" hoverText=\""+j+"月\"   /> ";
			
		}
		StringBuffer strbuf = new StringBuffer();
		for(int k=0;k<12;k++){
	       strbuf.append(b[k]);
			
			
		}
		String xml="<graph  caption=\"最近一年发生的安全事故数\" baseFontSize=\"12\"  baseFontColor=\"24425A\" subcaption=\"\" xAxisName=\"月份\" yAxisMinValue=\"15000\" yAxisName=\"\" decimalPrecision=\"0\" formatNumberScale=\"0\" numberPrefix=\"\" showNames=\"1\" showValues=\"0\" showAlternateHGridColor=\"1\" AlternateHGridColor=\"ff5904\" divLineColor=\"ff5904\" divLineAlpha=\"120\" alternateHGridAlpha=\"5\">" +
				strbuf.toString()+
				"</graph>";
		String uuid = Guid.create().replaceAll("-", "");
		String str=
				"<script language=\"JavaScript\" src=\""+Global.getName()+"/pms/platform/fusionCharts/FusionCharts.js\"></script>" +
				
				"<div id=\"chartdiv"+uuid+"\" align=\"center\">Fourth Chart Container Area 2D</div>"+
				"<script type=\"text/javascript\">" +
        		" var chart = new FusionCharts(\""+Global.getName()+"/pms/platform/fusionCharts/FCF_Line.swf\", \"ChartId\", \""+cell.getWidth()+"\", \""+cell.getHeight()+"\");" +
				" chart.setDataXML('"+xml+"');"+
				" chart.render(\"chartdiv"+uuid+"\");" +
				"</script>" ;
			
		return str;
	}
	/**
	 * 首页用户自定义原件--饼状图
	 * HSE观察
	 */
	public static String userDefinedPieChart(DynamicCell cell){
		ArrayList dataSet = null;
		ResultSet rs;
		StringBuffer strbuffer = new StringBuffer();
		
		
		String sqlstr="select sheetstatusname,count(sheetstatusname) as count1 from tb_hse_chk_problem t group by sheetstatusname";
		  try {

				rs = DbOper.executeQuery(sqlstr);
				while(rs.next()){
					strbuffer.append("<set name=\"");
					strbuffer.append(rs.getString("SHEETSTATUSNAME"));
					strbuffer.append("\" value=\"");
					strbuffer.append(rs.getInt("count1"));
					strbuffer.append("\"  />");
				}
				
				
			} catch (Exception e) {
				e.getMessage();
			}
		
		String xml="<graph showNames=\"1\"baseFontSize=\"12\"  baseFontColor=\"24425A\" decimalPrecision=\"0\">" +
		        strbuffer.toString()+
				"</graph>";
		String uuid = Guid.create().replaceAll("-", "");
		String str=
				"<script language=\"JavaScript\" src=\""+Global.getName()+"/pms/platform/fusionCharts/FusionCharts.js\"></script>" +
				
				"<div id=\"chartdiv"+uuid+"\" align=\"center\">Fourth Chart Container Area 3D</div>"+
				"<script type=\"text/javascript\">" +
        		" var chart = new FusionCharts(\""+Global.getName()+"/pms/platform/fusionCharts/FCF_Pie3D.swf\", \"ChartId\", \""+cell.getWidth()+"\", \""+cell.getHeight()+"\");" +
				" chart.setDataXML('"+xml+"');"+
				" chart.render(\"chartdiv"+uuid+"\");" +
				"</script>" ;
		
		return str;
	}
}
