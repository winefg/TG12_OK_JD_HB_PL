package db;

import java.sql.*;
import constants.CommonConstants;

public class MyJDBC {

    public static boolean register(String nickname, String password) {
        try {
            if(!checkUser(nickname)) {
                Connection connection = DriverManager.getConnection(CommonConstants.DB_URL,
                        CommonConstants.DB_USERNAME, CommonConstants.DB_PASSWORD);

                PreparedStatement insertUser = connection.prepareStatement(
                        "INSERT INTO " + CommonConstants.DB_USERS_TABLE_NAME + "(nickname, password)" +
                                "VALUES(?, ?)"
                );

                insertUser.setString(1, nickname);
                insertUser.setString(2, password);

                insertUser.executeUpdate();
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    public static boolean checkUser(String nickname){
        try{
            Connection connection = DriverManager.getConnection(CommonConstants.DB_URL,
                    CommonConstants.DB_USERNAME, CommonConstants.DB_PASSWORD);

            PreparedStatement checkUserExists = connection.prepareStatement(
                    "SELECT * FROM " + CommonConstants.DB_USERS_TABLE_NAME +
                            " WHERE NICKNAME = ?"
            );
            checkUserExists.setString(1, nickname);

            ResultSet resultSet = checkUserExists.executeQuery();

            if (!resultSet.isBeforeFirst()){
                return false;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return true;
    }


    //_________________________________________________________________________________________________



    // Der "Datenbank-Getter": Holt den Score live aus der DB
    public static int getScoreAusDatenbank(int idusers) {
        try {
            Connection connection = DriverManager.getConnection(
                    CommonConstants.DB_URL, CommonConstants.DB_USERNAME, CommonConstants.DB_PASSWORD);

            String sql = "SELECT score FROM highScores WHERE idusers = ?";
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
    public static void setScoreInDatenbank(int idusers, int neuerScore) {
        try {
            Connection connection = DriverManager.getConnection(
                    CommonConstants.DB_URL, CommonConstants.DB_USERNAME, CommonConstants.DB_PASSWORD);

            String sql = "UPDATE highScores SET score = ? WHERE idusers = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, neuerScore);
            statement.setInt(2, idusers);

            statement.executeUpdate(); // Schreibt den Wert in die DB
            System.out.println("Score erfolgreich in DB aktualisiert!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }














    public static void updateHighscore(int spielerID, int highscore) {

        try {
            Connection connection = DriverManager.getConnection(
                    CommonConstants.DB_URL,
                    CommonConstants.DB_USERNAME,
                    CommonConstants.DB_PASSWORD
            );

            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE " + CommonConstants.DB_HIGHSCORES_TABLE_NAME +
                            " SET highscore = ? WHERE id = ?"
            );

            statement.setInt(1, highscore);
            statement.setInt(2, spielerID);

            statement.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

}