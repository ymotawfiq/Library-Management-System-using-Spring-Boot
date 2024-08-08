package com.booklibrary.LibraryManagementSystem.Services.Mapping;

import java.util.List;


import com.booklibrary.LibraryManagementSystem.Data.DTOs.NativeQueryReturn.BookDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.NativeQueryReturn.PatronDto;
import com.booklibrary.LibraryManagementSystem.Data.Entities.Book;
import com.booklibrary.LibraryManagementSystem.Data.Entities.Patron;

public interface IMappingToDtoService {

	BookDto BookToBookDto(Book book);
	
	List<BookDto> BookToBookDto(List<Book> books);
	
	PatronDto PatronToPatronDto(Patron patron);
}
