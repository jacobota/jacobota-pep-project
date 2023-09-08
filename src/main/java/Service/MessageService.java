package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    public MessageDAO messageDao;

    // CONSTRUCTOR: create a new messageDAO object
    public MessageService() {
        this.messageDao = new MessageDAO();
    }

    // CONSTRUCT: use a messageDAO object that is passed in
    public MessageService(MessageDAO messageDAO) {
        this.messageDao = messageDAO;
    }

    /**
     * TODO: Insert a new message
     * 
     * @param message message to be created
     * @return message if it was persisted, null otherwise
     */
    public Message insertMessage(Message message) {
        return messageDao.insertMessage(message);
    }

     /**
     * TODO: Retrieve all Messages
     * 
     * @return list of all messages
     */
    public List<Message> getAllMessages() {
        return messageDao.getAllMessages();
    }

    /**
     * TODO: Retrieve message by ID
     * 
     * @param message_id message id to get specific message
     * @return message by ID, else null
     */
    public Message getMessageByID(int message_id) {
        return messageDao.getMessageByID(message_id);
    }

    /**
     * TODO: Delete message by ID
     * 
     * @param message_id message  to delete
     * @return deleted message by ID, else null
     */
    public Message deleteMessageByID(int message_id) {
        return messageDao.deleteMessageByID(message_id);
    }

    /**
     * TODO: Update message by ID
     * 
     * @param message_id message to update
     * @return updated message by ID, else null
     */
    public Message updateMessageByID(int message_id, Message message) {
        return messageDao.updateMessageByID(message_id, message);
    }

    /**
     * TODO: Get message by Account ID
     * 
     * @param posted_by account that posted message
     * @return list of all messages from account_id
     */
    public List<Message> getMessageByAccountID(int posted_by) {
        return messageDao.getMessageByAccountID(posted_by);
    }
}