package org.labmonkeys.home_library.librarian.dto;

import java.util.List;

import lombok.Data;

@Data
public class LibraryCardDTO {
    
    private Long libraryCardId;
    private boolean active;
    private Long libraryMemberId;
    private List<BorrowedBookDTO> borrowedBooks;
}
