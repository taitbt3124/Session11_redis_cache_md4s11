package com.example.session11.service;

import com.example.session11.entity.Medicine;
import com.example.session11.repository.MedicineRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final RedissonClient redissonClient;

    public MedicineService(MedicineRepository medicineRepository, RedissonClient redissonClient) {
        this.medicineRepository = medicineRepository;
        this.redissonClient = redissonClient;
    }

    public Medicine create(Medicine medicine) {
        medicine.setId(null);
        if (medicine.getQuantity() != null) {
            medicine.setStatusStock(medicine.getQuantity() > 0);
        }
        return medicineRepository.save(medicine);
    }

    @Cacheable(value = "medicines", key = "#id")
    public Medicine getById(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thuốc id = " + id));
    }

    @CacheEvict(value = "medicines", key = "#medicine.id")
    public Medicine update(Medicine medicine) {
        Medicine existing = medicineRepository.findById(medicine.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thuốc id = " + medicine.getId()));
        if (medicine.getMedicineName() != null) {
            existing.setMedicineName(medicine.getMedicineName());
        }
        if (medicine.getPrice() != null) {
            existing.setPrice(medicine.getPrice());
        }
        if (medicine.getQuantity() != null) {
            existing.setQuantity(medicine.getQuantity());
            existing.setStatusStock(medicine.getQuantity() > 0);
        }
        if (medicine.getExpiryDate() != null) {
            existing.setExpiryDate(medicine.getExpiryDate());
        }
        if (medicine.getDescription() != null) {
            existing.setDescription(medicine.getDescription());
        }
        return medicineRepository.save(existing);
    }

    @CacheEvict(value = "medicines", key = "#id")
    public String sellMedicine(Long id) {
        RLock lock = redissonClient.getLock("lock:medicine:" + id);
        boolean locked = false;
        try {
            locked = lock.tryLock(3, 5, TimeUnit.SECONDS);
            if (!locked) {
                return "Hệ thống đang bận, vui lòng thử lại!";
            }
            Medicine medicine = medicineRepository.findById(id).orElse(null);
            if (medicine == null) {
                return "Không tìm thấy thuốc!";
            }
            if (medicine.getQuantity() == null || medicine.getQuantity() <= 0) {
                return "Sản phẩm đã hết hàng!";
            }
            medicine.setQuantity(medicine.getQuantity() - 1);
            medicine.setStatusStock(medicine.getQuantity() > 0);
            medicineRepository.save(medicine);
            return "Thanh toán thành công thuốc: " + medicine.getMedicineName();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Giao dịch bị gián đoạn!";
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
