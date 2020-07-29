package gui;

import logic.client.XOClient;
import logic.XOException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginAndRegisterPanel extends GamePanel {
    private XOClient client;

    private JLabel gameLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField passwordField2;
    private JButton loginBtn;
    private JLabel userLabel;
    private JLabel passLabel;
    private JLabel pass2Label;
    private JButton registerBtn;
    private JLabel message;

    public LoginAndRegisterPanel(XOClient client) {
        this.client = client;
        createFields();
        initLogin();
    }

    void createFields() {
        gameLabel = new JLabel("XO");
        gameLabel.setFont(titleFont);

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        passwordField2 = new JPasswordField(15);
        usernameField.setFont(font3);
        passwordField.setFont(font3);
        passwordField2.setFont(font3);
        userLabel = new JLabel("username: ");
        passLabel = new JLabel("password: ");
        pass2Label = new JLabel("password: ");
        message = new JLabel("");
        userLabel.setFont(font3);
        passLabel.setFont(font3);
        pass2Label.setFont(font3);
        message.setFont(font3);
        registerBtn = new JButton("Create Account");
        loginBtn = new JButton( "        login         ");
        registerBtn.setFont(font3);
        loginBtn.setFont(font3);
    }

    private void initLogin() {
        setEmpty();
        setupLoginButtons();
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10,10,10,10);
        gc.gridy = 0;
        gc.gridx = 0;
        gc.gridheight = 5;
        gc.gridwidth = 2;
        add(gameLabel, gc);
        gc.gridheight = 1;
        gc.gridwidth = 1;
        /////////
        gc.gridy = 5;
        add(userLabel, gc);
        gc.gridx = 1;
        add(usernameField ,gc);
        /////////
        gc.gridy = 6;
        add(passwordField , gc);
        gc.gridx = 0;
        add(passLabel, gc);
        ////////
        gc.gridwidth = 1;
        gc.gridy = 7;
        add(registerBtn, gc);
        gc.gridx = 1;
        add(loginBtn,gc);
        ////////
        gc.gridwidth = 2;
        gc.gridy = 8;
        gc.gridx = 0;
        add(message, gc);
    }

    private void initSignIn() {
        setEmpty();
        setupRegisterButtons();
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10,10,10,10);
        gc.gridy = 0;
        gc.gridx = 0;
        gc.gridheight = 5;
        gc.gridwidth = 2;
        add(gameLabel, gc);
        gc.gridheight = 1;
        gc.gridwidth = 1;
        /////////
        gc.gridy = 5;
        add(userLabel, gc);
        gc.gridx = 1;
        add(usernameField ,gc);
        /////////
        gc.gridy = 6;
        add(passwordField , gc);
        gc.gridx = 0;
        add(passLabel, gc);
        /////////
        gc.gridy = 7;
        add(pass2Label, gc);
        gc.gridx = 1;
        add(passwordField2 , gc);
        ////////
        gc.gridwidth = 1;
        gc.gridy = 8;
        add(registerBtn, gc);
        gc.gridx = 0;
        add(loginBtn,gc);
        ////////
        gc.gridwidth = 2;
        gc.gridy = 9;
        gc.gridx = 0;
        add(message, gc);
    }

    private void setupLoginButtons() {
        removeActionListeners();
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            tryToLogin(username, password);
        });
        registerBtn.addActionListener(e -> {
            clearFields();
            initSignIn();
        });
    }

    private void setupRegisterButtons() {
        removeActionListeners();
        registerBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password1 = passwordField.getText();
            String password2 = passwordField2.getText();
            tryToRegister(username, password1,password2);
        });
        loginBtn.addActionListener(e -> {
            clearFields();
            initLogin();
        });
    }

    private void removeActionListeners() {
        for (ActionListener al : loginBtn.getActionListeners()) loginBtn.removeActionListener(al);
        for (ActionListener al : registerBtn.getActionListeners()) registerBtn.removeActionListener(al);
    }

    private void setEmpty() {
        message.setText(null);
        removeAll();
        revalidate();
        repaint();
    }

    private void clearFields() {
        usernameField.setText(null);
        passwordField.setText(null);
        passwordField2.setText(null);
    }

    private void tryToLogin(String userName, String pass) {
        try{
            client.tryToLogin(userName , pass);
        }catch (XOException e){
            message.setText(e.getMessage());
        }
    }

    private void tryToRegister(String username, String pass1, String pass2) {
        if (username.length() > 5 && pass1.length() > 5 && pass2.length() > 5) {
            if (!pass1.equals(pass2)) {
                message.setText("Entered passwords don't match. try again");
                message.setForeground(Color.RED);
            } else {
                try {
                    client.tryToRegister(username , pass1);
                    clearFields();
                    message.setText("Your Account has been made successfully");
                    message.setForeground(Color.GREEN);
                } catch (XOException ex) {
                    message.setText(ex.getMessage());
                    message.setForeground(Color.RED);
                }
            }
        } else {
            message.setText("You must enter at least 6 characters in each field");
            message.setForeground(Color.RED);
        }
    }

}
