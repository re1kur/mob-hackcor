package re1kur.uas.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import re1kur.core.event.ConfirmedTaskEvent;
import re1kur.uas.entity.Task;
import re1kur.uas.entity.UserTask;
import re1kur.uas.repository.TaskRepository;
import re1kur.uas.service.EventService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final TaskRepository taskRepo;
    private final RabbitTemplate template;

    @Value("${mq.rout.task-confirmed}")
    private String confirmedTaskRout;

    @Value("${mq.exchange}")
    private String exchange;

    @Override
    public void eventConfirmedTask(UserTask userTask) {
        Task task = taskRepo.findById(userTask.getId().getTaskId()).get();
        Integer reward = task.getReward();
        UUID userId = userTask.getId().getUserId();

        ConfirmedTaskEvent event = ConfirmedTaskEvent.builder()
                .userId(userId.toString())
                .reward(reward)
                .build();

        template.convertAndSend(exchange, confirmedTaskRout, event);
    }
}
