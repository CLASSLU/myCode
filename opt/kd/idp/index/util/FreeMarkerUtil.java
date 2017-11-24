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
 * E文件解析(两种格式的E文件都有)和生成程序
 * @author Administrator
 *
 */
public class FreeMarkerUtil {
	
	/**
	 * 
	 * @param root 键为模板中的输出项，值为对应的数据
	 * @param outputDir
	 *            写的文件路径，以\结尾
	 * @param outputFile
	 *            写的文件名
	 * @param ftlDir
	 *            ftl模板路径，以\结尾
	 * @param ftlFile
	 *            ftl模板文件名
	 * @param Encoding
	 *            编码
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
	 * @param root 键为模板中的输出项，值为对应的数据
	 * @param ftlDir
	 *            ftl模板路径，以\结尾
	 * @param ftlFile
	 *            ftl模板文件名
	 * @param Encoding
	 *            编码
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
	 * 按照ftl模板读取数据
	 *
	 * @param inputDir
	 *            目标文件路径，以\结尾
	 * @param inputFile
	 *            目标文件名
	 * @param ftlDir
	 *            ftl模板路径，以\结尾
	 * @param ftlFile
	 *            ftl模板名
	 * @param Encoding
	 *            编码
	 * @return 返回Map
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
			System.out.println("开始读取模板内容, 文件路径是:"
					+ fp.getObj().getParserDir() + "   文件名称是:"
					+ fp.getObj().getParserFile());
			ArrayList syntaxList = SyntaxReader.read(
					fp.getObj().getParserDir(), fp.getObj().getParserFile());
			System.out.println("模板内容" + syntaxList);
			ArrayList wordList = FileReader.read(fp.getObj().getInputDir(), fp
					.getObj().getInputFile(), fp.getObj().getEncoding(),
					new Date(), false);
			System.out.println("文件内容" + wordList);
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

	//解析文件
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
