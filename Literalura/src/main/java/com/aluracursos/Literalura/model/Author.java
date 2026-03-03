package com.aluracursos.Literalura.model;

import jakarta.persistence.*;

@Entity
@Table( name = "author")

public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer birthYear;
    private Integer deathYear;

    public Author() {
    }

    public Author(AuthorData data){
        this.name = data.name();
        this.birthYear = data.birthYear();
        this.deathYear = data.deathYear();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(int deathYear) {
        this.deathYear = deathYear;
    }

    @Override
    public String toString() {
        return "\n------ Autor ------" +
                "\nNombre: " + name +
                "\nFecha de nacimiento: " + birthYear +
                "\nFecha de fallecimiento: " + deathYear +
                "\n------------------";
    }
}
