package com.example.doctorappointment.repository;

import com.example.doctorappointment.model.AppointmentSlot;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@CacheConfig(cacheNames = "appointment-slot-cache")
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot,Long> {

    @Cacheable(key = "#root.methodName + #p0")
    List<AppointmentSlot> findByDoctor_IdAndIsAvailableTrue(Long doctorId, Pageable pageable);

    List<AppointmentSlot> findByIsAvailableAndStartTimeBetween(Boolean status,LocalDateTime startTime,LocalDateTime endTime, Pageable pageable);

    @Override
    Optional<AppointmentSlot> findById(Long id);

    @CacheEvict(key = "'findById' + #id")
    @Override
    @Transactional
    void deleteById(Long id);

    @CacheEvict(allEntries = true)
    @Override
    <S extends AppointmentSlot> List<S> saveAll(Iterable<S> entities);

    @CacheEvict(allEntries = true)
    @Override
    <S extends AppointmentSlot> S save(S entity);
}
