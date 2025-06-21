package re1kur.uas.service;

import org.springframework.data.domain.Pageable;
import re1kur.core.dto.TaskDto;
import re1kur.core.payload.DailyTaskPayload;
import re1kur.core.payload.DailyTaskUpdatePayload;

import java.util.List;

public interface TaskService {
    TaskDto create(DailyTaskPayload payload);

    TaskDto getById(Long id);

    TaskDto update(DailyTaskUpdatePayload payload);

    void delete(Long id);

    List<TaskDto> getDailyTasks(Pageable pageable);
}
