package com.booklibrary.BookLibrarySystem.Api.Service.Mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.booklibrary.LibraryManagementSystem.Data.DTOs.NativeQueryReturn.BookDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.NativeQueryReturn.PatronDto;
import com.booklibrary.LibraryManagementSystem.Data.Entities.Book;
import com.booklibrary.LibraryManagementSystem.Data.Entities.Patron;
import com.booklibrary.LibraryManagementSystem.Services.Mapping.IMappingToDtoService;
import com.booklibrary.LibraryManagementSystem.Services.Mapping.MappingToDtoService;

@SpringBootTest(classes = MappingToDtoService.class)
public class ToDtoTest {

	@Autowired
	IMappingToDtoService _mappingToDtoService;
	
	@Test
	public void BookToBookDtoTest() {
		Book book = new Book();
		book.setAuthor("aaa");
		book.setISBN("1234567894");
		book.setPublicationYear(Year.of(2019));
		book.setTitle("ttt");
		BookDto bookDto = _mappingToDtoService.BookToBookDto(book);
		assertEquals(book.getAuthor(), bookDto.getAuthor());
		assertEquals(book.getISBN(), bookDto.getISBN());
		assertEquals(book.getPublicationYear(), bookDto.getPublicationYear());
		assertEquals(book.getTitle(), bookDto.getTitle());
	}
	
	@Test
	public void IfBookIsNullReturnNewEmptyBookDto() {
		Book book = null;
		BookDto bookDto = _mappingToDtoService.BookToBookDto(book);
		assertThat(bookDto).hasSameClassAs(new BookDto());
	}
	
	@Test
	public void IfPatronIsNullReturnNewEmptyPatronDto() {
		Patron patron = null;
		PatronDto patronDto = _mappingToDtoService.PatronToPatronDto(patron);
		assertThat(patronDto).hasSameClassAs(new PatronDto());
	}
	
	@Test
	public void IfBooksIsNullReturnNewEmptyArrayListOfBooksDto() {
		List<Book> arrayOfBooks = null;
		List<BookDto> books = _mappingToDtoService.BookToBookDto(arrayOfBooks);
		assertThat(books).hasSameClassAs(new ArrayList<BookDto>());
	}
	
	@Test
	public void PatronToPatronDtoTest() {
		Patron patron = new Patron();
		patron.setEmail("mail@gmail.com");
		patron.setFullName("Yousef");
		patron.setUserName("user1");
		PatronDto patronDto = _mappingToDtoService.PatronToPatronDto(patron);
		assertEquals(patron.getEmail(), patronDto.getEmail());
		assertEquals(patron.getUsername(), patronDto.getUserName());
		assertEquals(patron.getFullName(), patronDto.getFullName());
	}
	
}
