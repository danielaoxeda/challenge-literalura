package com.aluracursos.Literalura.main;

import com.aluracursos.Literalura.model.Author;
import com.aluracursos.Literalura.model.Book;
import com.aluracursos.Literalura.model.BookData;
import com.aluracursos.Literalura.model.Datos;
import com.aluracursos.Literalura.repository.BookRepository;
import com.aluracursos.Literalura.service.ConsumoAPI;
import com.aluracursos.Literalura.service.ConvierteDatos;

import java.util.*;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<BookData> bookData = new ArrayList<>();
    private BookRepository repositorio;
    private List<Book> books;
    private Optional<Book> searchedBook;

    public Main(BookRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    
                    0 - Salir
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
        Optional<Book> searchedBook = repositorio.findByTitleContainsIgnoreCase(bookName);

        if (searchedBook.isPresent()) {
            System.out.println("Libro encontrado en la base de datos:");
            System.out.println(searchedBook.get());

        } else {
            System.out.println("Buscando libro en la API...");
            var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + bookName.replace(" ", "%20"));

            System.out.println("Respuesta de la API:");
            System.out.println(json);

            var datos = conversor.obtenerDatos(json, Datos.class);

            Optional<BookData> bookEncontrado = datos.results()
                    .stream()
                    .findFirst();

            if (bookEncontrado.isPresent()) {
                BookData data = bookEncontrado.get();
                Author author = new Author(data.authors().get(0));
                Book book = new Book(data, author);
                repositorio.save(book);

                System.out.println("Libro guardado:");
                System.out.println(book);

            } else {
                System.out.println("No se encontró el libro en la API");
            }
        }
    }

    private void buscarLibrosRegistrados() {
        books = repositorio.findAll();

        books.stream().sorted(Comparator.comparing(Book::getTitle)).forEach(System.out::println);
    }

    private void listarAutores() {

        books = repositorio.findAll();

        books.stream()
                .map(Book::getAuthor)
                .distinct()
                .forEach(System.out::println);
    }
}
