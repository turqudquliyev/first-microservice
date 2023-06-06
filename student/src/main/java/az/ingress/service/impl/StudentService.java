package az.ingress.service.impl;
import az.ingress.exception.StudentNotFoundException;
import az.ingress.mapper.StudentMapper;
import az.ingress.repository.StudentRepository;
import az.ingress.domain.Student;
import az.ingress.dto.request.StudentRequest;
import az.ingress.dto.response.StudentResponse;
import az.ingress.service.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService {
    private final StudentRepository repository;
    private final StudentMapper mapper;
    public StudentResponse saveStudent(StudentRequest request) {
        return mapper.studentToResponse(repository.save(mapper.requestToStudent(request)));
    }
    public Page<StudentResponse> findAll(Integer page, Integer pageSize, String sort) {
        PageRequest pr = PageRequest.of(page, pageSize, Sort.by(sort));
        return repository.findAll(pr).map(mapper::studentToResponse);
    }
    public List<StudentResponse> findAllBySchoolId(Long id) {
        return repository
                .findAllBySchoolId(id)
                .stream()
                .map(mapper::studentToResponse)
                .collect(Collectors.toList());
    }
    public StudentResponse findById(Long id) {
        return mapper.studentToResponse(checkIfExists(id));
    }
    public void deleteById(Long id) {
        checkIfExists(id);
        repository.deleteById(id);
    }
    @Override
    public StudentResponse updateById(Long id, StudentRequest request) {
        Student student = update(checkIfExists(id), request);
        return mapper.studentToResponse(repository.save(student));
    }
    private Student update(Student student, StudentRequest request) {
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setAge(request.getAge());
        student.setEmail(request.getEmail());
        student.setSchoolId(request.getSchoolId());
        return student;
    }
    private Student checkIfExists(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("STUDENT_NOT_FOUND"));
    }
}