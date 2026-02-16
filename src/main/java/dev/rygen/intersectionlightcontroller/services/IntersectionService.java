package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.repositories.IntersectionRepository;
import org.springframework.stereotype.Service;


@Service
public class IntersectionService {

    private final IntersectionRepository intersectionRepository;

    public IntersectionService(IntersectionRepository intersectionRepository) {
        this.intersectionRepository = intersectionRepository;
    }

    public Intersection createIntersection(Intersection intersection) {
        return this.intersectionRepository.save(intersection);
    }
}
