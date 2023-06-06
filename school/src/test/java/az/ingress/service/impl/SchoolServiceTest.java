package az.ingress.service.impl;
import az.ingress.dto.response.SchoolResponse;
import az.ingress.client.StudentClient;
import az.ingress.domain.School;
import az.ingress.dto.client.FullSchoolResponse;
import az.ingress.dto.client.StudentDto;
import az.ingress.dto.request.SchoolRequest;
import az.ingress.exception.SchoolNotFoundException;
import az.ingress.mapper.SchoolMapper;
import az.ingress.repository.SchoolRepository;
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
class SchoolServiceTest {
    @Mock
    private SchoolRepository repository;
    @Mock
    private SchoolMapper mapper;
    @Mock
    private StudentClient client;
    @InjectMocks
    private SchoolService service;
    private School school;
    private SchoolRequest schoolRequest;
    private SchoolResponse schoolResponse;
    private StudentDto studentDto;
    @BeforeEach
    void setUp() {
        school = School
                .builder()
                .id(1L)
                .name("ADNSU")
                .email("info@adnsu.com")
                .build();
        schoolRequest = SchoolRequest
                .builder()
                .name("ADNSU")
                .email("info@adnsu.com")
                .build();
        schoolResponse = SchoolResponse
                .builder()
                .id(1L)
                .name("ADNSU")
                .email("info@adnsu.com")
                .build();
        studentDto = StudentDto.builder()
                .firstName("Turqud")
                .lastName("Quliyev")
                .age(20)
                .email("turqudquliyev@gmail.com")
                .build();
        FullSchoolResponse fullSchoolResponse = FullSchoolResponse.builder()
                .id(1L)
                .name("ADNSU")
                .email("info@adnsu.com")
                .students(List.of(studentDto))
                .build();
    }
    @Test
    void givenSchoolRequestThenSaveSchoolThenReturnSchoolResponseThenOk() {
        when(mapper.requestToSchool(schoolRequest)).thenReturn(school);
        when(repository.save(school)).thenReturn(school);
        when(mapper.schoolToResponse(school)).thenReturn(schoolResponse);
        SchoolResponse actualResponse = service.saveSchool(schoolRequest);
        assertEquals(actualResponse, schoolResponse);
        verify(mapper, times(1)).requestToSchool(schoolRequest);
        verify(repository, times(1)).save(school);
        verify(mapper, times(1)).schoolToResponse(school);
    }
    @Test
    void givenDefaultPageRequestThenReturnSchoolResponseThenOk() {
        int defaultPage = 0;
        int defaultPageSize = 10;
        String defaultSort = "name";
        List<School> schools = List.of(school);
        PageRequest pr = PageRequest.of(defaultPage, defaultPageSize, Sort.by(defaultSort));
        Page<School> schoolPage = new PageImpl<>(schools, pr, schools.size());
        when(repository.findAll(pr)).thenReturn(schoolPage);
        when(mapper.schoolToResponse(school)).thenReturn(schoolResponse);
        Page<SchoolResponse> result = service.findAll(defaultPage, defaultPageSize, defaultSort);
        assertEquals(1, result.getTotalElements());
        assertEquals(schoolResponse, result.getContent().get(0));
        verify(repository, times(1)).findAll(pr);
        verify(mapper, times(1)).schoolToResponse(school);
    }
    @Test
    void givenValidSchoolIdThenReturnFullSchoolResponseThenOk() {
        Long id = school.getId();
        List<StudentDto> students = List.of(studentDto);
        when(repository.findById(id)).thenReturn(Optional.of(school));
        when(client.findAllBySchoolId(id)).thenReturn(students);
        FullSchoolResponse actualResponse = service.findAllWithStudents(id);
        assertEquals(id, actualResponse.getId());
        assertEquals(students, actualResponse.getStudents());
        verify(repository, times(1)).findById(id);
        verify(client, times(1)).findAllBySchoolId(id);
    }
    @Test
    void givenInvalidSchoolIdThenCannotReturnFullSchoolResponseThenThrowNotFoundExceptionThenFail() {
        Long id = 100L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(SchoolNotFoundException.class, () -> service.findById(id));
        verify(repository, times(1)).findById(id);
        verify(client, never()).findAllBySchoolId(id);
    }
    @Test
    void givenValidSchoolIdThenReturnSchoolResponseThenOk() {
        Long id = school.getId();
        when(repository.findById(id)).thenReturn(Optional.of(school));
        when(mapper.schoolToResponse(school)).thenReturn(schoolResponse);
        SchoolResponse actualResponse = service.findById(id);
        assertEquals(schoolResponse, actualResponse);
        verify(repository, times(1)).findById(id);
        verify(mapper, times(1)).schoolToResponse(school);
    }
    @Test
    void givenInvalidSchoolIdThenCannotReturnSchoolResponseThenThrowNotFoundExceptionThenFail() {
        Long id = 100L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(SchoolNotFoundException.class, () -> service.findById(id));
        verify(repository, times(1)).findById(id);
    }
    @Test
    void givenValidSchoolIdThenDeleteSchoolByidThenReturnVoidThenOk() {
        Long id = school.getId();
        when(repository.findById(id)).thenReturn(Optional.of(school));
        doNothing().when(repository).deleteById(id);
        service.deleteById(id);
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).deleteById(id);
    }
    @Test
    void givenInvalidSchoolIdThenCannotDeleteSchoolByIdThenThrowNotFoundExceptionThenFail() {
        Long id = 100L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(SchoolNotFoundException.class, () -> service.deleteById(id));
        verify(repository, times(1)).findById(id);
        verify(repository, never()).deleteById(id);
    }
    @Test
    void givenValidSchoolIdAndSchoolRequestThenUpdateSchoolByIdThenReturnSchoolResponseThenOk() {
        Long id = school.getId();
        SchoolRequest newRequest = SchoolRequest
                .builder()
                .name("UNEC")
                .email("info@unec.com")
                .build();
        School updatedSchool = School
                .builder()
                .id(id)
                .name(newRequest.getName())
                .email(newRequest.getEmail())
                .build();
        SchoolResponse updatedResponse = SchoolResponse
                .builder()
                .id(id)
                .name(updatedSchool.getName())
                .email(updatedSchool.getEmail())
                .build();
        when(repository.findById(id)).thenReturn(Optional.of(school));
        when(repository.save(updatedSchool)).thenReturn(updatedSchool);
        when(mapper.schoolToResponse(updatedSchool)).thenReturn(updatedResponse);
        SchoolResponse actualResponse = service.updateById(id, newRequest);
        assertEquals(updatedResponse, actualResponse);
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(school);
        verify(mapper, times(1)).schoolToResponse(school);
    }

    @Test
    void givenInvalidSchoolIdAndSchoolRequestThenCannotUpdateSchoolByIdThenThrowNotFoundExceptionThenFail() {
        Long id = 100L;
        SchoolRequest newRequest = SchoolRequest
                .builder()
                .name("UNEC")
                .email("info@unec.com")
                .build();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(SchoolNotFoundException.class, () -> service.updateById(id, newRequest));
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any());
        verify(mapper, never()).schoolToResponse(any());
    }
}