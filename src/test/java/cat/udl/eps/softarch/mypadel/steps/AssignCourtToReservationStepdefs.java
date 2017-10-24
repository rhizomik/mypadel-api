package cat.udl.eps.softarch.mypadel.steps;

import cat.udl.eps.softarch.mypadel.domain.Court;
import cat.udl.eps.softarch.mypadel.domain.CourtType;
import cat.udl.eps.softarch.mypadel.domain.Reservation;
import cat.udl.eps.softarch.mypadel.repository.CourtRepository;
import cat.udl.eps.softarch.mypadel.repository.ReservationRepository;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RestMediaTypes;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static cat.udl.eps.softarch.mypadel.steps.AuthenticationStepDefs.authenticate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class AssignCourtToReservationStepdefs {

	@Autowired
	private StepDefs stepDefs;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private CourtRepository courtRepository;
	private ZonedDateTime startdate;
	private Duration durationInMinutes;

	@Then("^An available court has been assigned to the reservation$")
	public void anAvailableCourtHasBeenAssignedToTheReservation() throws Throwable {
		Reservation reservation = reservationRepository.findOne(1L);
		Court reservedCourt = reservation.getCourt();
		assertThat(reservedCourt.getId(), is(1));
	}

	@And("^There is a reserved court at (\\d+)/(\\d+)/(\\d+)-(\\d+):(\\d+) for (\\d+) minutes$")
	public void thereIsAReservedCourtAtForMinutes(int day, int month, int year, int hour, int minutes, int duration)
		throws Throwable {
		setDateAndDuration(day, month, year, hour, minutes, duration);
		Reservation reservation = setUpReservation();
		Court court = new Court();
		reservation.setCourt(court);
		courtRepository.save(court);
		reservationRepository.save(reservation);
	}

	private void setDateAndDuration(int day, int month, int year, int hour, int minutes, int duration) {
		startdate = ZonedDateTime.of(year, month, day, hour, minutes, 0,
			0, ZoneId.of("+00:00"));
		durationInMinutes = Duration.ofMinutes(duration);
	}

	private Reservation setUpReservation() {
		Reservation reservation = new Reservation();
		reservation.setStartDate(startdate);
		reservation.setDuration(durationInMinutes);
		reservation.setCourtType(CourtType.UNDEFINED);
		return reservation;
	}

	@When("^I assign a court manually$")
	public void iAssignACourtManually() throws Throwable {
		String message = "courts/1";
		stepDefs.result = stepDefs.mockMvc.perform(
			post("/reservations/1/court")
				.contentType(RestMediaTypes.TEXT_URI_LIST)
				.content(message)
				.with(authenticate()))
			.andDo(print());
	}
}
