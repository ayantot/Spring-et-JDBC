package com.wildcodeschool.myProjectWithDB.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@ResponseBody
public class SchoolController {
    private final static String DB_URL = "jdbc:mysql://localhost:9501/wild_db_quest?serverTimezone=GMT";
    private final static String DB_USER = "root";
    private final static String DB_PASSWORD = "****";

    @GetMapping("/api/schools")
    public List<School> getSchool(@RequestParam(defaultValue = "%") String localisation) {
        try(
                Connection connection = DriverManager.getConnection(
                        DB_URL, DB_USER, DB_PASSWORD
                );
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM school WHERE country LIKE ?"
                );
        ) {
            statement.setString(1, localisation);

            try (
                    ResultSet resultSet = statement.executeQuery();
            ) {
                List<School> school = new ArrayList<School>();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    int capacity = resultSet.getInt("capacity");
                    String country = resultSet.getString("country");
                    school.add(new School(id, name, capacity, country));
                }

                return school;
            }
        }
        catch (SQLException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "", e
            );
        }
    }

    class School {

        private int id;
        private String name;
        private int capacity;
        private String country;

        public School(int id, String name, int capacity, String country) {
            this.id = id;
            this.name = name;
            this.capacity = capacity;
            this.country = country;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getCapacity(){
            return capacity;
        }

        public String getCountry(){
            return country;
        }
    }
}
