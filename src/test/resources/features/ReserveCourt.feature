Feature: Reserve a court
	in order to play a match with my own players
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

	Scenario: Link a court with a reservation as player
		Given I login as "testplayer@mypadel.cat" with password "password"
		And There is a reservation on 8 - 10 - 2017 for 60 minutes with CourtType "INDOOR"
		And There is an available court with CourtType "INDOOR"
		When I assign the court to the reservation
		Then The response code is 204
		And The court is assigned to the reservation

	Scenario: Link a court with a reservation as admin
