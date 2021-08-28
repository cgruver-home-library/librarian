package org.labmonkeys.home_library.librarian.messaging;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.labmonkeys.home_library.librarian.mapper.LibrarianMapper;
import org.labmonkeys.home_library.librarian.model.BorrowedBook;
import org.labmonkeys.home_library.librarian.model.LibraryCard;

import io.smallrye.reactive.messaging.annotations.Blocking;

@ApplicationScoped
public class BorrowedBookEventSubscriber {

    @Inject LibrarianMapper mapper;

    @Incoming("book-event")
    @Blocking
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public void bookEvents(BorrowedBookEvent bookEvent) {
        switch (bookEvent.getEventType()) {
            case CHECK_OUT:
                this.checkoutBooks(bookEvent);
                break;
        
            case CHECK_IN:
                this.returnBooks(bookEvent);
                break;
        }
    }

    private void checkoutBooks(BorrowedBookEvent bookEvent) {
        // Update the librarian data store with the borrowed books
        LibraryCard card = LibraryCard.findById(bookEvent.getLibraryCardId());
        List<BorrowedBook> borrowedBooks = new ArrayList<BorrowedBook>();
        Calendar cal = Calendar.getInstance();
        Date borrowedDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 14);
        Date dueDate = cal.getTime();
        for (BookState book : bookEvent.getBookList()) {
            BorrowedBook borrowedBook = new BorrowedBook();
            borrowedBook.setBookId(book.getBookId());
            borrowedBook.setLibraryCard(card);
            borrowedBook.setBorrowedDate(borrowedDate);
            borrowedBook.setDueDate(dueDate);
            borrowedBooks.add(borrowedBook);
        }
        BorrowedBook.persist(borrowedBooks);
        card.flush();
    }

    private void returnBooks(BorrowedBookEvent bookEvent) {
        // Delete the borrowed books from the card.
        LibraryCard card = LibraryCard.findById(bookEvent.getLibraryCardId());

        for (BookState book : bookEvent.getBookList()) {
            BorrowedBook.deleteById(book.getBookId());
        }
        card.flush();
    }
}
