package org.labmonkeys.home_library.librarian.service;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.labmonkeys.home_library.librarian.api.LibrarianAPI;
import org.labmonkeys.home_library.librarian.dto.BorrowedBookDTO;
import org.labmonkeys.home_library.librarian.dto.LibraryCardDTO;
import org.labmonkeys.home_library.librarian.mapper.LibrarianMapper;
import org.labmonkeys.home_library.librarian.messaging.BorrowedBookEventPublisher;
import org.labmonkeys.home_library.librarian.messaging.BookState;
import org.labmonkeys.home_library.librarian.messaging.BorrowedBookEvent;
import org.labmonkeys.home_library.librarian.messaging.BookState.BookStatusEnum;
import org.labmonkeys.home_library.librarian.messaging.BorrowedBookEvent.BookEventEnum;
import org.labmonkeys.home_library.librarian.model.BorrowedBook;
import org.labmonkeys.home_library.librarian.model.LibraryCard;

@Path("/librarian")
@ApplicationScoped
public class LibrarianService implements LibrarianAPI {

    @Inject
    LibrarianMapper mapper;

    @Inject
    BorrowedBookEventPublisher bookEventPublisher;

    public Response getLibraryCard(@PathParam("cardId") Long libraryCardId) {
        return Response.ok(mapper.libraryCardToDTO(LibraryCard.findById(libraryCardId))).build();
    }

    @Transactional
    public Response suspendLibraryCard(@PathParam("cardId") Long libraryCardId) {
        LibraryCard card = LibraryCard.findById(libraryCardId);
        if (card == null) {
            return Response.status(Status.NOT_FOUND).build();            
        }
        card.setActive(false);
        LibraryCard.persist(card);
        return Response.ok().build();
    }

    @Transactional
    public Response borrowBooks(LibraryCardDTO cardDto) {

        // Retrieve the Library Card
        LibraryCard card = LibraryCard.findById(cardDto.getLibraryCardId());
        if (card == null) {
            return Response.status(Status.NOT_FOUND).build();            
        }

        // Make sure the Card is not suspended
        if (!card.isActive())
        {
            return Response.status(Status.PRECONDITION_FAILED.getStatusCode(), "Inactive Library Card").build();
        }

        // Make sure that books are not already checked out.
        for (BorrowedBookDTO borrowedBook : cardDto.getBorrowedBooks()) {
            BorrowedBook book = BorrowedBook.findById(borrowedBook.getBookId());
            if (book != null) {
                return Response.status(Status.PRECONDITION_FAILED.getStatusCode(), "Book: " + book.getBookId() + " is already checked out").build();
            }
        }

        // Publish an event to let subscribers know that the books are checked-out (Since this is anyncronous, do it first)
        List<BookState> bookState = new ArrayList<BookState>();
        BorrowedBookEvent bookEvent = new BorrowedBookEvent();
        bookEvent.setBookList(bookState);
        bookEvent.setLibraryCardId(card.getLibraryCardId());
        bookEvent.setEventType(BookEventEnum.CHECK_OUT);
        for (BorrowedBookDTO borrowedBook : cardDto.getBorrowedBooks()) {
            BookState state = new BookState();
            state.setBookId(borrowedBook.getBookId());
            state.setBookCaseId(0L);
            state.setBookShelfId(0L);
            state.setStatus(BookStatusEnum.CHECKED_OUT);
            bookState.add(state);
        }
        bookEventPublisher.sendEvent(bookEvent);
        return Response.ok(mapper.libraryCardToDTO(card)).build();
    }

    @Transactional
    public Response returnBooks(LibraryCardDTO cardDto) {
        LibraryCard card = LibraryCard.findById(cardDto.getLibraryCardId());
        if (card == null) {
            return Response.status(Status.NOT_FOUND).build();            
        }
        // Publish an event to let subscribers know that the books are checked-in (Since this is anyncronous, do it first)
        List<BookState> bookState = new ArrayList<BookState>();
        BorrowedBookEvent bookEvent = new BorrowedBookEvent();
        bookEvent.setBookList(bookState);
        bookEvent.setLibraryCardId(card.getLibraryCardId());
        bookEvent.setEventType(BookEventEnum.CHECK_IN);
        for (BorrowedBookDTO borrowedBook : cardDto.getBorrowedBooks()) {
            BookState state = new BookState();
            state.setBookId(borrowedBook.getBookId());
            state.setBookCaseId(0L);
            state.setBookShelfId(0L);
            state.setStatus(BookStatusEnum.CHECKED_IN);
            bookState.add(state);
        }
        bookEventPublisher.sendEvent(bookEvent);
        return Response.ok(mapper.libraryCardToDTO(card)).build();
    }

    public Response lookUpMember(@PathParam("lastName") String lastName) {

        return Response.ok().build();
    }

    @Transactional
    public Response createLibraryCard(@PathParam("name") String name) {
    
        LibraryCard libraryCard = new LibraryCard();
        libraryCard.setActive(true);
        libraryCard.setName(name);
        LibraryCard.persist(libraryCard);
        return Response.ok(mapper.libraryCardToDTO(libraryCard)).build();
    }

    public List<BorrowedBookDTO> getAllBooksDueToday() {
        return mapper.BorrowedBooksToDtos(BorrowedBook.getBooksDueToday());
    }

    public Response getBooksDueByCard(@PathParam("cardId") Long libraryCardId) {
        return Response.ok(mapper.BorrowedBooksToDtos(BorrowedBook.getBooksDueByCard(libraryCardId))).build();
    }

    public Response getAllBooksDueByDate(@PathParam("date") String dueDate) {
        return Response.ok(mapper.BorrowedBooksToDtos(BorrowedBook.getBooksDueByDate(LocalDate.parse(dueDate)))).build();
    }
}
