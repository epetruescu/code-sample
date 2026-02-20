package dev.rygen.intersectionlightcontroller.controllers;

import dev.rygen.intersectionlightcontroller.dtos.PhaseDTO;
import dev.rygen.intersectionlightcontroller.dtos.requests.PhaseCreateRequest;
import dev.rygen.intersectionlightcontroller.entities.Phase;
import dev.rygen.intersectionlightcontroller.services.PhaseService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phase")
public class PhaseController {

    @Resource
    private PhaseService phaseService;

    @GetMapping
    public ResponseEntity<List<PhaseDTO>> getPhases(@RequestParam Integer intersectionId) {
        List<Phase> phases = phaseService.findByIntersectionId(intersectionId);
        return ResponseEntity.ok(
                phases.stream()
                        .map(PhaseDTO::fromEntity)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhaseDTO> getPhase(@PathVariable Integer id) {
        Phase phase = phaseService.findById(id);
        return ResponseEntity.ok(PhaseDTO.fromEntity(phase));
    }

    @PostMapping
    public ResponseEntity<PhaseDTO> createPhase(
            @RequestBody PhaseCreateRequest request) {
        Phase phase = phaseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PhaseDTO.fromEntity(phase));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhaseDTO> updatePhase(
            @PathVariable Integer id,
            @RequestBody PhaseDTO request) {
        Phase phase = phaseService.update(id, request);
        return ResponseEntity.ok(PhaseDTO.fromEntity(phase));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhase(@PathVariable Integer id) {
        phaseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
