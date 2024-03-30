package com.daquilema.appbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
	private int id;
	private String tipoidentificacion;
	private String nombre;
	private String apellido;
	private String usuario;
	private String password;
	

}

