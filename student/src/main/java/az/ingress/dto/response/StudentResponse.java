package az.ingress.dto.response;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentResponse {
    Long id;
    String firstName;
    String lastName;
    Integer age;
    String email;
    Long schoolId;
}