package db;

public class MyJDBC {
    public static boolean register(String nickname, String password) {

    }

    public static boolean checkUser(String username){
        try{
            Connection connection = DriveManager.getConnection(CommonConstants.DB_URL, CommonConstans.DB_PASSWORD);

            PreparedStatement checkUserExists = connection.prepareStatement(
                    "SELECT * FROM " + CommonConstants.DB_USERS_TABLE_NAME + "WHERE USERNAME = ?"
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
}


