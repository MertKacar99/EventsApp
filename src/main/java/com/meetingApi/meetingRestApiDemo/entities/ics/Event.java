package com.meetingApi.meetingRestApiDemo.entities.ics;

import com.meetingApi.meetingRestApiDemo.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;  // Etkinlikler için benzersiz tanımlayıcı

    private ZonedDateTime dtstamp;  // Etkinliğin oluşturulma zamanı
    private String organizer;  // Etkinliği organize eden kişi
    private ZonedDateTime startTime;  // Etkinlik başlangıç zamanı
    private ZonedDateTime endTime;  // Etkinlik bitiş zamanı
    private String summary;  // Etkinlik özeti
    private String description;  // Etkinlik açıklaması
    private String location;  // Etkinlik yeri

    @ManyToMany
    @JoinTable(
            name = "users_events",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;






    public Event(String organizer, ZonedDateTime startTime, ZonedDateTime endTime, String summary, String description, String location) {
        this.dtstamp = ZonedDateTime.now();
        this.organizer = organizer;
        this.startTime = startTime;
        this.endTime = endTime;
        this.summary = summary;
        this.description = description;
        this.location = location;
    }



}
