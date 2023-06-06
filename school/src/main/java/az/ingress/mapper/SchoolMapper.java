package az.ingress.mapper;
import az.ingress.domain.School;
import az.ingress.dto.request.SchoolRequest;
import az.ingress.dto.response.SchoolResponse;
import org.mapstruct.Mapper;
import static org.mapstruct.ReportingPolicy.IGNORE;
@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface SchoolMapper {
    School requestToSchool(SchoolRequest request);
    School responseToSchool(SchoolResponse response);
    SchoolRequest schoolToRequest(School school);
    SchoolResponse schoolToResponse(School school);
}