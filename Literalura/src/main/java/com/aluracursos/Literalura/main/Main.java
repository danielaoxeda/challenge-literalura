package com.aluracursos.Literalura.main;

import com.aluracursos.Literalura.model.Author;
import com.aluracursos.Literalura.model.Book;
import com.aluracursos.Literalura.model.BookData;
import com.aluracursos.Literalura.model.Datos;
import com.aluracursos.Literalura.repository.IAuthorRepository;
import com.aluracursos.Literalura.repository.IBookRepository;
import com.aluracursos.Literalura.service.ConsumoAPI;
import com.aluracursos.Literalura.service.ConvierteDatos;

import java.util.*;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<BookData> bookData = new ArrayList<>();
    private IBookRepository bookRepository;
    private IAuthorRepository authorRepository;
    private List<Book> books;
    private Optional<Book> searchedBook;

    public Main(IBookRepository bookRepository, IAuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                   ************** Menu **************
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en determinado año
                    5 - Listar libros por idioma
                  
                    0 - Salir
                   **********************************
                    """;
            System.out.println(menu);
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    buscarLibrosRegistrados();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    buscarAutoresVivosEnDeterminadosAños();
                    break;
                case 5:
                    buscarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }

        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el nombre del libro que deseas buscar: ");
        var bookName = scanner.nextLine();
        Optional<Book> searchedBook = bookRepository.findByTitleContainsIgnoreCase(bookName);

        if (searchedBook.isPresent()) {
            System.out.println("Libro encontrado en la base de datos:");
            System.out.println(searchedBook.get());

        } else {
            var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + bookName.replace(" ", "%20"));

            var datos = conversor.obtenerDatos(json, Datos.class);

            Optional<BookData> foundBook = datos.results()
                    .stream()
                    .findFirst();

            if (foundBook.isPresent()) {
                BookData data = foundBook.get();

                Author author = authorRepository
                        .findByName(data.authors().get(0).name())
                        .orElseGet(() -> authorRepository.save(
                                new Author(data.authors().get(0))
                        ));

                Book book = new Book(data, author);
                bookRepository.save(book);

                System.out.println(book);
            } else {
                System.out.println("No se encontró el libro en la API");
            }
        }
    }

    private void buscarLibrosRegistrados() {
        books = bookRepository.findAll();

        books.stream().sorted(Comparator.comparing(Book::getTitle)).forEach(System.out::println);
    }

    private void listarAutores() {

        books = bookRepository.findAll();

        books.stream()
                .map(Book::getAuthor)
                .distinct()
                .forEach(System.out::println);
    }

    private void buscarAutoresVivosEnDeterminadosAños() {
        System.out.println("Ingresa el año el cual desea buscar autores vivos : ");
        int year = scanner.nextInt();

        List<Author> authors = authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);

        if (authors.isEmpty()) {
            System.out.println("No se encontraron autores vivos en ese año");
        } else {
            authors.forEach(System.out::println);
        }

    }

    private void buscarLibrosPorIdioma() {
        System.out.println("""
                Escribe el idioma para buscar los libros:
                   es (Español)
                   en (Inglés)
                   fr (Francés)
                   pt (Portugués)
                """);

        String language = scanner.nextLine();

        List<Book> books = bookRepository.findByLanguage(language);

        if (books.isEmpty()) {
            System.out.println("No se encontraron libros en ese idioma");
        } else {
            books.forEach(System.out::println);
        }
    }



}
