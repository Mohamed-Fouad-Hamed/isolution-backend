package com.alf.accounts_service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="APP_PLACES")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_places_id_seq")
    @SequenceGenerator(name = "app_places_id_seq", sequenceName = "app_places_id_seq", allocationSize=1)
    @Column(name = "id")
    private Integer id;
    @Column(name = "build_name", nullable = false, length = 50)
    private String buildName;
    @Column(name = "apartment_mark", nullable = false, length = 20)
    private String apartmentMark;
    @Column(name = "level_apartment", length = 20)
    private String levelApartment;
    @Column(name = "street", nullable = false, length = 50)
    private String street;
    @Column(name = "special_mark", length = 50)
    private String specialMark;
    @Column(name = "phone_number", nullable = false, length = 25)
    private String phoneNumber;
    @Column(name = "place_name", length = 50)
    private String placeName;
    @Column(name = "lat", nullable = false)
    private Double lat;
    @Column(name = "lng", nullable = false)
    private Double lng;

    @JsonIgnore
    @ManyToMany(mappedBy = "placeSet")
    private Set<Account> accounts = new HashSet<>();

}
