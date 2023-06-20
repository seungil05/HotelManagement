package com.example.hotelmanagement.room;

import ch.ubs.m295.generated.v1.controller.RoomsApi;
import ch.ubs.m295.generated.v1.dto.Room;
import com.example.hotelmanagement.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class RoomController extends AbstractController implements RoomsApi {

    private final Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final RoomDao roomDao;

    public RoomController(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @Override
    public ResponseEntity<List<Room>> roomsGet() {
        Optional<List<Room>> rooms = roomDao.getRooms();
        if (rooms.isEmpty()){
            logger.error("No rooms found");
            return notFoundRespond();
        } else {
            logger.info("All rooms selected");
            return getRespond(rooms.get());
        }
    }

    @Override
    public ResponseEntity<List<Room>> roomsIdGet(Integer roomId) {
        Optional<List<Room>> room = roomDao.getRoomById(roomId);
        if (room.isEmpty()){
            logger.error("No rooms found");
            return notFoundRespond();
        } else {
            logger.info("Room with id {} selected", roomId);
            return getRespond(room.get());
        }
    }

    @Override
    public ResponseEntity<Void> roomsPut(Room room) {
        if (room.getRoomId() == null) {
            return badRespond();
        }
        try {
            logger.info("Room with id {} updated", room.getRoomId());
            roomDao.updateRoom(room);
            return putRespond();
        }
        catch (Exception e) {
            logger.error("Room with id {} not found", room.getRoomId());
            return notFoundRespond();
        }
    }

    @Override
    public ResponseEntity<Void> roomsPost(Room room) {
        if (room == null) {
            return badRespond();
        }

        try {
            logger.info("Room created");
            roomDao.insertRoom(room);
            return postRespond();
        }
        catch (Exception e) {
            logger.error("Room could not be created");
            return notFoundRespond();
        }
    }

    @Override
    public ResponseEntity<Void> roomsIdDelete(Integer roomId) {
        if (roomId == null) {
            return badRespond();
        }
        try {
            logger.info("Room with id {} deleted", roomId);
            roomDao.deleteRoom(roomId);
            return deleteRespond();
        }
        catch (Exception e) {
            logger.error("Room with id {} not found", roomId);
            return notFoundRespond();
        }
    }
}
