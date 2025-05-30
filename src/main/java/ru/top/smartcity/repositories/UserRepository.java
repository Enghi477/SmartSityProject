package ru.top.smartcity.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.top.smartcity.DTOs.UserFilterDTO;
import ru.top.smartcity.entities.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findByEmailWithRoles(@Param("email") String email);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE " +
           "(:#{#filter.firstname} IS NULL OR u.firstname LIKE %:#{#filter.firstname}%) AND " +
           "(:#{#filter.lastname} IS NULL OR u.lastname LIKE %:#{#filter.lastname}%) AND " +
           "(:#{#filter.minAge} IS NULL OR u.age >= :#{#filter.minAge}) AND " +
           "(:#{#filter.maxAge} IS NULL OR u.age <= :#{#filter.maxAge}) AND " +
           "(:#{#filter.phone} IS NULL OR u.phone LIKE %:#{#filter.phone}%) AND " +
           "(:#{#filter.email} IS NULL OR u.email LIKE %:#{#filter.email}%)")
    Page<User> findAllWithFilter(@Param("filter") UserFilterDTO filter, Pageable pageable);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

}