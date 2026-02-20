package dev.rygen.intersectionlightcontroller.controllers;

import dev.rygen.intersectionlightcontroller.dtos.SignalGroupDTO;
import dev.rygen.intersectionlightcontroller.dtos.requests.SignalGroupCreateRequest;
import dev.rygen.intersectionlightcontroller.entities.SignalGroup;
import dev.rygen.intersectionlightcontroller.services.SignalGroupService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/signal-groups")
public class SignalGroupController {

    @Resource
    private SignalGroupService signalGroupService;

    @GetMapping
    public ResponseEntity<List<SignalGroupDTO>> getSignalGroups(
            @RequestParam Integer intersectionId) {
        List<SignalGroup> signalGroups = signalGroupService.findByIntersectionId(intersectionId);
        return ResponseEntity.ok(
                signalGroups.stream()
                        .map(SignalGroupDTO::fromEntity)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SignalGroupDTO> getSignalGroup(@PathVariable Integer id) {
        SignalGroup signalGroup = signalGroupService.findById(id);
        return ResponseEntity.ok(SignalGroupDTO.fromEntity(signalGroup));
    }

    @PostMapping
    public ResponseEntity<SignalGroupDTO> createSignalGroup(
            @RequestBody SignalGroupCreateRequest request) {
        SignalGroup signalGroup = signalGroupService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SignalGroupDTO.fromEntity(signalGroup));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SignalGroupDTO> updateSignalGroup(
            @PathVariable Integer id,
            @RequestBody SignalGroupDTO request) {
        SignalGroup signalGroup = signalGroupService.update(id, request);
        return ResponseEntity.ok(SignalGroupDTO.fromEntity(signalGroup));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSignalGroup(@PathVariable Integer id) {
        signalGroupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
