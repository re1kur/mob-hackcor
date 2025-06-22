package re1kur.fs.mapper;

import org.springframework.web.multipart.MultipartFile;
import re1kur.core.dto.FileDto;
import re1kur.core.other.PresignedUrl;
import re1kur.fs.entity.File;

import java.util.UUID;

public interface FileMapper {
    File upload(MultipartFile payload, String id, PresignedUrl url);

    FileDto read(File saved);
}
