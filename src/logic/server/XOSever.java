package logic.server;

import logic.XOException;

import java.util.ArrayList;

public class XOSever {
    private AccountController accountController;
    private ArrayList<Account> accounts;
    private static String serverIP = "localhost";
    private static int defaultPort = 8000;

    XOSever(){
        accountController = AccountController.getInstance();
        accounts = new ArrayList<>();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    void register(String username , String pass) throws XOException {
        accountController.register(username , pass);
    }
    void login(String username , String pass) throws XOException {
        accounts.add(accountController.login(username , pass));
    }

    public static void main(String[] args)  {
        XOSever sever = new XOSever();
    }


}
