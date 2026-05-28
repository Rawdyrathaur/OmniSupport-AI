package com.omnisupport.filestorage.repository;

import com.omnisupport.filestorage.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
}
