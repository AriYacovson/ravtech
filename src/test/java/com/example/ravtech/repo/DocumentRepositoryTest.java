package com.example.ravtech.repo;

import com.example.ravtech.models.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class DocumentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    public void testSaveDocument() {
        Document document = new Document("Test Label", "TestDocumentName");
        Document savedDocument = documentRepository.save(document);

        assertThat(savedDocument).isNotNull();
        assertThat(savedDocument.getId()).isNotNull();
        assertThat(savedDocument.getLabel()).isEqualTo("Test Label");
        assertThat(savedDocument.getDocumentName()).isEqualTo("TestDocumentName");
        assertTrue(areTimestampsClose(savedDocument.getCreationDate(), Instant.now(), 1000));
    }

    @Test
    public void testFindDocumentById() {
        Document document = new Document("Test Label", "TestDocumentName");
        Document savedDocument = entityManager.persistAndFlush(document);

        Optional<Document> foundDocument = documentRepository.findById(savedDocument.getId());

        assertThat(foundDocument).isPresent();
        assertThat(foundDocument.get().getLabel()).isEqualTo("Test Label");
        assertThat(foundDocument.get().getDocumentName()).isEqualTo("TestDocumentName");
    }

    @Test
    public void testUpdateDocument() {
        Document document = new Document("Original Label", "OriginalDocumentName");
        Document savedDocument = entityManager.persistAndFlush(document);

        savedDocument.setLabel("Updated Label");
        savedDocument.setDocumentName("UpdatedDocumentName");
        Document updatedDocument = documentRepository.save(savedDocument);

        assertThat(updatedDocument.getLabel()).isEqualTo("Updated Label");
        assertThat(updatedDocument.getDocumentName()).isEqualTo("UpdatedDocumentName");
    }

    @Test
    public void testDeleteDocument() {
        Document document = new Document("Test Label", "TestDocumentName");
        Document savedDocument = entityManager.persistAndFlush(document);

        documentRepository.deleteById(savedDocument.getId());

        Optional<Document> foundDocument = documentRepository.findById(savedDocument.getId());

        assertThat(foundDocument).isNotPresent();
    }

    public boolean areTimestampsClose(Instant timestamp1, Instant timestamp2, long millisecondsTolerance) {
        Duration durationDifference = Duration.between(timestamp1, timestamp2);
        long differenceInMilliseconds = Math.abs(durationDifference.toMillis());
        return differenceInMilliseconds <= millisecondsTolerance;
    }
}
