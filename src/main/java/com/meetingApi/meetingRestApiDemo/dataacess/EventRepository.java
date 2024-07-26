package com.meetingApi.meetingRestApiDemo.dataacess;

import com.meetingApi.meetingRestApiDemo.entities.ics.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
}
