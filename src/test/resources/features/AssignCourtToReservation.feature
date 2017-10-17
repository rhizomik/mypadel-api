Feature: Assign an available court to a reservation
	In order to play in a court
	As a player
	I want to assign a court to my reservation

	Scenario: Link a court with a reservation as player
		Given I login as "testplayer@mypadel.cat" with password "password"
		And There is a reservation on 8 - 10 - 2017 for 60 minutes with CourtType "INDOOR"
		And There is an available court with CourtType "INDOOR"
		When I assign the court to the reservation
		Then The response code is 204
		And The court is assigned to the reservation

	Scenario: Link a court with a reservation as admin
		Given I login as "testadmin@mypadel.cat" with password "password"
		And There is a reservation on 8 - 10 - 2017 for 60 minutes with CourtType "INDOOR"
		And There is an available court with CourtType "INDOOR"
		When I assign the court to the reservation
		Then The response code is 204
		And The court is assigned to the reservation

	Scenario: Link a court with a reservation not logged
		Given I'm not logged in
		And There is a reservation on 8 - 10 - 2017 for 60 minutes with CourtType "INDOOR"
		And There is an available court with CourtType "INDOOR"
		When I assign the court to the reservation
		Then The response code is 401
		And The court is not assigned to the reservation
