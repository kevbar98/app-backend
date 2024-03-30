package com.daquilema.appbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daquilema.appbackend.dto.EmployeeDto;
import com.daquilema.appbackend.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    
    //Obtener Usuario RestAPI
    @GetMapping("/{usuario}")
    public ResponseEntity<EmployeeDto> getEmployeeByUsuario(@PathVariable("usuario")String usuario){
        EmployeeDto employeeDto = employeeService.getEmployeeByUsuario(usuario);
        return ResponseEntity.ok(employeeDto);
    }
    
}