package com.booklibrary.LibraryManagementSystem.Services.Mapping;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.booklibrary.LibraryManagementSystem.Data.DTOs.NativeQueryReturn.BookDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.NativeQueryReturn.PatronDto;
import com.booklibrary.LibraryManagementSystem.Data.Entities.Book;
import com.booklibrary.LibraryManagementSystem.Data.Entities.Patron;

@Component
public class MappingToDtoService implements IMappingToDtoService {

	@Override
	public BookDto BookToBookDto(Book book) {
		if(book==null) {
			return new BookDto();
		}
		BookDto bookDto = new BookDto();
		bookDto.setAuthor(book.getAuthor());
		bookDto.setId(book.getId());
		bookDto.setBookImages(book.getBookImages());
		bookDto.setISBN(book.getISBN());
		bookDto.setPublicationYear(book.getPublicationYear());
		bookDto.setTitle(book.getTitle());
		return bookDto;
	}

	@Override
	public List<BookDto> BookToBookDto(List<Book> books) {
		List<BookDto> ans = new ArrayList<>();
		if(books==null) {
			return ans;
		}
		for (Book book : books) {
			ans.add(BookToBookDto(book));
		}
		return ans;
	}

	@Override
	public PatronDto PatronToPatronDto(Patron patron) {
		if(patron==null) {
			return new PatronDto();
		}
		PatronDto patronDto = new PatronDto();
		patronDto.setEmail(patron.getEmail());
		patronDto.setFullName(patron.getFullName());
		patronDto.setId(patron.getId());
		patronDto.setUserName(patron.getUserName());
		return patronDto;
	}



	
	
}
