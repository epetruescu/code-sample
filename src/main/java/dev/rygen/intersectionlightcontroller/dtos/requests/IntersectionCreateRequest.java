package dev.rygen.intersectionlightcontroller.dtos.requests;

import lombok.Builder;

@Builder
public record IntersectionCreateRequest(
        String name
) {

}
