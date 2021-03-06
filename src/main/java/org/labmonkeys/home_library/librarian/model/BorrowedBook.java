package org.labmonkeys.home_library.librarian.model;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
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
    @Column(name = "book_id", updatable = false, nullable = false)
    private Long bookId;

    @Column(name = "catalog_id", updatable = false, nullable = false)
    private String catalogId;

    @Column(name = "borrowed_book")
    private Date borrowedDate;

    @Column(name = "due_date")
    private Date dueDate;

    @ManyToOne
    @JoinColumn(name = "library_card_id", nullable = false)
    private LibraryCard libraryCard;

    public static List<BorrowedBook> getBooksDueToday() {
        Calendar cal = Calendar.getInstance();
        List<BorrowedBook> booksDue = find("dueDate < ?1", Sort.ascending("dueDate"), cal.getTime()).list();
        return booksDue;
    }

    public static List<BorrowedBook> getBooksDueByDate(LocalDate dueDate) {
        
        List<BorrowedBook> booksDue = find("dueDate < ?1", Sort.ascending("dueDate") ,dueDate).list();
        return booksDue;
    }

    public static List<BorrowedBook> getBooksDueByCard(Long cardId) {
        Calendar cal = Calendar.getInstance();
        List<BorrowedBook> booksDue = find("libraryCard == ?1 and dueDate < ?2", Sort.ascending("dueDate"), cal.getTime(), cardId).list();
        return booksDue;
    }

}
