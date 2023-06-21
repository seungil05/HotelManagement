package com.example.hotelmanagement.guest;

import ch.ubs.m295.generated.v1.dto.Guest;
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
        verify(this.namedParameterJdbcTemplate).update(
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
    void getGuest(){
        // Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        Guest guest2 = new Guest();
        guest2.setGuestId(2);
        guest2.setFirstName("Max");
        guest2.setLastName("Mustermann");
        guest2.setRoomId(1);
        guest2.setEmailAddress("max.mustermann@example.com");
        guest2.setVisitTime(5);

        List<Guest> expectedGuests = Arrays.asList(guest, guest2);

        when(namedParameterJdbcTemplate.query(
                eq("SELECT * FROM Guests"),
                argumentCaptor.capture(),
                any(RowMapper.class)
        )).thenReturn(expectedGuests);

        // Act
        Optional<List<Guest>> guests = guestDao.getAllGuests();

        // Assert
        assertEquals(expectedGuests, guests.get());
    }

    @Test
    void getGuestById() {
        // Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        when(namedParameterJdbcTemplate.query(
                eq("SELECT * FROM Guests WHERE guestId = :guestId"),
                argumentCaptor.capture(),
                any(RowMapper.class)
        )).thenReturn(Arrays.asList(guest));

        // Act
        Optional<List<Guest>> guestById = guestDao.getGuestById(1);

        // Assert
        assertEquals(guest, guestById.get().get(0));
    }

    @Test
    void updateGuest() {
        // Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        when(namedParameterJdbcTemplate.update(
                eq("UPDATE Guests SET firstName = :firstName, lastName = :lastName, emailAddress = :emailAddress, roomId = :roomId, visitTime = :visitTime WHERE guestId = :guestId"),
                argumentCaptor.capture()
        )).thenReturn(1);

        // Act
        guestDao.updateGuest(guest);

        // Assert
        MapSqlParameterSource mapSqlParameterSource = argumentCaptor.getValue();
        assertThat(mapSqlParameterSource.getValue("firstName")).isEqualTo("John");
    }

    @Test
    void deleteGuest(){
        // Arrange
        ArgumentCaptor<MapSqlParameterSource> argumentCaptor =
                ArgumentCaptor.forClass(MapSqlParameterSource.class);

        when(namedParameterJdbcTemplate.update(
                eq("DELETE FROM Guests WHERE guestId = :guestId"),
                argumentCaptor.capture()
        )).thenReturn(1);

        // Act
        guestDao.deleteGuest(1);

        // Assert
        MapSqlParameterSource mapSqlParameterSource = argumentCaptor.getValue();
        assertThat(mapSqlParameterSource.getValue("guestId")).isEqualTo(1);
    }
}