package re1kur.uas.mapper;

import re1kur.core.dto.TaskDto;
import re1kur.core.payload.TaskPayload;
import re1kur.core.payload.TaskUpdatePayload;
import re1kur.uas.entity.Task;

public interface TaskMapper {
    Task write(TaskPayload payload);

    TaskDto read(Task task);

    Task update(Task found, TaskUpdatePayload payload);
}
