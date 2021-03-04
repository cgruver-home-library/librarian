package org.labmonkeys.home_library.librarian.mapper;

import java.util.List;

import org.labmonkeys.home_library.librarian.model.BorrowedBook;
import org.labmonkeys.home_library.librarian.model.LibraryCard;
import org.labmonkeys.home_library.librarian.dto.BorrowedBookDTO;
import org.labmonkeys.home_library.librarian.dto.LibraryCardDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface LibrarianMapper {

    LibraryCard libraryCardDtoToEntity(LibraryCardDTO card);

    LibraryCardDTO libraryCardToDTO(LibraryCard card);

    BorrowedBook borrowedBookDtoToEntity(BorrowedBookDTO book);

    BorrowedBookDTO borrowedBookToDto(BorrowedBook book);

    List<BorrowedBook> BorrowedBookDtosToEntities(List<BorrowedBookDTO> books);

    List<BorrowedBookDTO> BorrowedBooksToDtos(List<BorrowedBook> books);

    List<LibraryCard> libraryCardDtosToLibraryCards(List<LibraryCardDTO> cards);

    List<LibraryCardDTO> libraryCardsToDtos(List<LibraryCard> cards);

}
