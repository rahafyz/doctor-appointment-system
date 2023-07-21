package com.example.doctorappointment.repository;

import com.example.doctorappointment.model.Appointment;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@CacheConfig(cacheNames = "appointment-cache")
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    @CacheEvict(allEntries = true)
    @Override
    <S extends Appointment> S save(S entity);

    @Cacheable(key = "#root.methodName")
    @Override
    Page<Appointment> findAll(Pageable pageable);
}
