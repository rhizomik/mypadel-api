package cat.udl.eps.softarch.mypadel.steps;

import cat.udl.eps.softarch.mypadel.domain.Court;
import cat.udl.eps.softarch.mypadel.repository.CourtRepository;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RestMediaTypes;
import org.springframework.http.MediaType;

import static cat.udl.eps.softarch.mypadel.steps.AuthenticationStepDefs.authenticate;
import static cat.udl.eps.softarch.mypadel.steps.ReservationStepDefs.RESERVATIONS_URI;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AssignCourtToReservationSteps {

	@Autowired
	private StepDefs stepDefs;

	@Autowired
	private CourtRepository courtRepository;

	private Integer reservationId = 1;

	public static final String COURTS_URI = "/courts";

	@When("^I assign the court to the reservation$")
	public void iAssignTheCourtToTheReservation() throws Throwable {
		Court court = courtRepository.findOne(1);
		String message = COURTS_URI + "/" + court.getId();
		stepDefs.result = stepDefs.mockMvc.perform(
			put(RESERVATIONS_URI + "/{id}/court", reservationId)
				.contentType(RestMediaTypes.TEXT_URI_LIST)
				.content(message)
				.with(authenticate()))
			.andDo(print());
	}

	@And("^The court is assigned to the reservation$")
	public void theCourtIsAssignedToTheReservation() throws Throwable {
		Court court = courtRepository.findOne(1);
		stepDefs.mockMvc.perform(
			get(RESERVATIONS_URI + "/{id}/court", reservationId)
				.accept(MediaType.APPLICATION_JSON)
				.with(authenticate()))
			.andExpect(jsonPath("$.id", is(court.getId())))
			.andExpect(jsonPath("$.available", is(court.isAvailable())))
			.andExpect(jsonPath("$.indoor", is(court.isIndoor())))
			.andDo(print());

	}

	@And("^The court is not assigned to the reservation$")
	public void theCourtIsNotAssignedToTheReservation() throws Throwable {
		stepDefs.mockMvc.perform(
			get(RESERVATIONS_URI + "/{id}/court", reservationId)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
}
