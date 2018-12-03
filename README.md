# Rocket board app

The purpose of this app is to display a list of SpaceX rockets, and their associated launches.

To do so, it uses the [SpaceX REST API](https://github.com/r-spacex/SpaceX-API)

This project has been created to answer the below exercice.

**Note:** To answer the *allow a user to refresh the data* requirement, all screens implement *pull to refresh*.

### Subject

Implement SpaceX rocket launches informative application.

As a user, I want to see the list of rockets, when I open the application

- For each rocket in the list the next attributes should be displayed: name, country and
engines count
- When the application is opened for the first time it should display the welcome dialog

As a user, I want to have possibility to filter the list and see only active rockets. 
(Parameter “active” should be used for filtering data.)

As a user, when I press on the rocket in the list I want to see the next screen with rocket
description and selected rocket launches information

- Screen should be vertically scrollable
- In the beginning of the screen there should be a line graph showing the number of
launches per year
- After the graph there should be a description about chosen rocket
- After the description the list of chosen rocket launches should be grouped by year and
year should be displayed as a header
- For each launch should be displayed: mission name, launch date, was launch successful or
not and mission patch as an image

The application should:
- display a loading indicator if it is fetching data.
- should allow a user to refresh the data.

**Bonus 1:** All information should be cached for the future offline use.

**Bonus 2:** Animation added between screen transitions and on RecyclerView.

**Bonus 3:** Sticky headers for the years.


The app should support Android API 19 and be written with Kotlin.

