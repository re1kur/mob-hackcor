package re1kur.ues.service;

import re1kur.core.dto.CourseProgressDto;

import java.util.UUID;

public interface CourseProgressService {
    CourseProgressDto markLectureCompleted(UUID userId, UUID courseId);

    CourseProgressDto markQuizPassed(UUID userId, UUID courseId, int score);

    CourseProgressDto getProgress(UUID userId, UUID courseId);

    CourseProgressDto startCourse(UUID userId, UUID courseId);

    boolean hasStartedCourse(UUID userId, UUID courseId);


}
