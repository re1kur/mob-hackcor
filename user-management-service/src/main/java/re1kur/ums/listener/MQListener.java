package re1kur.ums.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import re1kur.core.event.ConfirmedTaskEvent;
import re1kur.ums.service.UserService;

@Slf4j
@Component
@RequiredArgsConstructor
public class MQListener {
    private final UserService service;

    @RabbitListener(queues = "${mq.queue.task-reward}")
    public void reward(ConfirmedTaskEvent event) {
        log.info("Task reward event received: {}", event);

        service.reward(event.userId(), event.reward());
    }
}
