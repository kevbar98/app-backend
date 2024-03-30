package com.daquilema.appbackend.mapper;

import com.daquilema.appbackend.dto.EmployeeDto;
import com.daquilema.appbackend.entity.Employee;

public class EmployeeMapper {
	public static EmployeeDto mapToEmployeeDto(Employee employee) {
		return new EmployeeDto(
			employee.getId(),
			employee.getTipoidentificacion(),
			employee.getNombre(),
			employee.getApellido(),
			employee.getUsuario(),
			employee.getPassword()
			
		);
	}
	
	public static Employee mapToEmployee(EmployeeDto employeeDto) {
		return new Employee(
			employeeDto.getId(),
			employeeDto.getTipoidentificacion(),
			employeeDto.getNombre(),
			employeeDto.getApellido(),
			employeeDto.getUsuario(),
			employeeDto.getPassword()
		);
		
	}
}
