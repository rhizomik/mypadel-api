package cat.udl.eps.softarch.mypadel.steps;

import cat.udl.eps.softarch.mypadel.controller.CancelationDeadlineController;
import cat.udl.eps.softarch.mypadel.domain.*;
import cat.udl.eps.softarch.mypadel.repository.PublicMatchRepository;
import cat.udl.eps.softarch.mypadel.repository.ReservationRepository;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static cat.udl.eps.softarch.mypadel.steps.AuthenticationStepDefs.authenticate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class CancelMatchStepDefs {

	@Autowired
	private StepDefs stepDefs;

	@Autowired
	private CancelationDeadlineControllerDouble cancelationDeadlineController;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private PublicMatchRepository publicMatchRepository;

	@And("^A public match is created with a reservation$")
	public void aPublicMatchIsCreatedWithAReservation() throws Throwable {
		createReservation();
		createMatch();
	}

	private void createReservation() {
		Reservation reservation = new Reservation();
		reservation.setDuration(Duration.ofMinutes(40));
		reservation.setCourtType(CourtType.INDOOR);
		reservationRepository.save(reservation);
	}

	private void createMatch() throws Exception {
		PublicMatch match = new PublicMatch();
		ZonedDateTime startDate = ZonedDateTime.now(ZoneId.of("UTC")).plusHours(23).plusMinutes(35);
		Duration duration = Duration.ofMinutes(40);
		match.setStartDate(startDate);
		match.setDuration(duration);
		match.setCourtType(CourtType.INDOOR);
		match.setLevel(Level.ADVANCED);
		match.setReservation(reservationRepository.findOne((long)1));
		saveMatch(match);
	}

	private void saveMatch(PublicMatch match) throws Exception {
		String message = stepDefs.mapper.writeValueAsString(match);
		stepDefs.result = stepDefs.mockMvc.perform(
			post("/publicMatches")
				.contentType(MediaType.APPLICATION_JSON)
				.content(message)
				.accept(MediaType.APPLICATION_JSON)
				.with(authenticate()))
			.andDo(print());
	}

	@Transactional
	@When("^The controller acts$")
	public void theCancelDateReaches() throws Throwable {
		cancelationDeadlineController.searchReachedDeadlines();
	}

	@Then("^It has been cancelled$")
	public void itHasBeenCancelled() throws Throwable {
		stepDefs.result = stepDefs.mockMvc.perform(
			get("/publicMatches/{id}", 1)
				.accept(MediaType.APPLICATION_JSON)
				.with(authenticate()))
			.andDo(print())
			.andExpect(jsonPath("$.cancelled", is(true))
			);
		assertThat(cancelationDeadlineController.mailSend, is(true));
	}

	@And("^The reservation has been cancelled$")
	public void theReservationHasBeenCancelled() throws Throwable {
		assertThat(reservationRepository.count(), is((long)0));
	}
}

@Component
class CancelationDeadlineControllerDouble extends CancelationDeadlineController{
	boolean mailSend = false;
	@Override
	protected void sendMailToPlayers(List<JoinMatch> joinMatches) {
		mailSend=true;
	}
}
