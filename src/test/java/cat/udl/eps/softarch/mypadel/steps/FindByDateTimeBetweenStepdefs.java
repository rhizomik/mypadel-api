package cat.udl.eps.softarch.mypadel.steps;

import static cat.udl.eps.softarch.mypadel.steps.AuthenticationStepDefs.authenticate;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import cat.udl.eps.softarch.mypadel.domain.CourtType;
import cat.udl.eps.softarch.mypadel.domain.Match;
import cat.udl.eps.softarch.mypadel.repository.MatchRepository;
import cat.udl.eps.softarch.mypadel.repository.PublicMatchRepository;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

/**
 * Created by http://rhizomik.net/~roberto/
 */
public class FindByDateTimeBetweenStepdefs {

	@Autowired private StepDefs stepDefs;

	@Autowired MatchRepository matchRepository;
	@Autowired PublicMatchRepository publicMatchRepository;

	private List<Match> matches;

	@When("^I list the matches starting between \"([^\"]*)\" and \"([^\"]*)\"$")
	public void iListTheMatchesBetweenAnd(String from, String to) throws Throwable {

		stepDefs.result = stepDefs.mockMvc.perform(
			get("/matches/search/findByStartDateBetween?from={from}&to={to}", from, to)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andDo(print());
	}

	@Then("^The list contains (\\d+) match(?:es)?$")
	public void theListConstainsMatch(int count) throws Throwable {
		// Assert.assertEquals(count, matches.size());
		stepDefs.result.andExpect(jsonPath("$._embedded.matches", hasSize(count)));
	}

	@And("^There is a match starting \"([^\"]*)\" with duration (\\d+) minutes$")
	public void thereIsAMatchStartingAtDurationMinutesAndDeadline(String start, int durationMinutes)
		throws Throwable {

		Match match = new Match();
		match.setStartDate(ZonedDateTime.parse(start));
		match.setDuration(Duration.ofMinutes(durationMinutes));
		match.setCourtType(CourtType.INDOOR);

		String message = stepDefs.mapper.writeValueAsString(match);
		stepDefs.result = stepDefs.mockMvc.perform(
			post("/matches")
				.contentType(MediaType.APPLICATION_JSON)
				.content(message)
				.accept(MediaType.APPLICATION_JSON)
				.with(authenticate()))
			.andDo(print());
	}

	@And("^There are (\\d+) match(?:es)?$")
	public void thereAreMatches(int count) throws Throwable {
		stepDefs.result = stepDefs.mockMvc.perform(
			get("/matches")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(jsonPath("$._embedded.matches", hasSize(count)));
	}
}
