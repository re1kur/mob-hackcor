package re1kur.ues.service;

import re1kur.core.dto.FlashcardDto;
import re1kur.core.dto.CourseDto;
import re1kur.core.dto.LectureDto;
import re1kur.core.payload.CoursePayload;
import re1kur.core.payload.LecturePayload;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    List<CourseDto> getAllCourses();

    CourseDto getCourse(UUID id);

    CourseDto createCourse(CoursePayload payload);

    List<LectureDto> getLecturesByCourse(UUID courseId);

    List<FlashcardDto> getFlashcards(UUID courseId);

    LectureDto createLecture(LecturePayload payload);

    void deleteLecture(UUID id);

}
