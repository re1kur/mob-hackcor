package re1kur.ums.mapper.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import re1kur.ums.entity.Code;
import re1kur.ums.mapper.CodeMapper;

import java.time.Duration;
import java.time.Instant;

@Component
public class CodeMapperImpl implements CodeMapper {
    @Value("${custom.code.ttl-minutes}")
    private Integer codeTtl;

    @Override
    public Code write(String userId, String value) {
        Instant now = Instant.now();
        return Code.builder()
                .id(userId)
                .value(value)
                .issuedAt(now)
                .expiresAt(now.plus(Duration.ofMinutes(codeTtl)))
                .build();
    }
}
