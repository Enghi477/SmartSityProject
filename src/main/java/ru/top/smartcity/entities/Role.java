package ru.top.smartcity.entities;

import jakarta.persistence.*;
import lombok.Setter;
import ru.top.smartcity.models.ERole;

@Entity
@Table
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role() {
    }

    public Role(ERole name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public ERole getName() {
        return name;
    }

}
