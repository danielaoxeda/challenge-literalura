package com.aluracursos.Literalura.repository;

import com.aluracursos.Literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IBookRepository extends JpaRepository<Book,Long> {
    Optional<Book> findByTitleContainsIgnoreCase(String bookName);
    List<Book> findByLanguage(String language);
}
