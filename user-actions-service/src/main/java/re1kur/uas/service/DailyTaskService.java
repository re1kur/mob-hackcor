package re1kur.uas.service;

import org.springframework.data.domain.Pageable;
import re1kur.core.dto.DailyTaskDto;
import re1kur.core.payload.DailyTaskPayload;
import re1kur.core.payload.DailyTaskUpdatePayload;

import java.util.List;

public interface DailyTaskService {
    DailyTaskDto create(DailyTaskPayload payload);

    DailyTaskDto getById(Long id);

    DailyTaskDto update(DailyTaskUpdatePayload payload);

    void delete(Long id);

    List<DailyTaskDto> getDailyTasks(Pageable pageable);
}
