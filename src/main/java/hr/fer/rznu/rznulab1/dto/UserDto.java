package hr.fer.rznu.rznulab1.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private List<Long> groups = new ArrayList<>();

    public UserDto(Long id, String email, String name, String surname, List<Long> groups) {
        this.id = id;
        this.email = email;
        this.surname = surname;
        this.name = name;
        this.groups = groups;
    }

    public List<Long> getGroups() {
        return groups;
    }

    public void setGroups(List<Long> groups) {
        this.groups = groups;
    }

    public UserDto(Long id, String email, String name, String surname) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
    }

    public UserDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
