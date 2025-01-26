//package Users;

import java.io.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

class User 
{
    private String username;
    private String password;
    private String mobileNumber;
    static final String USER_CREDENTIALS_FILE_PATH = "Users/user_credentials.txt";

    public User(String username, String password, String mobileNumber) 
    {
        this.username = username;
        this.password = password;
        this.mobileNumber = mobileNumber;
    }

    public String getUsername() 
    {
        return username;
    }

    public String getPassword() 
    {
        return password;
    }

    public String getMobileNumber() 
    {
        return mobileNumber;
    }

    public void setPassword(String newPassword) 
    {
        this.password = newPassword;
    }
}

class NewUser extends User 
{
    public NewUser(String username, String password, String mobileNumber) 
    {
        super(username, password, mobileNumber);
    }

    public String register(Map<String, User> existingUsernames, Scanner scanner) 
    {
        String newUsername;
        String newPassword;
        String newMobileNumber;

        boolean validUsername;
        boolean validMobileNumber;
        boolean validPassword;
        do 
        {
            System.out.print("Enter username: ");
            newUsername = scanner.next();
            validUsername = isValidUsername(existingUsernames, newUsername);

            if (!validUsername) 
            {
                System.out.println("Username already exists. Please choose a different one.");
            }

        } while (!validUsername);

        do 
        {
            System.out.print("Enter password: ");
            newPassword = scanner.next();
            validPassword = isValidPassword(newPassword);

            if (!validPassword) 
            {
                System.out.println("Invalid password. Password must contain one capital letter, one number, one special character, and be 8 characters long.");
            }
        } while (!validPassword);

        do 
        {
            System.out.print("Enter mobile number: ");
            newMobileNumber = scanner.next();
            validMobileNumber = isValidMobileNumber(newMobileNumber);

            if (!validMobileNumber) 
            {
                System.out.println("Invalid mobile number. Please enter a 10-digit mobile number.");
            }

        } while (!validMobileNumber);

        saveUserToFile(newUsername, newPassword, newMobileNumber);
        System.out.println("Registration successful!");
        return newUsername;
    }

    private boolean isValidUsername(Map<String, User> existingUsernames, String username) 
    {
        return !existingUsernames.containsKey(username);
    }

    private boolean isValidMobileNumber(String mobileNumber) 
    {
        return mobileNumber.matches("\\d{10}");
    }

    private boolean isValidPassword(String password) 
    {
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }

    private void saveUserToFile(String username, String password, String mobileNumber) 
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter("Users/" + username + ".txt"))) 
        {
            
        } 
        catch (IOException e) 
        {
            System.out.println("Error creating user file.");
            e.printStackTrace();
        }

        try (PrintWriter credentialsWriter = new PrintWriter(new FileWriter(USER_CREDENTIALS_FILE_PATH, true))) 
        {
            credentialsWriter.println(username + "," + password + "," + mobileNumber);
        } 
        catch (IOException e) 
        {
            System.out.println("Error updating user credentials.");
            e.printStackTrace();
        }
    }
}
class ExistingUser extends User 
{
    public ExistingUser(String username, String password, String mobileNumber) 
    {
        super(username, password, mobileNumber);
    }

    public String login(Map<String, User> existingUsernames, Scanner scanner) 
    {
        int option;
        boolean userAuthenticated = false;
	    String inputUsername="";
        do 
        {
            System.out.println("Choose an option:");
            System.out.println("1. Check Credentials");
            System.out.println("2. Forgot Password");

            option = UserManagementApp.readInt(scanner);

            switch (option) 
            {
                case 1:
                    System.out.print("Enter your username: ");
                    inputUsername = scanner.next();
                    System.out.print("Enter your password: ");
                    String inputPassword = scanner.next();

                    if(existingUsernames.containsKey(inputUsername)
                            && existingUsernames.get(inputUsername).getPassword().equals(inputPassword)) 
                    {
                        System.out.println("Credentials are correct!");
                        userAuthenticated = true;
                    } 
                    else 
                    {
                        System.out.println("Invalid username or password. Please try again.");
                    }
                    break;

                case 2:
                    forgotPassword(existingUsernames, scanner);
                    break;

                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        } 
        while (userAuthenticated==false);

        return inputUsername;
    }

    public void forgotPassword(Map<String, User> existingUsernames, Scanner scanner) 
    {
        scanner.nextLine();
        System.out.print("Enter your username: ");
        String inputUsername = scanner.nextLine();

        System.out.print("Enter your mobile number: ");
        String inputMobileNumber = scanner.nextLine();
        boolean validPassword = false;
        String newPassword = "";

        if (existingUsernames.containsKey(inputUsername) &&
                existingUsernames.get(inputUsername).getMobileNumber().equals(inputMobileNumber)) 
        {
            do 
            {
                System.out.print("Enter your new password: ");
                newPassword = scanner.next();
                validPassword = isValidPassword(newPassword);

                if (!validPassword) 
                {
                    System.out.println("Invalid password. Password must contain one capital letter, one number, one special character, and be 8 characters long.");
                }
            } while (!validPassword);

            existingUsernames.get(inputUsername).setPassword(newPassword);
            updateUserCredentialsDatabase(existingUsernames);

            System.out.println("Your password has been successfully reset.");
        } 
        else 
        {
            System.out.println("Invalid username or mobile number. Please try again.");
        }
    }

    private boolean isValidPassword(String password) 
    {
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }

    private void updateUserCredentialsDatabase(Map<String, User> existingUsernames) 
    {
        try (PrintWriter credentialsWriter = new PrintWriter(new FileWriter("Users/user_credentials.txt"))) 
        {
            for (User user : existingUsernames.values()) 
            {
                credentialsWriter.println(user.getUsername() + "," + user.getPassword() + "," + user.getMobileNumber());
            }
        } 
        catch (IOException e) 
        {
            System.out.println("Error updating user credentials.");
            e.printStackTrace();
        }
    }
}

public class UserManagementApp 
{
    public static String username;

    public static void main(String args[])
    {
        UserManagementApp obj = new UserManagementApp();
        obj.UserLoginRegister();
    }
    public String UserLoginRegister()
    {
        Scanner scanner = new Scanner(System.in);
        Map<String, User> existingUsernames = loadUsernamesAndPasswordsFromFile();
        boolean userAuthenticated = false;

        while (!userAuthenticated) 
        {
            System.out.println("Choose an option:");
            System.out.println("1. Register as a new user");
            System.out.println("2. Login as an existing user");

            int option = readInt(scanner);

            switch (option) {
                case 1:
                    NewUser newUser = new NewUser("", "", "");
                    username = newUser.register(existingUsernames, scanner);
                    userAuthenticated = true;
                    break;

                case 2:
                    ExistingUser existingUser = new ExistingUser("", "", "");
                    username = existingUser.login(existingUsernames, scanner);
                    userAuthenticated = true;
                    break;

                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
        return username;
    }

    private static Map<String, User> loadUsernamesAndPasswordsFromFile() 
    {
        Map<String, User> existingUsernames = new HashMap<>();
        try (Scanner scanner = new Scanner(new File("Users/user_credentials.txt"))) 
        {
            while (scanner.hasNextLine()) 
            {
                String[] parts = scanner.nextLine().split(",");
                String username = parts[0];
                String password = parts[1];
                String mobileNumber = parts[2];
                existingUsernames.put(username, new User(username, password, mobileNumber));
            }
        } 
        catch (FileNotFoundException e) 
        {
        }
        return existingUsernames;
    }

    public static int readInt(Scanner scanner) 
    {
        boolean validInput = false;
        int choice = 0;

        do 
        {
            try 
            {
                System.out.print("Enter your option: ");
                choice = scanner.nextInt();
                validInput = true;
            } 
            catch (InputMismatchException e) 
            {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        } 
        while (!validInput);

        return choice;
    }
}