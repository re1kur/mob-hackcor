package re1kur.ues.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re1kur.core.FlashcardDto;
import re1kur.core.dto.CourseDto;
import re1kur.core.dto.LectureDto;
import re1kur.core.payload.CoursePayload;
import re1kur.core.payload.LecturePayload;
import re1kur.ues.entity.Course;
import re1kur.ues.entity.Lecture;
import re1kur.ues.repository.CourseRepository;
import re1kur.ues.repository.FlashcardRepository;
import re1kur.ues.repository.LectureRepository;
import re1kur.ues.service.CourseService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repo;
    private final LectureRepository lectureRepo;
    private final FlashcardRepository flashRepo;

    @Override
    public List<CourseDto> getAllCourses() {
        return repo.findAll().stream()
                .map(course -> CourseDto.builder()
                        .id(course.getCourseId())
                        .title(course.getTitle())
                        .description(course.getDescription())
                        .build())
                .toList();
    }

    @Override
    public CourseDto getCourse(UUID id) {
        Course course = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return toDto(course);
    }

    private static CourseDto toDto(Course course) {
        return CourseDto.builder()
                .id(course.getCourseId())
                .title(course.getTitle())
                .description(course.getDescription())
                .build();
    }

    @Override
    public CourseDto createCourse(CoursePayload payload) {
        Course course = Course.builder()
                .courseId(UUID.randomUUID())
                .title(payload.title())
                .description(payload.description())
                .createdAt(Instant.now())
                .build();
        course = repo.save(course);
        return toDto(course);
    }

    @Override
    public List<LectureDto> getLecturesByCourse(UUID courseId) {
        return lectureRepo.findByCourseCourseIdOrderByOrderIndex(courseId).stream()
                .map(l -> LectureDto.builder()
                        .id(l.getLectureId())
                        .title(l.getTitle())
                        .content(l.getContent())
                        .courseId(courseId)
                        .order(l.getOrderIndex())
                        .build())
                .toList();
    }

    @Override
    public List<FlashcardDto> getFlashcards(UUID courseId) {
        return flashRepo.findByCourseCourseId(courseId).stream()
                .map(f -> FlashcardDto.builder()
                        .id(f.getFlashcardId())
                        .question(f.getQuestion())
                        .answer(f.getAnswer())
                        .build())
                .toList();
    }

    @Override
    public LectureDto createLecture(LecturePayload payload) {
        Course course = repo.findById(payload.courseId()).orElseThrow(() ->
                new EntityNotFoundException("Lecture not found"));
        Lecture lecture = Lecture.builder()
                .course(course)
                .title(payload.title())
                .content(payload.content())
                .orderIndex(payload.orderIndex())
                .build();
        lectureRepo.save(lecture);
        return LectureDto.builder()
                .id(lecture.getLectureId())
                .title(lecture.getTitle())
                .courseId(course.getCourseId())
                .content(lecture.getContent())
                .order(lecture.getOrderIndex())
                .build();
    }

    @Override
    public void deleteLecture(UUID id) {
        if (!lectureRepo.existsById(id)) throw new EntityNotFoundException("Lecture not found");
        lectureRepo.deleteById(id);
    }

}
