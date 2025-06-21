package re1kur.fs.mapper;

import org.springframework.web.multipart.MultipartFile;
import re1kur.core.dto.FileDto;
import re1kur.fs.entity.File;

public interface FileMapper {
    File upload(MultipartFile payload);

    FileDto read(File saved);
}
