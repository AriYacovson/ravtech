package com.example.ravtech.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    @NotNull(message = "Label cannot be null")
    @Size(min = 1, max = 50, message = "Label must be between 1 and 50 characters")
    private String label;

    @Getter
    @Setter
    @Column(name = "document_name")
    @NotNull(message = "Document name cannot be null")
    @Size(min = 1, max = 50, message = "Document name must be between 1 and 50 characters")
    private String documentName;


    @Column(name = "creation_date", updatable = false)
    private Instant creationDate;

    @PrePersist
    public void prePersist() {
        this.creationDate = Instant.now();
    }

    public Document(String label, String documentName) {
        this.label = label;
        this.documentName = documentName;
    }

}
