package re1kur.uas.mapper.impl;

import org.springframework.stereotype.Component;
import re1kur.core.dto.TaskDto;
import re1kur.core.payload.DailyTaskPayload;
import re1kur.uas.entity.Task;
import re1kur.uas.mapper.TaskMapper;

@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task write(DailyTaskPayload payload) {
        return Task.builder()
                .title(payload.title())
                .description(payload.description())
                .reward(payload.reward())
                .build();
    }

    @Override
    public TaskDto read(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .reward(task.getReward())
                .build();
    }

    @Override
    public Task update(Task found) {
        return Task.builder()
                .id(found.getId())
                .title(found.getTitle())
                .description(found.getDescription())
                .reward(found.getReward())
                .build();
    }
}
