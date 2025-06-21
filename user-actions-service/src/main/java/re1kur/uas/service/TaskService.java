package re1kur.uas.service;

import org.springframework.data.domain.Pageable;
import re1kur.core.dto.TaskDto;
import re1kur.core.payload.TaskPayload;
import re1kur.core.payload.TaskUpdatePayload;

import java.util.List;

public interface TaskService {
    TaskDto create(TaskPayload payload);

    TaskDto getById(Long id);

    TaskDto update(TaskUpdatePayload payload);

    void delete(Long id);

    List<TaskDto> getDailyTasks(Pageable pageable);
}
