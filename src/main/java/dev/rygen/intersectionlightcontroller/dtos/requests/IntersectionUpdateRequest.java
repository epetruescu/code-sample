package dev.rygen.intersectionlightcontroller.dtos.requests;

import lombok.Builder;


@Builder
public record IntersectionUpdateRequest(
        String name,
        Boolean active
)
{
    public IntersectionUpdateRequest {
        if (active == null) {
            active = false;
        }
    }
}
