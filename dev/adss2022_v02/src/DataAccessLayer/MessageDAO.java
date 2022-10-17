package DataAccessLayer;


import BussinessLayer.Objects.Message;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public class MessageDAO extends DAO {

    private static String TableName = "Messages";
    public static final String IDColumnName = "ID";
    public static final String JobTypeColumnName = "jobType";
    public static final String MessageColumnName = "message";
    public static final String ReadColumnName = "read";
    private HashMap<Integer, Message> messagesCache;

    public MessageDAO() {
        super(TableName);
        messagesCache = new HashMap<>();
    }

    public void resetCache() {
        messagesCache.clear();
    }

    public boolean Insert(Object messageObj) {
        Message message = (Message) messageObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1},{2},{3},{4}) VALUES(?, ?, ?, ?) "
                , _tableName, IDColumnName, JobTypeColumnName, MessageColumnName, ReadColumnName
        );

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, message.getIdMessage());
            pstmt.setString(2, message.getJob());
            pstmt.setString(3, message.getMessage());
            pstmt.setBoolean(4, message.isRead());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public boolean Delete(Object messageObj) {
        Message message = (Message) messageObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ?"
                , _tableName, IDColumnName);

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, message.getIdMessage());
            pstmt.executeUpdate();
            if (messagesCache.containsKey(message.getIdMessage()))
                messagesCache.remove(message.getIdMessage(), message);

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public Message getMessage(int idMessage) {
        if (messagesCache.containsKey(idMessage)) //message in cache
            return messagesCache.get(idMessage);
        else { //truck in db
            List<Message> result = Select(makeList(IDColumnName), makeList(String.valueOf(idMessage)));
            if (result.size() == 0)
                throw new IllegalArgumentException("message " + idMessage + " does not exist");
            Message message = result.get(0);
            messagesCache.put(message.getIdMessage(), message); //insert to cache
            return message;
        }
    }

    @Override
    public Object convertReaderToObject(ResultSet rs) throws SQLException, ParseException {
        if (messagesCache.containsKey(rs.getInt(1)))
            return messagesCache.get(rs.getInt(1));
        Message message = new Message(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4));
        messagesCache.put(message.getIdMessage(), message);
        return message;
    }


    public List<Message> SelectAllMessages() {
        List<Message> list = (List<Message>) (List<?>) Select();
        return list;
    }

    public void updateMessageIsRead(int idMessage) {
        getMessage(idMessage).setRead(true);
        Update(ReadColumnName, true, makeList(IDColumnName), makeList(String.valueOf(idMessage)));
    }
}

