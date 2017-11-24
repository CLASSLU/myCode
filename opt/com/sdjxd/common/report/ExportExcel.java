package com.sdjxd.common.report;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sdjxd.common.FusionCharts.meritit.fusionchart.OracleBlob;
import com.sdjxd.pms.platform.base.Global;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WritableImage;

public class ExportExcel extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3093168185381528386L;

	/**
	 * Constructor of the object.
	 */
	public ExportExcel() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");

		String patternId = request.getParameter("patternId");
		String cellId = request.getParameter("cellId");
		String fileName = request.getParameter("fileName");
		String strFCImageID = request.getParameter("FCImageID");
		String clientWidth = request.getParameter("FCWidth");
		String clientHeight = request.getParameter("FCHeight");
		
		Map<Integer, List<ReportCellInstanceInfo>> map = ReportCell.toExcel(
				patternId, Integer.valueOf(cellId));
		importToExcel(fileName, map, response, strFCImageID, clientWidth, clientHeight);
	}

	public void importToExcel(String fileName,
			Map<Integer, List<ReportCellInstanceInfo>> map,
			HttpServletResponse httpResponse, String strFCImageID, String clientWidth, String clientHeight) {
		try {
			if ((fileName == null) || (fileName.length() == 0)) {
				fileName = "sheet1";
			}
			InputStream sbs = null;
			PrintStream os = new PrintStream(httpResponse.getOutputStream());

			httpResponse.setContentType("application/vnd.ms-excel");
			httpResponse.setHeader("Content-disposition",
					"attachment; filename=" + fileName + ".xls");
			httpResponse.setHeader("Content-Transfer-Encoding", "binary");
			httpResponse.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			httpResponse.setHeader("Pragma", "public");

			WritableFont wfc1 = new WritableFont(WritableFont.COURIER, 10,
					WritableFont.BOLD, false);
			WritableCellFormat wcfFC1 = new WritableCellFormat(wfc1);
			wcfFC1.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			wcfFC1.setAlignment(Alignment.CENTRE);
			wcfFC1.setVerticalAlignment(VerticalAlignment.CENTRE);

			WritableCellFormat wcfFC2 = new WritableCellFormat();
			wcfFC2.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			wcfFC2.setAlignment(Alignment.CENTRE);
			wcfFC2.setVerticalAlignment(VerticalAlignment.CENTRE);

			WritableWorkbook wbook = Workbook.createWorkbook(os);
			WritableSheet wsheet = wbook.createSheet("sheet1", 0);
			int jl = 0;//隐藏行所占行数
			int j = 0;
			int nTitleRow = 0;
			//插入标题
			HttpSession session = Global.getContext().getSession();
			String excelName = (String)session.getAttribute("exportExcelName");
			String tjSdate = (String)session.getAttribute("exportTjSDate");
			String tjEdate = (String)session.getAttribute("exportTjEDate");
			List<ReportCellInstanceInfo> firstCciiList = map.get(1);
			if (excelName != null && excelName != "")
			{
				wsheet.addCell(new Label(0, nTitleRow, excelName, wcfFC1));
				wsheet.mergeCells(nTitleRow,0,firstCciiList.size()-1,0);
				nTitleRow++;
			}
			if (tjSdate != null && tjSdate != "" && tjEdate != null && tjEdate != "")
			{
				wsheet.addCell(new Label(0, nTitleRow, "开始时间", wcfFC2));
				wsheet.addCell(new Label(1, nTitleRow, tjSdate, wcfFC2));
				wsheet.addCell(new Label(2, nTitleRow, "结束时间", wcfFC2));
				wsheet.addCell(new Label(3, nTitleRow, tjEdate, wcfFC2));
				nTitleRow++;
			}
			for (j=0; j < map.size(); ++j) {
				
				List<ReportCellInstanceInfo> rciiList = map.get(j + 1);
				int il = 0;//隐藏列所占列数
				for (int i = 0; i < rciiList.size(); i++) {
					ReportCellInstanceInfo rcii = rciiList.get(i);
					if (rcii.getColspan() == 0
							|| rcii.getRowspan() == 0
							|| (rcii.getWidth() != null && rcii
									.getWidth().equals("0"))
							|| (rcii.getHeigth() != null && rcii
									.getHeigth().equals("0"))) {
						il++;
						continue;
					}
					if (j==0) {
						wsheet.addCell(new Label(i-il, j-jl+nTitleRow, rcii
								.getValue(), wcfFC1));
					} else {
						wsheet.addCell(new Label(i-il, j-jl+nTitleRow, rcii
								.getValue(), wcfFC2));
					}
					if(rcii.getColspan()>1||rcii.getRowspan()>1){
						wsheet.mergeCells(i-il,j-jl,i-il+rcii.getColspan()-1,j-jl+nTitleRow+rcii.getRowspan()-1);
					}
				}
				//一行全都为隐藏元件时隐藏一行
				if(il==rciiList.size()){
					jl++;
				}
			}
			
			//导出FC图表信息
			if (strFCImageID != null && strFCImageID != "")
			{
				byte[] data = null;
				java.util.Properties dbProps = new java.util.Properties();

				dbProps = Global.loadProperties("jdbc.properties");
			
				String url = dbProps.getProperty("jdbc.url");
				String username = dbProps.getProperty("jdbc.username");
				String password = dbProps.getProperty("jdbc.password");
				
			    OracleBlob ob=new OracleBlob(url,username,password);
				data = ob.GetImgByteById(null,strFCImageID);

				sbs = new ByteArrayInputStream(data);
				BufferedImage biFCImage = ImageIO.read(sbs); 
				//float fWidth = Float.parseFloat(clientWidth);
				//float fHeight = Float.parseFloat(clientHeight);
				int picWidth = biFCImage.getWidth(); // 图片宽, 像素 150 
		        int picHeight = biFCImage.getHeight(); // 图片高, 像素 105 
		        //picWidth = picWidth/64; //暂时根据最小单元格像素为:64,17；来计算所占单元格数
		        //picHeight = picHeight/17;
		        double[] fcD = calFCImageExportCell(wsheet, picWidth, picHeight, 0.2, j-jl+1);

				WritableImage wIamge = new WritableImage(0, j-jl+1+nTitleRow, fcD[0],fcD[1], data);  //列，行索引
				wsheet.addImage(wIamge); 
			}

			wbook.write();
			wbook.close();
			os.close();
			if (sbs != null)
				sbs.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	/*
	 * // 计算参数( picCellWidth, picCellHeight ), 图片显示大小, 默认 100% 显示: begin 
	    //     图片cell宽度 = 图片实际跨越每个cell所占长度的相对各个cell ratio的和 
	    //     方法: 根据起始位置,计算图片实际跨越的区域, 然后计算相对ratio,然后累加 
	 */
	private double[] calFCImageExportCell(WritableSheet ws, int picWidth, int picHeight, double picBeginCol, double picBeginRow)
	{
		/* 
        实际像素: 150/105 = 2.78 cm / 3.97 cm = 4832 / 1590 
        实际像素: 300/210 =           2倍大小 = 9600 / 3150 比率: 32 / 15 
	    */  
		double[] fcD = new double[2];
	    double picCellWidth = 0.0; // 是 cell的跨越个数, 可小数 
	    double picCellHeight = 0.0; 
	    
	    int _picWidth = picWidth * 32 ; // pic的宽度,循环递减, 是jxl的宽度单位, 32/15 
	    for(int x=0; x< 1234; x++) 
	    { 
	        int bc = (int)Math.floor( picBeginCol + x ); // 3.6 to 3 // 本次循环所在cell位置 
	        int v = ws.getColumnView( bc ).getSize(); //本次cell宽,jxl单位 
	        double _offset0 = 0.0; // >= 0 // 离左边的偏移量, 仅 x = 0 的时候才用 
	        if( 0 == x ) 
	            _offset0 = ( picBeginCol - bc ) * v ; // 
	        
	        if( 0.0 + _offset0 + _picWidth > v ) // _picWidth 剩余长度超过一个cell时 
	        { 
	            // 计算本次cell内, pic 所占 ratio值, 累加到 picCellWidth 
	            double _ratio = 1.0; 
	            if( 0 == x ) 
	                _ratio = ( 0.0 + v - _offset0 ) / v; 

	            // picCellWidth += 1.0; 
	            picCellWidth += _ratio; 
	            _picWidth -= (int)( 0.0 + v - _offset0 ); // int 
	        } 
	        else // _picWidth 剩余长度在一个cell内时 
	        { 
	            double _ratio = 0.0; 
	            if( v != 0 ) 
	                _ratio = ( 0.0 + _picWidth ) / v; 
	            picCellWidth += _ratio; 
	            break; 
	        } 
	        if( x >= 1233 ) 
	        	System.out.println("enwl_print_report_xls: 446: x >= 1233,循环超限,不影响运行,影响速度"); // 
	    } // for 
	    // 此时 picCellWidth 是图片实际的值了 
	
	    // 
	    int _picHeight = picHeight * 15 ; // pic的高度,循环递减, 是jxl的高度单位, 32/15 
	    for(int x=0; x< 1234; x++) 
	    { 
	        int bc = (int)Math.floor( picBeginRow + x ); // 3.6 to 3 // 本次循环所在cell位置 
	        int v = ws.getRowView( bc ).getSize(); //本次cell高,jxl单位 
	        double _offset0 = 0.0; // >= 0 // 离顶部的偏移量, 仅 x = 0 的时候才用 
	        if( 0 == x ) 
	            _offset0 = ( picBeginRow - bc ) * v ; // 
	        if( 0.0 + _offset0 + _picHeight > v ) // _picHeight 剩余长度超过一个cell时 
	        { 
	            // 计算本次cell内, pic 所占 ratio值, 累加到 picCellHeight 
	            double _ratio = 1.0; 
	            if( 0 == x ) 
	                _ratio = ( 0.0 + v - _offset0 ) / v; 
	            // picCellHeight += 1.0; 
	            picCellHeight += _ratio; 
	            _picHeight -= (int)( 0.0 + v - _offset0 ); // int 
	        } 
	        else // _picHeight 剩余长度在一个cell内时 
	        { 
	            double _ratio = 0.0; 
	            if( v != 0 ) 
	                _ratio = ( 0.0 + _picHeight ) / v; 
	            picCellHeight += _ratio; 
	            break; 
	        } 
	        if( x >= 1233 ) 
	        	System.out.println("enwl_print_report_xls: 446: x >= 1233,循环超限,不影响运行,影响速度"); // 
	    } // for 
	    // 此时 picCellHeight 是图片实际的值了 
	    // 计算参数( picCellWidth, picCellHeight ), 图片显示大小, 默认 100% 显示: end
	    fcD[0] = picCellWidth;
	    fcD[1] = picCellHeight;
	    
	    return fcD;
	}

}
