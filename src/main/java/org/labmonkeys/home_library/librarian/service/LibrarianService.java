package org.labmonkeys.home_library.librarian.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.labmonkeys.home_library.librarian.rest.LibrarianException;
import org.labmonkeys.home_library.librarian.rest.dto.BorrowedBookDTO;
import org.labmonkeys.home_library.librarian.rest.dto.LibraryCardDTO;

@ApplicationScoped
public class LibrarianService {
    
    public LibraryCardDTO getLibraryCard(Long libraryCardId) throws LibrarianException {

        return null;
    }

    public void saveLibraryCard(LibraryCardDTO libraryCard) throws LibrarianException {
        // TODO Auto-generated method stub

    }

    public List<BorrowedBookDTO> getBooksDue() throws LibrarianException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<BorrowedBookDTO> getBooksDue(Long libraryCardId) throws LibrarianException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<LibraryCardDTO> lookUpMember(String lastName) {
        // TODO Auto-generated method stub
        return null;
    }
}
