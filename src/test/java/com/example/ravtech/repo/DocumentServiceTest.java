package com.example.ravtech.repo;

import com.example.ravtech.exceptions.DocumentDeleteException;
import com.example.ravtech.exceptions.DocumentUpdateException;
import com.example.ravtech.models.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentService documentService;

    private Document mockDocument;

    @BeforeEach
    public void setUp() {
        mockDocument = new Document();
        mockDocument.setId(1L);
        mockDocument.setLabel("Test Label");
        mockDocument.setDocumentName("TestDocumentName");
    }

    @Test
    public void testSaveDocument() {
        when(documentRepository.save(any(Document.class))).thenReturn(mockDocument);
        Document result = documentService.saveDocument(mockDocument);
        verify(documentRepository, times(1)).save(mockDocument);
        assertEquals(mockDocument, result);
    }



    @Test
    public void testGetDocumentByIdFound() {
        when(documentRepository.findById(anyLong())).thenReturn(Optional.of(mockDocument));
        Optional<Document> result = documentService.getDocumentById(1L);
        assertTrue(result.isPresent());
        assertEquals(mockDocument, result.get());
    }

    @Test
    public void testGetDocumentByIdNotFound() {
        when(documentRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<Document> result = documentService.getDocumentById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSaveDocumentWithRetryOnException() {
        when(documentRepository.save(any(Document.class)))
                .thenThrow(new DataAccessException("Fake exception for test") {})
                .thenReturn(mockDocument);

        Document result = documentService.saveDocument(mockDocument);
        verify(documentRepository, times(2)).save(mockDocument);
        assertEquals(mockDocument, result);
    }



    @Test
    public void testGetAllDocuments() {
        List<Document> mockDocuments = Arrays.asList(mockDocument, new Document());
        when(documentRepository.findAll()).thenReturn(mockDocuments);

        List<Document> result = documentService.getAllDocuments();

        verify(documentRepository, times(1)).findAll();
        assertEquals(2, result.size());
        assertTrue(result.contains(mockDocument));
    }

    @Test
    public void testDeleteDocumentById() {
        doNothing().when(documentRepository).deleteById(1L);

        documentService.deleteDocumentById(1L);

        verify(documentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteDocumentByIdThrowsException() {
        doThrow(new DataAccessException("Fake exception for test") {}).when(documentRepository).deleteById(1L);

        assertThrows(DocumentDeleteException.class, () -> documentService.deleteDocumentById(1L));

        verify(documentRepository, times(1)).deleteById(1L);
    }


    @Test
    public void testUpdateDocument() {
        Long id = 1L;
        mockDocument.setDocumentName("UpdatedName");
        when(documentRepository.findById(id)).thenReturn(Optional.of(mockDocument));
        when(documentRepository.save(any(Document.class))).thenReturn(mockDocument);

        Document result = documentService.updateDocument(id, mockDocument);

        verify(documentRepository, times(1)).findById(id);
        verify(documentRepository, times(1)).save(mockDocument);
        assertEquals("UpdatedName", result.getDocumentName());
    }

    @Test
    public void testUpdateDocumentDocumentNotFound() {
        Long id = 1L;
        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> documentService.updateDocument(id, mockDocument));

        assertTrue(exception.getMessage().contains("Document with id: " + id + " not found"));

        verify(documentRepository, times(1)).findById(id);
        verify(documentRepository, times(0)).save(any(Document.class)); // Ensure save is not called
    }


    @Test
    public void testUpdateDocumentThrowsException() {
        Long id = 1L;
        when(documentRepository.findById(id)).thenReturn(Optional.of(mockDocument));
        when(documentRepository.save(any(Document.class)))
                .thenThrow(new DataAccessException("Fake exception for test") {});

        assertThrows(DocumentUpdateException.class, () -> documentService.updateDocument(id, mockDocument));

        verify(documentRepository, times(1)).findById(id);
        verify(documentRepository, times(1)).save(mockDocument);
    }


}