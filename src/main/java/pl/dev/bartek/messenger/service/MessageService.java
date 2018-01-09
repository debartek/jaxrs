package pl.dev.bartek.messenger.service;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.dev.bartek.messenger.database.Database;
import pl.dev.bartek.messenger.exception.DataNotFoundException;
import pl.dev.bartek.messenger.model.Comment;
import pl.dev.bartek.messenger.model.Message;

public class MessageService {
	
	private Map<Long,Message> messages = Database.getMessages();
	
	public MessageService() {
		messages.put(1L, new Message(1, "hello world", "3 2 0"));
		messages.put(2L, new Message(2, "Helllo Jery", "Roling"));
	}
	
	public List<Message> getAllMessages(){
		return new ArrayList<Message>(messages.values());
	}
	
	public List<Message> getMessagesByYear(int year){
		List<Message> messagesByYear = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		for(Message message : messages.values()) {
			cal.setTime(message.getCreated());
			if(cal.get(Calendar.YEAR) == year) {
				messagesByYear.add(message);
			}
		}
		return messagesByYear;
	}
	
	public List<Message> getMessagesPaginated(int start, int size){
		ArrayList<Message> list = new ArrayList<Message> (messages.values());
		if (start + size > list.size()) {
			return new ArrayList<Message>();
		}
		else {
			return list.subList(start, start+size);
		}
		
	}
	
	public Message getMessage(long id) {
		Message message = messages.get(id);
		if(message == null) {
			throw new DataNotFoundException("Message with id: "+id+" Not found");
		}
		return message;
	}
	
	public Message addMessage(Message message) {
		message.setId(messages.size()+1);
		messages.put(message.getId(), message);
		return message;
	}
	
	public Message updateMessage(Message message) {
		if (message.getId() <=0) {
			return null;
		}
		messages.put(message.getId(), message);
		return message;
	}
	
	public Message removeMessage(long id) {
		return messages.remove(id);
	}
}
