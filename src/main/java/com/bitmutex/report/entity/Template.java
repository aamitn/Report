package com.bitmutex.report.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "templates")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String filename;

    @Lob
    @Column(name = "file_data", columnDefinition = "LONGBLOB")  // Change from VARBINARY
    private byte[] fileData;

    @Column(nullable = false, unique = true)
    private String fileHash;

    public Template() {}

    public Template(String filename, byte[] fileData, String fileHash) {
        this.filename = filename;
        this.fileData = fileData;
        this.fileHash = fileHash;
    }

    public Long getId() { return id; }
    public String getFilename() { return filename; }
    public byte[] getFileData() { return fileData; }
    public String getFileHash() { return fileHash; }

    public void setId(Long id) { this.id = id; }
    public void setFilename(String filename) { this.filename = filename; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }
    public void setFileHash(String fileHash) { this.fileHash = fileHash; }
}
