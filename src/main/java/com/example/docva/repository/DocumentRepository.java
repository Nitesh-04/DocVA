package com.example.docva.repository;

import com.example.docva.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    List<Document> findByNameContaining(String name);

    List<Document> findByOwner(String owner);
    List<Document> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT d FROM Document d WHERE d.owner = :owner AND d.name LIKE %:name%")
    List<Document> searchByOwnerAndName(@Param("owner") String owner, @Param("name") String name);

}
