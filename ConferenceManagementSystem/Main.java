package com.fotosit;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)
    {
        Login loginView=new Login ();

        int selectedOption=welcome ();

        if (selectedOption==1)
        {
            Person user=loginView.login ();

            if (user.getRole ()==1)
            {
                Author aut=new Author(user.getUserId (),user.getName (),user.getSurname (),user.getMail (),user.getUniversityName (),user.getRole (),user.getUserName (),user.getUserPassword ());
                aut.welcome ();
            }
            else if(user.getRole ()==2 || user.getRole ()==3)
            {
                Referee ref=new Referee ( user.getUserId (),user.getName (),user.getSurname (),user.getMail (),user.getUniversityName (),user.getRole (),user.getUserName (),user.getUserPassword (),"" );

                if (user.getRole ()==2)
                {
                    ref.refereeShowMenu ();
                }
                else
                {
                    ref.authorRefereeShowMenu ();
                }
            }
            else if(user.getRole ()==4)
            {
                CommitteePresident head= new CommitteePresident (user.getUserId (),user.getName (),user.getSurname (),user.getMail (),user.getUniversityName (),user.getRole (),user.getUserName (),user.getUserPassword ());
                head.welcome ();
            }

        }
        else if (selectedOption==2)
        {
            loginView.registration ();
        }
        else if (selectedOption==3)
        {
            System.out.println("Terminating program...");

            System.exit(0);
        }
    }

    public static int welcome()
    {
        System.out.printf ( "%n*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*%n");
        System.out.printf ( "%nWELCOME TO CONFERENCE MANAGEMENT SYSTEM!%n%n%s%n%s%n%s%n%n","1 - Login", "2 - Register","3 - Quit");
        System.out.printf ( "*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*%n");

        int answer=0;

        try {
            Scanner input = new Scanner ( System.in );

            do {
                System.out.printf ( "\nPlease Select What You Want to Do : " );
                answer=input.nextInt ();
                System.out.print ( "\n" );

                if ((answer<1) || (answer>3))
                {
                    System.out.print("          WARNING: The Code You Entered Is Invalid!  \n");
                }

            }while ((answer<1) || (answer>3) );
        }
        catch (NoSuchElementException noSuchElementException)
        {
            System.err.println ("Invalid Selection, Program Will Terminate");
        }
        return answer;
    }
}
