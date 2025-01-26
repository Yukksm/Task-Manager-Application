import java.io.*;
import java.util.*;

//import Users.UserManagementApp;

import java.text.*;

class TaskNotFoundException extends Exception {
  public TaskNotFoundException(String message) {
    super(message);
  }
}

class InvalidOperationException extends Exception {
  public InvalidOperationException(String message) {
    super(message);
  }
}

class InvalidTimeException extends Exception {
  public InvalidTimeException(String message) {
    super(message);
  }
}

class Searcher<T extends Task> {
  String taskNameToEdit;

  public Searcher(String taskNameToEdit) {
    this.taskNameToEdit = taskNameToEdit;
  }

  public Searcher() {

  }

  int search(T t) {
    if (t.getTaskname().equals(taskNameToEdit)) {
      return 1;
    }
    return 0;
  }

  int search(T t, String a) {
    if (t.type.equals(a)) {
      return 1;
    }
    return 0;
  }

  int search(T t, String priority, String b)
  {
    if(t.priority.equals(priority))
    {
      return 1;
    }
    return 0;
  }
}

abstract class Task implements TaskManager{
  protected String taskname;
  protected String description;
  protected String duedate;
  protected String priority;
  protected int credits;
  protected String status;
  protected String type;
  protected String frequency;
  protected int number;
  public Task(String taskname, String description, String duedate, int credits, String priority, String type) {
    this.taskname = taskname;
    this.description = description;
    this.duedate = duedate;
    this.credits = credits;
    this.priority = priority;
    this.status = "Incomplete";
    this.type = type;
  }

  public Task(String taskname, String description, String duedate, String priority, String type) {
    this.taskname = taskname;
    this.description = description;
    this.duedate = duedate;
    this.priority = priority;
    this.status = "Incomplete";
    this.type = type;
  }

  public Task(String taskname, String description, String duedate, String priority, int credits, String type,
      String status) 
      {
    this.taskname = taskname;
    this.description = description;
    this.duedate = duedate;
    this.priority = priority;
    this.credits = credits;
    this.type = type;
    this.status = status;
  }


  public Task(String taskname, String description, String duedate, String priority, int credits, String status, String type, String frequency, int number)
  {
    this.taskname = taskname;
    this.description = description;
    this.duedate = duedate;
    this.priority = priority;
    this.credits = credits;
    this.type = type;
    this.status = status;
    this.frequency = frequency;
    this.number = number;
  }
  public String getPriority() {
    return priority;
  }

  public String getTaskname() {
    return taskname;
  }

  public void setTaskname(String taskname) {
    this.taskname = taskname;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDuedate() {
    return duedate;
  }

  public void setDuedate(String duedate) {
    this.duedate = duedate;
  }

  public abstract void addTask(Hashtable<String, ArrayList<Task>> tasks, String username);
  public void viewTask()
  {
    
  }
  public void viewTask(String type, String priority, Hashtable<String, ArrayList<Task>> tasks)
      throws TaskNotFoundException 
      {
    if (!tasks.containsKey(priority)) {
      throw new TaskNotFoundException("No tasks found with the specified priority.");
    }

    ArrayList<Task> taskList = tasks.get(priority);
    for (Task task : taskList) {
      if (type.equals(task.type)) {
        System.out.println("Task Name: " + task.taskname);
        System.out.println("Description: " + task.description);
        System.out.println("Due Date: " + task.duedate);
        System.out.println("Status: " + task.status);
        System.out.println("Credits: " + task.credits);
        System.out.println("Task type: " + task.type);
        System.out.println("----------------------");
      }
    }
  }

  public void editTask(String type, String username, String priority, Hashtable<String, ArrayList<Task>> tasks)
      throws TaskNotFoundException {
    if (!tasks.containsKey(priority)) {
      throw new TaskNotFoundException("No tasks found with the specified priority and type.");
    }
    ArrayList<Task> taskList = new ArrayList<>();
    for (Map.Entry<String, ArrayList<Task>> entry : tasks.entrySet()) 
    {
      ArrayList<Task> taskArrayList = entry.getValue();
      taskList.addAll(taskArrayList);
    }
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter the task name to edit:");
    String taskNameToEdit = scanner.nextLine();
    int j = 0;
    Searcher<Task> obj = new Searcher<>(taskNameToEdit);
    for (Task task : taskList) {
      if ((obj.search(task,priority,"")==1) && (obj.search(task) == 1) && (obj.search(task, type) == 1)) {
        System.out.println("Enter new due date:");
        task.duedate = scanner.nextLine();
        System.out.println("Enter new status:");
        task.status = scanner.nextLine();
        System.out.println("Task edited successfully!");
        j = 1;
      }
    }
    if (j == 1) {
      saveTaskToFile(username, taskList);

      return;
    }

    throw new TaskNotFoundException("No tasks found with the specified name in the given priority and type.");
  }

  public void deleteTask(String type, String username, String priority, Hashtable<String, ArrayList<Task>> tasks)
      throws TaskNotFoundException {
    if (!tasks.containsKey(priority)) {
      throw new TaskNotFoundException("No tasks found with the specified priority.");
    }

    ArrayList<Task> taskList = new ArrayList<>();
    for (Map.Entry<String, ArrayList<Task>> entry : tasks.entrySet()) 
    {
      ArrayList<Task> taskArrayList = entry.getValue();
      taskList.addAll(taskArrayList);
    }
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter the task name to delete:");
    String taskNametoDelete = scanner.nextLine();
    int j = 0, k = 0;
    ;
    for (Task task : taskList) {
      if (task.priority.equals(priority) && task.taskname.equals(taskNametoDelete) && task.type.equals(type)) {
        k = 1;
        break;
      }
      j++;
    }
    taskList.remove(j);
    if (k == 1) {
      saveTaskToFile(username, taskList);

      return;
    }

    throw new TaskNotFoundException("No tasks found with the specified name in the given priority and type.");

  }

  public void trackCredits(String type, Hashtable<String, ArrayList<Task>> tasks) {
    int completedCredits = 0;
    int totalCredits = 0;

    for (Map.Entry<String, ArrayList<Task>> entry : tasks.entrySet()) {
      ArrayList<Task> taskList = entry.getValue();
      for (Task task : taskList) {
        totalCredits += task.credits;
        if (task.type.equals(type)) {
          if (task.status.equalsIgnoreCase("Completed")) {
            completedCredits += task.credits;
          }
        }
      }
    }

    double percentage = (double) completedCredits / totalCredits * 100;

    System.out.println("Credit Percentage: " + percentage + "%");

    if (percentage < 40) {
      System.out.println("Your chores need attention.");
    } else if (percentage < 50) {
      System.out.println("You are halfway there.");
    } else if (percentage < 70) {
      System.out.println("At the right pace.");
    } else if (percentage < 90) {
      System.out.println("Almost there.");
    } else {
      System.out.println("Excellent work!");
    }
  }

  private void saveTaskToFile(String username, ArrayList<Task> taskList) {
    String filePath = "Users/" + username + ".txt";

    try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
      for (Task task : taskList) 
      {if(task.type.equalsIgnoreCase("Normal"))
      {
        writer.println(task.taskname + "," + task.description + "," + task.duedate + "," + task.priority + ","
            + task.credits + "," + task.status + "," + task.type);
      }
      else
      {
        writer.println(
          task.taskname + "," + task.description + "," + task.duedate + "," + task.priority + "," + task.credits + "," + task.status + "," + task.type  + "," + task.frequency + "," + task.number);
      }
      }
    } catch (IOException e) {
      System.out.println("Error saving task to file: " + e.getMessage());
    }
  }

}

class RecurringTask extends Task {
  private String frequency;

  public RecurringTask(String taskname, String description, String duedate, String priority, int credits, String type) {
    super(taskname, description, duedate, credits, priority, type);
  }

  public RecurringTask(String taskname, String description, String duedate, String priority, int credits, String type,
      String status) {
    super(taskname, description, duedate, priority, credits, type, status);
  }

  public RecurringTask(String taskname, String description, String duedate, String priority, String frequency,
      int periodInDays, int numberOfWeeks, String type) {
    super(taskname, description, duedate, priority, type);
    this.frequency = frequency;
  }

  public RecurringTask(String taskname, String description, String duedate, String priority, int credits, String status, String type, String frequency, int number)
      {
        super(taskname, description, duedate, priority, credits, status, type, frequency, number);
      }
  public void setCredits(int credits) 
  {
    this.credits = credits;
  }

  public void addTask(Hashtable<String, ArrayList<Task>> tasks, String username) 
  {
    Scanner scanner = new Scanner(System.in);
    String taskname;
    ArrayList<Task> taskList1 = new ArrayList<>();
    int checker = 0;
    for (Map.Entry<String, ArrayList<Task>> entry : tasks.entrySet()) 
    {
      ArrayList<Task> taskArrayList = entry.getValue();
      taskList1.addAll(taskArrayList);
    }
    while (true) {
      checker = 0;
      System.out.print("Enter task name:");
      taskname = scanner.nextLine();
      for (Task task : taskList1) {
        if (task.taskname.equals(taskname)) {
          System.out.println("Invalid task name as it is already present");
          checker = 1;
        }
      }

      if (checker == 1) {
        continue;
      }
      break;
    }

    System.out.println("Enter description:");
    String description = scanner.nextLine();

    System.out.println("Enter due date:");
    String duedate = scanner.nextLine();

    String priority = "high";

    System.out.println("Choose the frequency of repetition:");
    System.out.println("1. On count of days (less than a week)");
    System.out.println("2. Weekly basis");
    System.out.println("3. Monthly basis");
    int frequencyChoice = scanner.nextInt();

    String frequency = "";
    int periodInDays = 0;
    int numberOfWeeks = 0;
    int number=1;
    switch (frequencyChoice) 
    {
      case 1:
        frequency = "daily";
        System.out.println("Enter the period in days:");
        number = scanner.nextInt();
        break;
      case 2:
        frequency = "weekly";
        System.out.println("Enter the number of weeks:");
        number = scanner.nextInt();
        break;
      case 3:
        frequency = "monthly";
        break;
      default:
        System.out.println("Invalid choice. Setting frequency to default.");
    }

    Calendar calendar = Calendar.getInstance();
    if ("daily".equals(frequency)) 
    {
      calendar.add(Calendar.DATE, periodInDays);
    } 
    else if ("weekly".equals(frequency)) 
    {
      calendar.add(Calendar.WEEK_OF_YEAR, numberOfWeeks);
    } 
    else if ("monthly".equals(frequency)) {
      calendar.add(Calendar.MONTH, 1);
    }
    System.out.print("Enter your credits ");
    int credits = scanner.nextInt();
    String Duedate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    RecurringTask newTask = new RecurringTask(taskname, description, duedate, priority, frequency, periodInDays,
        numberOfWeeks, "Recurring");
    newTask.setCredits(credits);
    newTask.status = "Incomplete";

    if (tasks.containsKey(priority)) {
      tasks.get(priority).add(newTask);
    } else {
      ArrayList<Task> taskList = new ArrayList<>();
      taskList.add(newTask);
      tasks.put(priority, taskList);
    }

    System.out.println("Recurring task added successfully!");

    saveTaskToFile(username, taskname, description, duedate, priority, credits, "Incomplete", "Recurring", frequency, number);

  }

  private void saveTaskToFile(String username, String taskname, String description, String duedate, String priority,
      int credits, String status, String type, String frequency, int number) {
    String filePath = "Users/" + username + ".txt"; // Adjust the path based on your directory structure

    try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {

      writer.println(
          taskname + "," + description + "," + duedate + "," + priority + "," + credits + "," + status + "," + type  + "," + frequency + "," + number);
    } catch (IOException e) {
      System.out.println("Error saving task to file: " + e.getMessage());
    }
  }

}

class NormalTask extends Task {
  public NormalTask(String taskname, String description, String duedate, int credits, String priority, String type) {
    super(taskname, description, duedate, credits, priority, type);
  }

  public NormalTask(String taskname, String description, String duedate, int credits, String priority, String type,
      String status) {
    super(taskname, description, duedate, priority, credits, type, status);
  }

  public int setCredits(String priority) {
    int credits;
    // Credits for normal tasks are assigned based on priority
    switch (priority.toLowerCase()) {
      case "high":
        credits = 9;
        break;
      case "medium":
        credits = 5;
        break;
      case "low":
        credits = 2;
        break;
      default:
        credits = 0;
        break;
    }
    return credits;
  }

  public void addTask(Hashtable<String, ArrayList<Task>> tasks, String username) {
    Scanner scanner = new Scanner(System.in);

    ArrayList<Task> taskList1 = new ArrayList<>();
    int checker = 0;
    for (Map.Entry<String, ArrayList<Task>> entry : tasks.entrySet()) {
      ArrayList<Task> taskArrayList = entry.getValue();
      taskList1.addAll(taskArrayList);
    }

    String taskname;
    while (true) {
      checker = 0;
      System.out.print("Enter task name:");
      taskname = scanner.nextLine();
      for (Task task : taskList1) {
        if (task.taskname.equals(taskname)) {
          System.out.println("Invalid task name as it is already present");
          checker = 1;
          break;
        }
      }

      if (checker == 1) {
        continue;
      }
      break;
    }
    System.out.println("Enter description:");
    String description = scanner.nextLine();
    System.out.println("Enter due date(yyyy-mm-dd)*: ");
    String duedate = scanner.nextLine();
    System.out.println("Enter priority:");
    String priority = scanner.nextLine();
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date dueDate = dateFormat.parse(duedate);

      // Validate if the due date is ahead of the present date or the present date
      // itself
      if (dueDate.before(new Date())) {
        throw new InvalidTimeException(
            "Invalid date entered. Due date must be ahead of the present date or the present date itself.");
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }

    NormalTask newTask = new NormalTask(taskname, description, duedate, credits, priority, "Normal");
    int b = newTask.setCredits(priority);
    newTask.status = "Incomplete";
    int a = setCredits(priority);
    if (tasks.containsKey(priority)) {
      tasks.get(priority).add(newTask);
    } else {
      ArrayList<Task> taskList = new ArrayList<>();
      taskList.add(newTask);
      tasks.put(priority, taskList);
    }

    System.out.println("Normal task added successfully!");
    saveTaskToFile(username, taskname, description, duedate, priority, a, "Incomplete", "Normal");
  }

  private void saveTaskToFile(String username, String taskname, String description, String duedate, String priority,
      int credits, String status, String type) {
    String filePath = "Users/" + username + ".txt"; // Adjust the path based on your directory structure

    try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {

      writer.println(
          taskname + "," + description + "," + duedate + "," + priority + "," + credits + "," + status + "," + type);
    } catch (IOException e) {
      System.out.println("Error saving task to file: " + e.getMessage());
    }

  }
}

public class MainClass 
{
  public static String username;
  public static void main(String[] args) 
  {
    Hashtable<String, ArrayList<Task>> tasks = new Hashtable<>();
    Scanner scanner = new Scanner(System.in);
    tasks = loadTasksfromFile(username);
    System.out.println("....Manage Your Tasks....");
    while (true) {
      System.out.println("Choose an option:");
      System.out.println("1. Add new task");
      System.out.println("2. View tasks");
      System.out.println("3. Edit tasks");
      System.out.println("4. Delete task");
      System.out.println("5. Track credits");
      System.out.println("6. Exit");

      int option = scanner.nextInt();
      scanner.nextLine(); // Consume the newline character
      int op;
      try {
        switch (option) 
        {
          case 1:
            System.out.println("Enter the type of task (normal or recurring):");
            String type = scanner.nextLine();

            if (type.equalsIgnoreCase("normal")) {
              new NormalTask("", "", "", 0, "", "Normal").addTask(tasks, username);
            } else if (type.equalsIgnoreCase("recurring")) {
              new RecurringTask("", "", "", "", 0, "Recurring").addTask(tasks, username);
            } else {
              throw new InvalidOperationException("Invalid task type entered.");
            }

            tasks = loadTasksfromFile(username);
            break;

          case 2:

            System.out.println("1 - Normal Tasks\n2 - Recurring Tasks");
            op = scanner.nextInt();
            scanner.nextLine();
            switch (op) {
              case 1:
                System.out.println("Enter the priority to view tasks:");
                String priorityToView = scanner.nextLine();
                new NormalTask("", "", "", 0, "", "Normal").viewTask("Normal", priorityToView, tasks);
                break;
              case 2:
                new RecurringTask("", "", "", "", 0, "Recurring").viewTask("Recurring", "high", tasks);
                break;
            }

            tasks = loadTasksfromFile(username);
            break;
          case 3:

            System.out.println("1 - Normal Tasks\n2 - Recurring Tasks");
            op = scanner.nextInt();
            scanner.nextLine();
            switch (op) {
              case 1:
                System.out.println("Enter the priority to edit tasks:");
                String priorityToEdit = scanner.nextLine();
                new NormalTask("", "", "", 0, "", "Normal").editTask("Normal", username, priorityToEdit, tasks);
                break;
              case 2:
                new RecurringTask("", "", "", "", 0, "Recurring").editTask("Recurring", username, "high", tasks);
                break;
            }
            break;
          case 4:
            System.out.println("1 - Normal Tasks\n2 - Recurring Tasks");
            op = scanner.nextInt();
            scanner.nextLine();
            switch (op) {
              case 1:
                System.out.println("Enter the priority to delete tasks:");
                String priorityToDelete = scanner.nextLine();
                new NormalTask("", "", "", 0, "", "Normal").deleteTask("Normal", username, priorityToDelete, tasks);
                break;
              case 2:
                new RecurringTask("", "", "", "", 0, "Recurring").deleteTask("Recurring", username, "high", tasks);
                break;
            }
            break;
          case 5:
            System.out.println("1 - Normal Tasks\n2 - Recurring Tasks");
            op = scanner.nextInt();
            scanner.nextLine();
            switch (op) {
              case 1:

                new NormalTask("", "", "", 0, "", "Normal").trackCredits("Normal", tasks);
                break;
              case 2:
                new RecurringTask("", "", "", "", "", 0, 0, "Recurring").trackCredits("Recurring", tasks);
                break;
            }
            break;
          case 6:
            System.out.println("Exiting the program.");
            break;
          default:
            throw new InvalidOperationException("Invalid option entered.");
        }
      } catch (TaskNotFoundException | InvalidOperationException e) {
        System.out.println("Error: " + e.getMessage());
      }
      if(option==6)
      {
        break;
      }
    }
  }

  private static Hashtable<String, ArrayList<Task>> loadTasksfromFile(String username) {
    Hashtable<String, ArrayList<Task>> tasks = new Hashtable<>();
    try (Scanner scanner = new Scanner(new File("Users/" + username +  ".txt"))) {
      while (scanner.hasNextLine()) {
        String[] parts = scanner.nextLine().split(",");
        String taskname = parts[0];
        String description = parts[1];
        String duedate = parts[2];
        String priority = parts[3];
        String credits1 = parts[4];
        String status = parts[5];
        int credits = Integer.parseInt(credits1);
        String type = parts[6];
        if (tasks.containsKey(priority)) {
          if (type.equals("Normal")) {
            tasks.get(priority).add(new NormalTask(taskname, description, duedate, credits, priority, type, status));
          } else if (type.equals("Recurring")) 
          {
            String frequency = parts[7];
            int number = Integer.parseInt(parts[8]);
            tasks.get(priority).add(new RecurringTask(taskname, description, duedate, priority, credits, status, type, frequency, number));
          }
        } else {
          ArrayList<Task> taskList = new ArrayList<>();
          if (type.equals("Normal")) {
            taskList.add(new NormalTask(taskname, description, duedate, credits, priority, type, status));
          } else if (type.equals("Recurring")) {
            String frequency = parts[7];
            int number = Integer.parseInt(parts[8]);
            taskList.add(new RecurringTask(taskname, description, duedate, priority, credits, status, type, frequency, number));
          }

          tasks.put(priority, taskList);
        }
      }
    } catch (FileNotFoundException e) {
    }
    return tasks;
  }
}