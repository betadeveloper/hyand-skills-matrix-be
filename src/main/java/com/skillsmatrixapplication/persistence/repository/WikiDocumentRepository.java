package com.skillsmatrixapplication.persistence.repository;

import com.skillsmatrixapplication.persistence.entity.WikiDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WikiDocumentRepository extends JpaRepository<WikiDocument, Long> {
    List<WikiDocument> findByCategory(String category);
}
