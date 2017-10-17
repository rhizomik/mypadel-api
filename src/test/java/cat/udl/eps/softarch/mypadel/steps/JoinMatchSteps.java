package cat.udl.eps.softarch.mypadel.steps;

import cat.udl.eps.softarch.mypadel.domain.*;
import cat.udl.eps.softarch.mypadel.repository.PublicMatchRepository;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import cat.udl.eps.softarch.mypadel.repository.PlayerRepository;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static cat.udl.eps.softarch.mypadel.steps.AuthenticationStepDefs.authenticate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class JoinMatchSteps {

    @Autowired
    private StepDefs stepDefs;


	JoinMatch joinMatch = new JoinMatch();

	PublicMatch match = new PublicMatch();

	private ZonedDateTime start;

	private Duration duration;

	private ZonedDateTime cancelation;

	@Autowired
	PlayerRepository playerRepository;

	@Autowired
	PublicMatchRepository pub;

	@When("^I join to a match$")
    public void iJoinToAMatchWithDatetime() throws Throwable {
		String message = stepDefs.mapper.writeValueAsString(joinMatch);
        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/joinMatches")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(message)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(authenticate()))
                .andDo(print());
    }

    @Then("^I successfully joined a match$")
    public void iSuccessfullyJoinedAMatch() throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/joinMatches/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(authenticate()))
                .andDo(print())
                .andExpect(status().isOk());
    }

	@And("^the user creating it is \"([^\"]*)\"$")
	public void theUserCreatingItIs(String player) throws Throwable {
		joinMatch.setPlayer(playerRepository.findOne(player));
	}


	@When("^I join to a match on (\\d+) - (\\d+) - (\\d+) at (\\d+) pm for (\\d+) minutes and deadline (\\d+) - (\\d+) - (\\d+)$")
	public void iJoinToAMatchOnAtPmForMinutesAndDeadline(int day, int month, int year, int hour, int duration,
														 int cancelationDay, int cancelationMonth,
												 int cancelationYear) throws Throwable {


		start = ZonedDateTime.of(year, month, day, hour, 0, 0,0, ZoneId.of("+00:00"));
		this.duration = Duration.ofMinutes(duration);
		cancelation = ZonedDateTime.of(cancelationYear, cancelationMonth, cancelationDay,
			hour, 0, 0,0, ZoneId.of("+00:00"));
		match.setStartDate(start);
		match.setDuration(this.duration);
		match.setCancelationDeadline(cancelation);
		match.setCourtType(CourtType.OUTDOOR);
		match.setLevel(Level.INTERMEDIATE);
		joinMatch.setMatch(match);
		ZonedDateTime date = ZonedDateTime.now(ZoneId.systemDefault());
		joinMatch.setEventDate(date);

		String message = stepDefs.mapper.writeValueAsString(joinMatch);
		stepDefs.result = stepDefs.mockMvc.perform(
			post("/joinMatches")
				.contentType(MediaType.APPLICATION_JSON)
				.content(message)
				.accept(MediaType.APPLICATION_JSON)
				.with(authenticate()))
			.andDo(print());
	}
}
