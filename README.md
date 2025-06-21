# Project Structure

The code is split into packages:

1. **controllers**  
   Handles user interactions with the UI—button clicks, scene switches, etc.

2. **models**  
   Carries data for each domain object (e.g. User, Book, Transaction).

3. **services**  
   Implements how information is stored and retrieved from CSV files.

4. **utils**  
   Parses and formats data for the services (CSV readers/writers, hashing, constants).

5. **views**  
   The frontend of the application—FXML layouts, CSS, and other UI resources.
