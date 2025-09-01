package com.example.docva.repository;

import com.example.docva.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    Optional<Document> findByFileName(String name);

    List<Document> findByOwner(String owner);

    List<Document> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT d FROM Document d WHERE d.owner = :owner AND d.fileName LIKE %:fileName%")
    List<Document> searchByOwnerAndFileName(@Param("owner") String owner, @Param("fileName") String fileName);

}

//
//Spring Data JPA (your repos)
//       ↓
//JPA (annotations, interfaces)
//       ↓
//Hibernate (implementation, SQL generator)
//       ↓
//Database (e.g., MySQL, Postgres, H2)
