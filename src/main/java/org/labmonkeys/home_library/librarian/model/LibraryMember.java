package org.labmonkeys.home_library.librarian.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "library_member", indexes = {@Index(name = "idx_lastname", columnList = "last_name")})
public class LibraryMember extends PanacheEntityBase {
    
    @Id()
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(updatable = false)
    private Long libraryMemberId;

    @Column()
    private String firstName;

    @Column()
    private String lastName;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "libraryMember", cascade = CascadeType.ALL)
    @OrderBy("libraryCardId ASC")
    private List<LibraryCard> libraryCards;
}
