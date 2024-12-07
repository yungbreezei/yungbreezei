import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Utility class for handling user input.
 */
public class getUserInput {

    /**
     * Prompts the user for input and returns the entered string.
     *
     * @param prompt The message prompt displayed to the user.
     * @return The user-entered string.
     * @throws RuntimeException If an error occurs while reading user input.
     */
    public static String getUserInput(String prompt) {
        // Display the prompt to the user
        System.out.print(prompt);

        // Create a BufferedReader for reading user input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            // Read and return the user-entered string
            return reader.readLine();
        } catch (IOException e) {
            // Throw a runtime exception if an error occurs during input reading
            throw new RuntimeException("Error reading user input", e);
        }
    }

    /**
     * Prompts the user for an integer input and returns the entered integer.
     *
     * @param prompt The message prompt displayed to the user.
     * @return The user-entered integer.
     */
    public static int getUserInputAsInt(String prompt) {
        // Continue prompting until valid integer input is received
        while (true) {
            try {
                // Attempt to parse the user input as an integer
                return Integer.parseInt(getUserInput(prompt).trim());
            } catch (NumberFormatException e) {
                // Display an error message for invalid input and continue the loop
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}
