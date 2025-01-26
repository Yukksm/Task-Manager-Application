import java.util.*;
import java.text.*;
import java.io.*;

public class Dashboard implements TaskManager
{
  public static String username;

  public Dashboard() {

  }

  public static void main(String args[]) {

    Dashboard dashboard = new Dashboard();
    dashboard.viewTask();
  }

  public void viewTask() 
  {
    String userFileName = "Users/" + username + ".txt";
    System.out.println("Dashboard for user: " + username);
    System.out.println("Task Name | Due Date | Priority | Status");
    int credits=0, completedcredits=0;
    try (Scanner scanner = new Scanner(new File(userFileName))) {

      while (scanner.hasNextLine()) {
        String[] parts = scanner.nextLine().split(",");

        String taskName = parts[0];
        String dueDate = parts[2];
        String priority = parts[3];
        String status = parts[5];   
        String type = parts[6];
        credits += Integer.parseInt(parts[4]);
        if(status.equalsIgnoreCase("Completed"))
        {
            completedcredits += Integer.parseInt(parts[4]);
        }
        // Display task details
        System.out.println(taskName + " | " + dueDate + " | " + priority + " | " + status + " | " + type + "\n");
      }
    } catch (IOException e) {
      System.err.println("Error reading the file: " + e.getMessage());
    }
    System.out.println("You have completed " + (completedcredits*100.0/credits) + "% of your tasks");
  }
}
