package com.example.ravtech.controllers;

import com.example.ravtech.models.Document;
import com.example.ravtech.repo.DocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService){
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<Document> createDocument(@Valid @RequestBody Document document){
        Document createDocument = documentService.saveDocument(document);
        return new ResponseEntity<>(createDocument, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id){
        Optional<Document> document = documentService.getDocumentById(id);
        return document.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments(){
        List<Document> documents = documentService.getAllDocuments();
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id){
        boolean deleted = documentService.deleteDocumentById(id);
        if(deleted)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @Valid @RequestBody Document document){
        Document updatedDocument = documentService.updateDocument(id, document);
        return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
    }
}
