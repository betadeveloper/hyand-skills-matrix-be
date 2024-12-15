package com.skillsmatrixapplication.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class WikiDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String category;

    @Lob
    @Column(name = "file_data", columnDefinition = "MEDIUMBLOB")
    private byte[] cfileData;

    private String fileType;
    private String author;

    public byte[] getFileData() {
        return cfileData;
    }

    public void setFileData(byte[] fileData) {
        this.cfileData = fileData;
    }
}
