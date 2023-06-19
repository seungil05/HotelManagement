package com.example.hotelmanagement;

import ch.ubs.m295.generated.v1.controller.GuestsApi;
import ch.ubs.m295.generated.v1.dto.Guest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;
import java.util.Optional;

@Configuration
public class GuestDao {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final static String SELECT_ALL_QUERY = "SELECT * FROM Guests";

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

}
