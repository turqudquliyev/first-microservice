package az.ingress.controller;
import az.ingress.dto.request.StudentRequest;
import az.ingress.dto.response.StudentResponse;
import az.ingress.service.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final IStudentService service;
    @GetMapping("{id}")
    public StudentResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }
    @GetMapping
    public Page<StudentResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int pageSize,
                                        @RequestParam(required = false, defaultValue = "firstName") String sort
    ) {
        return service.findAll(page, pageSize, sort);
    }
    @GetMapping("/school/{schoolId}")
    public List<StudentResponse> getAll(@PathVariable Long schoolId) {
        return service.findAllBySchoolId(schoolId);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public StudentResponse createStudent(@RequestBody StudentRequest student) {
        return service.saveStudent(student);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("{id}")
    public StudentResponse updateById(@PathVariable Long id,
                                      @RequestBody StudentRequest student) {
        return service.updateById(id, student);
    }
}