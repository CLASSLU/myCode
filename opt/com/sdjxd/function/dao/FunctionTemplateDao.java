package com.sdjxd.function.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sdjxd.pms.platform.data.DbOper;

import com.sdjxd.function.db.sql.SqlHelper;
import com.sdjxd.function.model.Argument;
import com.sdjxd.function.model.Constant;
import com.sdjxd.function.model.FunctionTemplate;

public class FunctionTemplateDao 
{
	private static SqlHelper sqlHelper = new SqlHelper();
	public FunctionTemplate getFunctionById(String id) throws Exception
	{
		String sql = sqlHelper.getMethod(id);
		ResultSet rs = DbOper.executeQuery(sql);
		FunctionTemplate funTmp = new FunctionTemplate();
		if(rs.next())
		{
			funTmp.setInfixExpression(rs.getString("FORMULA"));
			funTmp.setId(rs.getString("SHEETID"));
			funTmp.setName(rs.getString("METHOD_NAME"));
			sql = sqlHelper.getArgs(id);
			rs = DbOper.executeQuery(sql);
			List<Argument> args = new ArrayList<Argument>();
			List<Constant> cont = new ArrayList<Constant>();
			while(rs.next())
			{
				Argument arg = new Argument();
				arg.setName(rs.getString("ARG_NAME"));
				arg.setSymbol(rs.getString("ARG_SYMBOL"));
				arg.setId(rs.getString("SHEETID"));
				int argType = rs.getInt("ARG_TYPE");
				double argValue = rs.getDouble("ARG_VALUE");
				if(argType==Argument.CONST_VARIABLE)
				{
					cont.add(new Constant(arg, argValue));
				}
				else if(argType == Argument.VARIABLE)
					args.add(arg);
				else
				{
					throw new Exception("错误的参数类型:"+argType);
				}
			}
			funTmp.setArguments(args);
			funTmp.setConstants(cont);
		}
		return funTmp;
	}
}
