package com.booklibrary.LibraryManagementSystem.Services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.booklibrary.LibraryManagementSystem.Data.DTOs.AddBookDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.UpdateBookAuthorDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.UpdateBookDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.UpdateBookIsbnDto;
import com.booklibrary.LibraryManagementSystem.Data.DTOs.UpdateBookTitleDto;
import com.booklibrary.LibraryManagementSystem.Data.Entities.Book;
import com.booklibrary.LibraryManagementSystem.Data.Entities.BookImages;
import com.booklibrary.LibraryManagementSystem.Data.Entities.Patron;
import com.booklibrary.LibraryManagementSystem.Data.Entities.PatronRoles;
import com.booklibrary.LibraryManagementSystem.Data.ResponseModel.ResponseModel;
import com.booklibrary.LibraryManagementSystem.Repository.BookImagesRepository;
import com.booklibrary.LibraryManagementSystem.Repository.BookRepository;
import com.booklibrary.LibraryManagementSystem.Repository.PatronRolesRepository;
import com.booklibrary.LibraryManagementSystem.Repository.RolesRepository;
import com.booklibrary.LibraryManagementSystem.Services.Mapping.IMappingToDtoService;

import jakarta.transaction.Transactional;

@Service
public class BookService {


    private final BookRepository _bookRepository;
    private final PatronRolesRepository _patronRolesRepository;
    private final RolesRepository _rolesRepository;
    private final BookImagesRepository _bookImagesRepository;
    private final IMappingToDtoService _mappingToDtoService;

    public BookService(
        BookRepository bookRepository,
        PatronRolesRepository patronRolesRepository,
        RolesRepository rolesRepository,
        BookImagesRepository bookImagesRepository,
        IMappingToDtoService mappingService
    ){
        _bookRepository = bookRepository;
        _patronRolesRepository = patronRolesRepository;
        _rolesRepository = rolesRepository;
        _bookImagesRepository = bookImagesRepository;
        _mappingToDtoService = mappingService;
    }

    @Async
    @Transactional
    public ResponseModel<?> AddBook(Patron patron, AddBookDto addBookDto) throws IOException{
        Optional<PatronRoles> isPatronAdmin = _patronRolesRepository.isPatronInRole(patron.getId(), _rolesRepository.findByRoleName("ADMIN").get().getId());
        if(!isPatronAdmin.isPresent()){
            return new ResponseModel<>(403, false, "You have no authorities to add books");
        }
        Optional<Book> findByIsbn = _bookRepository.findByISBN(addBookDto.getIsbn());
        if(findByIsbn.isPresent()){
            return new ResponseModel<>(403, false, "Book with this isbn already exists");
        }
        if(addBookDto.getImages()==null){
            _bookRepository.save(new Book(addBookDto.getTitle(), addBookDto.getAuthor(), addBookDto.getIsbn(), addBookDto.getPublicationYear()));
            return new ResponseModel<>(201, true, "Book saved successfully");
        }    
        Book book = new Book(addBookDto.getTitle(), addBookDto.getAuthor(), addBookDto.getIsbn(), addBookDto.getPublicationYear());
        _bookRepository.save(book);
        saveBookImages(addBookDto, book);
        return new ResponseModel<>(201, true, "Book saved successfully",
        		_mappingToDtoService.BookToBookDto(book));
    }


    @Async
    @Transactional
    public ResponseModel<?> UpdateBook(Patron patron, UpdateBookDto updateBookDto){
        Optional<PatronRoles> isPatronAdmin = _patronRolesRepository.isPatronInRole(patron.getId(), _rolesRepository.findByRoleName("ADMIN").get().getId());
        if(!isPatronAdmin.isPresent()){
            return new ResponseModel<>(403, false, "You have no authorities to add books");
        }
        Optional<Book> bookById = _bookRepository.findById(updateBookDto.getId());
        if(!bookById.isPresent()){
            return new ResponseModel<>(404, false, "Book not found");    
        }
        Optional<Book> findByIsbn = _bookRepository.findByISBN(updateBookDto.getIsbn());
        if(findByIsbn.isPresent()){
            return new ResponseModel<>(403, false, "Book with isbn already exists");
        }
        bookById.get().setAuthor(updateBookDto.getAuthor());
        bookById.get().setISBN(updateBookDto.getIsbn());
        bookById.get().setPublicationYear(updateBookDto.getPublicationYear());
        bookById.get().setTitle(updateBookDto.getTitle());
        _bookRepository.save(bookById.get());
        return new ResponseModel<>(200, true, "Book updated successfully",
        		_mappingToDtoService.BookToBookDto(bookById.get()));
    }

    @Async
    @Transactional
    public ResponseModel<?> UpdateBookAuthor(Patron patron, UpdateBookAuthorDto updateBookDto){
        Optional<PatronRoles> isPatronAdmin = _patronRolesRepository.isPatronInRole(patron.getId(), _rolesRepository.findByRoleName("ADMIN").get().getId());
        if(!isPatronAdmin.isPresent()){
            return new ResponseModel<>(403, false, "You have no authorities to add books");
        }
        Optional<Book> bookById = _bookRepository.findById(updateBookDto.getId());
        if(!bookById.isPresent()){
            return new ResponseModel<>(404, false, "Book not found");    
        }
        bookById.get().setAuthor(updateBookDto.getAuthor());
        _bookRepository.save(bookById.get());
        return new ResponseModel<>(200, true, "Book author updated successfully", 
        		_mappingToDtoService.BookToBookDto(bookById.get()));
    }

    @Async
    @Transactional
    public ResponseModel<?> UpdateBookTitle(Patron patron, UpdateBookTitleDto updateBookDto){
        Optional<PatronRoles> isPatronAdmin = _patronRolesRepository.isPatronInRole(patron.getId(), _rolesRepository.findByRoleName("ADMIN").get().getId());
        if(!isPatronAdmin.isPresent()){
            return new ResponseModel<>(403, false, "You have no authorities to add books");
        }
        Optional<Book> bookById = _bookRepository.findById(updateBookDto.getId());
        if(!bookById.isPresent()){
            return new ResponseModel<>(404, false, "Book not found");    
        }
        bookById.get().setTitle(updateBookDto.getTitle());
        _bookRepository.save(bookById.get());
        return new ResponseModel<>(200, true, "Book title updated successfully",
        		_mappingToDtoService.BookToBookDto(bookById.get()));
    }

    @Async
    @Transactional
    public ResponseModel<?> UpdateBookIsbn(Patron patron, UpdateBookIsbnDto updateBookDto){
        Optional<PatronRoles> isPatronAdmin = _patronRolesRepository.isPatronInRole(patron.getId(), _rolesRepository.findByRoleName("ADMIN").get().getId());
        if(!isPatronAdmin.isPresent()){
            return new ResponseModel<>(403, false, "You have no authorities to add books");
        }
        Optional<Book> bookById = _bookRepository.findById(updateBookDto.getId());
        if(!bookById.isPresent()){
            return new ResponseModel<>(404, false, "Book not found");    
        }
        Optional<Book> findByIsbn = _bookRepository.findByISBN(updateBookDto.getIsbn());
        if(findByIsbn.isPresent()){
            return new ResponseModel<>(403, false, "Book with isbn already exists");
        }
        bookById.get().setISBN(updateBookDto.getIsbn());
        _bookRepository.save(bookById.get());
        return new ResponseModel<>(200, true, "Book ISBN updated successfully",
        		_mappingToDtoService.BookToBookDto(bookById.get()));
    }

    @Async
    @Transactional
    public ResponseModel<?> FindById(String bookId){
        Optional<Book> bookById = _bookRepository.findById(bookId);
        if(!bookById.isPresent()){
            return new ResponseModel<>(404, false, "Book not found"); 
        }
        return new ResponseModel<>(200, true, "book found successfully", 
        		_mappingToDtoService.BookToBookDto(bookById.get()));
    }

    @Async
    @Transactional
    public ResponseModel<?> FindByIsbn(String isbn){
        Optional<Book> bookByIsbn = _bookRepository.findByISBN(isbn);
        if(!bookByIsbn.isPresent()){
            return new ResponseModel<>(404, false, "Book not found");  
        }
        return new ResponseModel<>(200, true, "book found successfully", 
        		_mappingToDtoService.BookToBookDto(bookByIsbn.get()));
    }

    @Async
    @Transactional
    public ResponseModel<?> DeleteById(String bookId){
        Optional<Book> bookById = _bookRepository.findById(bookId);
        if(!bookById.isPresent()){
            return new ResponseModel<>(404, false, "Book not found");  
        }
        ResponseModel<?> deleteImages = DeleteBookImages(bookId);
        if(!deleteImages.isSuccess()){
            return deleteImages;
        }
        _bookRepository.delete(bookById.get());
        return new ResponseModel<>(204, true, "book deleted successfully");
    }

    @Async
    @Transactional
    public ResponseModel<?> DeleteByIsbn(String isbn){
        Optional<Book> bookByIsbn = _bookRepository.findByISBN(isbn);
        if(!bookByIsbn.isPresent()){
            return new ResponseModel<>(404, false, "Book not found");  
        }
        ResponseModel<?> deleteImages = DeleteBookImages(bookByIsbn.get().getId());
        if(!deleteImages.isSuccess()){
            return deleteImages;
        }
        _bookRepository.delete(bookByIsbn.get());    
        return new ResponseModel<>(204, true, "book deleted successfully");
    }

    @Async
    @Transactional
    public ResponseModel<?> MakeBookImageDefault(Patron patron, String bookId, String imgId){
        if(!_patronRolesRepository.isPatronInRole(patron.getId(), "ADMIN").isPresent()){
            return new ResponseModel<>(403, false, "forbidden");
        }
        Optional<BookImages> img = _bookImagesRepository.findByBookAndImgId(bookId, imgId);
        if(!img.isPresent()){
            return new ResponseModel<>(403, false, "forbidden");
        }
        Optional<BookImages> defaultImage = _bookImagesRepository.findBookDefaultImage(bookId);
        if(defaultImage.isPresent()){
            defaultImage.get().setIsDefault(false);
            _bookImagesRepository.save(defaultImage.get());
        }
        img.get().setIsDefault(true);
        _bookImagesRepository.save(img.get());
        return new ResponseModel<>(200, true, "Image set default successfully");
    }

    @Async
    @Transactional
    public ResponseModel<?> FindAll(){
        List<Book> books =  _bookRepository.findAllBooks();
        if(books == null || books.size()==0){
            return new ResponseModel<>(204, true, "No books found");
        }
        return new ResponseModel<>(200, true, "Books found successfully", 
        		_mappingToDtoService.BookToBookDto(books));
    }


    private ResponseModel<?> saveBookImages(AddBookDto addBookDto, Book book) throws IOException{
        File path = new File("D:/my_source_code/Java workspace/LibraryManagementSystem/Images/BookImages/");
        if(!path.exists()){
            path.mkdirs();
        }
        List<MultipartFile> images = addBookDto.getImages();
        for (MultipartFile image : images) {
            if(isAvailableImageExtension(image)){
                @SuppressWarnings("null")
                String uniqueImageName = UUID.randomUUID().toString() + "." + image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1);
                try(InputStream inputStream = image.getInputStream()){
                    Files.copy(inputStream, Paths.get(path.getAbsolutePath()+"/"+uniqueImageName).normalize().toAbsolutePath(), 
                    StandardCopyOption.REPLACE_EXISTING);
                    _bookImagesRepository.save(new BookImages(uniqueImageName, book));
                }
                catch(IOException e){
                    return new ResponseModel<>(500, false, "Failed to save image");
                }
            }
        }
        return new ResponseModel<>(201, true, "Images saved successfully");
    }

    private boolean isAvailableImageExtension(MultipartFile image) throws IOException{
        if(image.getOriginalFilename()!=null){
            @SuppressWarnings("null")
            String extension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1);
            if(extension.equals("jpg") || extension.equals("png")){
                return true;
            }
            return false;
        }
        return false;
    }

    @Transactional
    private ResponseModel<?> DeleteBookImages(String bookId){
        List<BookImages> images = _bookImagesRepository.findBookImages(bookId);
        if(images!=null){
            try{
                _bookImagesRepository.deleteBookImages(bookId);
            }
            catch(Exception e){
                return new ResponseModel<>(500, false, "Failed to delete images");
            }
            for (BookImages image : images) {
                
                File file = new File("D:/my_source_code/Java workspace/LibraryManagementSystem/Images/BookImages/"+image.getImagePath());                
                if(file.exists()){
                    file.delete();
                }
                else{
                    return new ResponseModel<>(500, false, "image not exists");
                }
            }
            return new ResponseModel<>(204, true, "Book images deleted successfully");    
        }
        return new ResponseModel<>(404, false, "No images found");
    }

}
