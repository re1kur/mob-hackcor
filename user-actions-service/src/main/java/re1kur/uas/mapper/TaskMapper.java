package re1kur.uas.mapper;

import re1kur.core.dto.TaskDto;
import re1kur.core.payload.DailyTaskPayload;
import re1kur.uas.entity.Task;

public interface TaskMapper {
    Task write(DailyTaskPayload payload);

    TaskDto read(Task task);

    Task update(Task found);
}
