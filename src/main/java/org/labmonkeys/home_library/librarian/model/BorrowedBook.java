package org.labmonkeys.home_library.librarian.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.labmonkeys.home_library.librarian.dto.BorrowedBookDTO.BookStatusEnum;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "borrowed_book", indexes = { @Index(name = "library_card_id_idx", columnList = "library_card_id") })
public class BorrowedBook extends PanacheEntityBase {

    @Id()
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    
    @Column(name = "book_id", updatable = false, nullable = false)
    private Long bookId;

    @Column(name = "catalog_id", updatable = false, nullable = false)
    private String catalogId;

    @Column(name = "status")
    private BookStatusEnum status;

    @Column(name = "borrowed_book")
    private LocalDate borrowedDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "library_card_id", nullable = false)
    private LibraryCard libraryCard;

    public static List<BorrowedBook> getBooksDueToday() {
        List<BorrowedBook> booksDue = find("status == ?1 and dueDate < ?2", Sort.ascending("dueDate"), BookStatusEnum.CHECKED_OUT ,LocalDate.now()).list();
        return booksDue;
    }

    public static List<BorrowedBook> getBooksDueByDate(LocalDate dueDate) {
        
        List<BorrowedBook> booksDue = find("status == ?1 and dueDate < ?2", Sort.ascending("dueDate"), BookStatusEnum.CHECKED_OUT ,dueDate).list();
        return booksDue;
    }

    public static List<BorrowedBook> getBooksDueByCard(Long cardId) {
        List<BorrowedBook> booksDue = find("status == ?1 and libraryCard == ?2 and dueDate < ?3", Sort.ascending("dueDate"), BookStatusEnum.CHECKED_OUT ,LocalDate.now(), cardId).list();
        return booksDue;
    }

}
