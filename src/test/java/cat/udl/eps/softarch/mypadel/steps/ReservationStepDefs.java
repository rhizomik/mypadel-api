package cat.udl.eps.softarch.mypadel.steps;


import cat.udl.eps.softarch.mypadel.domain.CourtType;
import cat.udl.eps.softarch.mypadel.domain.Reservation;
import cat.udl.eps.softarch.mypadel.repository.ReservationRepository;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static cat.udl.eps.softarch.mypadel.steps.AuthenticationStepDefs.authenticate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservationStepDefs {

	@Autowired
	private StepDefs stepDefs;

	@Autowired
	private ReservationRepository reservationRepository;

	private ZonedDateTime startdate;
	private Duration duration;

	@When("^I make a reservation on (\\d+)/(\\d+)/(\\d+)-(\\d+):(\\d+) for (\\d+) minutes with CourtType \"([^\"]*)" +
		"\"$")
	public void iMakeAReservationOnForMinutesWithCourtType(int day, int month, int year, int hour, int minutes, int
		duration, String courtType) throws Throwable {
		this.startdate = ZonedDateTime.of(year, month, day, hour, minutes, 0,
			0, ZoneId.of("+00:00"));
		this.duration = Duration.ofMinutes(duration);

		Reservation reservation = new Reservation();
		reservation.setStartDate(startdate);
		reservation.setDuration(this.duration);
		reservation.setCourtType(CourtType.valueOf(courtType));

		createReservation(reservation);
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


	@And("^The reservation is created on on (\\d+)/(\\d+)/(\\d+)-(\\d+):(\\d+) for (\\d+) minutes with CourtType \"" +
		"([^\"]*)\"$")
	public void theReservationIsCreatedOnForMinutesWithCourtType(int day, int month, int year, int hour, int minutes,
																 int duration, String courtType) throws Throwable {
		long id = 1L;

		startdate = ZonedDateTime.of(year, month, day, hour, minutes, 0,
			0, ZoneId.of("+00:00"));
		this.duration = Duration.ofMinutes(duration);

		Reservation createdReservation = reservationRepository.findOne(id);
		assertThat(createdReservation.getId(), is(id));
		assertThat(createdReservation.getDuration(), is(this.duration));
		assertThat(formatDate(createdReservation.getStartDate()), is(formatDate(this.startdate)));
		assertThat(createdReservation.getCourtType(), is(CourtType.valueOf(courtType)));
	}

	private String formatDate(ZonedDateTime date) {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm").format(date);
	}

	@And("^The reservation can't be created$")
	public void theReservationCanTBeCreated() throws Throwable {
		stepDefs.result = stepDefs.mockMvc.perform(
			get("/reservations/1")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

}
