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
//			out.write("ҳ���������,����Ŀ����û����Ȩ������ϵ����Ա");
			out.write("��û�и���Ŀ�����Ȩ��");
		} catch (IOException e) {
			
			throw new TemplateException(
					"�������: " + e, env);
		}
	}
}
