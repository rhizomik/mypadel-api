Feature: Reserve a court
	In order to play a match with my own players
	As a player
	I want to reserve a court

	Scenario: Make a reservation as player
		Given I login as "testplayer@mypadel.cat" with password "password"
		When I make a reservation on 8 - 10 - 2017 for 60 minutes with CourtType "INDOOR"
		Then The response code is 201
		And The reservation is created on 8 - 10 - 2017 for 60 minutes with CourtType "INDOOR"

	Scenario: Make a reservation as admin
		Given I login as "testadmin@mypadel.cat" with password "password"
		When I make a reservation on 8 - 10 - 2017 for 60 minutes with CourtType "INDOOR"
		Then The response code is 201
		And The reservation is created on 8 - 10 - 2017 for 60 minutes with CourtType "INDOOR"

	Scenario: Make a reservation not logged
		Given I'm not logged in
		When I make a reservation on 8 - 10 - 2017 for 60 minutes with CourtType "INDOOR"
		Then The response code is 401
		And The reservation can't be created
