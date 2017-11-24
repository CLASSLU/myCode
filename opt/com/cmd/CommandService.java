package com.cmd;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import com.spring.dbservice.TransactionUtils;

public class CommandService {

	/**
	 * 
	 * @param cmd
	 * @return
	 */
	public static Object exec(ICommand cmd) {
		Object result = null;
		TransactionStatus txStatus = TransactionUtils.getDefaultTransaction();
		PlatformTransactionManager txManager = TransactionUtils
				.getDefaultTransactionManager();
		try {
			result = cmd.excute();
			txManager.commit(txStatus);
		} catch (Exception e) {
			txManager.rollback(txStatus);
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 
	 * @param cmds
	 * @return
	 */
	public static List<Object> exec(List<ICommand> cmds) {
		List<Object> results = new ArrayList<Object>();
		TransactionStatus txStatus = TransactionUtils.getDefaultTransaction();
		PlatformTransactionManager txManager = TransactionUtils
				.getDefaultTransactionManager();
		try {
			for (ICommand cmd : cmds) {
				results.add(cmd.excute());
			}
			txManager.commit(txStatus);
		} catch (Exception e) {
			txManager.rollback(txStatus);
			throw new RuntimeException(e);
		}
		return results;
	}

}
