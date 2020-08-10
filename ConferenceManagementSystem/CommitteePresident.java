package com.fotosit;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class CommitteePresident extends Person
{
    private Database db;
    private ArrayList <Paper> waitingAssignRefereeList;
    private ArrayList <Paper> waitingApproveList;
    private ArrayList <Paper> allPaperList;
    private int selectionPaperIndex;
    private Paper selectedPaper;
    private List<String> assignedReferee;
    private ArrayList<Referee> relatedReferees;



    public CommitteePresident(int userId, String name, String surname, String mail,String universityName, int role, String userName, String userPassword)
    {
        super (userId,name, surname, mail, universityName, role, userName, userPassword );
        db= Database.getInstance ();
    }


    public void welcome()
    {
        System.out.printf ( "%n*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*%n");
        System.out.printf ( "\nWelcome %s %s,\n",getName (),getSurname ());


        waitingAssignRefereeList=db.getWaitForAssignPapers ();

        if (waitingAssignRefereeList.size ()==0)
        {
            System.out.println ("\nWARNING-1: THERE IS NO PAPERS WAITING FOR YOUR REFEREES IN THE SYSTEM\n");
        }
        else
        {
            System.out.printf ("\nWARNING-1: THERE ARE %d PAPERS WAITING FOR REFEREES IN THE SYSTEM\n",waitingAssignRefereeList.size ());
        }

        //Onay Bekleyen Bildirilerin Sayısı
        waitingApproveList=db.getWaitingApprovePaper ();
        if (waitingApproveList.size ()==0)
        {
            System.out.println ("WARNING-2: THERE IS NO CONFERENCE PAPER WAITING TO BE APPROVED IN THE SYSTEM");
        }
        else
        {
            System.out.printf ("WARNING-2: THERE ARE %d CONFERENCE PAPER WAITING TO BE APPROVED IN THE SYSTEM\n",waitingApproveList.size ());
        }
        showMenu ();
    }


    private void showMenu()
    {
        System.out.println ("\n----------------PRESIDENT OF THE COMMISSION MENU----------------");
        System.out.printf ( "%n%s%n%s%n%s%n%s%n","1- Assign Referee", "2- Evaluate and Publish","3- View All Conference Papers","4- Main Menu");

        int answer=0;

        try {
            Scanner input = new Scanner ( System.in );

            do {
                System.out.printf ( "\nPlease Select What You Want to Do:" );
                answer=input.nextInt ();

                if ((answer<1) || (answer>4))
                {
                    System.out.print("          WARNING: The Code You Entered Is Invalid!  \n");
                }
                else
                {
                    if (answer==1)
                    {
                        System.out.println ("\n---------------------ASSIGN REFEREE MENU---------------------");
                        assignReferee ();
                        returnMenu ();
                    }
                    else if (answer==2)
                    {
                        System.out.println ("\n-------------EVALUATE AND PUBLISH MENU--------------");
                        publishPaper ();
                        returnMenu ();
                    }
                    else if (answer==3)
                    {
                        System.out.println ("\n-----------------------ALL CONFERENCE PAPERS-----------------------");
                        showAllPapers();
                        returnMenu ();
                    }
                    else if (answer==4)
                    {
                        db.closeConnection ();
                        Main.main ( null );
                    }
                }

            }while ((answer<1) || (answer>4));
        }
        catch (NoSuchElementException noSuchElementException)
        {
            System.err.println ("Invalid Selection, Program Will Terminate");
        }
    }

    private void assignReferee()
    {
        int assignRefereeMethod=0;

        if (waitingAssignRefereeList.size ()==0)
        {
            System.out.println ("\nThere is No Papers Waiting for Your Referees in the System.");
        }
        else
        {
            System.out.printf ( "%-15s%-50s%-40s%-20s%-32s%n","Paper Id","Paper Name","Paper Author","Uploaded Date","Topics" );
            for (int i=0; i<waitingAssignRefereeList.size (); i++)
            {
                Paper unassignedPaper=waitingAssignRefereeList.get ( i );
                System.out.printf ( "%-15s%-50s%-40s%-20s%-32s%n",unassignedPaper.getPaperId (),unassignedPaper.getPaperName (),unassignedPaper.getAuthorNames (),unassignedPaper.getSubmitDate (),unassignedPaper.getPaperTopicsName () );
            }

            Scanner input = new Scanner ( System.in );
            boolean isValidPaperId=true;

            do {
                if (!isValidPaperId)
                {
                    System.out.print("\n          WARNING: The Code You Entered Is Invalid!  \n");
                }
                System.out.print ("\nEnter the Number of the Paper for Assign Referee: ");
                selectionPaperIndex=input.nextInt ();
                isValidPaperId=checkPaperIdIsValid ( selectionPaperIndex );

            }while (!isValidPaperId);


            do {
                System.out.print ( "\nHow Would You Like to Do an Assign Referee? ( 1-Automatic, 2-Manual ): " );
                assignRefereeMethod=input.nextInt ();

                if ((assignRefereeMethod<1) || (assignRefereeMethod>2))
                {
                    System.out.print("          WARNING: The Code You Entered Is Invalid!  \n");
                }

            }while ((assignRefereeMethod<1) || (assignRefereeMethod>2));


            System.out.printf ("\nPaper Topics: %s\n",selectedPaper.getPaperTopicsName ());

            relatedReferees = findRelatedPaperAuthor ( selectedPaper,assignRefereeMethod );

            String assignRefereeList="";
            System.out.printf ( "\n%-10s%-30s%-40s%n","Referee Id","Referee Name Surname","Areas of Interest\n" );

            for (int i=0; i<relatedReferees.size (); i++)
            {
                Referee ref=relatedReferees.get ( i );
                if (i != relatedReferees.size ()-1)
                {
                    assignRefereeList= assignRefereeList + ref.getUserId () + ",";
                }
                else
                {
                    assignRefereeList= assignRefereeList + ref.getUserId ();
                }

                System.out.printf ( "%-10s%-30s%-40s%n",ref.getUserId (),ref.getName () + " " + ref.getSurname (),ref.getRefereeInterestNames ());
            }
            System.out.print ( "\n" );

            boolean refCountStatus=true, refValidStatus=true;

            if (assignRefereeMethod==2)
            {
                do {
                    if (!refValidStatus)
                    {
                        System.out.print("\n         WARNING: Please Enter One Of The Referees Id Numbers Suggested By The System  \n");
                    }
                    do {
                        if (!refCountStatus)
                        {
                            System.out.print("\n          WARNING: At least 3 referees must be assigned to the papers!  \n");
                        }

                        System.out.print ("\nWrite the Referee Id Numbers You Want to Assign, Adding Commas Between them:");
                        assignRefereeList=input.next ();

                        assignedReferee = Arrays.asList(assignRefereeList.split(","));
                        if (assignedReferee.size ()<3)
                        {
                            refCountStatus=false;
                        }
                        else
                        {
                            refCountStatus=true;
                        }

                    }while (!refCountStatus);

                    refValidStatus=checkAssignedReferees ();

                }while (!refValidStatus);
            }

            System.out.print ("\nEnter the Deadline for the Evaluation of the Paper: ");
            String deadline=input.next ();

            System.out.print ("\nDo You Approve the Assignment of Referees ( 1-Yes, 2-Cancel ): ");

            if (input.nextInt ()==1)
            {
                if (db.assignReferee ( assignRefereeList, deadline, selectedPaper.getPaperId () ))
                {
                    System.out.print ("\nCongratulations! You Successfully Assigned Referees.");
                }
            }
            else
            {
                returnMenu ();
            }
        }
    }

    private boolean checkAssignedReferees()
    {
        int i=0, j=0;

        for (i=0; i<assignedReferee.size (); i++)
        {
            for (j=0; j<relatedReferees.size (); j++)
            {
                Referee ref=relatedReferees.get ( j );

                if (Integer.parseInt ( assignedReferee.get ( i ) )==ref.getUserId ())
                {
                    break;
                }
            }
            if (j==relatedReferees.size ())
            {
                return false;
            }
        }
        return true;
    }

    private boolean checkPaperIdIsValid(int _paperId)
    {
        boolean returnVal=false;

        for (int i=0; i<waitingAssignRefereeList.size (); i++)
        {
            Paper authorPaper = waitingAssignRefereeList.get ( i );

            if (authorPaper.getPaperId ()==_paperId)
            {
                selectedPaper=authorPaper;
                returnVal=true;
                break;
            }
        }
        return returnVal;
    }


    private ArrayList <Referee>  findRelatedPaperAuthor(Paper _paper, int asssignMethod)
    {
        ArrayList<Referee> relatedReferee = new ArrayList<Referee>();

        String[] elements = _paper.getPaperTopic ().split(",");
        List<String> _paperTopics = Arrays.asList(elements);

        ArrayList <Referee> ref = db.getReferees ();

        for (int i=0; i<ref.size (); i++)
        {
            Referee _referee =ref.get ( i );

            boolean isSameRefereeAndAuthor = _paper.getAuthorNames ().contains(_referee.getName () + " " + _referee.getSurname ());

            if (!isSameRefereeAndAuthor)
            {
                String[] refereeInterest = _referee.getRefereeInterests ().split(",");
                List<String> _refereeTopics = Arrays.asList(refereeInterest);

                Set<String> result = _paperTopics.stream()
                        .distinct()
                        .filter(_refereeTopics::contains)
                        .collect( Collectors.toSet());

                if (result.size ()>0)
                {
                    if (asssignMethod==1 && relatedReferee.size ()>=3)
                    {
                        break;
                    }
                    else
                    {
                        relatedReferee.add ( _referee );
                    }
                }
            }
        }
    return relatedReferee;
    }

    private void publishPaper()
    {
        if (waitingApproveList.size ()==0)
        {
            System.out.println ("\n There is no conference paper paper waiting to be approved in the system !");
        }
        else
        {
            System.out.printf ( "\n%-15s%-50s%-30s%-20s%-30s%-30s%-60s%n","Paper Id","Paper Name","Author","Uploaded Date","Deadline","Average Score","Reviewing Authors" );

            for (int i=0; i<waitingApproveList.size (); i++)
            {
                Paper unassignedPaper=waitingApproveList.get ( i );

                System.out.printf ( "%-15s%-50s%-30s%-20s%-30s%-30.2f%-60s%n",unassignedPaper.getPaperId (),unassignedPaper.getPaperName (),unassignedPaper.getAuthorNames (),unassignedPaper.getSubmitDate (),unassignedPaper.getDeadline (),unassignedPaper.getMeanScore (),unassignedPaper.getRefereesName ());
            }

            selectionPaperIndex=0;
            Scanner input = new Scanner ( System.in );

            boolean isValid=true;
            do {

                if (!isValid)
                {
                    System.out.println ("\n     WARNING: The Code You Entered Is Invalid!");
                }
                System.out.print ( "\nEnter the Id Number of the Conference Paper: " );
                selectionPaperIndex=input.nextInt ();
                isValid=checkPaperIdIsEvaluation ( selectionPaperIndex );

            }while (!isValid);

            ArrayList <Evaluation> evaList=db.getEvaluationByPaperId ( selectionPaperIndex );

            System.out.printf ( "\n%-15s%-50s%-30s%-30s%-20s%-80s%-20s%n","Paper Id","Paper Name","Paper Author","Reviewing Authors","Evaluation Score","Author Comment","Evaluation Date" );

            for (int i=0; i<evaList.size (); i++)
            {
                Evaluation evaPaper=evaList.get ( i );
                System.out.printf ( "%-15s%-50s%-30s%-30s%-20d%-80s%-20s%n",evaPaper.getPaperId (),evaPaper.getPaperName (),evaPaper.getPaperAuthor (), evaPaper.getRefereeName (),evaPaper.getRefereeScore (),evaPaper.getRefereeComment (),evaPaper.getEvaluationDate ());
            }

            int decisionPresident=0;
            boolean isAnswerValid=true;

            do {
                if (!isAnswerValid)
                {
                    System.out.println ("\n     WARNING: The Code You Entered Is Invalid!");
                }
                System.out.print ( "\nDo You Approve the Conference Peper? (0-Reject, 1-Approve): " );
                decisionPresident=input.nextInt ();

                if ((decisionPresident!=0) && (decisionPresident!=1))
                {
                    isAnswerValid=false;
                }
                else
                {
                    if (decisionPresident==0)
                    {
                        System.out.print ( "\n Conference Paper Rejected According to Your Decision." );
                    }
                    else
                    {
                        System.out.print ( "\nConference Paper Accepted According to Your Decision." );
                    }
                }

            }while (!isAnswerValid);

            db.evaluationByPresident ( selectionPaperIndex,decisionPresident );
        }
    }


    private void showAllPapers()
    {
        allPaperList = db.getAllPapers ();

        if (allPaperList.size ()==0)
        {
            System.out.println ("\n There are no papers registered in the system!");
        }
        else
        {
            System.out.printf ( "\n%-15s%-50s%-30s%-20s%-40s%-30s%-30s%-60s%n","Paper Id","Paper Name","Author","Uploaded Date","Status","Evaluation Date", "Average Score","Reviewing Authors" );

            for (int i=0; i<allPaperList.size (); i++)
            {
                Paper _paper=allPaperList.get ( i );

                if (_paper.getReferees ()=="----" && !_paper.getisEvaluationComplete ())
                {
                    System.out.printf ( "%-15s%-50s%-30s%-20s%-40s%-30s%-30.2f%-60s%n",_paper.getPaperId (),_paper.getPaperName (),_paper.getAuthorNames () ,_paper.getSubmitDate (),"Referee Assignment Awaited",_paper.getDeadline (),_paper.getMeanScore (),_paper.getRefereesName ());

                }
                else if (_paper.getReferees ()!=null && _paper.getMeanScore()==0 && !_paper.getisEvaluationComplete ())
                {
                    System.out.printf ( "%-15s%-50s%-30s%-20s%-40s%-30s%-30.2f%-60s%n",_paper.getPaperId (),_paper.getPaperName (),_paper.getAuthorNames () ,_paper.getSubmitDate (),"In Review",_paper.getDeadline (),_paper.getMeanScore (),_paper.getRefereesName ());
                }
                else if (_paper.getReferees ()!=null && _paper.getMeanScore()!=0 && !_paper.getisEvaluationComplete ())
                {
                    System.out.printf ( "%-15s%-50s%-30s%-20s%-40s%-30s%-30.2f%-60s%n",_paper.getPaperId (),_paper.getPaperName (),_paper.getAuthorNames (),_paper.getSubmitDate (),"Awaiting President of the Commission Approval",_paper.getDeadline (),_paper.getMeanScore (),_paper.getRefereesName ());
                }
                else if (_paper.getPublishDate ()!=null)
                {

                    if (!_paper.getPaperStatus ())
                    {
                        System.out.printf ( "%-15s%-50s%-30s%-20s%-40s%-30s%-30.2f%-60s%n",_paper.getPaperId (),_paper.getPaperName (),_paper.getAuthorNames (),_paper.getSubmitDate (),"Reject",_paper.getDeadline (),_paper.getMeanScore (),_paper.getRefereesName ());
                    }
                    else
                    {
                        System.out.printf ( "%-15s%-50s%-30s%-20s%-40s%-30s%-30.2f%-60s%n",_paper.getPaperId (),_paper.getPaperName (),_paper.getAuthorNames (),_paper.getSubmitDate (),"Approve",_paper.getDeadline (),_paper.getMeanScore (),_paper.getRefereesName ());
                    }
                }
            }
        }
    }


    private void returnMenu()
    {
        int answer=0;
        Scanner input = new Scanner ( System.in );

        do {
            System.out.print ( "\n\nPlease Make a Selection (1- Main Menu, 2- Sign out): " );
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
                else if (answer==2)
                {
                    db.closeConnection ();
                    Main.main ( null );
                }
            }
        }while ((answer<1) || (answer>2));

    }


    private boolean checkPaperIdIsEvaluation(int paperId)
    {
        boolean returnVal=false;

        for (int i=0; i<waitingApproveList.size (); i++)
        {
            Paper unassignedPaper=waitingApproveList.get ( i );
            if (unassignedPaper.getPaperId ()==paperId)
            {
                returnVal=true;
                break;
            }
        }
        return returnVal;
    }
}
