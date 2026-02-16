package dev.rygen.intersectionlightcontroller.dtos;

import lombok.Builder;

@Builder
public record IntersectionDTO(
        String activeLight
) {}
