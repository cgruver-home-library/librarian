package org.labmonkeys.home_library.librarian.api;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.labmonkeys.home_library.librarian.dto.BorrowedBookDTO;
import org.labmonkeys.home_library.librarian.dto.LibraryMemberDTO;

@Path("/librarian")
@ApplicationScoped
public interface LibrarianAPI {

    @GET
    @Path("/getLibraryCard/{cardId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLibraryCard(@PathParam("cardId") Long libraryCardId); 

    @POST
    @Path("/suspendLibraryCard/{cardId}")
    public Response suspendLibraryCard(@PathParam("cardId") Long libraryCardId); 

    @POST
    @Path("borrowBooks/{cardId")
    public Response borrowBooks(@PathParam("cardId") Long libraryCardId, List<BorrowedBookDTO> books); 

    @POST
    @Path("returnBooks/{cardId")
    public Response returnBooks(@PathParam("cardId") Long libraryCardId, List<BorrowedBookDTO> books); 

    @GET
    @Path("/getLibraryMembers/{lastName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response lookUpMember(@PathParam("lastName") String lastName); 

    @POST
    @Path("/addMember")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLibraryMember(LibraryMemberDTO member); 

    @POST
    @Path("/createCard/{memberId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createLibraryCard(@PathParam("memberId") Long memberId); 

    @GET
    @Path("/getBooksDueToday")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BorrowedBookDTO> getAllBooksDueToday(); 

    @GET
    @Path("/getBooksDue/card/{cardId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksDueByCard(@PathParam("cardId") Long libraryCardId); 

    @GET
    @Path("/getBooksDue/member/{memberId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksDueByMember(@PathParam("memberId") Long libraryMemberId); 

    @GET
    @Path("/getBooksDueByDate/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooksDueByDate(@PathParam("date") String dueDate); 
}
