package com.esi.mscours.API;

import com.esi.mscours.entities.Document;
import com.esi.mscours.repository.DocumentRepository;
import com.esi.mscours.repository.GroupeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.Doc;
import java.util.List;

@RestController
@RequestMapping("api/v1")
public class DocumentController {
    @Autowired
    private DocumentRepository documentRepository;
    @GetMapping("/documents/{id}")
    public List<Document> getDocuments(@PathVariable(value="id") Long idTeacher){
        List<Document> documents=documentRepository.findDocumentByIdTeacher(idTeacher);
        return documents;
    }
}
