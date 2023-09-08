package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    /**
     * TODO: Insert a New Message
     * 
     * @param message message to be inserted
     * @return message that is inserted, null otherwise
     */
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // SQL Statement to Insert into the DB
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Write into the prepared statements and execute
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();

            // return the new message with the new messageID
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                int generatedMessageID = (int) rs.getLong(1);
                return new Message(generatedMessageID, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TODO: Retrieve all messages
     * 
     * @return List of all the messages 
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> returnList = new ArrayList<>();
        try {
            // SQL Statement to select all records from the DB
            String sql = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement(sql);

            // put all records into the return list
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                returnList.add(message);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return returnList;
    }

    /**
     * TODO: Retrieve message by ID
     * 
     * @param message_id message id to get specific message
     * @return message by ID, else null
     */
    public Message getMessageByID(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // SQL Statement to select all records from the DB
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, id);

            // put all records into the return list
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TODO: Delete message by ID
     * 
     * @param message_id message  to delete
     * @return deleted message by ID, else null
     */
    public Message deleteMessageByID(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // SQL Statement to select all records from the DB
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, message_id);

            // put all records into the return list
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

     /**
     * TODO: Update message by ID
     * 
     * @param message_id message to update
     * @return updated message by ID, else null
     */
    public Message updateMessageByID(int message_id, Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
             // SQL Statement to select all records from the DB
             String sql = "UPDATE message SET message_text = ?, time_posted_epoch = ? WHERE message_id = ?";
             PreparedStatement ps = connection.prepareStatement(sql);
 
             ps.setString(1, message.message_text);
             ps.setLong(2, message.time_posted_epoch);
             ps.setInt(3, message_id);
 
             // execute the query
             ps.executeUpdate();
             
             // select the newly updated message
             return getMessageByID(message_id);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TODO: Get message by Account ID
     * 
     * @param posted_by account that posted message
     * @return list of all messages from account_id
     */
    public List<Message> getMessageByAccountID(int posted_by) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> returnList = new ArrayList<>();
        try {
            // SQL Statement to select all records from the DB
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, posted_by);

            // put all records into the return list
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                returnList.add(message);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return returnList;
    }
}
