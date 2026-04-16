// package com.verisure.backend.mapper;

// import org.mapstruct.Mapper;
// import org.mapstruct.Mapping;
// import com.verisure.backend.entity.Application;
// import com.verisure.backend.dto.response.EmployeeApplicationResponseDTO;
// import com.verisure.backend.dto.response.AdminApplicationResponseDTO;

// @Mapper(componentModel = "spring")
// public interface ApplicationMapper {

//     @Mapping(source = "id", target = "applicationId")
//     @Mapping(source = "createdAt", target = "appliedAt")
//     @Mapping(source = "project.id", target = "projectId")
//     @Mapping(source = "project.title", target = "projectTitle")
//     @Mapping(source = "project.startDate", target = "startDate")
//     @Mapping(source = "project.endDate", target = "endDate")
//     @Mapping(source = "project.locationType", target = "locationType")
//     @Mapping(source = "project.imageUrl", target = "imageUrl")

//     EmployeeApplicationResponseDTO toEmployeeResponse(Application application);

//     @Mapping(source = "id", target = "applicationId")
//     @Mapping(source = "createdAt", target = "appliedAt")
//     @Mapping(source = "project.id", target = "projectId")
//     @Mapping(source = "project.title", target = "projectTitle")
//     @Mapping(source = "employee.employeeId", target = "corporateEmployeeId")
//     @Mapping(source = "employee.firstName", target = "firstName")
//     @Mapping(source = "employee.lastName", target = "lastName")
//     @Mapping(source = "employee.department", target = "department")
    
//     AdminApplicationResponseDTO toAdminResponse(Application application);
// }