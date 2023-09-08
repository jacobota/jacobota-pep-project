package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {
    /**
     * TODO: Insert new User
     * 
     * @param user account to be inserted
     * @return Account if it was inserted, null otherwise
     */
    public Account insertUser(Account user) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // SQL Statement to Insert into the DB
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Write into the prepared statements and execute
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());

            ps.executeUpdate();

            // get account id from result set
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                int generatedAccountID = (int) rs.getLong(1);
                return new Account(generatedAccountID, user.getUsername(), user.getPassword());
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * TODO: Process a user login
     * 
     * @param user account to be verified
     * @return Account if it was verified, null otherwise
     */
    public Account verifyUser(Account user) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // SQL Statement to select an account and check it
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            // Write into the prepared statements and execute
            ps.setString(1, user.username);
            ps.setString(2, user.password);

            // get result set from query if the if statement succeeds then return the account
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
