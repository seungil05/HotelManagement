package com.example.hotelmanagement.room;

import ch.ubs.m295.generated.v1.dto.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomDaoTest {
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    private RoomDao roomDao;

    Room room = new Room()
            .roomId(1)
            .roomNumber(101)
            .floor(1)
            .capacity(2)
            .price(100);

    @BeforeEach
    void setUp() {
        this.roomDao = new RoomDao(this.namedParameterJdbcTemplate);
    }

    @Test
    void insertRoom(){
        //Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        //Act
        roomDao.insertRoom(room);

        //Assert
        verify(this.namedParameterJdbcTemplate).update(
                eq("""
                INSERT INTO Rooms (roomNumber, floor, capacity, price) VALUES (:roomNumber, :floor, :capacity, :price)"""),
                argumentCaptor.capture(),
                any(GeneratedKeyHolder.class)
        );
        MapSqlParameterSource mapSqlParameterSource = argumentCaptor.getValue();
        assertThat(mapSqlParameterSource.getValue("floor")).isEqualTo(1);
        assertThat(mapSqlParameterSource.getValue("roomNumber")).isEqualTo(101);
    }

    @Test
    void getRoom(){
        // Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        Room room2 = new Room();
        room2.setRoomId(2);
        room2.setRoomNumber(102);
        room2.setFloor(1);
        room2.setCapacity(2);
        room2.setPrice(100);

        List<Room> expectedRooms = Arrays.asList(room, room2);

        when(namedParameterJdbcTemplate.query(
                eq("SELECT * FROM Rooms"),
                argumentCaptor.capture(),
                any(RowMapper.class)
        )).thenReturn(expectedRooms);

        // Act
        Optional<List<Room>> rooms = roomDao.getRooms();

        // Assert
        assertEquals(expectedRooms, rooms.get());
    }

    @Test
    void getRoomById() {
        // Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        when(namedParameterJdbcTemplate.query(
                eq("SELECT * FROM Rooms WHERE roomId = :roomId"),
                argumentCaptor.capture(),
                any(RowMapper.class)
        )).thenReturn(Arrays.asList(room));

        // Act
        Optional<List<Room>> roomById = roomDao.getRoomById(1);

        // Assert
        assertEquals(room, roomById.get().get(0));
    }

    @Test
    void updateRoom() {
        // Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        when(namedParameterJdbcTemplate.update(
                eq("UPDATE Rooms SET roomNumber = :roomNumber, floor = :floor, capacity = :capacity, price = :price WHERE roomId = :roomId"),
                argumentCaptor.capture()
        )).thenReturn(1);

        // Act
        roomDao.updateRoom(room);

        // Assert
        MapSqlParameterSource mapSqlParameterSource = argumentCaptor.getValue();
        assertThat(mapSqlParameterSource.getValue("roomNumber")).isEqualTo(101);
    }

    @Test
    void deleteRoom(){
        // Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        when(namedParameterJdbcTemplate.update(
                eq("DELETE FROM Rooms WHERE roomId = :roomId"),
                argumentCaptor.capture()
        )).thenReturn(1);

        // Act
        roomDao.deleteRoom(1);

        // Assert
        MapSqlParameterSource mapSqlParameterSource = argumentCaptor.getValue();
        assertThat(mapSqlParameterSource.getValue("roomId")).isEqualTo(1);
    }

}