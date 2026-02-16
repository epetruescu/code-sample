package dev.rygen.intersectionlightcontroller.controllers;

import dev.rygen.intersectionlightcontroller.dtos.IntersectionDTO;
import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.services.IntersectionService;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/intersections")
public class IntersectionController {

    private final IntersectionService intersectionService;

    public IntersectionController(IntersectionService intersectionService) {
        this.intersectionService = intersectionService;
    }

    @PostMapping
    public void createIntersection(@RequestBody IntersectionDTO intersectionDto) {
        Intersection intersection = Intersection.builder()
                .activeLight(intersectionDto.activeLight())
                .build();
        this.intersectionService.createIntersection(intersection);
    }
}
