package com.example.hotelmanagement.room;

import ch.ubs.m295.generated.v1.dto.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Optional;

public class RoomDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final static String SELECT_ALL_QUERY = "SELECT * FROM Rooms";

    private final static String SELECT_BY_ID_QUERY = "SELECT * FROM Rooms WHERE roomId = :roomId";

    private final static String UPDATE_ROOM_QUERY = "UPDATE Rooms SET roomNumber = :roomNumber, floor = :floor, capacity = :capacity, price = :price WHERE roomId = :roomId";

    private final static String DELETE_ROOM_QUERY = "DELETE FROM Rooms WHERE roomId = :roomId";

    private final static String CREATE_ROOM_QUERY = "INSERT INTO Rooms (roomNumber, floor, capacity, price) VALUES (:roomNumber, :floor, :capacity, :price)";

    public RoomDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<List<Room>> getRooms(){
        SqlParameterSource namedParameters = new MapSqlParameterSource();
        List<Room> result = namedParameterJdbcTemplate.query(
                SELECT_ALL_QUERY,
                namedParameters,
                (rs, rowNum) ->{
                    int id = rs.getInt("roomId");
                    int floor = rs.getInt("floor");
                    int roomNumber = rs.getInt("roomNumber");
                    int capacity = rs.getInt("capacity");
                    int price = rs.getInt("price");
                    return new Room()
                            .roomId(id)
                            .floor(floor)
                            .roomNumber(roomNumber)
                            .capacity(capacity)
                            .price(price);
                }
        );
        if (result.isEmpty()){
            return Optional.empty();
        } else {
            return Optional.of(result);
        }
    }

    public Optional<List<Room>> getRoomById(int roomId){
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("roomId", roomId);
        List<Room> result = namedParameterJdbcTemplate.query(
                SELECT_BY_ID_QUERY,
                namedParameters,
                (rs, rowNum) -> new Room()
                        .roomId(rs.getInt("roomId"))
                        .floor(rs.getInt("floor"))
                        .roomNumber(rs.getInt("roomNumber"))
                        .capacity(rs.getInt("capacity"))
                        .price(rs.getInt("price"))
        );
        if (result.size() > 1) {
            throw new RuntimeException("More than one room with id " + roomId);
        }
        if (result.isEmpty()){
            return Optional.empty();
        } else {
            return Optional.of(result);
        }
    }

    public ResponseEntity<Void> updateRoom(Room room){
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("roomId", room.getRoomId())
                .addValue("roomNumber", room.getRoomNumber())
                .addValue("floor", room.getFloor())
                .addValue("capacity", room.getCapacity())
                .addValue("price", room.getPrice());
        namedParameterJdbcTemplate.update(
                UPDATE_ROOM_QUERY,
                namedParameters
        );
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> createRoom(Room room){
        KeyHolder keyHolder = new GeneratedKeyHolder();

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("roomId", room.getRoomId())
                .addValue("roomNumber", room.getRoomNumber())
                .addValue("floor", room.getFloor())
                .addValue("capacity", room.getCapacity())
                .addValue("price", room.getPrice());
        namedParameterJdbcTemplate.update(
                CREATE_ROOM_QUERY,
                namedParameters,
                keyHolder
        );
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteRoom(int roomId){
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("roomId", roomId);
        namedParameterJdbcTemplate.update(
                DELETE_ROOM_QUERY,
                namedParameters
        );
        return ResponseEntity.ok().build();
    }
}
