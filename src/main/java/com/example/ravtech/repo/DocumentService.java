package com.example.ravtech.repo;

import com.example.ravtech.exceptions.*;
import com.example.ravtech.models.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository){
        this.documentRepository = documentRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    @Retryable(retryFor = {SQLException.class, DataAccessException.class, DocumentSaveException.class}, maxAttempts = 5, backoff = @Backoff(delay = 2000))
    public Document saveDocument(Document document)
    {
        try {
            Document new_document = documentRepository.save(document);
            logger.info("Document with id {} saved successfully", new_document.getId());
            return new_document;
        }
        catch (Exception e)
        {
            logger.error("Error saving document with id {}", document.getId(), e);
            throw new DocumentSaveException("Error saving document.", e);
        }
    }
    @Retryable(retryFor = {SQLException.class, DataAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 2000))
    public Optional<Document> getDocumentById(Long id)
    {
        try{
            Optional<Document> document = documentRepository.findById(id);
            logger.info("Document with id {} retrieved successfully", id);
            return document;
        }
        catch (Exception e)
        {
            logger.error("Error retrieving document with id {}", id, e);
            throw new DocumentFetchException("Error retrieving document with id: " + id, e);
        }
    }
    @Retryable(retryFor = {SQLException.class, DataAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 2000))
    public List<Document> getAllDocuments()
    {
        try{
            List<Document> documents = documentRepository.findAll();
            logger.info("Documents retrieved successfully");
            return documents;
        }
        catch (Exception e)
        {
            logger.error("Error retrieving documents", e);
            throw new DocumentFetchException("Error retrieving all documents.", e);
        }
    }
    @Retryable(retryFor = {SQLException.class, DataAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 2000))
    public boolean deleteDocumentById(Long id)
    {
        try {
            documentRepository.deleteById(id);
            logger.info("Document with id {} deleted successfully", id);
            return true;
        }
        catch (Exception e)
        {
            logger.error("Error deleting document with id {}", id, e);
            throw new DocumentDeleteException("Error deleting document with id: " + id, e);
        }
    }
    @Retryable(retryFor = {SQLException.class, DataAccessException.class}, maxAttempts = 5, backoff = @Backoff(delay = 2000))
    public Document updateDocument(Long id, Document document)
    {
        try {
            Optional<Document> dbDocument = documentRepository.findById(id);
            if (dbDocument.isEmpty()) throw new DocumentNotFoundException("Document with id: " + id + " not found");

            document.setId(id);
            Document updatedDocument = documentRepository.save(document);
            logger.info("Document with id {} updated successfully", id);
            return updatedDocument;
        }
        catch (DocumentNotFoundException dnfe) {
            throw dnfe;
        }
        catch (Exception e)
        {
            logger.error("Error updating document with id {}", id, e);
            throw new DocumentUpdateException("Error updating document with id: " + id, e);
        }
    }
}
