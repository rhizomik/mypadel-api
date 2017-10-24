Feature: Reserve a court
	in order to play a match with my own players
	As a player
	I want to reserve a court

	Scenario: Reserve a court
		Given I login as "testplayer@mypadel.cat" with password "password"
		And There is an "available" court
		When I make a reservation on 8/10/2017-18:30 for 60 minutes with CourtType "INDOOR"
		Then The response code is 201
		And The reservation is created on on 8/10/2017-18:30 for 60 minutes with CourtType "INDOOR"

	Scenario: Reserve a court
		Given I'm not logged in
		And There is an "available" court
		When I make a reservation on 8/10/2017-18:00 for 60 minutes with CourtType "INDOOR"
		Then The response code is 401
		And The reservation can't be created
