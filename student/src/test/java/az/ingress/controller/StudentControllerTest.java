package az.ingress.controller;
import az.ingress.dto.request.StudentRequest;
import az.ingress.dto.response.StudentResponse;
import az.ingress.service.IStudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IStudentService service;
    private StudentRequest studentRequest;
    private StudentResponse studentResponse;
    @BeforeEach
    void setUp() {
        studentRequest = StudentRequest
                .builder()
                .firstName("Turqud")
                .lastName("Quliyev")
                .age(20)
                .email("turqudquliyev@gmail.com")
                .schoolId(1L)
                .build();
        studentResponse = StudentResponse
                .builder()
                .id(1L)
                .firstName("Turqud")
                .lastName("Quliyev")
                .age(20)
                .email("turqudquliyev@gmail.com")
                .schoolId(1L)
                .build();
    }
    @Test
    void givenStudentIdThenReturnStudentResponseThenOk() throws Exception {
        Long id = 1L;
        when(service.findById(id)).thenReturn(studentResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/students/{id}", id)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(studentResponse.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(studentResponse.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(studentResponse.getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(studentResponse.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.schoolId").value(studentResponse.getSchoolId()));
        verify(service, times(1)).findById(id);
    }
    @Test
    void givenDefaultPageRequestThenReturnPageOfStudentResponseThenOk() throws Exception {
        int defaultPage = 0;
        int defaultPageSize = 10;
        String defaultSort = "firstName";
        Page<StudentResponse> responsePage = new PageImpl<>(List.of(studentResponse));
        when(service.findAll(defaultPage, defaultPageSize, defaultSort)).thenReturn(responsePage);
        mockMvc.perform(MockMvcRequestBuilders.get("/students")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(studentResponse.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value(studentResponse.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value(studentResponse.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].age").value(studentResponse.getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value(studentResponse.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].schoolId").value(studentResponse.getSchoolId()));
        verify(service, times(1)).findAll(defaultPage, defaultPageSize, defaultSort);
    }
    @Test
    void givenSchoolIdThenReturnListOfSchoolResponseThenOk() throws Exception {
        Long schoolId = 1L;
        List<StudentResponse> studentResponses = List.of(studentResponse);
        when(service.findAllBySchoolId(schoolId)).thenReturn(studentResponses);
        mockMvc.perform(MockMvcRequestBuilders.get("/students/school/{schoolId}", schoolId)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(studentResponses.get(0).getId()))
                .andExpect(jsonPath("$.[0].firstName").value(studentResponses.get(0).getFirstName()))
                .andExpect(jsonPath("$.[0].lastName").value(studentResponses.get(0).getLastName()))
                .andExpect(jsonPath("$.[0].age").value(studentResponses.get(0).getAge()))
                .andExpect(jsonPath("$.[0].email").value(studentResponses.get(0).getEmail()))
                .andExpect(jsonPath("$.[0].schoolId").value(studentResponses.get(0).getSchoolId()));
        verify(service, times(1)).findAllBySchoolId(schoolId);
    }
    @Test
    void givenStudentRequestThenSaveStudentThenReturnStudentResponseThenIsCreated() throws Exception {
        when(service.saveStudent(studentRequest)).thenReturn(studentResponse);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(studentRequest);
        mockMvc.perform(post("/students")
                        .accept(APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(studentResponse.getId()))
                .andExpect(jsonPath("$.firstName").value(studentResponse.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(studentResponse.getLastName()))
                .andExpect(jsonPath("$.age").value(studentResponse.getAge()))
                .andExpect(jsonPath("$.email").value(studentResponse.getEmail()))
                .andExpect(jsonPath("$.schoolId").value(studentResponse.getSchoolId()));
        verify(service, times(1)).saveStudent(studentRequest);
    }

    @Test
    void givenStudentIdThenDeleteStudentByIdThenStatusCodeIsNoContent() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/students/{id}", id)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(service, times(1)).deleteById(id);
    }

    @Test
    void givenStudentIdAndStudentRequestThenUpdateStudentByIdThenStatusCodeIsNoContent() throws Exception {
        Long id = 1L;
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(studentRequest);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students/{id}", id)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        verify(service, times(1)).updateById(id, studentRequest);
    }
}