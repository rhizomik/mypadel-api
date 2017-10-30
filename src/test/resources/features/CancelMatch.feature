Feature: Cancel match
	In order to control the cancelled matches
	As an administrator
	I want a periodic control over the cancel deadlines

	Scenario: A match has reached the deadline
		Given I login as "testplayer@mypadel.cat" with password "password"
		When I set a new public match on tomorrow at same time
		And I create it
		Then The response code is 201
		And A match with the id 1 has been created
		And It has been cancelled