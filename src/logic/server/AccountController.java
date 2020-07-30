package logic.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import logic.ResourceManager;
import logic.XOException;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class AccountController {
    private static AccountController instance;
    private static String profilesPath = ResourceManager.getInstance().getProfilesPath();


    private AccountController() {
    }

    public static AccountController getInstance() {
        if (instance == null) instance = new AccountController();
        return instance;
    }


    void register(String userName, String password) throws XOException {
        if (new File(profilesPath + "/" + userName + ".json").exists()) {
            throw new XOException("USERNAME IS INVALID");
        } else {
            Account account = new Account(userName, password);
            saveAccount(account);
            System.out.println(account.getName() + " created and saved");
        }

    }

    Account login(String useName, String password) throws XOException {
        if (allPlayersContain(useName, password)) {
            Account account = getAccount(useName);
            System.out.println(useName + " logged in");
            return account;
        } else {
            throw new XOException("username or password is incorrect");
        }
    }

    void saveAccount(Account account) {
        try {
            FileWriter writer = new FileWriter(profilesPath + "/" + account.getName() + ".json");
            PrintWriter printer = new PrintWriter(writer);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(account);
            printer.println(json);
            printer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private Account getAccount(String username) {
        Account player = null;
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        try {
            FileReader reader = new FileReader(profilesPath + "/" + username + ".json");
            player = gson.fromJson(jsonParser.parse(reader), Account.class); //here we make a json element and then turn it into a Player object
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return player;
    }

    private boolean allPlayersContain(String username, String password) {
        boolean ans = false;

        try {
            JsonParser jsonParser = new JsonParser();

            FileReader reader = new FileReader(profilesPath + "/" + username + ".json");
            // if this file doesn't exist, we handle the exception with no message and we print an error in signUP panel instead
            // , because this method is designed to give us true or false and exception is not expected from allPlayersContain
            JsonObject json = (JsonObject) jsonParser.parse(reader);
            reader.close();

            String playerRealPassword = json.get("password").toString();   //it gives us a string with additional quotation marks
            playerRealPassword = playerRealPassword.substring(1, playerRealPassword.length() - 1);  //we remove extra quotation marks
            if (playerRealPassword.equals(password)) {
                ans = true;
            }
            // if passwords don't match, we handle the exception with no message and we print an error in CLIRunner instead
            // , because this method is designed to give us true or false and "exception" is not expected from allPlayersContain

        } catch (FileNotFoundException e) {
//            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ans;
    }

    ArrayList<Account> getSortedAccounts() {
        // every time this method is called, new accounts get made in memory
        ArrayList<Account> accounts = new ArrayList<>();
        String[] allAccounts = new File(profilesPath).list();
        for (String accountName : allAccounts) {
            accountName = accountName.substring(0, accountName.length() - 5); //because of '.json' thing we have '-5'
            accounts.add(getAccount(accountName));
        }
        Collections.sort(accounts, Comparator.comparingInt(Account::getScore));
        Collections.reverse(accounts);
        return accounts;
    }

    void increaseWins(Account account){
        account.increaseWins();
        saveAccount(account);
    }
    void increaseLosses(Account account){
        account.increaseLosses();
        saveAccount(account);
    }


}
