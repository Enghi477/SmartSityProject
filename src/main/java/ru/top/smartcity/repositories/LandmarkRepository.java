package ru.top.smartcity.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.top.smartcity.entities.Landmark;

import java.util.List;

public interface LandmarkRepository extends JpaRepository<Landmark, Long> {
    List<Landmark> findByCategory(String category);

    @Query("SELECT DISTINCT l.category FROM Landmark l")
    List<String> findDistinctCategories();

    Page<Landmark> findAll(Pageable pageable);

    @Query("SELECT l FROM Landmark l WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Landmark> searchByName(String query);

}
