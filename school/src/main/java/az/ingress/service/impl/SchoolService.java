package az.ingress.service.impl;
import az.ingress.client.StudentClient;
import az.ingress.dto.client.FullSchoolResponse;
import az.ingress.dto.client.StudentDto;
import az.ingress.dto.request.SchoolRequest;
import az.ingress.dto.response.SchoolResponse;
import az.ingress.exception.SchoolNotFoundException;
import az.ingress.repository.SchoolRepository;
import az.ingress.domain.School;
import az.ingress.mapper.SchoolMapper;
import az.ingress.service.ISchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class SchoolService implements ISchoolService {
    private final SchoolRepository repository;
    private final SchoolMapper mapper;
    private final StudentClient client;
    public SchoolResponse saveSchool(SchoolRequest request) {
        return mapper.schoolToResponse(repository.save(mapper.requestToSchool(request)));
    }
    public Page<SchoolResponse> findAll(Integer page, Integer pageSize, String sort) {
        PageRequest pr = PageRequest.of(page, pageSize, Sort.by(sort));
        return repository.findAll(pr).map(mapper::schoolToResponse);
    }
    public FullSchoolResponse findAllWithStudents(Long schoolId) {
        School school = checkIfExists(schoolId);
        List<StudentDto> students = client.findAllBySchoolId(schoolId);
        return FullSchoolResponse
                .builder()
                .id(schoolId)
                .name(school.getName())
                .email(school.getEmail())
                .students(students)
                .build();
    }
    public SchoolResponse findById(Long id) {
        return mapper.schoolToResponse(checkIfExists(id));
    }
    public void deleteById(Long id) {
        checkIfExists(id);
        repository.deleteById(id);
    }
    public SchoolResponse updateById(Long id, SchoolRequest request) {
        School school = update(checkIfExists(id),request);
        return mapper.schoolToResponse(repository.save(school));
    }
    private School update(School school, SchoolRequest request) {
        school.setName(request.getName());
        school.setEmail(request.getEmail());
        return school;
    }
    private School checkIfExists(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new SchoolNotFoundException("SCHOOL_NOT_FOUND"));
    }
}