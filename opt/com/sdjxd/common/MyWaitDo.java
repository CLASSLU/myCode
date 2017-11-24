package com.sdjxd.common;

import com.sdjxd.pms.platform.workflow.service.WaitDo;

public class MyWaitDo {
	/**
	 * 
	 * @param operId
	 *            ����ʵ��ID
	 * @param operName
	 *            ��������
	 * @param operUrl
	 *            �򿪵�ַ
	 * @param sender
	 *            ������
	 * @param receiverType
	 *            ��������
	 * @param receiver
	 *            ������
	 * @param resLimit
	 *            ��Դ�޶�
	 * @param operContent
	 *            ����
	 * @param openType
	 *            �򿪷�ʽ
	 */
	public static void addWaitDo(String operId, String operName,
			String operUrl, String sender, int receiverType, String receiver,
			String resLimit, String operContent, String openType) {
		WaitDo addWait;
		try {

			if (receiver.contains(",")) {
				String[] receiverArray = receiver.split(",");
				for (String receiverId : receiverArray) {
					addWait = WaitDo.create(operId, operName, operUrl, sender,
							receiverType, receiverId, resLimit, operContent,
							openType);
					addWait.save();
				}

			} else {
				addWait = WaitDo
						.create(operId, operName, operUrl, sender,
								receiverType, receiver, resLimit, operContent,
								openType);
				addWait.save();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param operId
	 * @return
	 * @throws Exception
	 */
	public static String delWaitDoAll(String operId) throws Exception {
		WaitDo delWait = WaitDo.delete(operId);

		delWait.save();
		return null;
	}

	/**
	 * 
	 * @param operId
	 * @param userId
	 * @throws Exception
	 */
	public static void delWaitDoUser(String operId, String userId)
			throws Exception {
		WaitDo delWait = WaitDo.delete(operId, userId);

		delWait.save();
	}
}
