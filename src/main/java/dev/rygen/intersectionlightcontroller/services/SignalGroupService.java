package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.dtos.SignalGroupDTO;
import dev.rygen.intersectionlightcontroller.entities.SignalGroup;
import dev.rygen.intersectionlightcontroller.entities.SignalGroupPhase;
import dev.rygen.intersectionlightcontroller.enums.LightColor;
import dev.rygen.intersectionlightcontroller.repositories.IntersectionRepository;
import dev.rygen.intersectionlightcontroller.repositories.SignalGroupPhaseRepository;
import dev.rygen.intersectionlightcontroller.repositories.SignalGroupRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignalGroupService {

    @Resource
    private SignalGroupRepository signalGroupRepository;

    @Resource
    private IntersectionRepository intersectionRepository;

    @Resource
    private SignalGroupPhaseRepository signalGroupPhaseRepository;

    public List<SignalGroup> findByIntersectionId(Integer intersectionId) {
        return signalGroupRepository.findByIntersectionIdEquals(intersectionId);
    }

    public SignalGroup findById(Integer id) {
        return signalGroupRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Signal Group not found with id: " + id)
        );
    }

    @Transactional
    public SignalGroup create(SignalGroupDTO request) {
        if (!intersectionRepository.existsById(request.intersectionId())) {
            throw new EntityNotFoundException("Intersection not found with Id: " + request.intersectionId());
        }
        SignalGroup signalGroup = SignalGroup.builder()
                .name(request.name())
                .intersectionId(request.intersectionId())
                .build();
        return signalGroupRepository.save(signalGroup);
    }

    @Transactional
    public SignalGroup update(Integer id, SignalGroupDTO request) {
        SignalGroup signalGroup = findById(id);
        if (request.intersectionId() != null && !request.intersectionId().equals(signalGroup.getIntersectionId())) {
            throw new IllegalArgumentException("Cannot change intersection for existing signal group");
        }
        if (StringUtils.isNotBlank(request.name())) {
            signalGroup.setName(request.name());
        }

        return signalGroupRepository.save(signalGroup);
    }

    @Transactional
    public void delete(Integer id) {
        //it can be deleted. State management will happen else where
        signalGroupPhaseRepository.findBySignalGroupId(id);
        signalGroupRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllByIntersectionId(Integer intersectionId) {
        signalGroupRepository.deleteByIntersectionIdEquals(intersectionId);
    }

    public boolean existsById(Integer signalGroupId) {
        return signalGroupRepository.existsById(signalGroupId);
    }

    public void updateLight(int signalGroupId, LightColor currentLight) {
        signalGroupRepository.updateCurrentColorBySignalGroupIdEquals(currentLight, signalGroupId);
    }
}
