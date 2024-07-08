package com.example.task7;

import com.example.task7.model.User;
import com.example.task7.repo.DAO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private DAO dao;

    public void addNewUser(User user) throws Exception {
        dao.insertUser(user);


    }
    public void updateUser(User user,int id) throws Exception {
        dao.updateUser(id, user);
    }
    public void deleteUser(int id) throws Exception {
        dao.deleteUser(id);
    }

    public List<User> searchByName(String name) throws Exception {
        List<User> users = dao.searchByName(name);
        return users;
    }
        public List<User> searchByAddr(String addr) throws Exception {
            List<User> users=dao.searchByAddress(addr);
            return users;
    }

    public User searchById(int id) throws Exception {
        User user=dao.searchById(id);
        return user;
    }

    public List<User> sx() throws Exception {
        return dao.arrangeByName();
    }

    public void congtien(int userId,long amount) throws Exception{
        dao.addMoney(userId,amount);
    }

    public void transferMoney(int userAId, int userBId, long amount) throws Exception {
        dao.transferMoney(userAId,userBId,amount);
    }
}
