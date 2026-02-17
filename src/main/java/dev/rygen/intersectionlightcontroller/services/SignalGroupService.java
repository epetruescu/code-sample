package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.dtos.SignalGroupDTO;
import dev.rygen.intersectionlightcontroller.entities.SignalGroup;
import dev.rygen.intersectionlightcontroller.repositories.IntersectionRepository;
import dev.rygen.intersectionlightcontroller.repositories.SignalGroupRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignalGroupService {

    @Resource
    private SignalGroupRepository signalGroupRepository;

    @Resource
    private IntersectionRepository intersectionRepository;

    public List<SignalGroup> findByIntersectionId(Integer intersectionId) {
        return signalGroupRepository.findByIntersectionIdEquals(intersectionId);
    }

    public SignalGroup findById(Integer id) {
        return signalGroupRepository.findById(id).orElseThrow();
    }

    public SignalGroup create(SignalGroupDTO request) {
        if (!intersectionRepository.existsById(request.intersectionId())) {
            throw new EntityNotFoundException("Intersection not found with Id: " + request.intersectionId());
        }
        SignalGroup signalGroup = SignalGroup.builder()
                .name(request.name()).intersectionId(request.id()).build();
        return signalGroupRepository.save(signalGroup);
    }

    public SignalGroup update(Integer id, SignalGroupDTO request) {
        SignalGroup signalGroup = findById(id);
        if (request.intersectionId() != null && !request.intersectionId().equals(signalGroup.getIntersectionId())) {
            throw new IllegalArgumentException("Cannot change intersection for existing phase");
        }
        if (StringUtils.isNotBlank(request.name())) {
            signalGroup.setName(request.name());
        }

        return signalGroupRepository.save(signalGroup);
    }

    public void delete(Integer id) {
        signalGroupRepository.deleteById(id);
    }

    public void deleteAllByIntersectionId(Integer intersectionId) {
        signalGroupRepository.deleteByIntersectionIdEquals(intersectionId);
    }
}
