package az.ingress.dto.client;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.util.List;
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FullSchoolResponse {
    Long id;
    String name;
    String email;
    List<StudentDto> students;
}