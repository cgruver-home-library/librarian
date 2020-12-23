package org.labmonkeys.home_library.librarian.api;

import java.util.List;
import javax.inject.Inject;

import org.labmonkeys.home_library.librarian.rest.LibraryException;
import org.labmonkeys.home_library.librarian.rest.api.LibraryApi;
import org.labmonkeys.home_library.librarian.rest.dto.BorrowedBookDTO;
import org.labmonkeys.home_library.librarian.rest.dto.LibraryCardDTO;
import org.labmonkeys.home_library.librarian.service.LibrarianService;

public class LibrarianController implements LibraryApi {

    @Inject LibrarianService librarian;

    @Override
    public LibraryCardDTO getLibraryCard(String cardId) throws LibraryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveLibraryCard(LibraryCardDTO libraryCard) throws LibraryException {
        // TODO Auto-generated method stub

    }

    @Override
    public List<BorrowedBookDTO> getBooksDue() throws LibraryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<BorrowedBookDTO> getBooksDue(String cardId) throws LibraryException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<LibraryCardDTO> lookUpMember(String lastName) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
