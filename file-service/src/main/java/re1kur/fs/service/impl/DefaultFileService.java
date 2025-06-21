package re1kur.fs.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import re1kur.core.dto.FileDto;
import re1kur.core.exception.FileNotFoundException;
import re1kur.core.exception.UrlUpdateException;
import re1kur.core.other.PresignedUrl;
import re1kur.fs.client.StoreClient;
import re1kur.fs.entity.File;
import re1kur.fs.mapper.FileMapper;
import re1kur.fs.repository.FileRepository;
import re1kur.fs.service.FileService;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultFileService implements FileService {
    private final FileRepository repo;
    private final FileMapper mapper;
    private final StoreClient client;

    @Override
    public FileDto upload(MultipartFile payload) throws IOException {
        File file = mapper.upload(payload);

        file = repo.save(file);

        String id = file.getId().toString();
        client.upload(id, payload);

        PresignedUrl resp = client.getUrl(id);
        file.setUrl(resp.url());
        file.setUrlExpiresAt(resp.expiration().atZone(ZoneId.systemDefault()));

        file = repo.save(file);

        return mapper.read(file);
    }


    @Override
    public ResponseEntity<String> getUrl(String id) {
        File file = repo.findById(UUID.fromString(id)).orElseThrow(() -> new FileNotFoundException("File with id '%s' does not exist.".formatted(id)));
        if (ZonedDateTime.now().isAfter(file.getUrlExpiresAt().toInstant().atZone(ZoneId.systemDefault()))) {
            file = updateUrl(file);
        }
        return ResponseEntity.ok().body(file.getUrl());
    }

    private File updateUrl(File file) {
        PresignedUrl resp = client.getUrl(file.getId().toString());
        if (resp.expiration().equals(file.getUrlExpiresAt().toInstant().atZone(ZoneId.systemDefault()))) {
            throw new UrlUpdateException("The expiration has not been updated.");
        }
        file.setUrl(resp.url());
        file.setUrlExpiresAt(resp.expiration().atZone(ZoneId.systemDefault()));
        return repo.save(file);
    }
}
