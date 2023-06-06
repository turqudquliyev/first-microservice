package az.ingress.dto.client;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentDto {
    String firstName;
    String lastName;
    Integer age;
    String email;
}