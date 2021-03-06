package org.labmonkeys.home_library.librarian.dto;

import java.util.List;

import lombok.Data;

@Data
public class LibraryCardDTO {
    
    private Long libraryCardId;
    private String name;
    private List<BorrowedBookDTO> borrowedBooks;
}
