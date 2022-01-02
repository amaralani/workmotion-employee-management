package com.workmotion.employeemanagement.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workmotion.employeemanagement.TestUtil;
import com.workmotion.employeemanagement.domain.Employee;
import com.workmotion.employeemanagement.dto.EmployeeResponse;
import com.workmotion.employeemanagement.dto.EmployeeStatusUpdateRequest;
import com.workmotion.employeemanagement.dto.NewEmployeeRequest;
import com.workmotion.employeemanagement.service.EmployeeService;
import com.workmotion.employeemanagement.service.IllegalStateChangeException;
import com.workmotion.employeemanagement.service.InvalidEmployeeIdException;
import com.workmotion.employeemanagement.state.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@AutoConfigureMockMvc
@SpringBootTest
public class EmployeeApiTest {
	@MockBean
	private EmployeeService mockEmployeeService;
	@Autowired
	private MockMvc mvc;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public EmployeeApiTest() {
	}

	@Test
	public void getAllShouldReturnEmployees() throws Exception {
		Employee employee1 = TestUtil.createEmployee(1, Status.ADDED);
		Employee employee2 = TestUtil.createEmployee(2, Status.APPROVED);
		Mockito.when(mockEmployeeService.getAll()).thenReturn(List.of(employee1, employee2));
		String stringResponse =
				mvc.perform(MockMvcRequestBuilders.get("/employee"))
						.andExpect(MockMvcResultMatchers.status().isOk())
						.andReturn().getResponse().getContentAsString();

		List<EmployeeResponse> responseList = objectMapper.readValue(stringResponse, new TypeReference<>() {});
		Assertions.assertThat(responseList).isNotEmpty();
		Assertions.assertThat(responseList).size().isEqualTo(2);
		Assertions.assertThat(responseList).anyMatch((employeeResponse) -> TestUtil.matches(employeeResponse, employee1));
		Assertions.assertThat(responseList).anyMatch((employeeResponse) -> TestUtil.matches(employeeResponse, employee2));
	}

	@Test
	public void getAllShouldReturnEmpty() throws Exception {
		Mockito.when(mockEmployeeService.getAll()).thenReturn(Collections.emptyList());
		String stringResponse =
				mvc.perform(MockMvcRequestBuilders.get("/employee"))
						.andExpect(MockMvcResultMatchers.status().isOk())
						.andReturn().getResponse().getContentAsString();

		List<EmployeeResponse> responseList = objectMapper.readValue(stringResponse, new TypeReference<>() {});
		Assertions.assertThat(responseList).isEmpty();
	}

	@Test
	public void getByIdShouldReturnNotFound() throws Exception {
		String id = UUID.randomUUID().toString();
		Mockito.when(mockEmployeeService.getById(id)).thenReturn(null);

		mvc.perform(MockMvcRequestBuilders.get("/employee/" + id))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void getByIdShouldReturnEmployee() throws Exception {
		String id = UUID.randomUUID().toString();
		Employee employee = TestUtil.createEmployee(1, Status.ADDED);
		employee.setId(id);
		Mockito.when(mockEmployeeService.getById(id)).thenReturn(employee);

		String stringResponse =
				mvc.perform(MockMvcRequestBuilders
						.get("/employee/" + id))
						.andExpect(MockMvcResultMatchers.status().isOk())
						.andReturn().getResponse().getContentAsString();

		EmployeeResponse response = objectMapper.readValue(stringResponse, EmployeeResponse.class);
		Assertions.assertThat(TestUtil.matches(response, employee)).isTrue();
	}

	@Test
	public void createEmployeeShouldReturnLocation() throws Exception {
		String id = UUID.randomUUID().toString();
		NewEmployeeRequest newEmployeeRequest = new NewEmployeeRequest();
		newEmployeeRequest.setName("name");
		newEmployeeRequest.setUsername("username");
		Mockito.when(mockEmployeeService.createEmployee("name", "username")).thenReturn(id);

		mvc.perform(MockMvcRequestBuilders.post("/employee")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newEmployeeRequest)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().string("Location", "/employee/" + id))
				.andExpect(MockMvcResultMatchers.content().string(""));
	}

	@Test
	public void updateEmployeeWhenSuccessFullShouldReturnAccepted() throws Exception {
		String id = UUID.randomUUID().toString();
		EmployeeStatusUpdateRequest employeeStatusUpdateRequest = new EmployeeStatusUpdateRequest();
		employeeStatusUpdateRequest.setId(id);
		employeeStatusUpdateRequest.setState(Status.IN_CHECK);

		Employee employee = TestUtil.createEmployee(1, Status.ADDED);
		employee.setId(id);

		Mockito.when(mockEmployeeService.getById(id)).thenReturn(employee);
		Mockito.doNothing().when(mockEmployeeService).updateEmployee(Mockito.any());
		Mockito.doReturn(employee).when(mockEmployeeService).getById(id);

		mvc.perform(MockMvcRequestBuilders.put("/employee")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeStatusUpdateRequest)))
				.andExpect(MockMvcResultMatchers.status().isAccepted())
				.andExpect(MockMvcResultMatchers.content().string(""));
	}

	@Test
	public void updateEmployeeNotAcceptableShouldReturnBadRequest() throws Exception {
		String id = UUID.randomUUID().toString();
		EmployeeStatusUpdateRequest employeeStatusUpdateRequest = new EmployeeStatusUpdateRequest();
		employeeStatusUpdateRequest.setId(id);
		employeeStatusUpdateRequest.setState(Status.IN_CHECK);

		Employee employee = TestUtil.createEmployee(1, Status.ADDED);
		employee.setId(id);

		Mockito.when(mockEmployeeService.getById(id)).thenReturn(employee);
		String errorMessage = "Bad State Change";
		Mockito.doThrow(new IllegalStateChangeException(errorMessage)).when(mockEmployeeService).updateEmployee(Mockito.any());

		mvc.perform(MockMvcRequestBuilders.put("/employee")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeStatusUpdateRequest)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string(errorMessage));
	}

	@Test
	public void updateEmployeeInvalidEmployeeShouldReturnBadRequest() throws Exception {
		EmployeeStatusUpdateRequest employeeStatusUpdateRequest = new EmployeeStatusUpdateRequest();
		employeeStatusUpdateRequest.setId(UUID.randomUUID().toString());
		employeeStatusUpdateRequest.setState(Status.IN_CHECK);

		String errorMessage = "Invalid employee id";
		Mockito.doThrow(new InvalidEmployeeIdException(errorMessage)).when(mockEmployeeService).updateEmployee(Mockito.any());

		mvc.perform(MockMvcRequestBuilders.put("/employee")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employeeStatusUpdateRequest)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().string(errorMessage));
	}
}