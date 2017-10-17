package cat.udl.eps.softarch.mypadel.steps;


import cat.udl.eps.softarch.mypadel.domain.Court;
import cat.udl.eps.softarch.mypadel.domain.CourtType;
import cat.udl.eps.softarch.mypadel.domain.Reservation;
import cat.udl.eps.softarch.mypadel.repository.CourtRepository;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static cat.udl.eps.softarch.mypadel.steps.AuthenticationStepDefs.authenticate;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservationStepDefs {


	@Autowired
	private StepDefs stepDefs;

	@Autowired
	private CourtRepository courtRepository;

	private ZonedDateTime startDate;
	private Duration duration;
	private Integer reservationId = 1;

	public static final String INDOOR = "indoor";
	public static final String RESERVATIONS_URI = "/reservations";

	@When("^I make a reservation on (\\d+) - (\\d+) - (\\d+) for (\\d+) minutes with CourtType \"([^\"]*)\"$")
	public void iMakeAReservationOnForMinutesWithCourtType(int day, int month, int year,
														   int duration,
														   String courtType) throws Throwable {
		Reservation reservation = makeNewReservation(day, month, year, duration, courtType);
		createReservation(reservation);
	}

	private Reservation makeNewReservation(int day, int month, int year, int duration, String courtType) {

		setFormatedDateAndDuration(day, month, year, duration);

		Reservation reservation = new Reservation();
		reservation.setStartDate(startDate);
		reservation.setDuration(this.duration);
		reservation.setCourtType(CourtType.valueOf(courtType));
		return reservation;
	}

	private void setFormatedDateAndDuration(int day, int month, int year, int duration) {
		this.startDate = ZonedDateTime.of(year, month, day, 0, 0, 0,
			0, ZoneId.of("+00:00"));
		this.duration = Duration.ofMinutes(duration);
	}

	private void createReservation(Reservation reservation) throws Throwable {
		String message = stepDefs.mapper.writeValueAsString(reservation);
		stepDefs.result = stepDefs.mockMvc.perform(
			post(RESERVATIONS_URI)
				.contentType(MediaType.APPLICATION_JSON)
				.content(message)
				.accept(MediaType.APPLICATION_JSON)
				.with(authenticate()))
			.andDo(print());
	}


	@And("^The reservation is created on (\\d+) - (\\d+) - (\\d+) for (\\d+) minutes with CourtType \"([^\"]*)\"$")
	public void theReservationIsCreatedOnForMinutesWithCourtType(int day, int month, int year,
																 int duration,
																 String courtType) throws Throwable {
		setFormatedDateAndDuration(day, month, year, duration);
		stepDefs.result = stepDefs.mockMvc.perform(
			get(RESERVATIONS_URI + "/{id}", reservationId)
				.accept(MediaType.APPLICATION_JSON)
				.with(authenticate()))
			.andExpect(jsonPath("$.id", is(reservationId)))
			.andExpect(jsonPath("$.duration", is(this.duration.toString())))
			.andExpect(jsonPath("$.startDate", is(parseDate(startDate.toString()))))
			.andExpect(jsonPath("$.courtType", is(courtType)))
			.andDo(print());

	}

	private String parseDate(String date) {
		String[] parts = date.split(":");
		return parts[0] + ":00:00" + date.substring(date.length() - 1);
	}

	@And("^The reservation can't be created$")
	public void theReservationCanTBeCreated() throws Throwable {
		stepDefs.result = stepDefs.mockMvc.perform(
			get(RESERVATIONS_URI + "/{id}", reservationId)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

	@And("^There is a reservation on (\\d+) - (\\d+) - (\\d+) for (\\d+) minutes with CourtType \"([^\"]*)\"$")
	public void thereIsAReservationOnForMinutesWithCourtType(int day, int month, int year,
															 int duration,
															 String courtType) throws Throwable {
		Reservation reservation = makeNewReservation(day, month, year, duration, courtType);
		createReservation(reservation);
	}

	@And("^There is an available court with CourtType \"([^\"]*)\"$")
	public void thereIsAnAvailableCourtWithCourtType(String courtType) throws Throwable {
		Court court = new Court();
		boolean isIndoor = courtType.equalsIgnoreCase(INDOOR);
		court.setIndoor(isIndoor);
		court.setAvailable(true);
		courtRepository.save(court);
	}

}
