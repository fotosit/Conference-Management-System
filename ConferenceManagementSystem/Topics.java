package com.fotosit;

public enum Topics
{
    Forensic_Psychiatry(1),
    Affective_Disorders(2),
    Alcohol_and_Substance_Addiction(3),
    Anxiety_Disorders(4),
    Sexual_Dysfunctions(5),
    Sexual_Identity_Disorders(6),
    Child_Mental_Health(7),
    Education(8),
    Epidemiology(9),
    Adolescent(10),
    Genetic(11),
    Geriatrics(12),
    Movement_Disorders(13),
    Suicide(14),
    Women_Mental_Health(15),
    Consultation_Liaison(16),
    Neuroscience(17),
    Psychoanalysis(18),
    Psychometry(19),
    Psychotherapy(20),
    Mental_Trauma(21),
    Somatization(22),
    Schizophrenia(23),
    Sleeping_Disorders(24),
    Eating_Disorders(25);

    private int indexNo;

    Topics(int _indexNo)
    {
        this.indexNo = _indexNo;
    }

    public String getFullTopicName(Topics topicName)
    {
        switch (topicName)
        {
            case Forensic_Psychiatry: return "Forensic Psychiatry";
            case Affective_Disorders: return "Affective Disorders";
            case Alcohol_and_Substance_Addiction: return "Alcohol and Substance Addiction";
            case Anxiety_Disorders: return "Anxiety Disorders";
            case Sexual_Dysfunctions: return "Sexual Dysfunctions";
            case Sexual_Identity_Disorders: return "Sexual Identity Disorders";
            case Child_Mental_Health: return "Child Mental Health";
            case Education: return "Education";
            case Epidemiology: return "Epidemiology";
            case Adolescent: return "Adolescent";
            case Genetic: return "Genetic";
            case Geriatrics: return "Geriatrics";
            case Movement_Disorders: return "Movement Disorders";
            case Suicide: return "Suicide";
            case Women_Mental_Health: return "Women's Mental Health";
            case Consultation_Liaison: return "Consultation Liaison";
            case Neuroscience: return "Neuroscience";
            case Psychoanalysis: return "Psychoanalysis";
            case Psychometry: return "Psychometry";
            case Psychotherapy: return "Psychotherapy";
            case Mental_Trauma: return "Mental Trauma";
            case Somatization: return "Somatization";
            case Schizophrenia: return "Schizophrenia";
            case Sleeping_Disorders: return "Sleeping Disorders";
            case Eating_Disorders: return "Eating Disorders";
            default: return "";
        }
    }
}
