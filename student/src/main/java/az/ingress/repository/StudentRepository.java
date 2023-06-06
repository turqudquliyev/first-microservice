package az.ingress.repository;
import az.ingress.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface StudentRepository extends CrudRepository<Student, Long>,
        PagingAndSortingRepository<Student,Long> {
    Student save(Student student);
    Page<Student> findAll(Pageable pageable);
    List<Student> findAllBySchoolId(Long id);
    Optional<Student> findById(Long id);
    void deleteById(Long id);
}