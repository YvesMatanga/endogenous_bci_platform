/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.user.bci;

import java.util.ArrayList;

/**
 *
 * @author Yves Matanga
 */

//Database bcu user is a inherited class use for databse content display
public class DatabaseBciUser extends BciUser{
    protected int nbOfBciSessions;
    protected int TotalNbOfTrials;
    protected int nbOfTrialsPerSession;
    protected int nbOfRunsPerSession;
    protected ArrayList<ImageryEnum> ListOfUsedClasses;
    
    public DatabaseBciUser(){
        super();        
    }   
    
}
