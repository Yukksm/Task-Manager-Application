//import Users.UserManagementApp;
import java.util.*;

class InvalidOptionException extends Exception {
    public InvalidOptionException(String message) {
        super(message);
    }
}

public class MainProgram {
  public static void main(String[] args) 
  {
    String username="";
    Scanner sc = new Scanner(System.in);
    int choice=1;
    UserManagementApp obj = new UserManagementApp();
      username = obj.UserLoginRegister();
      System.out.println("The username is " + username);
      if(username!="")
      {
        while(choice==1 || choice == 2)
        {
          try
        {
        System.out.println("-----------Home------------");
        System.out.println("1. Dashboard");
        System.out.println("2. Manage your tasks");
        System.out.println("3. Exit");
        System.out.println("------------------------------");
        choice = sc.nextInt();
      
        switch(choice){
        case 1:
          Dashboard.username = username;
          Dashboard.main(args);
          break;
        case 2:
          MainClass.username = username;
          MainClass.main(args);
          break;
        case 3:
          System.out.println("Exiting the program.");
          break;
        
      }
     }catch(InputMismatchException e)
    {
      System.out.println("Enter valid value of choice");
    }
    }
  }
  }
}