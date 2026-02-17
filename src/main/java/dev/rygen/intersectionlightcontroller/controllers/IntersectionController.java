package dev.rygen.intersectionlightcontroller.controllers;

import dev.rygen.intersectionlightcontroller.dtos.IntersectionDTO;
import dev.rygen.intersectionlightcontroller.dtos.IntersectionRequest;
import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.services.IntersectionService;
import jakarta.annotation.Resource;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/intersections")
public class IntersectionController {

    @Resource
    private IntersectionService intersectionService;

    @GetMapping("/{id}")
    public ResponseEntity<IntersectionDTO> getIntersection(@PathVariable int id) {
        return ResponseEntity.ok(intersectionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<IntersectionDTO> createIntersection(@RequestBody IntersectionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(intersectionService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IntersectionDTO> updateIntersection(
            @NonNull @PathVariable Integer id,
            @RequestBody IntersectionRequest request) {
        return ResponseEntity.ok(intersectionService.update(id, request));
    }


    @DeleteMapping("/{id}")
    public void deleteIntersection(@PathVariable int id) {
        intersectionService.deleteIntersection(id);
    }
}
