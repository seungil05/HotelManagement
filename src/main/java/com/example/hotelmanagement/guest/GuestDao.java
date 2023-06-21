package com.example.hotelmanagement.guest;

import ch.ubs.m295.generated.v1.dto.Guest;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;
import java.util.Optional;

public class GuestDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String SELECT_ALL_QUERY = "SELECT * FROM Guests";

    private final static String SELECT_BY_ID_QUERY = "SELECT * FROM Guests WHERE guestId = :guestId";

    private final static String UPDATE_GUEST_QUERY = "UPDATE Guests SET firstName = :firstName, lastName = :lastName, emailAddress = :emailAddress, roomId = :roomId, visitTime = :visitTime WHERE guestId = :guestId";

    private final static String INSERT_GUEST_QUERY = "INSERT INTO Guests (firstName, lastName, emailAddress, roomId, visitTime) VALUES (:firstName, :lastName, :emailAddress, :roomId, :visitTime)";

    private final static String DELETE_GUEST_QUERY = "DELETE FROM Guests WHERE guestId = :guestId";

    public GuestDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<List<Guest>> getAllGuests(){
        SqlParameterSource namedParameters = new MapSqlParameterSource();
        List<Guest> result = namedParameterJdbcTemplate.query(
                SELECT_ALL_QUERY,
                namedParameters,
                (rs, rowNum) ->{
                    int id = rs.getInt("guestId");
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    String emailAddress = rs.getString("emailAddress");
                    int roomId = rs.getInt("roomId");
                    int visitTime = rs.getInt("visitTime");
                    return new Guest()
                            .guestId(id)
                            .firstName(firstName)
                            .lastName(lastName)
                            .emailAddress(emailAddress)
                            .roomId(roomId)
                            .visitTime(visitTime);
                }
        );
        if (result.isEmpty()){
            return Optional.empty();
        } else {
            return Optional.of(result);
        }
    }

    public Optional<List<Guest>> getGuestById(int guestId){
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("guestId", guestId);
        List<Guest> result = namedParameterJdbcTemplate.query(SELECT_BY_ID_QUERY, namedParameters, (rs, rowNum)
                -> new Guest()
                .guestId(rs.getInt("guestId"))
                .firstName(rs.getString("firstName"))
                .lastName(rs.getString("lastName"))
                .emailAddress(rs.getString("emailAddress"))
                .roomId(rs.getInt("roomId"))
                .visitTime(rs.getInt("visitTime")));

        if (result.size() > 1) {
            throw new RuntimeException("More than one guest with id " + 1);
        }
        if (result.isEmpty()){
            return Optional.empty();
        } else {
            return Optional.of(result);
        }
    }

    public ResponseEntity<Void> updateGuest(Guest guest){
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("guestId", guest.getGuestId())
                .addValue("firstName", guest.getFirstName())
                .addValue("lastName", guest.getLastName())
                .addValue("emailAddress", guest.getEmailAddress())
                .addValue("roomId", guest.getRoomId())
                .addValue("visitTime", guest.getVisitTime());
        namedParameterJdbcTemplate.update(UPDATE_GUEST_QUERY, namedParameters);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> insertGuest(Guest guest){
        KeyHolder keyHolder = new GeneratedKeyHolder();

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("firstName", guest.getFirstName())
                .addValue("lastName", guest.getLastName())
                .addValue("emailAddress", guest.getEmailAddress())
                .addValue("roomId", guest.getRoomId())
                .addValue("visitTime", guest.getVisitTime());
        namedParameterJdbcTemplate.update(INSERT_GUEST_QUERY, namedParameters, keyHolder);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteGuest(int guestId){
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("guestId", guestId);
        namedParameterJdbcTemplate.update(DELETE_GUEST_QUERY, namedParameters);
        return ResponseEntity.ok().build();
    }

}
