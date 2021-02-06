package org.labmonkeys.home_library.librarian.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

import org.labmonkeys.home_library.librarian.LibrarianException;
import org.labmonkeys.home_library.librarian.dto.BorrowedBookDTO;
import org.labmonkeys.home_library.librarian.dto.LibraryCardDTO;
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
    @Path("/getLibraryCard/{card-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public LibraryCardDTO getLibraryCard(@PathParam("card-id") Long libraryCardId) throws LibrarianException {

        return mapper.libraryCardToDTO(LibraryCard.findById(libraryCardId));
    }

    @POST
    @Path("/saveLibraryCard")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void saveLibraryCard(LibraryCardDTO libraryCard) throws LibrarianException {
        LibraryCard.persist(mapper.libraryCardDtoToEntity(libraryCard));
    }

    @GET
    @Path("/getLibraryMembers/{lastName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<LibraryMemberDTO> lookUpMember(@PathParam("lastName") String lastName) throws LibrarianException {

        return null;
    }

    @POST
    @Path("/addMember")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public LibraryMemberDTO addLibraryMember(LibraryMemberDTO member) throws LibrarianException {
        LibraryMember libraryMember = mapper.libraryMemberDtoToLibraryMember(member);
        LibraryMember.persist(libraryMember);
        return mapper.libraryMemberToDto(libraryMember);
    }

    @POST
    @Path("/createCard/{memberId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public LibraryCardDTO createLibraryCard(@PathParam("memberId") Long memberId) throws LibrarianException {
        LibraryMember libraryMember = LibraryMember.findById(memberId);
        LibraryCard libraryCard = new LibraryCard();
        for (LibraryCard card : libraryMember.getLibraryCards()) {
            card.setActive(false);
        }
        libraryCard.setActive(true);
        libraryCard.setLibraryMember(libraryMember);
        LibraryCard.persist(libraryCard);
        return mapper.libraryCardToDTO(libraryCard);
    }

    @GET
    @Path("/getBooksDueToday")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BorrowedBookDTO> getAllBooksDueToday() throws LibrarianException {
        return mapper.BorrowedBooksToDtos(BorrowedBook.getBooksDueToday());
    }

    @GET
    @Path("/getBooksDue/card/{card-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BorrowedBookDTO> getBooksDueByCard(@PathParam("card-id") Long libraryCardId) throws LibrarianException {
        return mapper.BorrowedBooksToDtos(BorrowedBook.getBooksDueByCard(libraryCardId));
    }

    @GET
    @Path("/getBooksDue/member/{member-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BorrowedBookDTO> getBooksDueByMember(@PathParam("member-id") Long libraryMemberId) throws LibrarianException {
        LibraryMember member = LibraryMember.findById(libraryMemberId);
        List<BorrowedBook> borrowedBooks = new ArrayList<BorrowedBook>();
        for (LibraryCard card : member.getLibraryCards()) {
            borrowedBooks.addAll(BorrowedBook.getBooksDueByCard(card.getLibraryCardId()));
        }
        return mapper.BorrowedBooksToDtos(borrowedBooks);
    }

    @GET
    @Path("/getBooksDueByDate/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BorrowedBookDTO> getAllBooksDueByDate(@PathParam("date") String dueDate) throws LibrarianException {
        return mapper.BorrowedBooksToDtos(BorrowedBook.getBooksDueByDate(LocalDate.parse(dueDate)));
    }
}
