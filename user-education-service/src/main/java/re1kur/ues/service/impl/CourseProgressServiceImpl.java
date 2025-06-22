package re1kur.ues.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import re1kur.core.dto.CourseProgressDto;
import re1kur.ues.entity.Course;
import re1kur.ues.entity.CourseProgress;
import re1kur.ues.entity.CourseProgressId;
import re1kur.ues.repository.CourseProgressRepository;
import re1kur.ues.repository.CourseRepository;
import re1kur.ues.repository.LectureRepository;
import re1kur.ues.service.CourseProgressService;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseProgressServiceImpl implements CourseProgressService {
    private final CourseProgressRepository progressRepository;
    private final LectureRepository lectureRepository;
    private final CourseRepository courseRepository;

    @Value("${custom.points.lecture}")
    public Integer lecturePoint;

    @Value("${custom.points.quiz}")
    public Integer quizPoint;

    @Override
    public CourseProgressDto markLectureCompleted(UUID userId, UUID courseId) {
        CourseProgress progress = progressRepository.findById(new CourseProgressId(userId, courseId))
                .orElseGet(() -> CourseProgress.builder()
                        .userId(userId)
                        .course(courseRepository.getReferenceById(courseId))
                        .completed(false)
                        .earnedCertificate(false)
                        .earnedPoints(0)
                        .build());

        int totalLectures = lectureRepository.findByCourseCourseIdOrderByOrderIndex(courseId).size();
        int completedLectures = (progress.getScore() != null ? progress.getScore() : 0);

        completedLectures++;
        progress.setScore(completedLectures);

        if (completedLectures >= totalLectures && Boolean.TRUE.equals(progress.getCompleted())) {
            progress.setEarnedCertificate(true);
        }

        progress.setEarnedPoints(completedLectures * lecturePoint);
        progress.setCompletedAt(Instant.now());

        return toDto(progressRepository.save(progress));
    }

    @Override
    public CourseProgressDto markQuizPassed(UUID userId, UUID courseId, int score) {
        CourseProgress progress = progressRepository.findById(new CourseProgressId(userId, courseId))
                .orElseThrow();

        progress.setScore(score);
        progress.setCompleted(true);
        progress.setCompletedAt(Instant.now());

        if (score >= 70) {
            progress.setEarnedCertificate(true);
            progress.setEarnedPoints(progress.getEarnedPoints() + quizPoint);
        }

        return toDto(progressRepository.save(progress));
    }

    @Override
    public CourseProgressDto getProgress(UUID userId, UUID courseId) {
        return toDto(progressRepository.findById(new CourseProgressId(userId, courseId))
                .orElseThrow());
    }

    @Override
    public CourseProgressDto startCourse(UUID userId, UUID courseId) {
        if (progressRepository.existsById(new CourseProgressId(userId, courseId))) {
            throw new IllegalStateException("Курс уже начат этим пользователем");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Курс не найден"));

        CourseProgress progress = CourseProgress.builder()
                .userId(userId)
                .course(course)
                .score(0)
                .completed(false)
                .earnedCertificate(false)
                .earnedPoints(0)
                .build();

        return toDto(progressRepository.save(progress));
    }

    @Override
    public boolean hasStartedCourse(UUID userId, UUID courseId) {
        return progressRepository.existsById(new CourseProgressId(userId, courseId));
    }

    private CourseProgressDto toDto(CourseProgress progress) {
        return CourseProgressDto.builder()
                .courseId(progress.getCourse().getCourseId())
                .score(progress.getScore())
                .completed(progress.getCompleted())
                .earnedPoints(progress.getEarnedPoints())
                .earnedCertificate(progress.getEarnedCertificate())
                .completedAt(progress.getCompletedAt())
                .build();
    }
}
