package cat.udl.eps.softarch.mypadel.steps;


import cat.udl.eps.softarch.mypadel.domain.Court;
import cat.udl.eps.softarch.mypadel.domain.CourtType;
import cat.udl.eps.softarch.mypadel.domain.Reservation;
import cat.udl.eps.softarch.mypadel.repository.CourtRepository;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RestMediaTypes;
import org.springframework.http.MediaType;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static cat.udl.eps.softarch.mypadel.steps.AuthenticationStepDefs.authenticate;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservationStepDefs {

	public static final String INDOOR = "indoor";
	public static final String RESERVATIONS_URI = "/reservations";
	public static final String COURTS_URI = "/courts";
	@Autowired
	private StepDefs stepDefs;
	private ZonedDateTime startdate;
	private Duration duration;
	@Autowired
	private CourtRepository courtRepository;

	private Court court = new Court();
	private final int id = 1;

	@When("^I make a reservation on (\\d+) - (\\d+) - (\\d+) for (\\d+) minutes with CourtType \"([^\"]*)\"$")
	public void iMakeAReservationOnForMinutesWithCourtType(int day, int month, int year, int duration, String courtType) throws Throwable {
		Reservation reservation = makeNewReservation(day, month, year, duration, courtType);

		createReservation(reservation);
	}

	private Reservation makeNewReservation(int day, int month, int year, int duration, String courtType) {
		this.startdate = ZonedDateTime.of(year, month, day, 0, 0, 0,
			0, ZoneId.of("+00:00"));
		this.duration = Duration.ofMinutes(duration);

		Reservation reservation = new Reservation();
		reservation.setStartDate(startdate);
		reservation.setDuration(this.duration);
		reservation.setCourtType(CourtType.valueOf(courtType));
		return reservation;
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
		stepDefs.result = stepDefs.mockMvc.perform(
			get(RESERVATIONS_URI + "/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.with(authenticate()))
			.andDo(print())
			.andExpect(jsonPath("$.id", is(id)))
			.andExpect(jsonPath("$.duration", is(this.duration.toString())))
			.andExpect(jsonPath("$.startDate", is(parseDate(startdate.toString()))))
			.andExpect(jsonPath("$.courtType", is(courtType)));

	}

	private String parseDate(String date) {
		String[] parts = date.split(":");
		return parts[0] + ":00:00" + date.substring(date.length() - 1);
	}

	@And("^The reservation can't be created$")
	public void theReservationCanTBeCreated() throws Throwable {
		stepDefs.result = stepDefs.mockMvc.perform(
			get(RESERVATIONS_URI + "/{id}",id)
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
		boolean isIndoor = courtType.equalsIgnoreCase(INDOOR);
		court.setIndoor(isIndoor);
		court.setAvailable(true);
		courtRepository.save(court);
	}

	@When("^I assign the court to the reservation$")
	public void iAssignTheCourtToTheReservation() throws Throwable {
		String message = COURTS_URI + "/" + id;
		stepDefs.result = stepDefs.mockMvc.perform(
			put(RESERVATIONS_URI + "/1/court")
				.contentType(RestMediaTypes.TEXT_URI_LIST)
				.content(message)
				.with(authenticate()))
			.andDo(print());
	}

	@And("^The court is assigned to the reservation$")
	public void theCourtIsAssignedToTheReservation() throws Throwable {
		stepDefs.mockMvc.perform(
			get(RESERVATIONS_URI + "/1/court")
				.accept(MediaType.APPLICATION_JSON)
				.with(authenticate()))
			.andExpect(jsonPath("$.available", is(court.isAvailable())))
			.andExpect(jsonPath("$.indoor", is(court.isIndoor())))
			.andDo(print());

	}
}
