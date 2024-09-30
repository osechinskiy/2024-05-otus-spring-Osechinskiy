package ru.otus.hw.models.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class JpaAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "authors_seq")
    @SequenceGenerator(name = "authors_seq",
            sequenceName = "SEQ_AUTHORS", allocationSize = 1)
    private long id;

    @Column(name = "full_name")
    private String fullName;
}
