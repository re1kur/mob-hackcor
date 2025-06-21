package re1kur.fs.mapper.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import re1kur.core.dto.FileDto;
import re1kur.fs.entity.File;
import re1kur.fs.mapper.FileMapper;

import java.util.UUID;

@Component
public class DefaultFileMapper implements FileMapper {
    @Override
    public File upload(MultipartFile payload) {
        return File.builder()
                .id(UUID.randomUUID())
                .extension(payload.getContentType())
                .build();
    }

    @Override
    public FileDto read(File saved) {
        return FileDto.builder()
                .id(saved.getId().toString())
                .extension(saved.getExtension())
                .url(saved.getUrl())
                .uploadedAt(saved.getUploadedAt())
                .urlExpiresAt(saved.getUrlExpiresAt())
                .build();
    }
}
