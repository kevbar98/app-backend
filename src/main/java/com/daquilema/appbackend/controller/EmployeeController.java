package com.daquilema.appbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daquilema.appbackend.dto.EmployeeDto;
import com.daquilema.appbackend.exception.ResourceNotFoundException;
import com.daquilema.appbackend.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    
    //Crear Usuario RestAPI
    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDto employeeDto) {
    	 // Validar si el usuario ya existe
    	String usuario = employeeDto.getUsuario();
    	if (employeeService.existsByUsuario(usuario)) {
    	    return ResponseEntity.badRequest().body("Este usuario ya existe.");
    	}
    	
        // Validar el tipo de identificación
        String tipoIdentificacion = employeeDto.getTipoidentificacion();
        if (!isValidTipoIdentificacion(tipoIdentificacion)) {
            return ResponseEntity.badRequest().body("El tipo de identificación es inválido.");
        }
        
        // Validar el usuario según el tipo de identificación
        usuario = employeeDto.getUsuario();
        if (!isValidUsuario(tipoIdentificacion, usuario)) {
            if (tipoIdentificacion.equals("cedula")) {
                return ResponseEntity.badRequest().body("Error: El usuario debe contener solo números y exactamente 10 dígitos.");
            } else if (tipoIdentificacion.equals("ruc")) {
                return ResponseEntity.badRequest().body("Error: El usuario debe contener solo números y exactamente 13 dígitos.");
            } else if (tipoIdentificacion.equals("pasaporte")) {
                return ResponseEntity.badRequest().body("Error: El usuario debe contener solo números y letras (mayúsculas o minúsculas).");
            }
        }
        
        // Validar nombre y apellido
        String nombre = employeeDto.getNombre();
        String apellido = employeeDto.getApellido();
        if (!isValidNombreApellido(nombre) || !isValidNombreApellido(apellido)) {
            return ResponseEntity.badRequest().body("El nombre y el apellido deben contener solo letras mayúsculas.");
        }
        
     // Guardar el empleado si pasa las validaciones
        EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);
        return ResponseEntity.ok("El usuario se ha creado exitosamente.");
    }
    
    private boolean isValidTipoIdentificacion(String tipoIdentificacion) {
        return tipoIdentificacion.equals("cedula") || tipoIdentificacion.equals("pasaporte") || tipoIdentificacion.equals("ruc");
    }
    
    private boolean isValidUsuario(String tipoIdentificacion, String usuario) {
        if (tipoIdentificacion.equals("cedula")) {
            if (!usuario.matches("\\d{10}")) {
                return false;
            }
        } else if (tipoIdentificacion.equals("ruc")) {
            if (!usuario.matches("\\d{13}")) {
                return false;
            }
        } else if (tipoIdentificacion.equals("pasaporte")) {
            if (!usuario.matches("[a-zA-Z0-9]+")) {
                return false;
            }
        }
        return true;
    }

    
    private boolean isValidNombreApellido(String nombreApellido) {
        return nombreApellido.matches("[A-ZÑÁÉÍÓÚÜ\\s]+");
    }
    
    
    //Obtener Usuario RestAPI
    @GetMapping("/{usuario}")
    public ResponseEntity<EmployeeDto> getEmployeeByUsuario(@PathVariable("usuario")String usuario){
        EmployeeDto employeeDto = employeeService.getEmployeeByUsuario(usuario);
        return ResponseEntity.ok(employeeDto);
    }
    
    //Actualizar Usuario RestAPI
    @PutMapping("/{usuario}")
    public ResponseEntity<?> updateEmployee(@PathVariable("usuario") String usuario, @RequestBody EmployeeDto updatedEmployeeDto) {
        // Validar campos actualizados
        if (updatedEmployeeDto == null) {
            return ResponseEntity.badRequest().body("Debe proporcionar al menos un campo para actualizar.");
        }

        // Validar el tipo de identificación si se proporciona
        String tipoIdentificacion = updatedEmployeeDto.getTipoidentificacion();
        if (tipoIdentificacion != null && !isValidTipoIdentificacion(tipoIdentificacion)) {
            return ResponseEntity.badRequest().body("El tipo de identificación es inválido.");
        }

        // Validar el usuario según el tipo de identificación si se proporciona
        String nuevoUsuario = updatedEmployeeDto.getUsuario();
        if (nuevoUsuario != null && !isValidUsuario(tipoIdentificacion, nuevoUsuario)) {
            return ResponseEntity.badRequest().body("Error: El usuario no cumple con los requisitos para el tipo de identificación proporcionado.");
        }

        // Validar nombre y apellido si se proporcionan
        String nombre = updatedEmployeeDto.getNombre();
        String apellido = updatedEmployeeDto.getApellido();
        if (nombre != null && apellido != null && (!isValidNombreApellido(nombre) || !isValidNombreApellido(apellido))) {
            return ResponseEntity.badRequest().body("El nombre y el apellido deben contener solo letras mayúsculas.");
        }

        // Actualizar el empleado con los campos proporcionados
        EmployeeDto updatedEmployee = employeeService.updateEmployee(usuario, updatedEmployeeDto);
        if (updatedEmployee != null) {
            return ResponseEntity.ok("El usuario se ha actualizado exitosamente.");
        } else {
            return ResponseEntity.notFound().build(); // Devolver 404 si el empleado no existe
        }
    }


    //Eliminar Usuario RestAPI
    
    @DeleteMapping("/{usuario}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("usuario") String usuario) {
        try {
            // Llama al servicio para eliminar al empleado con el usuario proporcionado
            employeeService.deleteEmployee(usuario);
            
            // Devuelve una respuesta con estado 200 OK y un mensaje de éxito
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (ResourceNotFoundException e) {
            // Si el empleado no se encuentra, devuelve una respuesta con estado 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } catch (Exception e) {
            // Si ocurre algún otro error, devuelve una respuesta con estado 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar Usuario: " + e.getMessage());
        }
    }

    
    
    
    
}