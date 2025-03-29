package com.bitmutex.report.repository;

import com.bitmutex.report.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> {

    @Query(value = "SELECT id, filename, file_data, file_hash FROM templates WHERE file_hash = :fileHash", nativeQuery = true)
    Optional<Template> findByFileHash(@Param("fileHash") String fileHash);

    Optional<Template> findByFilename(String filename); // Updated to use filename
}
