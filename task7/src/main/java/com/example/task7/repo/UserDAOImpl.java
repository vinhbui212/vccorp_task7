package com.example.task7.repo;

import com.example.task7.exception.UserException;
import com.example.task7.model.User;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAOImpl implements DAO {

    private static final String INSERT_USER_SQL = "INSERT INTO user (name, address, age) VALUES (?, ?, ?)";
    private static final String UPDATE_USER_SQL = "UPDATE user SET name = ?, address = ?, age = ? WHERE id = ?";
    private static final String DELETE_USER_SQL = "DELETE FROM user WHERE id = ?";
    private static final String SELECT_USER_BY_ID = "SELECT id, name, address, age, money FROM user WHERE id = ?";
    private static final String SELECT_USERS_BY_NAME = "SELECT id, name, address, age FROM user WHERE name LIKE ?";
    private static final String SELECT_USERS_BY_ADDRESS = "SELECT id, name, address, age FROM user WHERE address LIKE ?";
    private static final String SELECT_ALL_USERS_ORDER_BY_NAME = "SELECT id, name, address, age FROM user ORDER BY name";
    private static final String ADD_MONEY_SQL = "UPDATE user SET money = money + ? WHERE id = ?";
    private static final String TRANSFER_MONEY_SQL = "UPDATE user SET money = money - ? WHERE id = ?";
    private static final String RECEIVE_MONEY_SQL = "UPDATE user SET money = money + ? WHERE id = ?";
    private static final String CHECK_BALANCE_SQL = "SELECT money FROM user WHERE id = ?";

    private final HikariDataSource dataSource;
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000;

    @Autowired
    public UserDAOImpl(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }



    @Override
    public void insertUser(User user) throws Exception {

            if (StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getAddress()) ||
                    user.getAge() < 1 || user.getAge() > 100) {
                throw new UserException("Check form again", true, 902);
            }
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {

                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getAddress());
                preparedStatement.setInt(3, user.getAge());
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                throw new UserException("Add fail", true, 404);
            }


    }

    @Override
    public void updateUser(int id, User user) throws Exception {

            if (StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getAddress()) ||
                    user.getAge() < 1 || user.getAge() > 100) {
                throw new UserException("Check form again", true, 902);
            }
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL)) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getAddress());
                preparedStatement.setInt(3, user.getAge());
                preparedStatement.setInt(4, id);
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                throw new UserException("Update fail", true, 404);
            }

    }

    @Override
    public void deleteUser(int id) throws Exception {

            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_SQL)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            } catch (Exception e) {
                throw new UserException("Delete fail", true, 404);
            }

    }

    @Override
    public List<User> searchByName(String name) throws Exception {

            List<User> users = new ArrayList<>();
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERS_BY_NAME)) {
                preparedStatement.setString(1, "%" + name + "%");
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String username = rs.getString("name");
                    String address = rs.getString("address");
                    int age = rs.getInt("age");
                    users.add(new User(id, username, address, age));
                }
            } catch (Exception e) {
                throw new UserException("Can't not find", true, 404);
            }
            return users;

    }

    @Override
    public User searchById(int id) throws Exception {

            User user = null;
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID)) {
                preparedStatement.setInt(1, id);
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    int age = rs.getInt("age");
                    long money = rs.getLong("money");
                    user = new User(id, name, address, age, money);
                }
            } catch (Exception e) {
                throw new UserException("Can't find this id", true, 404);
            }
            return user;

    }

    @Override
    public List<User> searchByAddress(String address) throws Exception {

            List<User> users = new ArrayList<>();
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USERS_BY_ADDRESS)) {
                preparedStatement.setString(1, "%" + address + "%");
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String userAddress = rs.getString("address");
                    int age = rs.getInt("age");
                    users.add(new User(id, name, userAddress, age));
                }
            } catch (Exception e) {
                throw new UserException("Find fail", true, 404);
            }
            return users;

    }

    @Override
    public List<User> arrangeByName() throws Exception {

            List<User> users = new ArrayList<>();
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS_ORDER_BY_NAME)) {
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    int age = rs.getInt("age");
                    users.add(new User(id, name, address, age));
                }
            } catch (Exception e) {
                throw new UserException("Get fail", true, 404);
            }
            return users;

    }

    @Override
    public void addMoney(int userId, long amount) throws Exception {

            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(ADD_MONEY_SQL)) {
                preparedStatement.setLong(1, amount);
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new UserException("Failed to add money", true, 500);
            }

    }

    @Override
    public synchronized void transferMoney(int userAId, int userBId, long amount) throws Exception {

            try (Connection connection = getConnection()) {
                connection.setAutoCommit(false);

                try (PreparedStatement checkBalanceStmt = connection.prepareStatement(CHECK_BALANCE_SQL)) {
                    checkBalanceStmt.setInt(1, userAId);
                    ResultSet rs = checkBalanceStmt.executeQuery();
                    if (rs.next()) {
                        long balance = rs.getLong("money");

                        if (balance < amount) {
                            throw new UserException("Insufficient funds", true, 400);
                        }
                    } else {
                        throw new UserException("User A not found", true, 404);
                    }
                }

                // tru tien
                try (PreparedStatement deductStmt = connection.prepareStatement(TRANSFER_MONEY_SQL)) {
                    deductStmt.setLong(1, amount);
                    deductStmt.setInt(2, userAId);
                    deductStmt.executeUpdate();
                }

                // cong tien
                try (PreparedStatement addStmt = connection.prepareStatement(RECEIVE_MONEY_SQL)) {
                    addStmt.setLong(1, amount);
                    addStmt.setInt(2, userBId);
                    addStmt.executeUpdate();
                }

                connection.commit();
            } catch (SQLException e) {
                throw new UserException("Failed to transfer money", true, 500);
            }

    }

    @Override
    public long checkBalance(int userId) throws Exception {

            long balance = 0;
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(CHECK_BALANCE_SQL)) {
                preparedStatement.setInt(1, userId);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    balance = rs.getLong("money");
                } else {
                    throw new UserException("User not found", true, 404);
                }
            } catch (SQLException e) {
                throw new UserException("Failed to check balance", true, 500);
            }
            return balance;

    }

   
}
