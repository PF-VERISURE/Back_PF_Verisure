package com.verisure.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.verisure.backend.entity.ParticipationRecord;

@Repository
public interface ParticipationRecordRepository extends JpaRepository<ParticipationRecord, Long> {

    //query para obtener el historial de participaciones de un empleado: 
    // SELECT * FROM participation_records WHERE employee_id = ?
    //List<ParticipationRecord> findByApplicationEmployeeId(Long employeeId);

}