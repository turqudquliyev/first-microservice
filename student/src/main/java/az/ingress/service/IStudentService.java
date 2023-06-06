package az.ingress.service;
import az.ingress.dto.request.StudentRequest;
import az.ingress.dto.response.StudentResponse;
import org.springframework.data.domain.Page;

import java.util.List;
public interface IStudentService {
    StudentResponse saveStudent(StudentRequest student);
    Page<StudentResponse> findAll(Integer page, Integer pageSize, String sort);
    List<StudentResponse> findAllBySchoolId(Long id);
    StudentResponse findById(Long id);
    void deleteById(Long id);
    StudentResponse updateById(Long id, StudentRequest request);
}