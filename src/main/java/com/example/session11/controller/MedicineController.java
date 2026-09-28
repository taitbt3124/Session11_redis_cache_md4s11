package com.example.session11.controller;

import com.example.session11.entity.Medicine;
import com.example.session11.service.MedicineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping
    public ResponseEntity<Medicine> create(@RequestBody Medicine medicine) {
        return ResponseEntity.ok(medicineService.create(medicine));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getById(@PathVariable Long id) {
        long start = System.nanoTime();
        Medicine medicine = medicineService.getById(id);
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Thời gian lấy thuốc id " + id + ": " + elapsedMs + " ms");
        return ResponseEntity.ok(medicine);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicine> update(@PathVariable Long id, @RequestBody Medicine medicine) {
        medicine.setId(id);
        return ResponseEntity.ok(medicineService.update(medicine));
    }

    @PutMapping("/sell/{id}")
    public void sellMedicine(@PathVariable Long id) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String rsBuy = medicineService.sellMedicine(id);
                System.out.println("Người dùng 1 : " + rsBuy);
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String rsBuy = medicineService.sellMedicine(id);
                System.out.println("Người dùng 2 : " + rsBuy);
            }
        });
        thread2.start();
        thread1.start();
    }
}
