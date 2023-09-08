package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    public AccountDAO accountDAO;

    // CONSTRUCTOR: create a new AccountDAO object
    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    // CONSTRUCTOR: use a AccountDAO object passed in
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * TODO: Insert new User
     * 
     * @param user account to be inserted
     * @return Account if it was inserted, null otherwise
     */
    public Account insertUser(Account user) {
        return accountDAO.insertUser(user);
    }

    /**
     * TODO: Process a user login
     * 
     * @param user account to be verified
     * @return Account if it was verified, null otherwise
     */
    public Account verifyUser(Account user) {
        return accountDAO.verifyUser(user);
    }
}
