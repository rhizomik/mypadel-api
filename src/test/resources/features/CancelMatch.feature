Feature: Cancel match
	In order to control the cancelled matches
	As an administrator
	I want a periodic control over the cancel deadlines

	Scenario: A match has reached the deadline
		Given I login as "testplayer@mypadel.cat" with password "password"
		And A public match is created with a reservation
		When The controller acts
		Then It has been cancelled
		And The reservation has been cancelled
