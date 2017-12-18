Feature: Find matches by date and time between
	In order to find matches starting during a given time span
	As a player
	I want to list the matches starting between two time points

	Scenario: Long time span selects all matches
		Given I login as "testplayer@mypadel.cat" with password "password"
		And There is a match starting "2017-10-11T13:00+01:00" with duration 30 minutes
		And There is a match starting "2017-11-11T13:30Z" with duration 30 minutes
		And There are 2 matches
		When I list the matches starting between "2007-10-11T00:00Z" and "2027-11-11T23:59Z"
		Then The list contains 2 matches

	Scenario: Short time span selects none
		Given I login as "testplayer@mypadel.cat" with password "password"
		And There is a match starting "2017-10-11T13:00+01:00" with duration 30 minutes
		And There is a match starting "2017-11-11T13:30Z" with duration 30 minutes
		And There are 2 matches
		When I list the matches starting between "2017-10-11T12:30Z" and "2017-11-11T13:00Z"
		Then The list contains 0 matches

	Scenario: Adjusted time span selects just one match
		Given I login as "testplayer@mypadel.cat" with password "password"
		And There is a match starting "2017-10-11T13:00+01:00" with duration 30 minutes
		And There is a match starting "2017-11-11T13:30Z" with duration 30 minutes
		And There are 2 matches
		When I list the matches starting between "2017-10-11T11:30Z" and "2017-10-11T12:30Z"
		Then The list contains 1 match
