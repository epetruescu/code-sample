package dev.rygen.intersectionlightcontroller.controllers;

import dev.rygen.intersectionlightcontroller.dtos.IntersectionDTO;
import dev.rygen.intersectionlightcontroller.dtos.IntersectionRequest;
import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.services.IntersectionService;
import jakarta.annotation.Resource;
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
        Intersection intersection = intersectionService.findById(id);
        return ResponseEntity.ok(IntersectionDTO.fromEntity(intersection));
    }

    @PostMapping
    public ResponseEntity<IntersectionDTO> createIntersection(@RequestBody IntersectionRequest request) {
        Intersection intersection = intersectionService.createIntersection(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(IntersectionDTO.fromEntity(intersection));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IntersectionDTO> updateIntersection(
            @PathVariable Integer id,
            @RequestBody IntersectionRequest request) {
        intersectionService.update(id, request);
        Intersection intersection = Intersection.builder()
                .intersectionId(id).name(request.name()).active(request.active()).build();
        return ResponseEntity.ok(IntersectionDTO.fromEntity(intersection));
    }


    @DeleteMapping("/{id}")
    public void deleteIntersection(@PathVariable int id) {
        intersectionService.deleteIntersection(id);
    }
}
