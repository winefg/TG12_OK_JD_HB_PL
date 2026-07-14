package db;

import java.sql.*;
import constants.CommonConstants;

public class MyJDBC {

    public static int register(String nickname, String password) {
        try {
            if(!checkUser(nickname, password)) {
                Connection connection = DriverManager.getConnection(CommonConstants.DB_URL,
                        CommonConstants.DB_USERNAME, CommonConstants.DB_PASSWORD);

                PreparedStatement insertUser = connection.prepareStatement(
                        "INSERT INTO " + CommonConstants.DB_USERS_TABLE_NAME +
                                "(nickname, password)" +
                                "VALUES(?, ?)",
                                Statement.RETURN_GENERATED_KEYS
                );

                insertUser.setString(1, nickname);
                insertUser.setString(2, password);

                insertUser.executeUpdate();
                ResultSet generatedKeys = insertUser.getGeneratedKeys();
                if(generatedKeys.next()){
                    int userID = generatedKeys.getInt(1);
                    System.out.println("Created user ID: " + userID);
// Highscore für neuen Spieler erzeugen (= 0)
                    PreparedStatement insertHighscore = connection.prepareStatement(
                            "INSERT INTO " + CommonConstants.DB_HIGHSCORES_TABLE_NAME +
                                    "(idusers, score)" +
                                    "VALUES(?, ?)"
                    );
                    insertHighscore.setInt(1, userID);
                    insertHighscore.setInt(2, 0);

                    insertHighscore.executeUpdate();
                    System.out.println(
                            "Highscore created for user: " + userID
                    );
                    return userID;

                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return -1;
    }

    public static boolean checkUser(String nickname, String password){

        nickname = nickname.trim();
        password = password.trim();

        try{
            Connection connection = DriverManager.getConnection(CommonConstants.DB_URL,
                    CommonConstants.DB_USERNAME, CommonConstants.DB_PASSWORD);

            PreparedStatement checkUserExists = connection.prepareStatement(
                    "SELECT * FROM " + CommonConstants.DB_USERS_TABLE_NAME +
                            " WHERE NICKNAME = ? AND PASSWORD = ?"
            );
            checkUserExists.setString(1, nickname);
            checkUserExists.setString(2, password);

            System.out.println("Searching:");
            System.out.println("Nickname: " + nickname);
            System.out.println("Password: " + password);

           /* System.out.println(                                                   überprüfen
                    "SELECT * FROM " + CommonConstants.DB_USERS_TABLE_NAME +
                            " WHERE nickname = '" + nickname +
                            "' AND password = '" + password + "'"
            ); */

            ResultSet resultSet = checkUserExists.executeQuery();
            if(resultSet.next()){
                System.out.println("USER FOUND!");
                return true;
            }
        } catch(SQLException e){

            e.printStackTrace();
        }
        System.out.println("USER NOT FOUND!");
        return false;
    }


    public static int login(String nickname, String password){
        nickname = nickname.trim();
        password = password.trim();
        try {
            Connection connection = DriverManager.getConnection(
                    CommonConstants.DB_URL,
                    CommonConstants.DB_USERNAME,
                    CommonConstants.DB_PASSWORD
            );
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT idusers FROM " + CommonConstants.DB_USERS_TABLE_NAME +
                            " WHERE nickname = ? AND password = ?"
            );
            statement.setString(1, nickname);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();

            if(rs.next()){
                int id = rs.getInt("idusers");
                System.out.println("LOGIN SUCCESS. USER ID: " + id);
                return id;
            }


        } catch(SQLException e){
            e.printStackTrace();
        }
        System.out.println("LOGIN FAILED");
        return -1;
    }


    //_________________________________________________________________________________________________



    // Der "Datenbank-Getter": Holt den Score live aus der DB
    public static int getHighscore(int idusers) {
        try {
            Connection connection = DriverManager.getConnection(
                    CommonConstants.DB_URL, CommonConstants.DB_USERNAME, CommonConstants.DB_PASSWORD);

            String sql = "SELECT score FROM " + CommonConstants.DB_HIGHSCORES_TABLE_NAME +
                            " WHERE idusers = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idusers);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("score"); // Liefert den Score zurück
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Standardwert, falls nichts gefunden wurde
    }

    // Der "Datenbank-Setter": Aktualisiert den Score live in der DB
    public static void setScoreInDatenbank(String nickname, int neuerScore) {
        try {
            Connection connection = DriverManager.getConnection(
                    CommonConstants.DB_URL, CommonConstants.DB_USERNAME, CommonConstants.DB_PASSWORD);

            String sql = "UPDATE " + CommonConstants.DB_HIGHSCORES_TABLE_NAME + " SET score = ? WHERE nickname = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, neuerScore);
            statement.setString(2, nickname);

            statement.executeUpdate(); // Schreibt den Wert in die DB
            System.out.println("Score erfolgreich in DB aktualisiert!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void updateHighscore(int idusers, int score) {

        try {
            Connection connection = DriverManager.getConnection(
                    CommonConstants.DB_URL,
                    CommonConstants.DB_USERNAME,
                    CommonConstants.DB_PASSWORD
            );

            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE " + CommonConstants.DB_HIGHSCORES_TABLE_NAME +
                            " SET score = ? WHERE idusers = ?"
            );

            statement.setInt(1, score);
            statement.setInt(2, idusers);

            statement.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

}