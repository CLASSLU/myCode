package com.sdjxd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

public class MimeTest {
	/**
	 * 探测文件mime,查看指定文件mimes中是否包含指定的mime
	 * 
	 * @param filePath
	 * @param mime
	 * @return
	 * @throws IOException
	 */
	public static boolean getMimeTest(String filePath, String mime)
			throws IOException {
		FileInputStream is = null;
		try {
			is = new FileInputStream(filePath);

			ContentHandler contenthandler = new BodyContentHandler();
			Metadata metadata = new Metadata();
			metadata.set(Metadata.RESOURCE_NAME_KEY, filePath);
			Parser parser = new AutoDetectParser();
			// OOXMLParser parser = new OOXMLParser();
			parser.parse(is, contenthandler, metadata, new ParseContext());
			if (mime.equals(metadata.get(Metadata.CONTENT_TYPE))) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (is != null)
				is.close();
		}
	}

	/**
	 * 探测文件mime,查看指定文件mimes中是否包含指定的mime
	 * 
	 * @param file
	 * @param mime
	 * @return
	 */
	public static boolean getMimeTest(File file, String mime) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);

			ContentHandler contenthandler = new BodyContentHandler();
			Metadata metadata = new Metadata();
			metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
			Parser parser = new AutoDetectParser();
			// OOXMLParser parser = new OOXMLParser();
			parser.parse(is, contenthandler, metadata, new ParseContext());
			if (mime.equals(metadata.get(Metadata.CONTENT_TYPE))) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static String getMimeType(File file) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);

			ContentHandler contenthandler = new BodyContentHandler();
			Metadata metadata = new Metadata();
			metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
			Parser parser = new AutoDetectParser();
			// OOXMLParser parser = new OOXMLParser();
			parser.parse(is, contenthandler, metadata, new ParseContext());
			return metadata.get(Metadata.CONTENT_TYPE);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static void main(String[] args) {
		String c = getMimeType(new File("C:\\test.xlsx44"));
		System.out.println(c);
	}
}
