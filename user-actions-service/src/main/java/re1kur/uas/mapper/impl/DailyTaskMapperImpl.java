package re1kur.uas.mapper.impl;

import org.springframework.stereotype.Component;
import re1kur.core.dto.DailyTaskDto;
import re1kur.core.payload.DailyTaskPayload;
import re1kur.uas.entity.DailyTask;
import re1kur.uas.mapper.DailyTaskMapper;

@Component
public class DailyTaskMapperImpl implements DailyTaskMapper {

    @Override
    public DailyTask write(DailyTaskPayload payload) {
        return DailyTask.builder()
                .title(payload.title())
                .description(payload.description())
                .reward(payload.reward())
                .build();
    }

    @Override
    public DailyTaskDto read(DailyTask task) {
        return DailyTaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .reward(task.getReward())
                .build();
    }

    @Override
    public DailyTask update(DailyTask found) {
        return DailyTask.builder()
                .id(found.getId())
                .title(found.getTitle())
                .description(found.getDescription())
                .reward(found.getReward())
                .build();
    }
}
