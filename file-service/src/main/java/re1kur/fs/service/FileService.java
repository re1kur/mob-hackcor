package re1kur.fs.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import re1kur.core.dto.FileDto;

import java.io.IOException;

public interface FileService {
    FileDto upload(MultipartFile payload) throws IOException;

    ResponseEntity<String> getUrl(String id);
}
