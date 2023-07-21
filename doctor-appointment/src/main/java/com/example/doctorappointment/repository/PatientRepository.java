package com.example.doctorappointment.repository;

import com.example.doctorappointment.model.Patient;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "patient-cache")
public interface PatientRepository extends JpaRepository<Patient,Long> {

    @Cacheable(key = "#root.methodName + #p0")
    Optional<Patient> findByPhoneNumber(String phoneNumber);

    @CacheEvict(allEntries = true)
    @Override
    <S extends Patient> S save(S entity);

    @Cacheable(key = "#root.methodName + #p0")
    @Override
    Optional<Patient> findById(Long id);
}
