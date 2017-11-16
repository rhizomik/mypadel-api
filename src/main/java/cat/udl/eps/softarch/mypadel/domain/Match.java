package cat.udl.eps.softarch.mypadel.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Match extends UriEntity<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private ZonedDateTime startDate;

	@NotNull
	private Duration duration;

	@NotNull
	private CourtType courtType;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private ZonedDateTime cancelationDeadline;

	private boolean isCancelled = false;

	@OneToMany(mappedBy = "invitesTo")
	@JsonIdentityReference(alwaysAsId = true)
	private List<MatchInvitation> invitations = new ArrayList<>();

	@ManyToOne
	@JsonIdentityReference(alwaysAsId = true)
	private Player matchCreator;

	@OneToOne
	@JsonIdentityReference(alwaysAsId = true)
	private Reservation reservation;

	@Override
	public Long getId() {
		return id;
	}

	public ZonedDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public CourtType getCourtType() {
		return courtType;
	}

	public void setCourtType(CourtType court) {
		this.courtType = court;
	}

	public ZonedDateTime getCancelationDeadline() {
		return cancelationDeadline;
	}

	public void setCancelationDeadline(ZonedDateTime cancelationDeadline) {
		this.cancelationDeadline = cancelationDeadline;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean cancelled) {
		isCancelled = cancelled;
	}

	public List<MatchInvitation> getInvitations() {
		return invitations;
	}

	public void setInvitations(List<MatchInvitation> invitations) {
		this.invitations = invitations;
	}

	public Player getMatchCreator() {
		return matchCreator;
	}

	public void setMatchCreator(Player matchCreator) {
		this.matchCreator = matchCreator;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
}
