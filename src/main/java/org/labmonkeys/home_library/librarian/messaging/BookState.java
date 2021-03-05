package org.labmonkeys.home_library.librarian.messaging;

import lombok.Data;

@Data
public class BookState {
    public enum BookStatusEnum{ON_SHELF,CHECKED_OUT,CHECKED_IN,LOST,DAMAGED};
    private BookStatusEnum status;
    private Long bookCaseId;
    private Long bookShelfId;
}
