package re1kur.uas.mapper.impl;

import org.springframework.stereotype.Component;
import re1kur.core.dto.TaskDto;
import re1kur.core.payload.TaskPayload;
import re1kur.core.payload.TaskUpdatePayload;
import re1kur.uas.entity.Task;
import re1kur.uas.entity.TaskImage;
import re1kur.uas.mapper.TaskMapper;

import java.time.OffsetDateTime;

@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task write(TaskPayload payload) {
        String shortDescription = payload.shortDescription();
        OffsetDateTime endsAt = payload.endsAt();
        Task build = Task.builder()
                .title(payload.title())
                .startsAt(payload.startsAt())
                .description(payload.description())
                .reward(payload.reward())
                .build();

        if (shortDescription != null) {
            build.setShortDescription(shortDescription);
        }
        if (endsAt != null) {
            build.setEndsAt(endsAt);
        }
        return build;
    }

    @Override
    public TaskDto read(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .shortDescription(task.getShortDescription() == null ? null : task.getShortDescription())
                .description(task.getDescription())
                .startsAt(task.getStartsAt())
                .endsAt(task.getEndsAt() == null ? null : task.getEndsAt())
                .imageUrl(task.getTaskImage() == null ? null : task.getTaskImage().getFileUrl())
                .expiresAt(task.getTaskImage() == null ? null : task.getTaskImage().getExpiresAt())
                .reward(task.getReward())
                .build();
    }

    @Override
    public Task update(Task found, TaskUpdatePayload payload) {
        return Task.builder()
                .id(found.getId())
                .title(payload.title())
                .description(payload.description())
                .reward(payload.reward())
                .build();
    }

    @Override
    public TaskImage taskImage(TaskPayload payload, Task saved) {
        String file = payload.imageFileId();
        String url = payload.imageFileUrl();
        OffsetDateTime expiresAt = payload.urlExpiresAt();
        return TaskImage.builder()
                .fileId(file)
                .fileUrl(url)
                .task(saved)
                .expiresAt(expiresAt)
                .build();
    }
}
