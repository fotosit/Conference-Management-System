package com.fotosit;

import java.io.*;
import java.nio.file.Path;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Referee extends Author
{
    private String refereeInterests;
    private String refereeInterestNames;
    private Paper selectedPaper;
    private Database db;
    private ArrayList<Paper> waitingPapers;
    private int showMenuIndex=0,refereeScore=0;

    public Referee(int userId, String name, String surname, String mail,String universityName, int role, String userName, String userPassword, String _refereeInterests)
    {
        super ( userId,name, surname, mail, universityName, role, userName, userPassword  );
        this.refereeInterests=_refereeInterests;
        db= Database.getInstance ();

    }

    public String  getRefereeInterests()
    {
        return refereeInterests;
    }

    private void refereeWelcome()
    {
        System.out.printf ( "%n*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*%n");
        System.out.printf ( "\nWelcome %s %s,\n",getName (),getSurname ());

        waitingPapers=db.getWaitingPapersByReferee ( getUserId () );

        if (waitingPapers.size ()==0)
        {
            System.out.println ("\nWARNING: THERE IS NO PAPER WAITING FOR YOUR EVALUATION ON THE SYSTEM");
        }
        else
        {
            System.out.printf ( "\nWARNING: THERE IS %d PAPERS WAITING FOR YOUR EVALUATION IN THE SYSTEM\n",waitingPapers.size ());
        }
    }


    public  void refereeShowMenu()
    {
        refereeWelcome ();
        System.out.println ("\n-------------REFEREE MENU-------------");
        System.out.printf ( "%n%s%n%s%n%s%n%n","1 - Pending Review Papers", "2 - Evaluate Paper","3 - Main Menu");

        try {
            Scanner input = new Scanner ( System.in );

            do {
                System.out.print ( "Please Select What You Want to Do: " );
                showMenuIndex=input.nextInt ();
                System.out.print ( "\n" );

                if ((showMenuIndex<1) || (showMenuIndex>4))
                {
                    System.out.print("          WARNING: The Code You Entered Is Invalid!  \n");
                }
                else
                {
                    if (showMenuIndex==1)
                    {
                        System.out.println ("\n-------------PENDING REVIEW PAPERS-------------\n");
                        waitingPapers ();
                        returnMenu();
                    }
                    else if (showMenuIndex==2)
                    {
                        System.out.println ("\n-------------EVALUATE PAPER-------------\n");
                        paperEvaluation ();
                        returnMenu();
                    }
                    else if (showMenuIndex==3)
                    {
                        db.closeConnection ();
                        Main.main ( null );
                    }
                }
            }while ((showMenuIndex<1) || (showMenuIndex>4));
        }
        catch (NoSuchElementException noSuchElementException)
        {
            System.err.println ("Invalid Selection, Program Will Terminate");
        }
    }


    public void authorRefereeShowMenu()
    {
        refereeWelcome ();
        System.out.println ("\n-------------AUTHOR AND REFEREE MENU-------------");
        System.out.printf ( "%n%s%n%s%n%s%n%s%n%s%n%n","1 - Pending Review Papers", "2 - Evaluate Paper","3 - Upload Paper","4 - Uploaded Papers","5 - Main Menu");

        try {
            Scanner input = new Scanner ( System.in );

            do {
                System.out.print ( "Please Select What You Want to Do: " );
                showMenuIndex=input.nextInt ();
                System.out.print ( "\n" );

                if ((showMenuIndex<1) || (showMenuIndex>5))
                {
                    System.out.print("          WARNING: The Code You Entered Is Invalid!  \n");
                }
                else
                {
                    if (showMenuIndex==1)
                    {
                        System.out.println ("\n-------------PENDING REVIEW PAPERS-------------\n");
                        waitingPapers ();
                        returnMenu();
                    }
                    else if (showMenuIndex==2)
                    {
                        System.out.println ("\n---------------EVALUATE PAPER-------------\n");
                        paperEvaluation ();
                        returnMenu();
                    }
                    else if (showMenuIndex==3)
                    {
                        System.out.println ("\n------------------UPLOAD PAPER---------------\n");
                        super.submitPaper ();
                        returnMenu();
                    }
                    else if (showMenuIndex==4)
                    {
                        System.out.println ("\n-------------------UPLOADED PAPER------------------\n");
                        super.showPapers ();
                        returnMenu();
                    }
                    else if (showMenuIndex==5)
                    {
                        db.closeConnection ();
                        Main.main ( null );
                    }
                }
            }while ((showMenuIndex<1) || (showMenuIndex>5));
        }
        catch (NoSuchElementException noSuchElementException)
        {
            System.err.println ("Invalid Selection, Program Will Terminate");
        }
    }

    private void paperEvaluation()
    {
        waitingPapers();

        if (waitingPapers.size ()!=0)
        {
            int selectionPaperIndex=0;

            Scanner input = new Scanner ( System.in );
            boolean isValidPaperId=true;

            do {
                if (!isValidPaperId)
                {
                    System.out.print("\n          WARNING: The Entered Paper Id is Invalid  \n");
                }
                System.out.print ("\nEnter the Id of the Paper You Want to Evaluate: ");
                selectionPaperIndex=Integer.parseInt ( input.nextLine () );
                isValidPaperId=isValidPaper ( selectionPaperIndex );

            }while (!isValidPaperId);


            int selectAnswer=0;
            do {
                System.out.print ("\nWould You Like to Review the Paper? (1-Yes, 2-No): ");
                selectAnswer=Integer.parseInt ( input.nextLine () );

                if ((selectAnswer<1) || (selectAnswer>2))
                {
                    System.out.print("          WARNING: The Entered Code is Invalid!  \n");
                }
                else
                {
                    if (selectAnswer==1)
                    {
                        Path paperPath = null;
                        System.out.print ( "\nSelect the Location to Save the Paper" );

                        do {
                            try {
                                TimeUnit.SECONDS.sleep ( 1 );
                            } catch (InterruptedException e) {
                                e.printStackTrace ();
                            }

                            try {
                                paperPath = getFilePath ();

                                if (paperPath != null)
                                {
                                    Clob clob = selectedPaper.getPaperFile ();
                                    Reader r = clob.getCharacterStream ();
                                    String filePath = paperPath.toString () + "/" + selectedPaper.getPaperName () + ".txt";
                                    FileWriter writer = new FileWriter ( filePath );

                                    int i;
                                    while ((i = r.read ()) != -1)
                                    {
                                        writer.write ( i );
                                    }
                                    writer.close ();
                                }
                                else
                                {
                                    System.out.println ( "\nThe Paper must be Saved to the Computer. !" );
                                }

                            } catch (SQLException | IOException e) {
                                e.printStackTrace ();
                            }

                        } while (paperPath == null);
                    }

                }

            }while ((selectAnswer<1) || (selectAnswer>2));


            do {
                System.out.print ("\n\nEnter the Evaluation Score of the Paper ( 1-Very Bad, 2-Bad, 3-Middle, 4-Good, 5-Very Good ): ");
                refereeScore=Integer.parseInt ( input.nextLine ());

                if ((refereeScore<1) || (refereeScore>5))
                {
                    System.out.print("          WARNING: The Entered Code is Invalid!  \n");
                }

            }while ((refereeScore<1) || (refereeScore>5));


            System.out.print ("\nSpecify Your Opinions for the Paper: ");
            String comment=input.nextLine ();

            int answer=0;
            do {
                System.out.print ( "\nDo You Approve Your Evaluation? 1- Yes, 2- Cancel: " );
                answer=input.nextInt ();
                System.out.print ( "\n" );

                if ((answer<1) || (answer>2))
                {
                    System.out.print("          WARNING: The Entered Code is Invalid!  \n");
                }
                else
                {
                    if (answer==1)
                    {

                        if (db.evaluationPaper (selectedPaper.getPaperId (),selectedPaper.getPaperName (),selectedPaper.getAuthorNames (),getUserId (),getName () + " " + getSurname (),comment,refereeScore))
                        {
                            System.out.print ( "Değerlendirmeniz Kaydedildi.\n" );

                            if (db.checkAllEvaluationIsComplete ( selectedPaper.getPaperId () ))
                            {
                                db.setAllEvaluationIsComplete ( selectedPaper.getPaperId () );
                            }
                        }
                        else
                        {
                            System.out.print ( "\nThere was a problem with the system! please try again later" );
                        }
                    }
                    else
                    {
                        System.out.print ( "\nYour transaction has been canceled! !\n" );
                        returnMenu ();
                    }
                }
            }while ((answer<1) || (answer>2));

        }
    }

    private void waitingPapers()
    {
        if (waitingPapers.size ()==0)
        {
            System.out.println ("There are no paper registered in the system!");
        }
        else
        {
            System.out.printf ( "%-15s%-50s%-40s%-20s%-27s%-62s%n","Paper Id","Paper Name","Paper Author","Uploaded DAte","Evaluation Date","Topics" );

            for (Paper _paper : waitingPapers) {
                System.out.printf ( "%-15s%-50s%-40s%-20s%-27s%-62s%n", _paper.getPaperId (), _paper.getPaperName (), _paper.getAuthorNames (), _paper.getSubmitDate (), _paper.getDeadline (), _paper.getPaperTopicsName () );
            }
        }
    }


    public Boolean isValidPaper(int paperId)
    {
        boolean returnVal=false;

        for (int i=0; i<waitingPapers.size (); i++)
        {
            Paper _paper = waitingPapers.get ( i );

            if (paperId==_paper.getPaperId ())
            {
                selectedPaper=_paper;
                returnVal=true;
                break;
            }
        }
        return returnVal;
    }


    @Override
    protected void returnMenu()
    {
        int answer=0;
        Scanner input = new Scanner ( System.in );

        do {
            System.out.print ( "\nPlease Make a Selection (1- Main Menu, 2- Sign out):  " );
            answer=input.nextInt ();
            System.out.print ( "\n" );

            if ((answer<1) || (answer>2))
            {
                System.out.print("          WARNING: The Code You Entered Is Invalid!  \n");
            }
            else
            {
                if (answer==1)
                {
                    if (getRole ()==2)
                    {
                        refereeShowMenu ();
                    }
                    else
                    {
                        authorRefereeShowMenu ();
                    }
                }
                else
                {
                    db.closeConnection ();
                    Main.main ( null );
                }
            }
        }while ((answer<1) || (answer>2));

    }


    public String getRefereeInterestNames()
    {
        refereeInterestNames="";

        List<String> code = Arrays.asList(refereeInterests.split(","));

        for (int i=0; i<code.size (); i++)
        {
            Topics topicRawName=Topics.values()[Integer.parseInt ( code.get ( i ) )-1];

            if (i==(code.size ()-1))
            {
                refereeInterestNames = refereeInterestNames + topicRawName.getFullTopicName ( topicRawName );
            }
            else
            {
                refereeInterestNames = refereeInterestNames + topicRawName.getFullTopicName ( topicRawName ) + ", ";
            }
        }
        return refereeInterestNames;
    }

}
