package org.labmonkeys.home_library.librarian.dto;

import java.util.List;

import lombok.Data;

@Data
public class LibraryMemberDTO {
    
    private Long libraryMemberId;
    private String firstName;
    private String lastName;
    private List <LibraryCardDTO> libraryCards;
}
