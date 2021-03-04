package org.labmonkeys.home_library.librarian.messaging;

import lombok.Data;

@Data
public class BookEvent {
    public enum BookStatusEnum{ON_SHELF,CHECKED_OUT,CHECKED_IN,LOST,DAMAGED};
    private String catalogId;
    private Long bookId;
    private BookStatusEnum status;
}
