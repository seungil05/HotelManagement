package com.example.hotelmanagement.guest;

import ch.ubs.m295.generated.v1.controller.GuestsApi;
import ch.ubs.m295.generated.v1.dto.Guest;
import com.example.hotelmanagement.AbstractController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
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

    @Override
    public ResponseEntity<List<Guest>> guestsIdGet(Integer guestId) {
        Optional<List<Guest>> guests = guestDao.getGuestById(guestId);
        if (guests.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return getRespond(guests.get());
        }
    }

    @Override
    public ResponseEntity<Void> guestsPut(Guest guest){
        if (guest.getGuestId() == null){
            return ResponseEntity.badRequest().build();
        }
        guestDao.updateGuest(guest);
        return putRespond();
    }

    @Override
    public ResponseEntity<Void> guestsPost(Guest guest) {
        if (guest == null) {
            return ResponseEntity.badRequest().build();
        }
        guestDao.insertGuest(guest);
        return postRespond();
    }

    @Override
    public ResponseEntity<Void> guestsIdDelete(Integer guestId) {
        if (guestId == null) {
            return ResponseEntity.badRequest().build();
        }
        guestDao.deleteGuest(guestId);
        return deleteRespond();
    }
}
