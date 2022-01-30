package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.Model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.*;

@Service
public class AuthorService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<Character, ArrayList<Author>> getAuthorsData() {
        List<Author> authors = jdbcTemplate.query("SELECT author FROM books", (ResultSet rs, int rowNum) -> {
            Author author = new Author();
            author.setName(rs.getString("author"));
            return author;
        });
        authors.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));
        Map<Character, ArrayList<Author>> authorsMap = new HashMap<>();
        for (Author author : authors) {
            Character letter = author.getName().charAt(0);
            if (authorsMap.get(letter) == null) {
                authorsMap.put(letter, new ArrayList<>());
                authorsMap.get(letter).add(author);
            } else {
                authorsMap.get(letter).add(author);
                authorsMap.put(letter, authorsMap.get(letter));
            }
        }
        return authorsMap;
    }
}