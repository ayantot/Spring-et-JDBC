package com.wildcodeschool.myProjectWithDB.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

@RestController
@ResponseBody
public class WizardController {

    private final static String DB_URL = "jdbc:mysql://localhost:9501/wild_db_quest?serverTimezone=GMT";
    private final static String DB_USER = "root";
    private final static String DB_PASSWORD = "******";

    @GetMapping("/api/wizards")
    public List<Wizard> getWizards(@RequestParam(defaultValue = "%") String family) {
        try(
            Connection connection = DriverManager.getConnection(
               DB_URL, DB_USER, DB_PASSWORD
            );
            PreparedStatement statement = connection.prepareStatement(
             "SELECT * FROM wizard WHERE lastname LIKE ?"
            );
        ) {
            statement.setString(1, family);

            try (
                    ResultSet resultSet = statement.executeQuery();
            ) {
                List<Wizard> wizards = new ArrayList<Wizard>();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstname = resultSet.getString("firstname");
                    wizards.add(new Wizard(id, firstname));
                }

                return wizards;
            }
        }
        catch (SQLException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "", e
            );
        }
    }

    class Wizard {

        private int id;
        private String firstname;

        public Wizard(int id, String firstname) {
            this.id = id;
            this.firstname = firstname;
        }

        public int getId() {
            return id;
        }

        public String getFirstname() {
            return firstname;
        }
    }
}
