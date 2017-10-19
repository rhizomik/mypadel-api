package cat.udl.eps.softarch.mypadel.steps;

import cat.udl.eps.softarch.mypadel.domain.Court;
import cat.udl.eps.softarch.mypadel.repository.CourtRepository;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import org.hamcrest.core.Is;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static cat.udl.eps.softarch.mypadel.steps.AuthenticationStepDefs.authenticate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RemoveCourtSteps {

	@Autowired
	private StepDefs stepDefs;
	@Autowired
	private CourtRepository courtRepository;


	@And("^There is an existing court$")
	public void thereIsAnExistingCourt() throws Throwable {
		Court court = new Court();
		courtRepository.save(court);
	}

	@When("^I remove a court$")
	public void iRemoveACourt() throws Throwable {
		stepDefs.result = stepDefs.mockMvc.perform(
			delete("/courts/1")
				.accept(MediaType.APPLICATION_JSON)
				.with(authenticate()));
	}

	@And("^The court has been removed")
	public void theCourtHasBeenRemoved() throws Throwable {
		stepDefs.result = stepDefs.mockMvc.perform(
			get("/courts/1")
				.accept(MediaType.APPLICATION_JSON)
				.with(authenticate()))
			.andExpect(status().isNotFound());
	}

	@And("^The court has not been removed")
	public void theCourtHasNotBeenRemoved() throws Throwable {
		Court court = courtRepository.findOne(1);
		assertThat(court.getId(), Is.is(1));
	}
}
