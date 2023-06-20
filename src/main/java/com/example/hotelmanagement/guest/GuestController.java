package com.example.hotelmanagement.guest;

import ch.ubs.m295.generated.v1.controller.GuestsApi;
import ch.ubs.m295.generated.v1.dto.Guest;
import com.example.hotelmanagement.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class GuestController extends AbstractController implements GuestsApi {

    private final Logger logger = LoggerFactory.getLogger(GuestController.class);

    private final GuestDao guestDao;

    public GuestController(GuestDao guestDao) {
        this.guestDao = guestDao;
    }

    @Override
    public ResponseEntity<List<Guest>> guestsGet() {
        Optional<List<Guest>> guests = guestDao.getAllGuests();
        if (guests.isEmpty()){
            logger.error("No guests found");
            return getRespond(guests.get());
        } else {
            logger.info("All guests selected");
            return getRespond(guests.get());
        }
    }

    @Override
    public ResponseEntity<List<Guest>> guestsIdGet(Integer guestId) {
        Optional<List<Guest>> guests = guestDao.getGuestById(guestId);
        if (guests.isEmpty()){
            logger.error("No guests found");
            return notFoundRespond();
        } else {
            logger.info("Guest with matching Id found");
            return getRespond(guests.get());
        }
    }

    @Override
    public ResponseEntity<Void> guestsPut(Guest guest){
        if (guest.getGuestId() == null){
            return badRespond();
        }
        try {
            logger.info("Guest with id {} updated", guest.getGuestId());
            guestDao.updateGuest(guest);
            return putRespond();
        } catch (Exception e){
            logger.error("Guest with id {} not found", guest.getGuestId());
            return notFoundRespond();
        }
    }

    @Override
    public ResponseEntity<Void> guestsPost(Guest guest) {
        if (guest == null) {
            return badRespond();
        }
        try {
            logger.info("Guest created");
            guestDao.insertGuest(guest);
            return postRespond();
        } catch (Exception e) {
            logger.error("Guest could not be created");
            return notFoundRespond();
        }
    }

    @Override
    public ResponseEntity<Void> guestsIdDelete(Integer guestId) {
        if (guestId == null) {
            return badRespond();
        }
        try {
            logger.info("Guest with id {} deleted", guestId);
            guestDao.deleteGuest(guestId);
            return deleteRespond();
        }
        catch (Exception e) {
            logger.error("Guest with id {} not found", guestId);
            return notFoundRespond();
        }
    }
}
