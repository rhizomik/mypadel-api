Feature: Assign an available court to a Reservation when it is created
	In order to make the reservation of a court possible
	As the system
	I will automatically assign an available court to the reservation

	Scenario: Assign an available court to a new Reservation
		Given I login as "testplayer@mypadel.cat" with password "password"
		And There is an "available" court
		When I make a reservation on 15/12/2017-18:30 for 60 minutes with CourtType "UNDEFINED"
		Then An available court has been assigned to the reservation
		And The response code is 201

	Scenario: There are no courts available
		Given I login as "testplayer@mypadel.cat" with password "password"
		And There is an "unavailable" court
		When I make a reservation on 15/12/2017-18:30 for 60 minutes with CourtType "UNDEFINED"
		Then The response code is 500

	Scenario: There is an available court but not at the desired date-time
		Given I login as "testplayer@mypadel.cat" with password "password"
		And There is a reserved court at 15/12/2017-18:30 for 60 minutes
		When I make a reservation on 15/12/2017-18:00 for 60 minutes with CourtType "UNDEFINED"
		Then The response code is 500

	Scenario: A users tries to manually assign a court
		Given I login as "testadmin@mypadel.cat" with password "password"
		And There is an "available" court
		And I make a reservation on 15/12/2017-18:00 for 60 minutes with CourtType "UNDEFINED"
		When I assign a court manually
		Then The response code is 403
