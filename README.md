# Bus Line Stops

This is a simple Spring Boot application that provides the top 10 Bus Lines with the most stops using Trafiklab's API.
It also prints the name of all the stops for the Bus Line with most stops in the list.

## How to use

Add your API key to the application.yml file (located in src/main/resources). You can get your own key here: https://www.trafiklab.se/

Then simply run the application, and it will print the information to the console.

In the root of the project there is also two different run scripts for Windows and MacOS/Unix. Run mvn clean package
and go to the target folder and run the script for your OS.
