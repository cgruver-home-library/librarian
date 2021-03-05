package org.labmonkeys.home_library.librarian.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BorrowedBookDTO {
    
    private Long bookId;
    private String catalogId;
    private Date borrowedDate;
    private Date dueDate;
}
