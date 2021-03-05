package org.labmonkeys.home_library.librarian.service;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import org.labmonkeys.home_library.librarian.messaging.BookEvent;
import org.labmonkeys.home_library.librarian.messaging.BookEventPublisher;
import org.labmonkeys.home_library.librarian.messaging.BookState;
import org.labmonkeys.home_library.librarian.messaging.BookState.BookStatusEnum;
import org.labmonkeys.home_library.librarian.model.BorrowedBook;
import org.labmonkeys.home_library.librarian.model.LibraryCard;

@Path("/librarian")
@ApplicationScoped
public class LibrarianService implements LibrarianAPI {

    @Inject
    LibrarianMapper mapper;

    @Inject
    BookEventPublisher bookEventPublisher;

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

        // Publish an event to let subscribers know that the books are checked-out (Since this is anyncronous, do it first)

        List<BookState> bookState = new ArrayList<BookState>();
        BookEvent bookEvent = new BookEvent();
        for (BorrowedBookDTO borrowedBook : cardDto.getBorrowedBooks()) {
            BookState state = new BookState();
            state.setBookCaseId(0L);
            state.setBookShelfId(0L);
            state.setStatus(BookStatusEnum.CHECKED_OUT);
            bookState.add(state);
        }
        bookEventPublisher.sendEvent(bookEvent);

        // Update the librarian data store with the borrowed books
        List<BorrowedBook> borrowedBooks = mapper.BorrowedBookDtosToEntities(cardDto.getBorrowedBooks());
        Calendar cal = Calendar.getInstance();
        Date borrowedDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 14);
        Date dueDate = cal.getTime();
        for (BorrowedBook borrowedBook : borrowedBooks) {
            borrowedBook.setLibraryCard(card);
            borrowedBook.setBorrowedDate(borrowedDate);
            borrowedBook.setDueDate(dueDate);
        }
        BorrowedBook.persist(borrowedBooks);
        card.flush();
        
        return Response.ok(mapper.libraryCardToDTO(card)).build();
    }

    @Transactional
    public Response returnBooks(LibraryCardDTO cardDto) {
        LibraryCard card = LibraryCard.findById(cardDto.getLibraryCardId());
        if (card == null) {
            return Response.status(Status.NOT_FOUND).build();            
        }
        for (BorrowedBookDTO borrowedBookDTO : cardDto.getBorrowedBooks()) {
            BorrowedBook.deleteById(borrowedBookDTO.getBookId());
        }
        card.flush();
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
