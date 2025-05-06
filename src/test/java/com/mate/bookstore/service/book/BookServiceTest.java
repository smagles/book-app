package com.mate.bookstore.service.book;

import com.mate.bookstore.dto.book.BookDto;
import com.mate.bookstore.dto.book.BookSearchParametersDto;
import com.mate.bookstore.dto.book.CreateBookRequestDto;
import com.mate.bookstore.dto.book.UpdateBookRequestDto;
import com.mate.bookstore.exception.EntityNotFoundException;
import com.mate.bookstore.mapper.BookMapper;
import com.mate.bookstore.model.Book;
import com.mate.bookstore.repository.book.BookRepository;
import com.mate.bookstore.repository.book.BookSpecificationBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book createTestBook() {
        return Book.builder()
                .id(1L)
                .title("Test Book Title")
                .author("Test Author")
                .isbn("978-3-16-148410-0")
                .price(new BigDecimal("19.99"))
                .description("Test book description")
                .coverImage("test_cover.jpg")
                .build();
    }

    @Test
    @DisplayName("Verify findAll() method works")
    void findAll_ValidPageable_ReturnAllBooksDto() {
        // Given
        Book book = createTestBook();

        BookDto bookDto = BookDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .isbn(book.getIsbn())
                .description(book.getDescription())
                .coverImage(book.getCoverImage())
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        // When
        Page<BookDto> bookDtos = bookService.findAll(pageable);

        // Then
        assertThat(bookDtos).hasSize(1);
        assertThat(bookDtos.getContent().get(0)).isEqualTo(bookDto);
        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toBookDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify save() method works")
    void save_ValidCreateBookRequestDto_ReturnBookDto() {
        // Given
        CreateBookRequestDto validCreateRequest = new CreateBookRequestDto(
                "Effective Java",
                "Joshua Bloch",
                "978-0134685991",
                new BigDecimal("39.99"),
                "Definitive guide to Java programming language",
                "effective_java_3rd_edition.jpg"
        );

        Book book = Book.builder()
                .id(1L)
                .title(validCreateRequest.title())
                .author(validCreateRequest.author())
                .price(validCreateRequest.price())
                .isbn(validCreateRequest.isbn())
                .description(validCreateRequest.description())
                .coverImage(validCreateRequest.coverImage())
                .build();

        BookDto bookDto = BookDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .isbn(book.getIsbn())
                .description(book.getDescription())
                .coverImage(book.getCoverImage())
                .build();

        when(bookRepository.existsByIsbn(validCreateRequest.isbn())).thenReturn(false);
        when(bookMapper.toModel(validCreateRequest)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        // When
        BookDto bookDtoSaved = bookService.save(validCreateRequest);

        // Then
        assertThat(bookDtoSaved).isEqualTo(bookDto);
        verify(bookRepository, times(1)).save(book);
        verify(bookRepository, times(1)).existsByIsbn(validCreateRequest.isbn());
        verify(bookMapper).toModel(validCreateRequest);
        verify(bookMapper).toBookDto(book);
        verifyNoMoreInteractions(bookMapper, bookRepository);
    }

    @Test
    @DisplayName("Verify the correct book was returned when book exists")
    void getBookById_WithValidBookId_ShouldReturnValidBook() {
        // Given
        Long bookId = 1L;
        Book expectedBook = createTestBook();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(expectedBook));

        // When
        Book actualBook = bookService.getBookById(bookId);

        // Then
        assertThat(actualBook)
                .isNotNull()
                .isEqualTo(expectedBook);

        verify(bookRepository, times(1)).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify the exception was thrown with non existing book id")
    void getBookById_WithNonExistingBook_ShouldThrowException() {
        // Given
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        //When
        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class,
                        () -> bookService.getBookById(bookId));

        //Then
        String expected = "Book not found with id " + bookId;
        String actual = entityNotFoundException.getMessage();
        assertThat(actual).isEqualTo(expected);

        verify(bookRepository, times(1)).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify update() works with valid data")
    void update_ValidRequest_ReturnsUpdatedBookDto() {
        // Given
        Long bookId = 1L;
        UpdateBookRequestDto updateRequest = new UpdateBookRequestDto(
                "Updated Title",
                "Updated Author",
                new BigDecimal("29.99"),
                "Updated description",
                "updated_cover.jpg"
        );

        Book existingBook = Book.builder()
                .id(bookId)
                .title("Original Title")
                .author("Original Author")
                .build();

        Book updatedBook = Book.builder()
                .id(bookId)
                .title(updateRequest.title())
                .author(updateRequest.author())
                .price(updateRequest.price())
                .description(updateRequest.description())
                .coverImage(updateRequest.coverImage())
                .build();

        BookDto expectedDto = BookDto.builder()
                .title(updatedBook.getTitle())
                .author(updatedBook.getAuthor())
                .price(updatedBook.getPrice())
                .isbn(updatedBook.getIsbn())
                .description(updatedBook.getDescription())
                .coverImage(updatedBook.getCoverImage())
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);
        when(bookMapper.toBookDto(updatedBook)).thenReturn(expectedDto);

        // When
        BookDto result = bookService.update(bookId, updateRequest);

        // Then
        assertThat(result).isEqualTo(expectedDto);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(existingBook);
        verify(bookMapper).updateModel(updateRequest, existingBook);
        verify(bookMapper).toBookDto(updatedBook);
        verifyNoMoreInteractions(bookMapper, bookRepository);

    }

    @Test
    @DisplayName("Verify update() throws exception for non-existent book")
    void update_NonExistentBook_ThrowsException() {
        // Given
        Long bookId = 100L;
        UpdateBookRequestDto updateRequest = new UpdateBookRequestDto(
                "Updated Title",
                "Updated Author",
                new BigDecimal("29.99"),
                "Updated description",
                "updated_cover.jpg"
        );

        // When
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Then
        assertThrows(EntityNotFoundException.class,
                () -> bookService.update(bookId, updateRequest));

        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify searchBooks() with valid parameters")
    void searchBooks_ValidParameters_ReturnsMatchingBooks() {
        // Given
        BookSearchParametersDto params = new BookSearchParametersDto(
                new String[]{"Author"},
                new String[]{"Title"});

        Pageable pageable = PageRequest.of(0, 10);
        Book book = createTestBook();

        BookDto expectedDto = BookDto.builder()
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .isbn(book.getIsbn())
                .description(book.getDescription())
                .coverImage(book.getCoverImage())
                .build();
        Specification<Book> specification = bookSpecificationBuilder.build(params);
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findAll(specification, pageable)).thenReturn(bookPage);
        when(bookMapper.toBookDto(book)).thenReturn(expectedDto);

        // When
        Page<BookDto> result = bookService.searchBooks(params, pageable);

        // Then
        assertThat(result).hasSize(1);
        verify(bookRepository).findAll(specification, pageable);
        verify(bookMapper).toBookDto(book);
    }

}
