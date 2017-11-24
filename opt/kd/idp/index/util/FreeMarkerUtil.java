package kd.idp.index.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spring.exceptionhander.FreeMarkerExceptionHander;

import KD.IDP.etl.ConfigDAO.FileParserObject;
import KD.IDP.etl.fileparser.FileParser;
import KD.IDP.etl.fileparser.match.Context;
import KD.IDP.etl.fileparser.reader.FileReader;
import KD.IDP.etl.fileparser.reader.SyntaxReader;

import freemarker.template.Configuration;

/**
 * E�ļ�����(���ָ�ʽ��E�ļ�����)�����ɳ���
 * @author Administrator
 *
 */
public class FreeMarkerUtil {
	
	/**
	 * 
	 * @param root ��Ϊģ���е�����ֵΪ��Ӧ������
	 * @param outputDir
	 *            д���ļ�·������\��β
	 * @param outputFile
	 *            д���ļ���
	 * @param ftlDir
	 *            ftlģ��·������\��β
	 * @param ftlFile
	 *            ftlģ���ļ���
	 * @param Encoding
	 *            ����
	 */
	public static String writeFileByFtl(Map root, String outputDir,
			String outputFile, String ftlDir, String ftlFile, String Encoding) throws Exception {
		FileWriter out = null;
		try {
			// ByteArrayOutputStream b = new ByteArrayOutputStream();
			// Writer out = new OutputStreamWriter(b);
			if (!new File(outputDir.trim()).exists()) {
				new File(outputDir.trim()).mkdir();
			}
			out = new FileWriter(outputDir.trim()+File.separator + outputFile);
			Configuration freemarker_cfg = null;
			freemarker_cfg = new Configuration();
			freemarker_cfg.setTemplateExceptionHandler(new FreeMarkerExceptionHander());
			freemarker_cfg.setNumberFormat("0.######");
			freemarker_cfg.setDirectoryForTemplateLoading(new File(ftlDir));
			freemarker.template.Template t = freemarker_cfg
					.getTemplate(ftlFile);
			t.process(root, out);
			// CommonTools.printLog(b);
		} catch (Exception e) {
			e.printStackTrace();
			if (out != null) {
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			throw e;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					throw e1;
				}
			}
		}
		return "true";
	}
	 /**
	 *
	 * @param root ��Ϊģ���е�����ֵΪ��Ӧ������
	 * @param ftlDir
	 *            ftlģ��·������\��β
	 * @param ftlFile
	 *            ftlģ���ļ���
	 * @param Encoding
	 *            ����
	 */
	public static String writeStringByFtl(Map root,
			String ftlDir, String ftlFile) {
		String resultString= null;
		StringWriter out = null;
		try {
			out = new StringWriter();
			Configuration freemarker_cfg = null;
			freemarker_cfg = new Configuration();
			freemarker_cfg.setNumberFormat("0.######");
			freemarker_cfg.setDirectoryForTemplateLoading(new File(ftlDir));
			freemarker.template.Template t = freemarker_cfg
					.getTemplate(ftlFile);
			t.process(root, out);
			if (out!=null) {
				resultString = out.getBuffer().toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (out != null) {
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			return resultString;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return resultString;
	}

	/**
	 * ����ftlģ���ȡ����
	 *
	 * @param inputDir
	 *            Ŀ���ļ�·������\��β
	 * @param inputFile
	 *            Ŀ���ļ���
	 * @param ftlDir
	 *            ftlģ��·������\��β
	 * @param ftlFile
	 *            ftlģ����
	 * @param Encoding
	 *            ����
	 * @return ����Map
	 */
	public static Map getdataByFtl(String inputDir, String inputFile,
			String ftlDir, String ftlFile, String Encoding) {
		Map alldata = new HashMap();
		try {
			FileParserObject fpo = new FileParserObject();
			fpo.setEncoding(Encoding);
			fpo.setInputDir(inputDir);
			fpo.setInputFile(inputFile);
			fpo.setParserDir(ftlDir);
			fpo.setParserFile(ftlFile);
			FileParser fp = new FileParser();
			fp.root = new HashMap();
			fp.setObj(fpo);
			System.out.println("��ʼ��ȡģ������, �ļ�·����:"
					+ fp.getObj().getParserDir() + "   �ļ�������:"
					+ fp.getObj().getParserFile());
			ArrayList syntaxList = SyntaxReader.read(
					fp.getObj().getParserDir(), fp.getObj().getParserFile());
			System.out.println("ģ������" + syntaxList);
			ArrayList wordList = FileReader.read(fp.getObj().getInputDir(), fp
					.getObj().getInputFile(), fp.getObj().getEncoding(),
					new Date(), false);
			System.out.println("�ļ�����" + wordList);
			String virtualRecordID = "mainRecord";
			Context cursor = new Context();
			cursor.setSyntaxIndex(0);
			cursor.setWordIndex(0);
			cursor.setSyntaxAll(syntaxList);
			cursor.setWordAll(wordList);
			cursor.putRecordContext(virtualRecordID, fp.root);
			cursor.setRecordID(virtualRecordID);
			KD.IDP.etl.fileparser.match.Matcher.deal(cursor, null);

			// CommonTools.printLog(fp.getRoot().get("data"));
			alldata = fp.getRoot();
		} catch (Exception e) {
			e.printStackTrace();
			alldata = new HashMap();
		}
		return alldata;
	}

	//�����ļ�
	public static ArrayList<Map<String, String>> parseTxtFile(File file) throws Exception{
		FileInputStream fileInput;
		BufferedReader in = null;
		ArrayList<Map<String, String>> list = new ArrayList<Map<String,String>>();
		try{
			fileInput = new FileInputStream(file);
			in = new BufferedReader(new InputStreamReader(fileInput, "GBK"));
			String cmd = "";
			List<String> headerList = null;
			while ((cmd = in.readLine()) != null) {
				Map<String, String> dataMap = new HashMap<String, String>();
				if(cmd.startsWith("@")){
					headerList = ParseTool.parseListFormatHeader_1(cmd);
				}else if(cmd.startsWith("#")){
					//dataMap = ParseTool.parseListFormatData_1(cmd, headerList, 2);
					dataMap = ParseTool.parseListFormatData_1(cmd, headerList, 1);
					list.add(dataMap);
				}
			}
			if (null != in){
				in.close();
				in = null;
			}
			return list;
		}catch(Exception e){
			if (null != in){
				in.close();
				in = null;
			}
			throw e;
		}
	}
}
