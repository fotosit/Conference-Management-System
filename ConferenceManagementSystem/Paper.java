package com.fotosit;

import java.sql.Clob;
import java.util.Arrays;
import java.util.List;

public class Paper {

    private int paperId;
    private final String authorNames;
    private final String authorMail;
    private final String authorUniversityName;
    private final String paperName;
    private final String paperAbstract;
    private String keywords;
    private String paperTopic;
    private final Clob paperFile;
    private int submitPerson;
    private String submitDate;
    private String referees;
    private double meanScore;
    private boolean isEvaluationComplete;
    private String deadline;
    private String publishDate;
    private boolean paperStatus;
    private String paperTopicsName;
    private String refereesName;

    public Paper(int _paperId,String _authorNames,String _authorMail,String _authorUniversityName,String _paperName,String _paperAbstract,String _keywords,String _paperTopics,Clob _paperFile, int _submitPerson,String _submitDate,String _referees,double _meanScore, boolean _isEvaluationComplete,String _deadline,String _publishDate, boolean _paperStatus)
    {
        this.paperId=_paperId;
        this.authorNames=_authorNames;
        this.authorMail=_authorMail;
        this.authorUniversityName=_authorUniversityName;
        this.paperName=_paperName;
        this.paperAbstract=_paperAbstract;
        this.keywords=_keywords;
        this.paperTopic=_paperTopics;
        this.paperFile=_paperFile;
        this.submitPerson=_submitPerson;
        this.submitDate=_submitDate;
        this.referees=_referees;
        this.meanScore=_meanScore;
        this.isEvaluationComplete=_isEvaluationComplete;
        this.deadline=_deadline;
        this.publishDate=_publishDate;
        this.paperStatus=_paperStatus;
    }

    public int getPaperId()
    {
        return paperId;
    }

    public String getAuthorNames()
    {
        return authorNames;
    }

    public String getAuthorMail()
    {
        return authorMail;
    }

    public String getAuthorUniversityName()
    {
        return authorUniversityName;
    }

    public  String getPaperName()
    {
        return paperName;
    }

    public String getPaperAbstract()
    {
        return  paperAbstract;
    }

    public String getKeywords()
    {
        return keywords;
    }

    public String getPaperTopic()
    {
        return paperTopic;
    }

    public Clob getPaperFile()
    {
        return paperFile;
    }

    public int getSubmitPerson()
    {
        return submitPerson;
    }

    public String getSubmitDate()
    {
        return submitDate;
    }

    public String getReferees()
    {
        return (referees == null) ? "----" : referees.toString();
    }

    public double getMeanScore()
    {
        return meanScore;
    }

    public boolean getisEvaluationComplete()
    {
        return isEvaluationComplete;
    }

    public String getDeadline()
    {
        return (deadline == null) ? "----" : deadline.toString();
    }

    public String getPublishDate()
    {
        return (publishDate == null) ? "----" : publishDate.toString();
    }

    public boolean getPaperStatus()
    {
        return paperStatus;
    }

    public String getPaperTopicsName()
    {
        paperTopicsName="";

        List<String> code = Arrays.asList(paperTopic.split(","));

        for (int i=0; i<code.size (); i++)
        {
            Topics topicRawName=Topics.values()[Integer.parseInt ( code.get ( i ) )-1];

            if (i==(code.size ()-1))
            {
                paperTopicsName = paperTopicsName + topicRawName.getFullTopicName ( topicRawName );
            }
            else
            {
                paperTopicsName = paperTopicsName + topicRawName.getFullTopicName ( topicRawName ) + ", ";
            }

        }
        return paperTopicsName;
    }

    public String getRefereesName()
    {
        Database db= Database.getInstance ();

        refereesName="";

        if (referees!=null)
        {
            List<String> refereeCode = Arrays.asList(referees.split(","));

            for (int i=0; i<refereeCode.size (); i++)
            {
                if (i==(refereeCode.size ()-1))
                {
                    refereesName = refereesName + db.getRefereeNameSurname ( Integer.parseInt ( refereeCode.get ( i ) ));
                }
                else
                {
                    refereesName = refereesName + db.getRefereeNameSurname ( Integer.parseInt ( refereeCode.get ( i ) )) + ", ";
                }

            }
        }

        return (refereesName == "") ? "----" : refereesName.toString();
    }


}
