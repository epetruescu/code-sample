package dev.rygen.intersectionlightcontroller.dtos;

import lombok.Builder;
import lombok.NonNull;


@Builder
public record IntersectionRequest(
        @NonNull
        String name,
        Boolean active
)
{
    public IntersectionRequest {
        if (active == null) {
            active = false;
        }
    }
}
