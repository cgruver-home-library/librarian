package org.labmonkeys.home_library.librarian.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BorrowedBookDTO {
    
    public enum BookStatusEnum{ON_SHELF,CHECKED_OUT,CHECKED_IN,LOST,DAMAGED};
    private Long bookId;
    private String catalogId;
    private BookStatusEnum status;
    private Date borrowedDate;
    private Date dueDate;
    private Long libraryCardId;
}
