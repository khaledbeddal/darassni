package com.esi.mscours.repository;

import com.esi.mscours.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.List;

@RepositoryRestResource
public interface DocumentRepository extends JpaRepository<Document,Long> {
    List<Document> findDocumentByIdTeacher(Long idTeacher);
}
