package az.ingress.client;
import az.ingress.dto.client.StudentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
@FeignClient(name = "student-service", url = "${student.url}")
public interface StudentClient {
    @GetMapping("/school/{schoolId}")
    List<StudentDto> findAllBySchoolId(@PathVariable Long schoolId);
}