package com.example.hotelmanagement;

import ch.ubs.m295.generated.v1.controller.GuestsApi;
import ch.ubs.m295.generated.v1.dto.Guest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public class GuestController extends AbstractController implements GuestsApi {

    private final GuestDao guestDao;

    public GuestController(GuestDao guestDao) {
        this.guestDao = guestDao;
    }

    @Override
    public ResponseEntity<List<Guest>> guestsGet() {
        Optional<List<Guest>> guests = guestDao.getAllGuests();
        if (guests.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return getRespond(guests.get());
        }
    }
}
