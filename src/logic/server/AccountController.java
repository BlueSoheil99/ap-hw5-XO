package logic.server;

import logic.XOException;

public class AccountController {
    private static AccountController instance;

    private AccountController() {

    }

    public static AccountController getInstance() {
        if (instance == null) instance = new AccountController();
        return instance;
    }

    void register(String userName, String password) throws XOException {
        if (true){
//            Account account = new Account();  use Gson
//            saveAccount(account);
//            return account;
        }else throw new XOException("unAvailable userName");
    }

    Account login(String useName, String password) throws XOException {
        if (true){
//            Account account = new Account();  use Gson
//            saveAccount(account);
//            return account;
            return null;
        }else throw new XOException("wrong userName or Password");
    }

    void saveAccount(Account account) {

    }
}
