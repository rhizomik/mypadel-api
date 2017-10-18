package cat.udl.eps.softarch.mypadel.steps;

import cat.udl.eps.softarch.mypadel.domain.Court;
import cat.udl.eps.softarch.mypadel.domain.Reservation;
import cat.udl.eps.softarch.mypadel.repository.ReservationRepository;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AssignCourtToReservationStepdefs {

	@Autowired
	private StepDefs stepDefs;

	@Autowired
	private ReservationRepository reservationRepository;

	@Then("^An available court has been assigned to the reservation$")
	public void anAvailableCourtHasBeenAssignedToTheReservation() throws Throwable {
		Reservation reservation = reservationRepository.findOne(1L);
		Court reservedCourt = reservation.getCourt();
		assertThat(reservedCourt.getId(), is(1));
	}

	@Given("^There is a reserved court at <day> - <month> - <year> for <minutes> minutes with " +
		"CourtType <type>$")
	public void thereIsAReservedCourtAtDayMonthYearForMinutesMinutesWithCourtTypeType() throws Throwable {
		throw new PendingException();
	}

	@Given("^There is a reserved court at (\\d+) - (\\d+) - (\\d+) for (\\d+) minutes$")
	public void thereIsAReservedCourtAtForMinutes(int day, int month, int year, int duration) throws Throwable {
		throw new PendingException();
	}

	@When("^I assign a court manually$")
	public void iAssignACourtManually() throws Throwable {
		throw new PendingException();
	}

	@And("^The court has not been assigned$")
	public void theCourtHasNotBeenAssigned() throws Throwable {
		throw new PendingException();
	}
}