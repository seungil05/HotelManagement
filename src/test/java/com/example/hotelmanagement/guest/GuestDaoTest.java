package com.example.hotelmanagement.guest;

import ch.ubs.m295.generated.v1.dto.Guest;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GuestDaoTest {

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    private GuestDao guestDao;

    Guest guest = new Guest()
            .guestId(1)
            .firstName("John")
            .lastName("Doe")
            .emailAddress("john.doe@example.com")
            .visitTime(4)
            .roomId(2);

    @BeforeEach
    void setUp() {
        this.guestDao = new GuestDao(this.namedParameterJdbcTemplate);
    }
    @Test
    void insertGuest(){
        //Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        //Act
        guestDao.insertGuest(guest);

        //Assert
        verify(this.namedParameterJdbcTemplate, times(1)).update(
                eq("""
                INSERT INTO Guests (firstName, lastName, emailAddress, roomId, visitTime) VALUES (:firstName, :lastName, :emailAddress, :roomId, :visitTime)"""),
                argumentCaptor.capture(),
                any(GeneratedKeyHolder.class)
        );
        MapSqlParameterSource mapSqlParameterSource = argumentCaptor.getValue();
        assertThat(mapSqlParameterSource.getValue("firstName")).isEqualTo("John");
        assertThat(mapSqlParameterSource.getValue("lastName")).isEqualTo("Doe");
    }

    @Test
    void updateGuest() {
        // Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        // Act
        guestDao.updateGuest(guest);
        verify(namedParameterJdbcTemplate, times(1)).update(
                eq("UPDATE Guests SET firstName = :firstName, lastName = :lastName, emailAddress = :emailAddress, roomId = :roomId, visitTime = :visitTime WHERE guestId = :guestId"),
                argumentCaptor.capture()
        );
        // Assert
        MapSqlParameterSource mapSqlParameterSource = argumentCaptor.getValue();
        assertThat(mapSqlParameterSource.getValue("firstName")).isEqualTo("John");
    }

    @Test
    void deleteGuest(){
        // Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        // Act
        guestDao.deleteGuest(1);
        verify(namedParameterJdbcTemplate, times(1)).update(
                eq("DELETE FROM Guests WHERE guestId = :guestId"),
                argumentCaptor.capture()
        );
        // Assert
        MapSqlParameterSource mapSqlParameterSource = argumentCaptor.getValue();
        assertThat(mapSqlParameterSource.getValue("guestId")).isEqualTo(1);
    }
}