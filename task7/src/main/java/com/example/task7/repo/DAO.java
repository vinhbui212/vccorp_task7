package com.example.task7.repo;

import com.example.task7.model.User;

import java.util.List;

public interface DAO {
    void insertUser(User user) throws Exception;
    void updateUser(int id,User user) throws  Exception;
    void deleteUser(int id) throws Exception;
    List<User> searchByName(String name) throws Exception;
    User searchById(int id) throws Exception;
    List<User> searchByAddress(String address) throws Exception;
    List<User> arrangeByName() throws Exception;
    void addMoney(int userId,long amount) throws Exception ;
    void transferMoney(int userAId, int userBId, long amount) throws Exception;
     long checkBalance(int userId) throws Exception ;


    }
