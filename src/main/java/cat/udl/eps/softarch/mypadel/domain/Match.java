package cat.udl.eps.softarch.mypadel.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.ZonedDateTime;

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

    private Player matchCreator = null;

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

	public Player getMatchCreator() {
		return matchCreator;
	}

	public void setMatchCreator(Player matchCreator) {
		this.matchCreator = matchCreator;
	}
}
