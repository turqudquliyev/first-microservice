package az.ingress.controller;
import az.ingress.dto.client.FullSchoolResponse;
import az.ingress.dto.client.StudentDto;
import az.ingress.dto.request.SchoolRequest;
import az.ingress.dto.response.SchoolResponse;
import az.ingress.service.ISchoolService;
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
class SchoolControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ISchoolService service;
    private SchoolRequest schoolRequest;
    private SchoolResponse schoolResponse;
    private FullSchoolResponse fullSchoolResponse;
    @BeforeEach
    void setUp() {
        schoolRequest = SchoolRequest
                .builder()
                .name("ADNSU")
                .email("info@adnsu.com")
                .build();
        schoolResponse = SchoolResponse
                .builder()
                .id(1L)
                .name("ADNSU")
                .email("info@adnsu.com")
                .build();
        StudentDto studentDto = StudentDto.builder()
                .firstName("Turqud")
                .lastName("Quliyev")
                .age(20)
                .email("turqudquliyev@gmail.com")
                .build();
        fullSchoolResponse = FullSchoolResponse.builder()
                .id(1L)
                .name("ADNSU")
                .email("info@adnsu.com")
                .students(List.of(studentDto))
                .build();
    }
    @Test
    void givenSchoolIdThenReturnSchoolResponseThenOk() throws Exception {
        Long id = 1L;
        when(service.findById(id)).thenReturn(schoolResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/schools/{id}", id)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(schoolResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(schoolResponse.getEmail()));
        verify(service, times(1)).findById(id);
    }
    @Test
    void givenDefaultPageRequestThenReturnPageOfSchoolResponseThenOk() throws Exception {
        int defaultPage = 0;
        int defaultPageSize = 10;
        String defaultSort = "name";
        Page<SchoolResponse> responsePage = new PageImpl<>(List.of(schoolResponse));
        when(service.findAll(defaultPage, defaultPageSize, defaultSort)).thenReturn(responsePage);
        mockMvc.perform(MockMvcRequestBuilders.get("/schools")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(schoolResponse.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value(schoolResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value(schoolResponse.getEmail()));
        verify(service, times(1)).findAll(defaultPage, defaultPageSize, defaultSort);
    }
    @Test
    void givenSchoolIdThenReturnFullSchoolResponseThenOk() throws Exception {
        Long id = 1L;
        when(service.findAllWithStudents(id)).thenReturn(fullSchoolResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/schools/{id}/students", id)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(fullSchoolResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(fullSchoolResponse.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.students", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.students[0].firstName").value(fullSchoolResponse.getStudents().get(0).getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.students[0].lastName").value(fullSchoolResponse.getStudents().get(0).getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.students[0].age").value(fullSchoolResponse.getStudents().get(0).getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.students[0].email").value(fullSchoolResponse.getStudents().get(0).getEmail()));
        verify(service, times(1)).findAllWithStudents(id);
    }
    @Test
    void givenSchoolRequestThenSaveSchoolThenReturnSchoolResponseThenIsCreated() throws Exception {
        when(service.saveSchool(schoolRequest)).thenReturn(schoolResponse);
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(schoolRequest);
        mockMvc.perform(post("/schools")
                        .accept(APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(schoolResponse.getId()))
                .andExpect(jsonPath("$.name").value(schoolResponse.getName()))
                .andExpect(jsonPath("$.email").value(schoolResponse.getEmail()));
        verify(service, times(1)).saveSchool(schoolRequest);
    }
    @Test
    void givenSchoolIdThenDeleteSchoolByIdThenStatusCodeIsNoContent() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/schools/{id}", id)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(service, times(1)).deleteById(id);
    }

    @Test
    void givenSchoolIdAndSchoolRequestThenUpdateSchoolByIdThenStatusCodeIsNoContent() throws Exception {
        Long id = 1L;
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(schoolRequest);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/schools/{id}", id)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        verify(service, times(1)).updateById(id, schoolRequest);
    }
}