package org.labmonkeys.home_library.librarian.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "borrowed_book", indexes = {@Index(name = "library_card_id_idx", columnList = "library_card_id")})
public class BorrowedBook {
    
    @Id()
    @Column(updatable = false, nullable = false)
    private Long bookId;

    @Column(updatable = false, nullable = false) 
    private String catalogId;

    @Column()
    private Date checkedOut;

    @Column()
    private Date dueDate;

    @ManyToOne
    @JoinColumn(name = "library_card_id", nullable = false)
    private LibraryCard libraryCard;

    
}
