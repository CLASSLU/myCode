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
			int jl = 0;//��������ռ����
			int j = 0;
			int nTitleRow = 0;
			//�������
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
				wsheet.addCell(new Label(0, nTitleRow, "��ʼʱ��", wcfFC2));
				wsheet.addCell(new Label(1, nTitleRow, tjSdate, wcfFC2));
				wsheet.addCell(new Label(2, nTitleRow, "����ʱ��", wcfFC2));
				wsheet.addCell(new Label(3, nTitleRow, tjEdate, wcfFC2));
				nTitleRow++;
			}
			for (j=0; j < map.size(); ++j) {
				
				List<ReportCellInstanceInfo> rciiList = map.get(j + 1);
				int il = 0;//��������ռ����
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
				//һ��ȫ��Ϊ����Ԫ��ʱ����һ��
				if(il==rciiList.size()){
					jl++;
				}
			}
			
			//����FCͼ����Ϣ
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
				int picWidth = biFCImage.getWidth(); // ͼƬ��, ���� 150 
		        int picHeight = biFCImage.getHeight(); // ͼƬ��, ���� 105 
		        //picWidth = picWidth/64; //��ʱ������С��Ԫ������Ϊ:64,17����������ռ��Ԫ����
		        //picHeight = picHeight/17;
		        double[] fcD = calFCImageExportCell(wsheet, picWidth, picHeight, 0.2, j-jl+1);

				WritableImage wIamge = new WritableImage(0, j-jl+1+nTitleRow, fcD[0],fcD[1], data);  //�У�������
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
	 * // �������( picCellWidth, picCellHeight ), ͼƬ��ʾ��С, Ĭ�� 100% ��ʾ: begin 
	    //     ͼƬcell��� = ͼƬʵ�ʿ�Խÿ��cell��ռ���ȵ���Ը���cell ratio�ĺ� 
	    //     ����: ������ʼλ��,����ͼƬʵ�ʿ�Խ������, Ȼ��������ratio,Ȼ���ۼ� 
	 */
	private double[] calFCImageExportCell(WritableSheet ws, int picWidth, int picHeight, double picBeginCol, double picBeginRow)
	{
		/* 
        ʵ������: 150/105 = 2.78 cm / 3.97 cm = 4832 / 1590 
        ʵ������: 300/210 =           2����С = 9600 / 3150 ����: 32 / 15 
	    */  
		double[] fcD = new double[2];
	    double picCellWidth = 0.0; // �� cell�Ŀ�Խ����, ��С�� 
	    double picCellHeight = 0.0; 
	    
	    int _picWidth = picWidth * 32 ; // pic�Ŀ��,ѭ���ݼ�, ��jxl�Ŀ�ȵ�λ, 32/15 
	    for(int x=0; x< 1234; x++) 
	    { 
	        int bc = (int)Math.floor( picBeginCol + x ); // 3.6 to 3 // ����ѭ������cellλ�� 
	        int v = ws.getColumnView( bc ).getSize(); //����cell��,jxl��λ 
	        double _offset0 = 0.0; // >= 0 // ����ߵ�ƫ����, �� x = 0 ��ʱ����� 
	        if( 0 == x ) 
	            _offset0 = ( picBeginCol - bc ) * v ; // 
	        
	        if( 0.0 + _offset0 + _picWidth > v ) // _picWidth ʣ�೤�ȳ���һ��cellʱ 
	        { 
	            // ���㱾��cell��, pic ��ռ ratioֵ, �ۼӵ� picCellWidth 
	            double _ratio = 1.0; 
	            if( 0 == x ) 
	                _ratio = ( 0.0 + v - _offset0 ) / v; 

	            // picCellWidth += 1.0; 
	            picCellWidth += _ratio; 
	            _picWidth -= (int)( 0.0 + v - _offset0 ); // int 
	        } 
	        else // _picWidth ʣ�೤����һ��cell��ʱ 
	        { 
	            double _ratio = 0.0; 
	            if( v != 0 ) 
	                _ratio = ( 0.0 + _picWidth ) / v; 
	            picCellWidth += _ratio; 
	            break; 
	        } 
	        if( x >= 1233 ) 
	        	System.out.println("enwl_print_report_xls: 446: x >= 1233,ѭ������,��Ӱ������,Ӱ���ٶ�"); // 
	    } // for 
	    // ��ʱ picCellWidth ��ͼƬʵ�ʵ�ֵ�� 
	
	    // 
	    int _picHeight = picHeight * 15 ; // pic�ĸ߶�,ѭ���ݼ�, ��jxl�ĸ߶ȵ�λ, 32/15 
	    for(int x=0; x< 1234; x++) 
	    { 
	        int bc = (int)Math.floor( picBeginRow + x ); // 3.6 to 3 // ����ѭ������cellλ�� 
	        int v = ws.getRowView( bc ).getSize(); //����cell��,jxl��λ 
	        double _offset0 = 0.0; // >= 0 // �붥����ƫ����, �� x = 0 ��ʱ����� 
	        if( 0 == x ) 
	            _offset0 = ( picBeginRow - bc ) * v ; // 
	        if( 0.0 + _offset0 + _picHeight > v ) // _picHeight ʣ�೤�ȳ���һ��cellʱ 
	        { 
	            // ���㱾��cell��, pic ��ռ ratioֵ, �ۼӵ� picCellHeight 
	            double _ratio = 1.0; 
	            if( 0 == x ) 
	                _ratio = ( 0.0 + v - _offset0 ) / v; 
	            // picCellHeight += 1.0; 
	            picCellHeight += _ratio; 
	            _picHeight -= (int)( 0.0 + v - _offset0 ); // int 
	        } 
	        else // _picHeight ʣ�೤����һ��cell��ʱ 
	        { 
	            double _ratio = 0.0; 
	            if( v != 0 ) 
	                _ratio = ( 0.0 + _picHeight ) / v; 
	            picCellHeight += _ratio; 
	            break; 
	        } 
	        if( x >= 1233 ) 
	        	System.out.println("enwl_print_report_xls: 446: x >= 1233,ѭ������,��Ӱ������,Ӱ���ٶ�"); // 
	    } // for 
	    // ��ʱ picCellHeight ��ͼƬʵ�ʵ�ֵ�� 
	    // �������( picCellWidth, picCellHeight ), ͼƬ��ʾ��С, Ĭ�� 100% ��ʾ: end
	    fcD[0] = picCellWidth;
	    fcD[1] = picCellHeight;
	    
	    return fcD;
	}

}
