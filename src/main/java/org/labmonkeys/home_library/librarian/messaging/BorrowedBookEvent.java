package org.labmonkeys.home_library.librarian.messaging;

import java.util.List;
import lombok.Data;

@Data
public class BorrowedBookEvent {
    public enum BookEventEnum{CHECK_OUT,CHECK_IN};
    private Long libraryCardId;
    private BookEventEnum eventType;
    private List<BookState> bookList;
}