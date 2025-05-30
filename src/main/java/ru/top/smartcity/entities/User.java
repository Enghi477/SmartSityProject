package ru.top.smartcity.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(length = 20)
    @NotBlank(message = "Поле не может быть пустым")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё\\s]+$", message = "Имя может содержать только буквы")
    private String firstname;

    @Column(length = 20)
    @NotBlank(message = "Поле не может быть пустым")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё\\s]+$", message = "Фамилия может содержать только буквы")
    private String lastname;

    @Min(value = 18, message = "Возраст должен быть не моложе 18 лет")
    @Max(value = 100, message = "Возраст не должен превышать 100 лет")
    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$", message = "Неверный формат телефонного номера")
    private String phone;

    @Column(unique = true)
    @Email(message = "Неверный формат электронной почты")
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}