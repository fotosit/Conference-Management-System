package com.fotosit;

public class Person {

    private int userId;
    private final String name;
    private final String surname;
    private final String mail;
    private final String universityName;
    private final int role;  //(1-Author, 2- Referee, 3-Author&Referee, 4-President of the Commission)
    private final String userName;
    private final String userPassword;

    public Person(int _userId, String _name, String _surname, String _mail, String _universityName, int _role, String _userName, String _userPassword)
    {
        this.userId=_userId;
        this.name=_name;
        this.surname=_surname;
        this.mail=_mail;
        this.universityName=_universityName;
        this.role=_role;
        this.userName=_userName;
        this.userPassword=_userPassword;
    }

    public int getUserId()
    {
        return userId;
    }

    public String getName()
    {
        return name;
    }

    public String getSurname()
    {
        return surname;
    }

    public String getMail()
    {
        return mail;
    }

    public String getUniversityName()
    {
        return universityName;
    }

    public int getRole()
    {
        return role;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getUserPassword()
    {
        return userPassword;
    }

}
