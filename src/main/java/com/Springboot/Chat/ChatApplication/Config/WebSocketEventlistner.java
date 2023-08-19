package com.Springboot.Chat.ChatApplication.Config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.Springboot.Chat.ChatApplication.Chat.ChatMessage;
import com.Springboot.Chat.ChatApplication.Chat.MessageType;



import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventlistner {
	
	private SimpMessageSendingOperations temp;
	@EventListener
	public void HandleSessionDisconnectEvent( SessionDisconnectEvent event) {
		
		StompHeaderAccessor header = StompHeaderAccessor.wrap(event.getMessage());
		String username = (String) header.getSessionAttributes().get("username");
		if(username != null) {
			log.info("User Disconnected {}",username);
			var chatMessage = ChatMessage.builder()
					.type(MessageType.LEAVE)
					.sender(username)
					.build();
			temp.convertAndSend("/topic/public",chatMessage);
		}
		
	}
}
