# Padel Match Organizer - MyPadel-API

Class project for the Software Architecture 2017-18 Course, Polytechnic School, University of Lleida

[![Build Status](https://travis-ci.org/UdL-EPS-SoftArch/mypadel-api.svg?branch=master)](https://travis-ci.org/UdL-EPS-SoftArch/mypadel-api/branches) 
<a href="https://zenhub.com"><img src="https://cdn.rawgit.com/ZenHubIO/support/master/zenhub-badge.svg"></a>

## Vision

**For** padel players **who** are interested in organizing padel matches with people of their same level.
The project **Padel match organizer** is a field reservation manager **that** also allows users to:

 * Create and join matches.
 * Indicate the result of previous matches so that system determines players level.
 * Level up or down depending on their results.
 * Reserve courts. 


**Unlike** other field reservation management apps, our project allows players to join matches with people 
of their same padel level. Additionally, they do not have to worry about reserving a field.


## Features per Stakeholder

| Player                        | Admin                         |
| ------------------------------| ------------------------------|
| Create match                  | Create match                  |
| Join match                    | Add player match              |
| Leave match                   | Remove player from match      |
| Reserve court                 | Reserve court for a player    |
| Cancel reservation            | Cancel reservation            |
| Add match result              | Add match result              |
| Confirm match result          | Confirm match result          |
| Invite players to match       | Invite players to match       |
| Accept invitation to match    | Create court                  |
|                               | Remove court                  |
|                               | Create player                 |
|                               | Remove player                 |

## Entities Model

![Entities Model Diagram](http://www.plantuml.com/plantuml/svg/3Sen3e0W3030h-W3eBkRWvk3YV4125L8A93InlXxNIytfkAp1bJEGnHfVG9WzzWxITHJExAlHR28zPNW2AjPjjtizPAPVGHwcmkOZMi9o7L1XlycUGtLtsrJoXy0?0)
