package cat.udl.eps.softarch.mypadel.handler;

import cat.udl.eps.softarch.mypadel.domain.Admin;
import cat.udl.eps.softarch.mypadel.domain.Match;
import cat.udl.eps.softarch.mypadel.domain.Player;
import cat.udl.eps.softarch.mypadel.repository.AdminRepository;
import cat.udl.eps.softarch.mypadel.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.transaction.Transactional;

public class MatchEventHandler {
	final Logger logger = LoggerFactory.getLogger(Match.class);

	@Autowired
	AdminRepository adminRepository;

	@Autowired
	PlayerRepository playerRepository;

	@HandleBeforeCreate
	@Transactional
	public void handleMatchPreCreate(Match match){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(adminInputsInvalidPlayer(match, auth)){
			throw new NullPointerException();
		}
		match.setMatchCreator((Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}

	private boolean adminInputsInvalidPlayer(Match match, Authentication auth) {
		return auth.getPrincipal() instanceof Admin && match.getMatchCreator() == null;
	}
}
