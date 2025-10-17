package mate.academy.book;

import java.math.BigDecimal;
import mate.academy.book.model.Book;
import mate.academy.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Love story");
            book.setAuthor("Erich Segal");
            book.setIsbn("9783596167692");
            book.setPrice(BigDecimal.valueOf(350));
            book.setDescription("Book about love story");
            book.setCoverImage("Love story cover");

            bookService.save(book);

            System.out.println(bookService.findAll());
        };
    }
}
