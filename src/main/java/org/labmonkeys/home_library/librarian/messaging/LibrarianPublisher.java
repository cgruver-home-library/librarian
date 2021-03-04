package org.labmonkeys.home_library.librarian.messaging;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.labmonkeys.home_library.librarian.dto.BorrowedBookDTO;

@ApplicationScoped
public class LibrarianPublisher {
    
    @Outgoing("book-event")
    public List<BookEvent> publishBorrowedBooks(List<BookEvent> books) {
        return books;
    }

}
