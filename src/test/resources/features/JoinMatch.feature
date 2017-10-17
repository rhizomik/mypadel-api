Feature: Join match
  In order to join an already created match
  As a player
  I want to join a match


  Scenario: Successfully joined a match
    Given I login as "testadmin@mypadel.cat" with password "password"
    When I join to a match
    Then I successfully joined a match

  Scenario: A admin joins a match
	  Given I login as "testadmin@mypadel.cat" with password "password"
	  And the user creating it is "player"
	  When I join to a match on 11 - 10 - 2017 at 1 pm for 30 minutes and deadline 30 - 9 - 2017
	  Then I successfully joined a match
