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
import org.labmonkeys.home_library.librarian.dto.LibraryCardDTO;

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
    @Path("/borrowBooks")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response borrowBooks(LibraryCardDTO card); 

    @POST
    @Path("/returnBooks")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response returnBooks(LibraryCardDTO card); 

    @POST
    @Path("/createCard/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createLibraryCard(@PathParam("name") String name); 

    @GET
    @Path("/getBooksDueToday")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BorrowedBookDTO> getAllBooksDueToday(); 

    @GET
    @Path("/getBooksDue/card/{cardId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksDueByCard(@PathParam("cardId") Long libraryCardId); 

    @GET
    @Path("/getBooksDueByDate/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooksDueByDate(@PathParam("date") String dueDate); 
}
