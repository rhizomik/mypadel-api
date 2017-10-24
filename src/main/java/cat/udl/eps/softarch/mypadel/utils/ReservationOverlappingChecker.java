package cat.udl.eps.softarch.mypadel.utils;

import cat.udl.eps.softarch.mypadel.domain.Reservation;

import java.time.ZonedDateTime;

public class ReservationOverlappingChecker {

	public static boolean checkDateCollision(Reservation pendingReservation, Reservation other) {
		ZonedDateTime pendingStartDate = pendingReservation.getStartDate();
		ZonedDateTime pendingEndDate = pendingStartDate.plus(pendingReservation.getDuration());
		ZonedDateTime otherStartDate = other.getStartDate();
		ZonedDateTime otherEndDate = otherStartDate.plus(other.getDuration());
		if (otherEndDate.isBefore(pendingStartDate)) {
			return false;
		} else if (otherStartDate.isAfter(pendingEndDate)) {
			return false;
		} else {
			return true;
		}
	}
}
