package org.labmonkeys.home_library.librarian.mapper;

import org.labmonkeys.home_library.librarian.model.BorrowedBook;
import org.labmonkeys.home_library.librarian.rest.dto.BorrowedBookDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface LibraryMapper {
    
    BorrowedBookDTO bookStatusEntityToDto(BorrowedBook entity);

    BorrowedBook bookStatusDtoToEntity(BorrowedBookDTO dto);

    
}
