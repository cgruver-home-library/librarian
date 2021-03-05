package org.labmonkeys.home_library.bookshelf.messaging;

import java.util.List;
import lombok.Data;

@Data
public class BookEvent {
    private List<BookState> bookList;
}
