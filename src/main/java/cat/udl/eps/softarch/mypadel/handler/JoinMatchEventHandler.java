package cat.udl.eps.softarch.mypadel.handler;

import cat.udl.eps.softarch.mypadel.domain.Admin;
import cat.udl.eps.softarch.mypadel.domain.JoinMatch;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class JoinMatchEventHandler {
	@HandleBeforeCreate
	@Transactional
	public void handleAdminPreCreate(JoinMatch joinMatch) {
		ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.systemDefault());
		joinMatch.setEventDate(dateTime);
	}
}
