package com.zakrzewski.reservationsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Table(name = "room")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column(name = "room_name", nullable = false, unique = true)
    private String roomName;

    @Column(name = "room_description", nullable = false, columnDefinition = "TEXT")
    private String roomDescription;

    @Column(name = "room_capacity", nullable = false)
    private Integer roomCapacity;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "room_user_details", joinColumns = @JoinColumn(name = "room_id"))
    @MapKeyColumn(name = "detail_key")
    @Column(name = "detail_value")
    private Map<String, String> userRoomDetails;
}
