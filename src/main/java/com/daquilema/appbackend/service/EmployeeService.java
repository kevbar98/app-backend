package com.daquilema.appbackend.service;

import com.daquilema.appbackend.dto.EmployeeDto;

public interface EmployeeService {
	EmployeeDto createEmployee(EmployeeDto employeeDto);

	EmployeeDto getEmployeeByUsuario(String usuario);
	
	EmployeeDto updateEmployee(String usuario, EmployeeDto updatedEmployee);
	
	void deleteEmployee(String usuario);
	
	boolean existsByUsuario(String usuario);

}
