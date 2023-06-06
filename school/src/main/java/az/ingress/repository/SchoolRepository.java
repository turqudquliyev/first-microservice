package az.ingress.repository;
import az.ingress.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface SchoolRepository extends CrudRepository<School, Long>,
        PagingAndSortingRepository<School, Long> {
    School save(School student);
    Page<School> findAll(Pageable pageable);
    Optional<School> findById(Long id);
    void deleteById(Long id);
}