package com.aluracursos.Literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table( name = "book")

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(length = 2000)
    private String summaries;
    private String language;
    private Integer downloadCount;

    @ManyToOne
    private Author author;

    public Book() {
    }

    public Book(BookData data, Author author){

        this.title = data.title();
        this.language = data.languages().isEmpty() ? "unknown" : data.languages().get(0);
        this.summaries = data.summaries().isEmpty() ? "" : data.summaries().get(0);
        this.downloadCount = data.downloadCount();
        this.author = author;
    }

    public Book(List<BookData> bookData, Author author) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummaries() {
        return summaries;
    }

    public void setSummaries(String summaries) {
        this.summaries = summaries;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return "\n------ LIBRO ------" +
                "\nTitulo: " + title +
                "\nAutor: " + author.getName() +
                "\nIdioma: " + language +
                "\nDescargas: " + downloadCount +
                "\n------------------";
    }
}
