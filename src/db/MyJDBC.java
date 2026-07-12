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



    public static int getHighscore(int spielerID) {

        try {

            Connection connection = DriverManager.getConnection(
                    CommonConstants.DB_URL,
                    CommonConstants.DB_USERNAME,
                    CommonConstants.DB_PASSWORD
            );

            PreparedStatement statement = connection.prepareStatement(

                    "SELECT highscore FROM " +
                            CommonConstants.DB_USERS_TABLE_NAME +
                            " WHERE id = ?"
            );

            statement.setInt(1, spielerID);

            ResultSet rs = statement.executeQuery();

            if(rs.next()) {
                return rs.getInt("highscore");
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static void updateHighscore(int spielerID, int highscore) {

        try {
            Connection connection = DriverManager.getConnection(
                    CommonConstants.DB_URL,
                    CommonConstants.DB_USERNAME,
                    CommonConstants.DB_PASSWORD
            );

            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE " + CommonConstants.DB_USERS_TABLE_NAME +
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