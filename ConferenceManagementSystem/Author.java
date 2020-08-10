package com.fotosit;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import javax.swing.JFileChooser;
import java.util.concurrent.TimeUnit;

public class Author extends Person
{
    private Database db;
    private ArrayList<Integer> approvalPapers;

    public Author(int userId, String name, String surname, String mail,String universityName, int role, String userName, String userPassword)
    {
        super ( userId,name, surname, mail, universityName, role, userName, userPassword );
        db= Database.getInstance ();
    }

    public void welcome()
    {
        System.out.printf ( "%n*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*%n");
        System.out.printf ( "\nWelcome %s %s,\n",getName (),getSurname ());
        System.out.printf ( "%n%n%s%n%s%n%s%n%n","1 - Upload Paper", "2 - Uploaded Papers","3 - Main Menu");

        int answer=0;

        try {
            Scanner input = new Scanner ( System.in );

            do {
                System.out.printf ( "\nPlease Select What You Want to Do : " );
                answer=input.nextInt ();
                System.out.print ( "\n" );

                if ((answer<1) || (answer>3))
                {
                    System.out.print("          WARNING: The Entered Code is Invalid!   \n");
                }
                else
                {
                    if (answer==1)
                    {
                        System.out.println ("\n------------------UPLOAD PAPER----------------\n");
                        submitPaper ();
                        returnMenu ();
                    }
                    else if (answer==2)
                    {
                        System.out.println ("\n-------------------UPLOADED PAPERS-------------------\n");
                        showPapers ();
                        returnMenu ();
                    }
                    else if (answer==3)
                    {
                        db.closeConnection ();
                        Main.main ( null );
                    }
                }
            }while ((answer<1) || (answer>3));
        }
        catch (NoSuchElementException noSuchElementException)
        {
            System.err.println ("Invalid Selection, Program Will Terminate");
        }
    }

    protected void submitPaper()
    {
        String authorNames, email, UniversityName, paperName, paperAbstract, keywords, paperTopics ;
        Path paperPath=null;
        FileReader paperFile=null;

        Scanner input = new Scanner ( System.in );
        System.out.print ( "Please fill in the blanks : \n" );

        do {
            System.out.print ( "\nWrite the Author's Names with Commas:" );
            authorNames=input.nextLine ();

        }while (authorNames.isEmpty ());

        do {
            System.out.print ( "Mail Address:" );
            email=input.nextLine ();

        }while (email.isEmpty ());

        do {
            System.out.print ( "Univesity Name:" );
            UniversityName=input.nextLine ();

        }while (UniversityName.isEmpty ());

        do {
            System.out.print ( "Paper Name:" );
            paperName=input.nextLine ();

        }while (paperName.isEmpty ());

        do {
            System.out.print ( "Paper Abstract:" );
            paperAbstract=input.nextLine ();

        }while (paperAbstract.isEmpty ());

        do {
            System.out.print ( "Write the Keywords Related to the Paper, Putting Commas in Between: " );
            keywords=input.nextLine ();

        }while (keywords.isEmpty ());

        System.out.println ( "\n ------ Subjects in which the Papers can be a Group----- \n" );

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
                System.out.println ("\n     WARNING: Please Enter Correct Codes of Paper Subjects!");
            }
            System.out.print ( "\nWrite the Keywords Related to the Paper, Putting Commas in Between: " );
            paperTopics=input.nextLine ();
            isValid=checkValidTopicCode ( paperTopics );

        }while (!isValid);


        do {
            System.out.print ( "\nPlease Upload Your Abstract...\n" );

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }

            try {
                paperPath=getFilePath ();
                if (paperPath!=null)
                {
                    paperFile= new FileReader (paperPath.toString ());
                }
                else
                {
                    System.out.println ("Your paper must be uploaded to the system !");
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace ();
            }

        }while (paperPath==null);

        System.out.printf ( "\nPaper Path: '%s'\n",paperPath );

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
                if (registrationIndex==1)
                {
                    if (db.addPaper ( authorNames,email,UniversityName,paperName,paperAbstract,keywords,paperTopics,paperFile,getUserId () ))
                    {
                        System.out.print ( "Congratulations Your Paper Uploaded Successfully\n" );
                    }
                    else
                    {
                        System.out.print ( "There was a problem with the system! please try again later" );
                    }
                }
                else
                {
                    System.out.print ( "\nYour transaction has been canceled!\n" );
                    welcome ();
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

    protected Path getFilePath()
    {
        JFileChooser fileChooser = new JFileChooser ();
        fileChooser.setFileSelectionMode ( JFileChooser.FILES_AND_DIRECTORIES );
        int result = fileChooser.showOpenDialog ( null);

        if (result==JFileChooser.CANCEL_OPTION)
        {
            return null;
        }
        else
        {
            return fileChooser.getSelectedFile ().toPath ();
        }
    }


    protected void showPapers()
    {

        ArrayList<Paper> authorPapers = db.getPaperByUserId (getUserId ());

        if (authorPapers.size ()==0)
        {
            System.out.println ("\n There are no paper registered in the system!");
        }
        else
        {
            System.out.printf ( "%-15s%-50s%-20s%-20s%-10s%n","Paper Id","Paper Name","Uploaded Date","Publish Date","Status" );

            for (int i=0; i<authorPapers.size (); i++)
            {
                Paper authorPaper=authorPapers.get ( i );

                if (authorPaper.getReferees ()=="----" && !authorPaper.getisEvaluationComplete ())
                {
                    System.out.printf ( "%-15s%-50s%-20s%-20s%-10s%n",authorPaper.getPaperId (),authorPaper.getPaperName (),authorPaper.getSubmitDate (),"------------","Referee Assignment Awaited");
                }
                else if (authorPaper.getReferees ()!=null && authorPaper.getMeanScore()==0 && !authorPaper.getisEvaluationComplete ())
                {
                    System.out.printf ( "%-15s%-50s%-20s%-20s%-10s%n",authorPaper.getPaperId (),authorPaper.getPaperName (),authorPaper.getSubmitDate (),"------------","In Review");
                }
                else if (authorPaper.getReferees ()!=null && authorPaper.getMeanScore()!=0 && !authorPaper.getisEvaluationComplete ())
                {
                    System.out.printf ( "%-15s%-50s%-20s%-20s%-10s%n",authorPaper.getPaperId (),authorPaper.getPaperName (),authorPaper.getSubmitDate (),"------------","Awaiting President of the Commission Approval");
                }
                else if (authorPaper.getPublishDate ()!=null)
                {
                    if (!authorPaper.getPaperStatus ())
                    {
                        System.out.printf ( "%-15s%-50s%-20s%-20s%-10s%n",authorPaper.getPaperId (),authorPaper.getPaperName (),authorPaper.getSubmitDate (),authorPaper.getPublishDate (),"Rejection");
                    }
                    else
                    {
                        System.out.printf ( "%-15s%-50s%-20s%-20s%-10s%n",authorPaper.getPaperId (),authorPaper.getPaperName (),authorPaper.getSubmitDate (),authorPaper.getPublishDate (),"Approval" );
                    }
                }
            }

            approvalPapers=db.getApprovalPaperId ( getUserId () );

            if (approvalPapers.size () >0)
            {
                Scanner input = new Scanner ( System.in );
                int approvalPaperId=0;

                System.out.print ( "\nWould You Like To See The Referee Reviews Of Your Submission (1- Yes, 2- No): " );

                if (input.nextInt ()==1)
                {

                    boolean isValid=true;
                    do {

                        if (!isValid)
                        {
                            System.out.println ("\n     WARNING: The Paper Number You Entered Is Invalid or In Review !");
                        }
                        System.out.print ( "\nEnter the Paper Id You Want to See the Referee Evaluation: " );
                        approvalPaperId=input.nextInt ();
                        isValid=checkPaperIdIsApproved ( approvalPaperId );

                    }while (!isValid);

                    System.out.println ("\n-------------------REFEREE COMMENTS-------------------\n");
                    System.out.printf ( "\n%-20s%-20s%n","Referee","Comments" );

                    ArrayList <Evaluation> evaList=db.getEvaluationByPaperId ( approvalPaperId );

                    for (int j=0; j<evaList.size (); j++)
                    {
                        Evaluation evaPaper=evaList.get ( j );

                        System.out.printf ( "%-20s%-20s%n",j + " no.lu Hakem --->",evaPaper.getRefereeComment ());
                    }
                }
                else
                {
                    returnMenu ();
                }
            }
        }
    }

    private boolean checkPaperIdIsApproved(int paperId)
    {
        boolean returnVal=false;

        for (int i=0; i<approvalPapers.size (); i++)
        {
            if (approvalPapers.get ( i )==paperId)
            {
                returnVal=true;
                break;
            }
        }
        return returnVal;
    }

    protected void returnMenu()
    {
        int answer=0;
        Scanner input = new Scanner ( System.in );
        do {
            System.out.print ( "\nPlease Make a Selection (1- Main Menu, 2- Sign out): " );
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
                    welcome ();
                }
                else
                {
                    db.closeConnection ();
                    Main.main ( null );
                }
            }
        }while ((answer<1) || (answer>2));
    }


}
