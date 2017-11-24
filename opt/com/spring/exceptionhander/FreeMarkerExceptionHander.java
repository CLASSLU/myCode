package com.spring.exceptionhander;
import java.io.IOException;
import java.io.Writer;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
public class FreeMarkerExceptionHander implements TemplateExceptionHandler {

	public void handleTemplateException(TemplateException te, Environment env,
			Writer out) throws TemplateException {
		try {
//			out.write("页面解析错误,该栏目可能没有授权，请联系管理员");
			out.write("您没有该栏目的浏览权限");
		} catch (IOException e) {
			
			throw new TemplateException(
					"输出错误: " + e, env);
		}
	}
}
