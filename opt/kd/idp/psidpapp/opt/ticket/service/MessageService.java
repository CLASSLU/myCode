package kd.idp.psidpapp.opt.ticket.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import flex.messaging.MessageBroker;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.util.UUIDUtils;

public class MessageService {
	
	/*******************��Ϣ����****************/
	public static boolean pushMessages(String subtopics, String userIds, Map<String, Object> messageMap){
		if (StringUtils.isNotEmpty(subtopics) && StringUtils.isNotEmpty(userIds) && null != messageMap){
			MessageBroker msgBroker = MessageBroker.getMessageBroker(null);
			String subtopicsArray[] = subtopics.split(",");
			for (String subtopic : subtopicsArray){
				AsyncMessage msg = new AsyncMessage();
				msg.setDestination("message-push");
				msg.setHeader(AsyncMessage.SUBTOPIC_HEADER_NAME, subtopic);
				msg.setHeader("USERID", userIds);
				msg.setClientId(UUIDUtils.createUUID());
				msg.setMessageId(UUIDUtils.createUUID());
				msg.setTimestamp(System.currentTimeMillis());
				messageMap.put("CONTENT", subtopics + ":��Ϣ���ĳɹ���");
				msg.setBody(messageMap);
				msgBroker.routeMessageToService(msg, null);
			}
			return true;
		}
		return false;
	}

}
