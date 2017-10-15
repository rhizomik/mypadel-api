package cat.udl.eps.softarch.mypadel.steps;


import cat.udl.eps.softarch.mypadel.domain.Court;
import cat.udl.eps.softarch.mypadel.domain.CourtType;
import cat.udl.eps.softarch.mypadel.domain.Reservation;
import cat.udl.eps.softarch.mypadel.repository.CourtRepository;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private StepDefs stepDefs;
	private ZonedDateTime startdate;
	private Duration duration;
	@Autowired
	private CourtRepository courtRepository;

	private Court court = new Court();
	private Reservation reservation;
	private boolean isIndoor;

	@When("^I make a reservation on (\\d+) - (\\d+) - (\\d+) for (\\d+) minutes with CourtType \"([^\"]*)\"$")
	public void iMakeAReservationOnForMinutesWithCourtType(int day, int month, int year, int duration, String courtType) throws Throwable {
		reservation = makeNewReservation(day, month, year, duration, courtType);

		createReservation(reservation);
	}

	private Reservation makeNewReservation(int day, int month, int year, int duration, String courtType) {
		this.startdate = ZonedDateTime.of(year, month, day, 0, 0, 0,
			0, ZoneId.of("+00:00"));
		this.duration = Duration.ofMinutes(duration);

		reservation = new Reservation();
		reservation.setStartDate(startdate);
		reservation.setDuration(this.duration);
		reservation.setCourtType(CourtType.valueOf(courtType));
		return reservation;
	}

	private void createReservation(Reservation reservation) throws Throwable {
		String message = stepDefs.mapper.writeValueAsString(reservation);
		stepDefs.result = stepDefs.mockMvc.perform(
			post("/reservations")
				.contentType(MediaType.APPLICATION_JSON)
				.content(message)
				.accept(MediaType.APPLICATION_JSON)
				.with(authenticate()))
			.andDo(print());
	}


	@And("^The reservation is created on (\\d+) - (\\d+) - (\\d+) for (\\d+) minutes with CourtType \"([^\"]*)\"$")
	public void theReservationIsCreatedOnForMinutesWithCourtType(int day, int month, int year, int duration, String courtType) throws Throwable {
		int id = 1;

		startdate = ZonedDateTime.of(year, month, day, 0, 0, 0,
			0, ZoneId.of("+00:00"));
		this.duration = Duration.ofMinutes(duration);

		stepDefs.result = stepDefs.mockMvc.perform(
			get("/reservations/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.with(authenticate()))
			.andDo(print())
			.andExpect(jsonPath("$.id", is(id)))
			.andExpect(jsonPath("$.duration", is(this.duration.toString())))
			.andExpect(jsonPath("$.startDate", is(parseData(startdate.toString()))))
			.andExpect(jsonPath("$.courtType", is(courtType)));

	}

	private String parseData(String data) {
		String[] parts = data.split(":");
		return parts[0] + ":00:00" + data.substring(data.length() - 1);
	}

	@And("^The reservation can't be created$")
	public void theReservationCanTBeCreated() throws Throwable {
		stepDefs.result = stepDefs.mockMvc.perform(
			get("/reservations/1")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

	@And("^There is a reservation on (\\d+) - (\\d+) - (\\d+) for (\\d+) minutes with CourtType \"([^\"]*)\"$")
	public void thereIsAReservationOnForMinutesWithCourtType(int day, int month, int year, int duration,
															 String courtType) throws Throwable {
		Reservation reservation = makeNewReservation(day, month, year, duration, courtType);
		createReservation(reservation);
	}

	@And("^There is an available court with CourtType \"([^\"]*)\"$")
	public void thereIsAnAvailableCourtWithCourtType(String courtType) throws Throwable {
		isIndoor = courtType.equalsIgnoreCase(INDOOR);
		court.setIndoor(isIndoor);
		court.setAvailable(true);
		courtRepository.save(court);
	}

	@When("^I assign the court to the reservation$")
	public void iAssignTheCourtToTheReservation() throws Throwable {
		reservation.setCourt(court);
		String message = stepDefs.mapper.writeValueAsString(reservation);
		stepDefs.result = stepDefs.mockMvc.perform(
			put("/reservations/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(message)
				.accept(MediaType.APPLICATION_JSON)
				.with(authenticate()));
	}

	@And("^The court is assigned to the reservation$")
	public void theCourtIsAssignedToTheReservation() throws Throwable {
		stepDefs.result = stepDefs.mockMvc.perform(
			get("/reservations/1/")
				.accept(MediaType.APPLICATION_JSON)
				.with(authenticate()))
			.andExpect(jsonPath("$.duration", is(reservation.getDuration().toString())))
			.andExpect(jsonPath("$.startDate", is(parseData(reservation.getStartDate().toString()))))
			.andExpect(jsonPath("$.courtType", is(reservation.getCourtType().toString())))
		.andDo(print());

		/*
		stepDefs.result = stepDefs.mockMvc.perform(
			get("/reservations/1/courtS")
				.accept(MediaType.APPLICATION_JSON)
				.with(authenticate()))
			.andExpect(jsonPath("$.available", is(court.isAvailable())))
			.andExpect(jsonPath("$.isIndoor", is(court.isIndoor())))
		.andDo(print());
		*/
	}
}
