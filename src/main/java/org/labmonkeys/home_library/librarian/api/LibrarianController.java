package org.labmonkeys.home_library.librarian.api;

import java.util.List;
import javax.inject.Inject;

import org.labmonkeys.home_library.librarian.rest.LibrarianException;
import org.labmonkeys.home_library.librarian.rest.api.LibrarianApi;
import org.labmonkeys.home_library.librarian.rest.dto.BorrowedBookDTO;
import org.labmonkeys.home_library.librarian.rest.dto.LibraryCardDTO;
import org.labmonkeys.home_library.librarian.service.LibrarianService;

public class LibrarianController implements LibrarianApi {

    @Inject LibrarianService librarian;

    @Override
    public LibraryCardDTO getLibraryCard(Long libraryCardId) throws LibrarianException {
        
        return librarian.getLibraryCard(libraryCardId);
    }

    @Override
    public void saveLibraryCard(LibraryCardDTO libraryCard) throws LibrarianException {
        
        librarian.saveLibraryCard(libraryCard);
    }

    @Override
    public List<BorrowedBookDTO> getBooksDue() throws LibrarianException {
        
        return librarian.getBooksDue();
    }

    @Override
    public List<BorrowedBookDTO> getBooksDue(Long libraryCardId) throws LibrarianException {
        
        return librarian.getBooksDue(libraryCardId);
    }

    @Override
    public List<LibraryCardDTO> lookUpMember(String lastName) {
        
        return librarian.lookUpMember(lastName);
    }

    @Override
    public void borrowBooks(LibraryCardDTO libraryCard) throws LibrarianException {
        // TODO Auto-generated method stub

    }

    @Override
    public void returnBooks(LibraryCardDTO libraryCard) throws LibrarianException {
        // TODO Auto-generated method stub

    }
    
}
