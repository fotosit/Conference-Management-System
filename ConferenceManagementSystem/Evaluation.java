package com.fotosit;

public class Evaluation {

    private int evaluationId;
    private int paperId;
    private String paperName;
    private String paperAuthor;
    private int refereeId;
    private String refereeName;
    private String refereeComment;
    private int refereeScore;
    private String evaluationDate;

    public Evaluation(int _evaluationId,int _paperId, String _paperName, String _paperAuthor,int _refereeId,String _refereeName,String _refereeComment,int _refereeScore,String _evaluationDate)
    {
        this.evaluationId=_evaluationId;
        this.paperId=_paperId;
        this.paperName=_paperName;
        this.paperAuthor=_paperAuthor;
        this.refereeId=_refereeId;
        this.refereeName=_refereeName;
        this.refereeComment=_refereeComment;
        this.refereeScore=_refereeScore;
        this.evaluationDate=_evaluationDate;
    }

    public int getEvaluationId()
    {
        return evaluationId;
    }
    public int getPaperId()
    {
        return paperId;
    }
    public String getPaperName()
    {
        return paperName;
    }
    public String getPaperAuthor()
    {
        return paperAuthor;
    }
    public int getRefereeId()
    {
        return refereeId;
    }
    public String getRefereeName()
    {
        return refereeName;
    }
    public String getRefereeComment()
    {
        return refereeComment;
    }
    public int getRefereeScore()
    {
        return refereeScore;
    }
    public String getEvaluationDate()
    {
        return evaluationDate;
    }

}
