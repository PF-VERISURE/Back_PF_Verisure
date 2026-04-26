package com.verisure.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.verisure.backend.entity.EmployeeProfile;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
    
    //query para verificar si un empleado tiene una aplicacion en un proyecto: 
    // SELECT * FROM applications WHERE project_id = ? AND employee_id = ?
    // boolean existsByProjectIdAndEmployeeId(Long projectId, Long employeeId);

    //query para obtener el perfil de un empleado por su id: 
    // SELECT * FROM employee_profiles WHERE employee_id = ?
    // Optional<EmployeeProfile> findByEmployeeId(Long employeeId);

    //query para obtener el perfil de un empleado por su id de usuario: 
    // SELECT * FROM employee_profiles WHERE user_id = ?
    Optional<EmployeeProfile> findByUserId(Long userId);

    //query para verificar si un empleado existe por su id: 
    // SELECT * FROM employee_profiles WHERE employee_id = ?
    // boolean existsByEmployeeId(Long employeeId);

    //query para obtener el perfil de un empleado por su departamento: 
    // SELECT * FROM employee_profiles WHERE department = ?
    // List<EmployeeProfile> findByDepartment(String department); 

}