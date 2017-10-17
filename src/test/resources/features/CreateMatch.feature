Feature: Create match
	In order to organize a new match
	As a player
	I want to create a new public match

	Scenario: A player creates a public match
		Given I login as "testplayer@mypadel.cat" with password "password"
		When I set a new public match on 1 - 10 - 2017 at 1 pm for 30 minutes and deadline 30 - 9 - 2017
		And I create it
		Then The response code is 201
		And A match with the id 1 has been created
		And The match creator is "testplayer"

	Scenario: An admin creates a public match
		Given I login as "testadmin@mypadel.cat" with password "password"
		When I set a new public match on 1 - 10 - 2017 at 1 pm for 30 minutes and deadline 30 - 9 - 2017
		And the user creating it is "testplayer@mypadel.cat"
		And I create it
		Then The response code is 201
		And A match with the id 1 has been created
		And The match creator is "testplayer"

	Scenario: Unlogged person tries to create a public match
		Given I'm not logged in
		When I set a new public match on 1 - 10 - 2017 at 1 pm for 30 minutes and deadline 30 - 9 - 2017
		And I create it
		Then The response code is 401

