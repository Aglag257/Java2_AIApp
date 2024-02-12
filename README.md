# Programming II Application
## About
This repository includes a java Artifficial Intelligence Movie recommendation application which constitutes the semester project for the Programming II class 
## Building

Run mvn clean compile assembly:single

## Running

Run java -jar "Java2_AIApp-1.0-SNAPSHOT-jar-with-dependencies.jar"

## External resources
1. The application uses a file from IMDB official website that provides IMDB ratings matched with the movie title (resources folder).
2. The application uses OpenAI API to get Artifficial Intelligence moview recommendations, TMDB API to get all details about the movies and YouTube API to get bonus content videos rilated to the Movie. Everyone who uses the code must create their own API keys for those services, put them in an external file and add their file path in the method loadApiKeys in App class. 
3. The application uses a mySQL data base for data storage. Everyone who uses the code must create their own local data base using mySQL by running the code in the project's SQL folder.

## Stracture
1. The app folder contains src, target folders and the pom.xml file. 
2. The pom.xml file contains the building stracture of the maven project.
3. The src folder contains folders with project's classes and external files folder (resources) in main.
4. The target folder contains the .class files of the project and the executable jar file.

## Usage
1. The login menu is printed in command line when running.
2. The user makes a selection from the menu (Sign-up, Login, Continue as a guest or Exit). All selections are made inputting the corresponding selections number in the terminal.
3. If they choose 1 or 2, they enter the app and the main menu after inserting their credentials. If they choose 3 they enter but with limited functionalities. If they choose 4 the application is terminated.
4. The main menu offers some basic functionalities which all have many sub-functionalities. All of them are displayed numbered and the user enters the number of the functionality they wish to run or type text when they are asked to, based on their preferences. 
5. At each point, the user has the option to return to the exact previous menu by pressing 0 or to return to the main menu (home) by pressing the number of the final option of each menu.

## UML 
This simplified UML diagram depicts the most critical aspects of the code architecture and is designed solely for the purpose of understanding.

## Data stracture 
Overview of the data structures and algorithms used by the application:

In terms of data structures we are using arrays and lists and in terms of algorithms we have used buble short.

## Contributing
- Feel free to contribute your ideas or suggestions by opening an issue
- Make sure to keep in mind what is stated in [CONTRIBUTING.md]

## Licensing
- This project is licensed under the EUPL 1.2
- More info about the licence can be found in [LICENSE.md]