package com.example.doctorappointment.util;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LockUtil {

    private static final String APPOINTMENT_SLOT_LOCK_PREFIX = "CM:LOCK:APPOINTMENT_SLOT:%s";

    private final RedissonClient redissonClient;
    public LockUtil(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }


    private RLock lock(String name, long waitTimeMilliseconds, long leasTimeMilliseconds){
        try {
            this.redissonClient.getLock(name).tryLock(waitTimeMilliseconds,leasTimeMilliseconds, TimeUnit.MILLISECONDS);
            return this.redissonClient.getLock(name);
        } catch (InterruptedException ignore) {
            log.error("Exception in acquiring redisson lock {}", name);
            Thread.currentThread().interrupt();
            return null;
        }
    }

    private RLock lock(String name) {
        return this.lock(name, 0, 1000);
    }

    public void releaseLock(RLock lock) {
        lock.unlock();
    }

    public RLock getLockForAppointmentSlot(Long appointmentTimeSlotID) {
        String lockName = String.format(APPOINTMENT_SLOT_LOCK_PREFIX, appointmentTimeSlotID);
        RLock lock = lock(lockName);
        log.info("Lock acquired for lockName={} by thread={}", lockName, Thread.currentThread().getName());
        return lock;
    }
    public void releaseLockForAppointmentSlotTimeSlot(Long appointmentTimeSlotID) {
        String lockName = String.format(APPOINTMENT_SLOT_LOCK_PREFIX, appointmentTimeSlotID);
        RLock lock = redissonClient.getLock(lockName);
        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
            releaseLock(lock);
            log.info("Lock released for lockName={} by thread={}", lockName, Thread.currentThread().getName());
        }
    }

}