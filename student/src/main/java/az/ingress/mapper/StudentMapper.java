package az.ingress.mapper;
import az.ingress.domain.Student;
import az.ingress.dto.request.StudentRequest;
import az.ingress.dto.response.StudentResponse;
import org.mapstruct.Mapper;
import static org.mapstruct.ReportingPolicy.IGNORE;
@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface StudentMapper {
    Student requestToStudent(StudentRequest request);
    Student responseToStudent(StudentResponse response);
    StudentResponse studentToResponse(Student student);
    StudentRequest studentToRequest(StudentRequest request);
}