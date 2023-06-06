package az.ingress.service.impl;
import az.ingress.domain.Student;
import az.ingress.dto.request.StudentRequest;
import az.ingress.dto.response.StudentResponse;
import az.ingress.exception.StudentNotFoundException;
import az.ingress.mapper.StudentMapper;
import az.ingress.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StudentServiceTest {
    @Mock
    private StudentRepository repository;
    @Mock
    private StudentMapper mapper;
    @InjectMocks
    private StudentService service;
    private Student student;
    private StudentRequest studentRequest;
    private StudentResponse studentResponse;
    @BeforeEach
    void setUp() {
        student = Student
                .builder()
                .id(1L)
                .firstName("Turqud")
                .lastName("Quliyev")
                .age(20)
                .email("turqudquliyev@gmail.com")
                .schoolId(1L)
                .build();
        studentRequest = StudentRequest
                .builder()
                .firstName("Turqud")
                .lastName("Quliyev")
                .age(20)
                .email("turqudquliyev@gmail.com")
                .schoolId(1L)
                .build();
        studentResponse = StudentResponse
                .builder()
                .id(1L)
                .firstName("Turqud")
                .lastName("Quliyev")
                .age(20)
                .email("turqudquliyev@gmail.com")
                .schoolId(1L)
                .build();
    }
    @Test
    void givenStudentRequestThenSaveStudentThenReturnStudentResponseThenOk() {
        when(mapper.requestToStudent(studentRequest)).thenReturn(student);
        when(repository.save(student)).thenReturn(student);
        when(mapper.studentToResponse(student)).thenReturn(studentResponse);
        StudentResponse actualResponse = service.saveStudent(studentRequest);
        assertEquals(actualResponse, studentResponse);
        verify(mapper, times(1)).requestToStudent(studentRequest);
        verify(repository, times(1)).save(student);
        verify(mapper, times(1)).studentToResponse(student);
    }
    @Test
    void givenDefaultPageRequestThenReturnStudentResponseThenOk() {
        int defaultPage = 0;
        int defaultPageSize = 10;
        String defaultSort = "age";
        List<Student> students = List.of(student);
        PageRequest pr = PageRequest.of(defaultPage, defaultPageSize, Sort.by(defaultSort));
        Page<Student> studentPage = new PageImpl<>(students, pr, students.size());
        when(repository.findAll(pr)).thenReturn(studentPage);
        when(mapper.studentToResponse(student)).thenReturn(studentResponse);
        Page<StudentResponse> result = service.findAll(defaultPage, defaultPageSize, defaultSort);
        assertEquals(1, result.getTotalElements());
        assertEquals(studentResponse, result.getContent().get(0));
        verify(repository, times(1)).findAll(pr);
        verify(mapper, times(1)).studentToResponse(student);
    }
    @Test
    void givenValidSchoolIdThenReturnStudentResponseThenOk() {
        Long schoolId = 1L;
        List<Student> students = List.of(student);
        List<StudentResponse> studentResponses = List.of(studentResponse);
        when(repository.findAllBySchoolId(schoolId)).thenReturn(students);
        when(mapper.studentToResponse(student)).thenReturn(studentResponse);
        List<StudentResponse> actualResponse = service.findAllBySchoolId(schoolId);
        assertEquals(studentResponses, actualResponse);
        verify(repository, times(1)).findAllBySchoolId(schoolId);
        verify(mapper, times(1)).studentToResponse(student);
    }
    @Test
    void givenValidStudentIdThenReturnStudentResponseThenOk() {
        Long id = student.getId();
        when(repository.findById(id)).thenReturn(Optional.of(student));
        when(mapper.studentToResponse(student)).thenReturn(studentResponse);
        StudentResponse actualResponse = service.findById(id);
        assertEquals(studentResponse, actualResponse);
        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).studentToResponse(student);
    }
    @Test
    void givenInvalidStudentIdThenCannotReturnStudentResponseThenThrowNotFoundExceptionThenFail() {
        Long id = 100L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> service.findById(id));
        verify(repository, times(1)).findById(id);
    }
    @Test
    void givenValidStudentIdThenDeleteStudentByidThenReturnVoidThenOk() {
        Long id = student.getId();
        when(repository.findById(id)).thenReturn(Optional.of(student));
        doNothing().when(repository).deleteById(id);
        service.deleteById(id);
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).deleteById(id);
    }
    @Test
    void givenInvalidStudentIdThenCannotDeleteStudentByIdThenThrowNotFoundExceptionThenFail() {
        Long id = 100L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> service.deleteById(id));
        verify(repository, times(1)).findById(id);
        verify(repository, never()).deleteById(id);
    }
    @Test
    void givenValidStudentIdAndStudentRequestThenUpdateStudentByIdThenReturnStudentResponseThenOk() {
        Long id = student.getId();
        StudentRequest newRequest = StudentRequest
                .builder()
                .firstName("Cahid")
                .lastName("Quliyev")
                .age(16)
                .email("info@unec.com")
                .schoolId(2L)
                .build();
        Student updatedStudent = Student
                .builder()
                .id(id)
                .firstName(newRequest.getFirstName())
                .lastName(newRequest.getLastName())
                .age(newRequest.getAge())
                .email(newRequest.getEmail())
                .schoolId(newRequest.getSchoolId())
                .build();
        StudentResponse updatedResponse = StudentResponse
                .builder()
                .id(id)
                .firstName(updatedStudent.getFirstName())
                .lastName(updatedStudent.getLastName())
                .age(updatedStudent.getAge())
                .email(updatedStudent.getEmail())
                .schoolId(updatedStudent.getSchoolId())
                .build();
        when(repository.findById(id)).thenReturn(Optional.of(student));
        when(repository.save(updatedStudent)).thenReturn(updatedStudent);
        when(mapper.studentToResponse(updatedStudent)).thenReturn(updatedResponse);
        StudentResponse actualResponse = service.updateById(id, newRequest);
        assertEquals(updatedResponse, actualResponse);
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(student);
        verify(mapper, times(1)).studentToResponse(student);
    }
    @Test
    void givenInvalidStudentIdAndStudentRequestThenCannotUpdateStudentByIdThenThrowNotFoundExceptionThenFail() {
        Long id = 100L;
        StudentRequest newRequest = StudentRequest
                .builder()
                .firstName("Cahid")
                .lastName("Quliyev")
                .age(16)
                .email("info@unec.com")
                .schoolId(2L)
                .build();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> service.updateById(id, newRequest));
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any());
        verify(mapper, never()).studentToResponse(any());
    }
}