package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_category_name", columnNames = {"name"})
        })
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String name;

    public Category(String name) {
        this.name = name;
    }
}
