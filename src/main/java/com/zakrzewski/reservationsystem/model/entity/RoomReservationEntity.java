package com.zakrzewski.reservationsystem.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "room_reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class RoomReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomReservationId;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(name = "reservation_title", nullable = false)
    private String reservationTitle;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "description")
    private String description;

    @Column(name = "is_private")
    private boolean isPrivate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "reservation_employees",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<EmployeeEntity> emplployeesList;
}
