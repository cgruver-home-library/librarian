package org.labmonkeys.home_library.librarian.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.labmonkeys.home_library.librarian.dto.BorrowedBookDTO;
import org.labmonkeys.home_library.librarian.dto.LibraryMemberDTO;
import org.labmonkeys.home_library.librarian.mapper.LibrarianMapper;
import org.labmonkeys.home_library.librarian.model.BorrowedBook;
import org.labmonkeys.home_library.librarian.model.LibraryCard;
import org.labmonkeys.home_library.librarian.model.LibraryMember;

@Path("/librarian")
@ApplicationScoped
public class LibrarianService {

    @Inject
    LibrarianMapper mapper;

    @GET
    @Path("/getLibraryCard/{cardId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLibraryCard(@PathParam("cardId") Long libraryCardId) {
        return Response.ok(mapper.libraryCardToDTO(LibraryCard.findById(libraryCardId))).build();
    }

    @POST
    @Path("/suspendLibraryCard/{cardId}")
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

    @POST
    @Path("borrowBooks/{cardId")
    @Transactional
    public Response borrowBooks(@PathParam("cardId") Long libraryCardId, List<BorrowedBookDTO> books) {
        LibraryCard card = LibraryCard.findById(libraryCardId);
        if (card == null) {
            return Response.status(Status.NOT_FOUND).build();            
        }
        if (!card.isActive())
        {
            return Response.status(Status.PRECONDITION_FAILED.getStatusCode(), "Inactive Library Card").build();
        }
        List<BorrowedBook> borrowedBooks = mapper.BorrowedBookDtosToEntities(books);
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

    @POST
    @Path("returnBooks/{cardId")
    @Transactional
    public Response returnBooks(@PathParam("cardId") Long libraryCardId, List<BorrowedBookDTO> books) {
        LibraryCard card = LibraryCard.findById(libraryCardId);
        if (card == null) {
            return Response.status(Status.NOT_FOUND).build();            
        }
        for (BorrowedBookDTO borrowedBookDTO : books) {
            BorrowedBook.deleteById(borrowedBookDTO.getBookId());
        }
        card.flush();
        return Response.ok(mapper.libraryCardToDTO(card)).build();
    }

    @GET
    @Path("/getLibraryMembers/{lastName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response lookUpMember(@PathParam("lastName") String lastName) {

        return Response.ok().build();
    }

    @POST
    @Path("/addMember")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addLibraryMember(LibraryMemberDTO member) {
        LibraryMember libraryMember = mapper.libraryMemberDtoToLibraryMember(member);
        LibraryMember.persist(libraryMember);
        return Response.ok(mapper.libraryMemberToDto(libraryMember)).build();
    }

    @POST
    @Path("/createCard/{memberId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createLibraryCard(@PathParam("memberId") Long memberId) {
        LibraryMember libraryMember = LibraryMember.findById(memberId);
        LibraryCard libraryCard = new LibraryCard();
        for (LibraryCard card : libraryMember.getLibraryCards()) {
            card.setActive(false);
        }
        libraryCard.setActive(true);
        libraryCard.setLibraryMember(libraryMember);
        LibraryCard.persist(libraryCard);
        return Response.ok(mapper.libraryCardToDTO(libraryCard)).build();
    }

    @GET
    @Path("/getBooksDueToday")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BorrowedBookDTO> getAllBooksDueToday() {
        return mapper.BorrowedBooksToDtos(BorrowedBook.getBooksDueToday());
    }

    @GET
    @Path("/getBooksDue/card/{cardId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksDueByCard(@PathParam("cardId") Long libraryCardId) {
        return Response.ok(mapper.BorrowedBooksToDtos(BorrowedBook.getBooksDueByCard(libraryCardId))).build();
    }

    @GET
    @Path("/getBooksDue/member/{memberId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksDueByMember(@PathParam("memberId") Long libraryMemberId) {
        LibraryMember member = LibraryMember.findById(libraryMemberId);
        List<BorrowedBook> borrowedBooks = new ArrayList<BorrowedBook>();
        for (LibraryCard card : member.getLibraryCards()) {
            borrowedBooks.addAll(BorrowedBook.getBooksDueByCard(card.getLibraryCardId()));
        }
        return Response.ok(mapper.BorrowedBooksToDtos(borrowedBooks)).build();
    }

    @GET
    @Path("/getBooksDueByDate/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooksDueByDate(@PathParam("date") String dueDate) {
        return Response.ok(mapper.BorrowedBooksToDtos(BorrowedBook.getBooksDueByDate(LocalDate.parse(dueDate)))).build();
    }
}
