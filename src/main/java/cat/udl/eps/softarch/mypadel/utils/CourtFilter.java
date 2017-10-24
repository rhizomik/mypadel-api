package cat.udl.eps.softarch.mypadel.utils;

import cat.udl.eps.softarch.mypadel.domain.Court;
import cat.udl.eps.softarch.mypadel.domain.Reservation;

import java.util.List;

import static cat.udl.eps.softarch.mypadel.utils.CourtTypeMatcher.matchCourtType;
import static cat.udl.eps.softarch.mypadel.utils.ReservationOverlappingChecker.checkDateCollision;
import static java.util.stream.Collectors.toList;

public class CourtFilter {

	public List<Court> filterCompatibleCourtsWithReservation(Reservation pendingReservation, List<Court>
		availableCourts) {
		return availableCourts.parallelStream()
			.filter(court -> matchCourtType(court, pendingReservation))
			.filter(court -> courtCanBeReserved(court, pendingReservation))
			.collect(toList());
	}

	private boolean courtCanBeReserved(Court court, Reservation pendingReservation) {
		if (court.getReservations().isEmpty())
			return true;
		else
			return !isOverlappingReservation(court, pendingReservation);
	}

	private boolean isOverlappingReservation(Court court, Reservation pendingReservation) {
		List<Reservation> assignedReservations = court.getReservations();
		boolean overlapping = false;
		for (Reservation other : assignedReservations) {
			overlapping = checkDateCollision(pendingReservation, other);
		}
		return overlapping;
	}

}
