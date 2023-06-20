package com.example.hotelmanagement.room;

import ch.ubs.m295.generated.v1.dto.Room;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoomDaoTest {
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    void insertRoom(){
        //Arrange
        RoomDao roomDao = new RoomDao(this.namedParameterJdbcTemplate);
        Room room = new Room();
        room.setRoomId(1);
        room.setRoomNumber(101);
        room.setFloor(1);
        room.capacity(2);
        room.setPrice(100);

        //Act
        roomDao.insertRoom(room);

        //Assert
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor = ArgumentCaptor.forClass(MapSqlParameterSource.class);
        verify(this.namedParameterJdbcTemplate, times(1))
                .update(anyString(),argumentCaptor.capture() ,any(GeneratedKeyHolder.class));
        assertThat(argumentCaptor.getValue().getValue("roomNumber")).isEqualTo(101);
    }

    @Test
    void updateRoom(){
        //Arrange
        RoomDao roomDao = new RoomDao(this.namedParameterJdbcTemplate);
        Room room = new Room();
        room.setRoomId(1);
        room.setRoomNumber(101);
        room.setFloor(2);
        room.capacity(2);
        room.setPrice(200);

        roomDao.insertRoom(room);

        room.setFloor(1);
        room.setRoomNumber(102);

        //Act
        roomDao.updateRoom(room);

        //Assert
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor = ArgumentCaptor.forClass(MapSqlParameterSource.class);
        verify(this.namedParameterJdbcTemplate, times(1))
                .update(anyString(),argumentCaptor.capture() ,any(GeneratedKeyHolder.class));
        MapSqlParameterSource mapSqlParameterSource = argumentCaptor.getValue();
        //asserThat floor is 1
        //assertThat roomNumber is 102
        assertThat(mapSqlParameterSource.getValue("roomNumber")).isEqualTo(102);
    }

}