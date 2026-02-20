package dev.rygen.intersectionlightcontroller.controllers;

import dev.rygen.intersectionlightcontroller.dtos.IntersectionDTO;
import dev.rygen.intersectionlightcontroller.dtos.requests.IntersectionUpdateRequest;
import dev.rygen.intersectionlightcontroller.dtos.requests.IntersectionCreateRequest;
import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.services.IntersectionService;
import dev.rygen.intersectionlightcontroller.services.WorkerService;
import jakarta.annotation.Resource;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/intersections")
public class IntersectionController {

    @Resource
    private IntersectionService intersectionService;

    @Resource
    private WorkerService workerService;

    @GetMapping("/{id}")
    public ResponseEntity<IntersectionDTO> getIntersection(@PathVariable int id) {
        return ResponseEntity.ok(intersectionService.findById(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<IntersectionDTO>> getIntersectionList() {
        List<Intersection> intersectionList = intersectionService.findAll();

        return ResponseEntity.ok(intersectionList.stream()
                .map(IntersectionDTO::fromEntity)
                .toList());
    }

    @PostMapping
    public ResponseEntity<IntersectionDTO> createIntersection(@RequestBody IntersectionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(intersectionService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IntersectionDTO> updateIntersection(
            @NonNull @PathVariable Integer id,
            @RequestBody IntersectionUpdateRequest request) {
        IntersectionDTO intersectionDTO = intersectionService.update(id, request);
        if(intersectionDTO.active()) {
            workerService.startIntersection(intersectionDTO.id());
        } else {
            workerService.stopIntersection(intersectionDTO.id());
        }
        return ResponseEntity.ok(intersectionService.update(id, request));
    }


    @DeleteMapping("/{id}")
    public void deleteIntersection(@PathVariable int id) {
        intersectionService.deleteIntersection(id);
    }
}
