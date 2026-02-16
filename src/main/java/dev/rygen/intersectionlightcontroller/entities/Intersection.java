package dev.rygen.intersectionlightcontroller.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "intersection")
public class Intersection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intersection_id")
    private int intersectionId;

    @Column(name = "active_light")
    private String activeLight;
}
