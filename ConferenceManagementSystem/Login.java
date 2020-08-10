package com.fotosit;

import java.util.*;

public class Login {

    private static Database db;

    public Login()
    {
        db= Database.getInstance ();
    }

    public Person login()
    {
        Scanner input = new Scanner ( System.in );

        String userName, userPassword;
        Person user=null;
        int loop=0,authStatus;

        do {
            System.out.print ( "Please Enter Username: " );
            userName=input.nextLine ();

            System.out.print ( "Please Enter Your Password: " );
            userPassword=input.nextLine ();

            authStatus=db.isExistUsers ( userName, userPassword );

            if (authStatus == -1 && loop < 3)
            {
                System.out.println ( "\nUsername or Password Incorrect!" );
                loop++;
                System.out.printf ( "Number of Error Attempts: %d\n\n", loop );
            }
            if (authStatus == -1 && loop == 3)
            {
                System.out.println ( "You Have Made Too Many Wrong Attempts! You are being redirected to the main menu..\n" );
                Main.main ( null );
            }
            else
            {
                user=db.searchByUserId ( authStatus );
            }
        }while (authStatus==-1 && loop<3);

        return user;
    }

    public void registration()
    {
        String name, surname, mail, university, userName, userPassword, interests="";
        int roleIndex=0;

        Scanner input = new Scanner ( System.in );
        System.out.println ( "Please fill in the blanks : " );

        do {
            System.out.print ( "Name: " );
            name=input.nextLine ();

        }while (name.isEmpty ());

        do {
            System.out.print ( "Surname: " );
            surname=input.nextLine ();

        }while (surname.isEmpty ());


        do {
            System.out.print ( "Mail Address: " );
            mail=input.nextLine ();

        }while (mail.isEmpty ());

        do {
            System.out.print ( "Institution you work for:" );
            university=input.nextLine ();

        }while (university.isEmpty ());

        do {
            System.out.print ( "Username:" );
            userName=input.nextLine ();
        }while (userName.isEmpty ());

        do {
            System.out.print ( "Password:" );
            userPassword=input.nextLine ();
        }while (userPassword.isEmpty ());

        do {
            System.out.print ( "1- Author, 2- Referee, 3- Author & Referee: " );
            roleIndex=input.nextInt ();
            System.out.print ( "\n" );

            if ((roleIndex<1) || (roleIndex>3))
            {
                System.out.print("          WARNING: The Code You Entered Is Invalid!  \n");
            }
            else
            {
                if(roleIndex==2 || roleIndex==3)
                {
                    System.out.printf ( "\n" );
                    System.out.println ( " ------ CONFERENCE TOPICS ----- " );
                    System.out.printf ( "\n" );

                    int i=1;
                    for (Topics s : Topics.values ())
                    {
                        System.out.printf("%d-%s\n",i,s.getFullTopicName ( s ));
                        i++;
                    }
                    boolean isValid=true;
                    do {

                        if (!isValid)
                        {
                            System.out.println ("\n     WARNING: Please Enter Correct Codes for Your Interests!");
                        }
                        System.out.print ( "\nPlease write the codes for the topics you are interested in, separated by commas: " );
                        interests=input.next ();
                        isValid=checkValidTopicCode ( interests );

                    }while (!isValid);
                }
            }
        }while ((roleIndex<1) || (roleIndex>3));

        int registrationIndex=0;

        do {
            System.out.print ( "\nDo you confirm that the information is correct? (1- Confirmation, 2- Cancel): " );
            registrationIndex=input.nextInt ();
            System.out.print ( "\n" );

            if ((registrationIndex<1) || (registrationIndex>2))
            {
                System.out.print("          WARNING: The Code You Entered Is Invalid!  \n");
            }
            else
            {
                //Kullanıcının Veri Tabanına Kaydı Yapılır
                if (registrationIndex==1)
                {
                    if (db.addPerson ( name, surname, mail, university, userName, userPassword, roleIndex, interests ))
                    {
                        System.out.print ( "Congratulations You Have Successfully Registered\n" );
                    }
                    else
                    {
                        System.out.print ( "There was a problem with the system! please try again later\n" );
                    }
                    Main.main ( null );
                }
                else
                {
                    System.out.print ( "\nRegistration Canceled" );
                    Main.main ( null );
                }
            }
        }while ((registrationIndex<1) || (registrationIndex>2));
    }

    private boolean checkValidTopicCode(String codes)
    {
        boolean returnVal=true;

        List<String> code = Arrays.asList(codes.split(","));

        for (int i=0; i<code.size (); i++)
        {
            int topicCode=Integer.parseInt ( code.get ( i ) );

            if (topicCode<1 || topicCode >25)
            {
                returnVal=false;
                break;
            }
        }
        return returnVal;
    }
}

