Feature: Create a MatchResult
	To check if the creation of a new MatchResults succeed
	As a Player/Admin
	I want to create a new MatchResult for an existing match that has already finished

	Scenario: An admin creates a new MatchReult
		Given I login as "admin" with password "password"
		When I create a new MatchResult
		Then the response code is 201
		And A new MatchResult is added

	Scenario:
		Given I login as "admin" with password "password"
		When I create a new MatchResult
		Then the response code is 201
		And A new MatchResult is added

	Scenario: Unlogged user tries to create a public match
        Given I'm not logged in as "admin" or "player"
        When I create a new public match
        Then The response code is 401
        And The new MatchResult is not addded
