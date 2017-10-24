package cat.udl.eps.softarch.mypadel.utils;

import cat.udl.eps.softarch.mypadel.domain.Court;
import cat.udl.eps.softarch.mypadel.domain.CourtType;
import cat.udl.eps.softarch.mypadel.domain.Reservation;

public class CourtTypeMatcher {

	public static boolean matchCourtType(Court court, Reservation reservation) {
		CourtType courtType = court.isIndoor() ? CourtType.INDOOR : reservation.getCourtType();
		return courtType.equals(reservation.getCourtType());
	}
}
