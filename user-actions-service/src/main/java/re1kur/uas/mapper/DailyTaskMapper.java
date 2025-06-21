package re1kur.uas.mapper;

import re1kur.core.dto.DailyTaskDto;
import re1kur.core.payload.DailyTaskPayload;
import re1kur.uas.entity.DailyTask;

public interface DailyTaskMapper {
    DailyTask write(DailyTaskPayload payload);

    DailyTaskDto read(DailyTask task);

    DailyTask update(DailyTask found);
}
