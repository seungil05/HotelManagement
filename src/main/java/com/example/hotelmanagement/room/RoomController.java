package com.example.hotelmanagement.room;

import ch.ubs.m295.generated.v1.controller.RoomsApi;
import ch.ubs.m295.generated.v1.dto.Room;
import com.example.hotelmanagement.AbstractController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class RoomController extends AbstractController implements RoomsApi {

    private final RoomDao roomDao;

    public RoomController(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @Override
    public ResponseEntity<List<Room>> roomsGet() {
        Optional<List<Room>> rooms = roomDao.getRooms();
        if (rooms.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return getRespond(rooms.get());
        }
    }

    @Override
    public ResponseEntity<List<Room>> roomsIdGet(Integer roomId) {
        Optional<List<Room>> room = roomDao.getRoomById(roomId);
        if (room.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return getRespond(room.get());
        }
    }

    @Override
    public ResponseEntity<Void> roomsPut(Room room) {
        if (room.getRoomId() == null) {
            return ResponseEntity.badRequest().build();
        }
        roomDao.updateRoom(room);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> roomsPost(Room room) {
        if (room == null) {
            return ResponseEntity.badRequest().build();
        }
        roomDao.createRoom(room);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> roomsIdDelete(Integer roomId) {
        if (roomId == null) {
            return ResponseEntity.badRequest().build();
        }
        roomDao.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }
}
