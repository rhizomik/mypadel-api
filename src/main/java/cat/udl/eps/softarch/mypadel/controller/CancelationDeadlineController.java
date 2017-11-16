package cat.udl.eps.softarch.mypadel.controller;

import cat.udl.eps.softarch.mypadel.config.MailConfig;
import cat.udl.eps.softarch.mypadel.domain.JoinMatch;
import cat.udl.eps.softarch.mypadel.domain.Match;
import cat.udl.eps.softarch.mypadel.domain.Player;
import cat.udl.eps.softarch.mypadel.domain.Reservation;
import cat.udl.eps.softarch.mypadel.repository.JoinMatchRepository;
import cat.udl.eps.softarch.mypadel.repository.MatchRepository;
import cat.udl.eps.softarch.mypadel.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class CancelationDeadlineController {

	private static int REVIEW_TIME = 30;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private JoinMatchRepository joinMatchRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	//This method will be called every 30 minutes after the last completion of this method.
	@Scheduled(fixedDelay = 1800000)
	public void searchReachedDeadlines() {
		List<Match> matchesToReview = getPossibleCancelledMatches();
		reviewMatches(matchesToReview);
	}

	private List<Match> getPossibleCancelledMatches() {
		ZonedDateTime znd = ZonedDateTime.now(ZoneId.of("UTC"));
		ZonedDateTime znd2 = ZonedDateTime.now(ZoneId.of("UTC")).minusMinutes(REVIEW_TIME);
		return matchRepository.findByCancelationDeadlineBetween(
			ZonedDateTime.now(ZoneId.of("UTC")).minusMinutes(REVIEW_TIME), ZonedDateTime.now(ZoneId.of("UTC")));
	}

	private void reviewMatches(List<Match> matchesToReview) {
		for (Match reviewedMatch : matchesToReview) {
			List<JoinMatch> joinMatches = joinMatchRepository.findByMatch(reviewedMatch);
			if (joinMatches.size() < 4){
				cancelMatch(reviewedMatch);
				sendMailToPlayers(joinMatches);
			}
		}
	}

	private void cancelMatch(Match reviewedMatch) {
		reviewedMatch.setCancelled(true);
		matchRepository.save(reviewedMatch);
		Reservation reservation = reviewedMatch.getReservation();
		if(reservation != null){
			reviewedMatch.setReservation(null);
			reservationRepository.delete(reservation);
		}
	}

	protected void sendMailToPlayers(List<JoinMatch> joinMatches) {
		for(JoinMatch joinMatch : joinMatches){
			Player player = joinMatch.getPlayer();
			Match match = joinMatch.getMatch();
			String matchDate = match.getStartDate().toString();
			JavaMailSender mailSender = MailConfig.getJavaMailSender();
			mailSender.send(getMessage(player, matchDate));
		}
	}

	private SimpleMailMessage getMessage(Player player, String matchDate) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(player.getEmail());
		message.setSubject("Paddle match cancelled");
		message.setText("The match on "+ matchDate +" has been cancelled. Less than four players joined.");
		return message;
	}
}
