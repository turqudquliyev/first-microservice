package az.ingress.service;
import az.ingress.dto.client.FullSchoolResponse;
import az.ingress.dto.request.SchoolRequest;
import az.ingress.dto.response.SchoolResponse;
import org.springframework.data.domain.Page;
public interface ISchoolService {
    SchoolResponse saveSchool(SchoolRequest request);
    Page<SchoolResponse> findAll(Integer page, Integer pageSize, String sort);
    SchoolResponse findById(Long id);
    void deleteById(Long id);
    SchoolResponse updateById(Long id, SchoolRequest request);
    FullSchoolResponse findAllWithStudents(Long schoolId);
}