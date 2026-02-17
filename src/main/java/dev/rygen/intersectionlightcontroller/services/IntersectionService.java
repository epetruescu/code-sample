package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.dtos.IntersectionRequest;
import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.repositories.IntersectionRepository;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.stereotype.Service;


@Service
public class IntersectionService {

    @Resource
    private IntersectionRepository intersectionRepository;

    public Intersection findById(int id) {
        return intersectionRepository.findById(id).get();
    }

    public Intersection createIntersection(IntersectionRequest request) {
        Intersection intersection = Intersection.builder()
                .name(request.name()).active(request.active()).build();
        return intersectionRepository.save(intersection);
    }

    public void update(Integer id, IntersectionRequest request) {
        intersectionRepository.updateNameAndActiveByIntersectionIdEquals(request.name(), request.active(), id);
    }
}
