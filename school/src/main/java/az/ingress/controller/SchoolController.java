package az.ingress.controller;
import az.ingress.dto.client.FullSchoolResponse;
import az.ingress.dto.request.SchoolRequest;
import az.ingress.dto.response.SchoolResponse;
import az.ingress.service.ISchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/schools")
@RequiredArgsConstructor
public class SchoolController {
    private final ISchoolService service;
    @GetMapping("{id}")
    public SchoolResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }
    @GetMapping
    public Page<SchoolResponse> getALl(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int pageSize,
                                       @RequestParam(required = false, defaultValue = "name") String sort) {
        return service.findAll(page,pageSize,sort);
    }
    @GetMapping("/{id}/students")
    public FullSchoolResponse getAllWithStudents(@PathVariable Long id) {
        return service.findAllWithStudents(id);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SchoolResponse createSchool(@RequestBody SchoolRequest school) {
        return service.saveSchool(school);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("{id}")
    public void updateById(@PathVariable Long id,
                           @RequestBody SchoolRequest school) {
        service.updateById(id,school);
    }
}