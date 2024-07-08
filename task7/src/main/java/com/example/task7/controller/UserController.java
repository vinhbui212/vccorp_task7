package com.example.task7.controller;

import com.example.task7.exception.UserException;
import com.example.task7.model.User;
import com.example.task7.repo.UserDAOImpl;
import com.example.task7.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserDAOImpl userDAO;

    @Autowired
    public UserController(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createUser(@RequestBody User user) {
        try {
            userDAO.insertUser(user);
            return new ResponseEntity<>(ApiResponse.success("User created successfully", user), HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage(), e.getCode()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.error("Internal server error", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateUser(@PathVariable int id, @RequestBody User user) {
        try {
            userDAO.updateUser(id, user);
            return new ResponseEntity<>(ApiResponse.success("User updated successfully", null), HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage(), e.getCode()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.error("Internal server error", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable int id) {
        try {
            userDAO.deleteUser(id);
            return new ResponseEntity<>(ApiResponse.success("User deleted successfully", null), HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage(), e.getCode()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.error("Internal server error", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable int id) {
        try {
            User user = userDAO.searchById(id);
            if (user != null) {
                return new ResponseEntity<>(ApiResponse.success("User information", user), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.error("User not found", 404), HttpStatus.NOT_FOUND);
            }
        } catch (UserException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage(), e.getCode()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.error("Internal server error", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchUsers(@RequestParam(required = false) String name,
                                                      @RequestParam(required = false) String address) {
        try {
            List<User> users;
            if (name != null) {
                users = userDAO.searchByName(name);
            } else if (address != null) {
                users = userDAO.searchByAddress(address);
            } else {
                return new ResponseEntity<>(ApiResponse.error("Please provide a search parameter", 900), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(ApiResponse.success("Search results", users), HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage(), e.getCode()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.error("Internal server error", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllUsers() {
        try {
            List<User> users = userDAO.arrangeByName();
            return new ResponseEntity<>(ApiResponse.success("All users", users), HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(ApiResponse.error(e.getMessage(), e.getCode()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.error("Internal server error", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


        @PostMapping("/addMoney")
        public ResponseEntity<String> addMoney(@RequestParam int userId, @RequestParam long amount) {
            try {
                userDAO.addMoney(userId, amount);
                return ResponseEntity.ok("Money added successfully");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Failed to add money");
            }
        }

        @PostMapping("/transferMoney")
        public ResponseEntity<String> transferMoney(@RequestParam int from, @RequestParam int to, @RequestParam long amount) {
            try {
                userDAO.transferMoney(from, to, amount);
                return ResponseEntity.ok("Transfer successful");
            } catch (Exception e) {
                return ResponseEntity.status(500).body(e.getMessage());
            }
        }

        @GetMapping("/checkBalance")
        public ResponseEntity<Long> checkBalance(@RequestParam int userId) {
            try {
                long balance = userDAO.checkBalance(userId);
                return ResponseEntity.ok(balance);
            } catch (Exception e) {
                return ResponseEntity.status(500).body(0L);
            }
        }
    }





