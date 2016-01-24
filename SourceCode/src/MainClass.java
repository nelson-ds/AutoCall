
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.skype.*;
import java.util.Properties;

/**
 *
 * @author nelson.dsouza
 */

public final class MainClass implements Runnable
{   
    public static void main(String[] args) 
    {
        new MainClass();
    }
    
    @SuppressWarnings("static-access")
    public MainClass()
    {
        threadrunner();
    }              
    
    String phno;
    String NewErrtext;
    String Errtext0 ;
    String Errtext;
    String mailpath;
    String nomail;
    String subjectline;
    String sendername;
    String begin;

    String errstr;
    String errstart;
    String errend;
    String date;
    String Datecall;
   
    String portno;
    String useskype;
    String skypeid;
    String subjectcontains;
    String sendercontains;
    String useboth;
    String usesender;
    String usesubject;
    String dontcall;
    String execanyway;
    
    String timestillcall;
    String subject;
    String sender;
    String Errno;
    
    String rifcondition;
    String rtakesendername;
    String rtakesendercontains;
    String rtakesubjectname;
    String rtakesubjectcontains;
    String relseoutput;
    String configuremultiple;
    String proceed;
    String last;
    String newmail;
    String searchforlines;
    String speakonlylines;
    String errorlines;
    String usenone;
    String htmlparse;
    String normalparse;

    FileWriter outputtext;
    File inputfile;
    File outputfile;
    File inputdirectory;
    VoiceTest objVoiceTest;
    
    int datecount;
    int subjectcount;
    int sendercount;
    int errstatus;
    int counter;
    int cnt;
    int errorhere;
    int noerror_other_mail;
    int j;
    int timesdontcall;
    int ts;
    int te;
    int configurations;
    int sendr;
    int filecount;
    int errcount;
    int subj;
    int count;
    int index;
    char info[] = new char[10000];
    char summ[] = new char[10000];
 
    Calendar currentDate;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    String dateNow;
    
    public void threadrunner()
    {        
        try 
        {
//******************************************Loading properties file and checking status***********************************************
            currentDate = Calendar.getInstance();
            formatter=  new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss");
            dateNow = formatter.format(currentDate.getTime());
                
            Properties props = new Properties();
            props.load(new FileInputStream("C:\\tmp\\Beatle-VBM\\Bugs\\CL_05\\config.properties"));
            
            configuremultiple = props.getProperty("configuremultiple");         
            if(configuremultiple.equals("true"))
            {
                configurations = Integer.parseInt(props.getProperty("configurations"));
                System.out.println("Running program for " + configurations + " Configurations" );              
            }
            else
            {
                System.out.println("Running for single configuration");
                 configurations=1;
            }         
            
 //******************************************Checking if any file is present***********************************************   
            
            nomail="'0 new mail'";
            inputdirectory=new File("C:\\tmp\\mail");
            System.out.println("The status of mails is: ");

            String filelists[] = inputdirectory.list();
            for (int i = 0; i < filelists.length; i++) 
            {
                filecount++;
                System.out.println((i + 1) + "-" + filelists[i]);
            }
        
            if(filecount!=0)    
            {      
                System.out.println();
                System.out.println("The total number of mails as on: " + dateNow + " is " + filecount);
                System.out.println("File taken for parsing is "+filelists[0].toUpperCase());
                mailpath="C:\\tmp\\mail\\"+filelists[0];
                System.out.println("At location "+mailpath);
                System.out.println();
            }  
            else
            {
                System.out.println(nomail.toUpperCase());
                mailpath="C:\\tmp\\mail\\farzi.txt"; //dummy path which cannot be read
            }            
            inputfile = new File(mailpath); 
       
//******************************************Finished Checking...now executing configuration loop and initializing variables***********************************************
            
            for(int j=1;j<=configurations;j++)
            {                
     
                currentDate = Calendar.getInstance();
                formatter=  new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss");
                dateNow = formatter.format(currentDate.getTime());
                
                formatter1=  new SimpleDateFormat("yyyy_MMM_dd_HH.mm.ss");
                date = formatter1.format(currentDate.getTime());
            
                outputtext = null;
                NewErrtext=null;
                Errtext0 = null;
                Errtext = null;    
             
                subjectcount=0;
                sendercount=0;
                errstatus=1;
                datecount=0;
                counter=0;
                cnt=1;
                sendr=0;
                filecount=0;
                errcount=0;
                subj=0;
                count=1;
                
                newmail="true";
                noerror_other_mail=1;
                timestillcall = "true";
                last="false";
                proceed="true";
                subject="Subject:";
                sender="From:";
                Errno = "0";
                begin="no";
                htmlparse="false";
                normalparse="false";
                execanyway = "false";
                
                if(j==configurations)
                    last="true";
                
//******************************************Picking up user specified configurations***********************************************                
                //Take the line which is to be searched in the mail
                errstr = props.getProperty("errstr" +j);
                errstart = props.getProperty("errstart" +j);
                errend = props.getProperty("errend" +j);
                errstr.toLowerCase();
                errstart.toLowerCase();
                errend.toLowerCase();                 
                                    
                useboth = props.getProperty("useboth" + j);
                usesender = props.getProperty("usesender" + j);
                usesubject = props.getProperty("usesubject" + j);
                usenone = props.getProperty("usenone" + j);
                sendercontains = props.getProperty("sendercontains" + j);
                subjectcontains = props.getProperty("subjectcontains" + j);
                searchforlines = props.getProperty("searchforlines" + j);
                speakonlylines = props.getProperty("speakonlylines" + j);
                
                dontcall = props.getProperty("dontcall" + j);
                timesdontcall = Integer.parseInt(props.getProperty("timesdontcall" + j));
                
                portno = props.getProperty("portno" + j);
                phno = props.getProperty("phno" + j);
                useskype = props.getProperty("useskype" + j);
                skypeid = props.getProperty("skypeid" + j);

//******************************************if file can be read then extract***********************************************
                //******************************************Sender name from mail***********************************************
                //******************************************subject name from mail***********************************************
                //******************************************are any error lines present in mail***********************************************
                if(inputfile.canRead())
                { 
                    if(searchforlines.equals("true"))
                    { 
                    if(!errstr.isEmpty() || !errstart.isEmpty() || !errend.isEmpty())
                        errcount++;   
                    }
                    else
                    errcount=1;
                    
                                            
//***********************Executed if user has specified either start, end or some line which is to be parsed**********************
                    if(errcount!=0)
                    {                   
                        FileInputStream fis1 =new FileInputStream(inputfile);
                        InputStreamReader isr1=new InputStreamReader(fis1);
                        BufferedReader br1=new BufferedReader(isr1);
    
                        String str1= "";
                
                        while((str1 = br1.readLine())!=null)
                        {    
                            if(!errstr.isEmpty() && str1.contains(errstr) && searchforlines.equals("true"))
                            {
                                noerror_other_mail=0;
                                
                                if(!errstart.isEmpty() && str1.startsWith(errstart) && searchforlines.equals("true"))
                                {
                                    noerror_other_mail=0;
                                }   
                    
                                if(!errend.isEmpty() && str1.endsWith(errend) && searchforlines.equals("true"))
                                {
                                    noerror_other_mail=0;
                                }           
                            }
              
                            if(str1.startsWith(sender) && sendr==0)
                            {
                                sendername=str1.substring(6);
                                sendername=sendername.trim();
                                sendr=1;
                            }
                    
                            if(str1.startsWith(subject) && subj==0)
                            {
                                if(str1.length()<9)
                                    subjectline="not displayed";
                                else     
                                    subjectline=str1.substring(9);
                                subjectline=subjectline.trim();
                                subj=1;
                            }
                        }
                
                        isr1.close();
                        fis1.close();
                
                        //This specifies that the current mail has none of the sequence 
                        //ie either start or containing or end specified by the user
                        //and will be handled in the part using the token errorno=0
                        if(noerror_other_mail==1)    
                        errorlines="notfound";
                
                        //this means that error lines specified by user have been found
                        if(noerror_other_mail==0)
                        errorlines="found";
                        
//******************************************Extracting configuration information***********************************************                
                        
                        if(useboth.equals("true"))
                        {
                            rifcondition=useboth;                      
                            rtakesendername=sendername.toLowerCase();
                            rtakesendercontains=sendercontains.toLowerCase();
                            rtakesubjectname=subjectline.toLowerCase();
                            rtakesubjectcontains=subjectcontains.toLowerCase();
                            relseoutput="SENDER and SUBJECT you specified";
                            if(rtakesendername.contains(rtakesendercontains)&& rtakesubjectname.contains(rtakesubjectcontains))
                                proceed="true";
                            else
                                proceed="false";
                        }
                        else 
                        {
                            if(usesender.equals("true"))
                            {
                                rifcondition=usesender;                       
                                rtakesendername=sendername.toLowerCase();
                                rtakesendercontains=sendercontains.toLowerCase();
                                rtakesubjectname="not specified";
                                rtakesubjectcontains="not specified";
                                relseoutput="SENDER you specified";
                                
                                if(rtakesendername.contains(rtakesendercontains))
                                    proceed="true";
                                else
                                    proceed="false";
                            }                            
                            else
                            {
                                if(usesubject.equals("true"))
                                {    
                                    rifcondition=usesubject;                              
                                    rtakesendername="not specified";
                                    rtakesendercontains="not specified";
                                    rtakesubjectname=subjectline.toLowerCase();
                                    rtakesubjectcontains=subjectcontains.toLowerCase();
                                    relseoutput="SUBJECT you specified";                                  
                                    if(rtakesubjectname.contains(rtakesubjectcontains))
                                        proceed="true";
                                    else
                                        proceed="false";
                                }
                                else 
                                {
                                    if(usenone.equals("true"))
                                    {
                                        rifcondition=usenone;                       
                                        rtakesendername="not specified";
                                        rtakesendercontains="not specified";
                                        rtakesubjectname="not specified";
                                        rtakesubjectcontains="not specified";
                                        relseoutput="The ERROR LINES you specified";
                                        if(searchforlines.equals("true"))
                                        {
                                            if(errorlines.equals("found"))
                                                proceed="true";
                                            else
                                                proceed="false";
                                        }
                                        else
                                        {
                                        proceed="false";
                                        }
                                    }    
                                    else
                                    {
                                        System.out.println("For the configuration number " + j +" You have not specified the filtering condition for mail based on either");
                                        System.out.println("1. Sender");
                                        System.out.println("2. Subject");
                                        System.out.println("3. Both");
                                        System.out.println("4. Error lines only");
                                        if(last.equals("true"))
                                        System.exit(0);
                                        else 
                                            proceed="false";
                                    }
                                }
                            }
                        }
                        if(proceed.equals("true") && searchforlines.equals("true") && errorlines.equals("found"))
                            proceed="true";
                        else
                           if(proceed.equals("true") && searchforlines.equals("true") && errorlines.equals("notfound"))
                                proceed="false";
                                                     
                        if(proceed.equals("true") && searchforlines.equals("false"))
                            errorlines="found";
                        
                        if(proceed.equals("true"))
                            System.out.println("Processing for configuration " + j + " :- MATCHED");
                        else
                            System.out.println("Processing for configuration " + j + " :- NOT MATCHED");
                            
                        if(last.equals("true"))
                        {
                            if(proceed.equals("false"))
                            {
                                System.out.println("");
                                System.out.println("Processing did NOT MATCH for ANY configurations");
                                execanyway="true";
                            }
                            
                            proceed="true";
                        }                        
        
//******************************************Printing the mail parsing details if filter is sucessfull or if it is last run***********************************************                                    
                        if(proceed.equals("true"))
                        {
                            System.out.println("*************************************************************");
                            if(last.equals("true"))
                            {
                                if(execanyway.equals("true"))
                                    System.out.println("Executing based on last configuration in order to generate logs ");
                                else
                                    System.out.println("       Executing for LAST configuration which is:- " + j);
                            }                            
                            else
                                System.out.println("             Executing for configuration:- " + j);
                            
                            if(searchforlines.equals("true"))
                            {
                                System.out.println("*************************************************************");
                                System.out.println("");
                                System.out.println("The mail parsing details are as follows:- ");
                            
                                if(errstr==null)
                                    System.out.println("--%--User has not specified which line he'd like in mail");
                                else 
                                    System.out.println("Mail line specified by user should contain the string:- \""+errstr.toUpperCase() + "\"");

                                if(errstart==null)
                                    System.out.println("--%--User has not specified by what sequence the line in mail should begin");
                                else 
                                    System.out.println("Sequence by which mail line should begin is:- \""+errstart.toUpperCase() + "\"");
                
                                if(errend==null)
                                    System.out.println("--%--user has not specified by what sequence the line in mail should end");
                                else 
                                    System.out.println("Sequence by which mail line should end is:-  \""+errend.toUpperCase() + "\"");

                                System.out.println("*************************************************************");
                            }
                        
                        }
                        
                    }
//******************************************If user has not specified error lines then he will be given this warning***********************************************                                    
                    else
                    {
                        if(searchforlines.equals("true"))
                        {
                        //givecustname=null;
                        System.out.println("Please check the configurations");
                        System.out.println("You have not specified any one of");
                        System.out.println("   1.Line which the mail should contains");
                        System.out.println("   2.Sequence Line starts with");
                        System.out.println("   3.Sequence Line ends in");
                        System.out.println(" for the software to check in the current mail");
                        System.out.println("Please exit Beatle-VBM and configure your settings to recieve calls");
                        System.out.println("Else only error logs will be generated");
                        System.out.println("");
                        }
                    }
                } 
        
                else 
                {
                    newmail="false"; 
                    if(last.equals("true"))
                    {
                        System.out.println("No new mail as on " + dateNow);
                        outputfile = new File("C:\\tmp\\Errorlog\\NoNewMail\\nnm_"+date+".txt");
                        outputtext = new FileWriter(outputfile,true);
                        outputtext.write("There is no new mail as on " + dateNow);
                        outputtext.close();
                    } 
                }
//******************************************Done with mail Parse***********************************************   

                
     
                if (newmail.equals("true") && proceed.equals("true"))
                {                                     
 //******************************************Checking if time has been specified when not to call***********************************************                  
                    if(dontcall.equals("true"))
                    {        
                        Datecall = dateNow.substring(12,14) + dateNow.substring(15,17);      

                        int timestart[] = new int[timesdontcall + 1];                        
                        int timeend[] = new int[timesdontcall + 1];

                        
                        for(int i=1;i<=timesdontcall;i++)
                        {    
                            timestart[i]=Integer.parseInt(props.getProperty("timestart" + j + i)) ;
                            timeend[i]=Integer.parseInt(props.getProperty("timeend" + j + i)) ;
                        }

                        for(int i=1;i<=timesdontcall;i++)
                        {
                            if(timestart[i]<=Integer.parseInt(Datecall) && timeend[i]>=Integer.parseInt(Datecall) )
                            {
                                timestillcall="false";
                                ts = timestart[i];
                                te = timeend[i];
                            }
                        }
                    }
                        if(errorlines.equals("notfound")) 
                        {            
                            if(rifcondition.equals("true"))
                            {
                            System.out.println("");
                            System.out.println("The mail filtering details are as follows:- ");

                            if(rtakesendercontains.equals("not specified"))
                                System.out.println("Senders name is:- " + rtakesendercontains);
                            else
                                System.out.println("Senders name is:- " + rtakesendercontains.toUpperCase());
                            
                            if(rtakesubjectcontains.equals("not specified"))
                                System.out.println("Subject is:- " + rtakesubjectcontains);
                            else
                                System.out.println("Subject is:- " + rtakesubjectcontains.toUpperCase());
                            
                            
                            System.out.println("*************************************************************");
                            System.out.println("");
                                              
                                if(rtakesendername.contains(rtakesendercontains) && rtakesubjectname.contains(rtakesubjectcontains))
                                {                                    
                                    System.out.println("There is NO MATCH in the mail for any of the lines either \n"
                                            + "1. Having string:- " + errstr + "\n"
                                            + "2. Starting with:- " + errstart + "\n"
                                            + "3. Ending with:- " + errend + "\n"
                                                    + "Sent by:- " + sendername.toUpperCase() + "\n"
                                                    + "having subject line:- " + subjectline.toUpperCase() + "\n"
                                                    + "on date " + dateNow + "\n");
                            
                                    outputfile = new File("C:\\tmp\\Errorlog\\NoErrorMail\\nem"+date+".txt");
                                    outputtext  = new FileWriter(outputfile,true);
                                    outputtext.write("There is no match in the mail for any of the lines either ");
                                        outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write("1. Having string:- " + errstr );
                                        outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write("2. Starting with:- " + errstart );
                                        outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write("3. Ending with:- " + errend );
                                        outputtext.write(System.getProperty( "line.separator" ));     
                                        outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write("Sent by:- " + sendername.toUpperCase()); 
                                        outputtext.write(System.getProperty( "line.separator" ));  
                                    outputtext.write("having subject line:- " + subjectline.toUpperCase()) ;
                                        outputtext.write(System.getProperty( "line.separator" ));          
                                    outputtext.write("on date " + dateNow);
                                    
                              //******************additional info***************************************      
                                    outputtext.write(System.getProperty( "line.separator" )); 
                                    outputtext.write(System.getProperty( "line.separator" ));  
                                    outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write("*************************************************************");
                                    outputtext.write(System.getProperty( "line.separator" )); 
                                    outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write("The mail filtering details are as follows:- ");
                                    outputtext.write(System.getProperty( "line.separator" ));   
                                    if(rtakesendercontains.equals("not specified"))
                                        outputtext.write("Senders name is:- " + rtakesendercontains);
                                    else
                                        outputtext.write("Senders name is:- " + rtakesendercontains.toUpperCase());
                                    
                                    outputtext.write(System.getProperty( "line.separator" ));    
                            
                                    if(rtakesubjectcontains.equals("not specified"))
                                        outputtext.write("Subject is:- " + rtakesubjectcontains);
                                    else
                                        outputtext.write("Subject is:- " + rtakesubjectcontains.toUpperCase());
                                
                                    outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write(System.getProperty( "line.separator" )); 
                                    outputtext.write("*************************************************************");
                                                                        

                                    outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write(System.getProperty( "line.separator" )); 
                                    outputtext.write("The mail parsing details are as follows:- ");
                            
                                    outputtext.write(System.getProperty( "line.separator" ));    
                                    
                                    if(errstr==null)                                   
                                        outputtext.write("--%--User has not specified which line he'd like in mail");                                   
                                    else 
                                        outputtext.write("Mail line specified by user should contain the string:- \""+errstr.toUpperCase() + "\"");

                                    outputtext.write(System.getProperty( "line.separator" ));    
                
                                    if(errstart==null)
                                        outputtext.write("--%--User has not specified by what sequence the line in mail should begin");
                                    else 
                                        outputtext.write("Sequence by which mail line should begin is:- \""+errstart.toUpperCase() + "\"");

                                    outputtext.write(System.getProperty( "line.separator" ));    
                                    
                                    if(errstr==null)
                                        outputtext.write("--%--user has not specified by what sequence the line in mail should end");
                                    else 
                                        outputtext.write("Sequence by which mail line should end is:-  \""+errend.toUpperCase() + "\"");
                                    outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write(System.getProperty( "line.separator" )); 
                                    outputtext.write("*************************************************************");
                                     //******************additional info***************************************  
               
                                    errstatus=0;
                                    outputtext.close();
                                    inputfile.delete();
                                    //System.exit(0);
                                }
                        
                                else
                                {
                                System.out.println("You have recieved some OTHER MAIL *NOT* having the " + relseoutput + "\n"
                                                    + "sent by:- " + sendername.toUpperCase() + "\n"
                                                    + "having subject line:- " + subjectline.toUpperCase() + "\n" 
                                                    + "on date:- " + dateNow + "\n");
                            
                                    outputfile = new File("C:\\tmp\\Errorlog\\OtherMail\\om_"+date+".txt");
                                    outputtext  = new FileWriter(outputfile,true);
                                    outputtext.write("You have recieved some OTHER MAIL *NOT* having the " + relseoutput);
                                        outputtext.write(System.getProperty( "line.separator" ));  
                                    outputtext.write("Sent by:- " + sendername.toUpperCase()); 
                                        outputtext.write(System.getProperty( "line.separator" ));  
                                    outputtext.write("having subject line:- " + subjectline.toUpperCase()); 
                                        outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write("on date:- " + dateNow);
                                    
                                    //******************additional info***************************************      
                                    outputtext.write(System.getProperty( "line.separator" )); 
                                    outputtext.write(System.getProperty( "line.separator" ));  
                                    outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write("*************************************************************");
                                    outputtext.write(System.getProperty( "line.separator" )); 
                                    outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write("The mail filtering details are as follows:- ");  
                                    outputtext.write(System.getProperty( "line.separator" ));    
                                    if(rtakesendercontains.equals("not specified"))
                                        outputtext.write("Senders name is:- " + rtakesendercontains);
                                    else
                                        outputtext.write("Senders name is:- " + rtakesendercontains.toUpperCase());
                                    
                                    outputtext.write(System.getProperty( "line.separator" ));    
                            
                                    if(rtakesubjectcontains.equals("not specified"))
                                        outputtext.write("Subject is:- " + rtakesubjectcontains);
                                    else
                                        outputtext.write("Subject is:- " + rtakesubjectcontains.toUpperCase());
                                
                                    outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write(System.getProperty( "line.separator" )); 
                                    outputtext.write("*************************************************************");
                                    

                                    outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write(System.getProperty( "line.separator" )); 
                                    outputtext.write("The mail parsing details are as follows:- ");
                            
                                    outputtext.write(System.getProperty( "line.separator" ));    
                                    
                                    if(errstr==null)                                   
                                        outputtext.write("--%--User has not specified which line he'd like in mail");                                   
                                    else 
                                        outputtext.write("Mail line specified by user should contain the string:- \""+errstr.toUpperCase() + "\"");

                                    outputtext.write(System.getProperty( "line.separator" ));    
                
                                    if(errstart==null)
                                        outputtext.write("--%--User has not specified by what sequence the line in mail should begin");
                                    else 
                                        outputtext.write("Sequence by which mail line should begin is:- \""+errstart.toUpperCase() + "\"");

                                    outputtext.write(System.getProperty( "line.separator" ));    
                                    
                                    if(errstr==null)
                                        outputtext.write("--%--user has not specified by what sequence the line in mail should end");
                                    else 
                                        outputtext.write("Sequence by which mail line should end is:-  \""+errend.toUpperCase() + "\"");
                                    outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write(System.getProperty( "line.separator" )); 
                                    outputtext.write("*************************************************************");
                                     //******************additional info***************************************  
                     
                                    errstatus=0;
                                    inputfile.delete();
                                    outputtext.close();
                                    //System.out.println("Error Status is " + errstatus);
                                    //System.exit(0);          
                                }
                            }
                        }
                        //executed if there are one or more error lines found
                        else   
                        {
                            if(rifcondition.equals("true"))
                            {
                                if(searchforlines.equals("false"))
                                System.out.println("*************************************************************");
                                System.out.println("");
                                System.out.println("The mail filtering details are as follows:- ");
                           
                                if(rtakesendercontains.equals("not specified"))
                                    System.out.println("Senders name is:- " + rtakesendercontains);
                                else
                                    System.out.println("Senders name is:- " + rtakesendercontains.toUpperCase());
                            
                                if(rtakesubjectcontains.equals("not specified"))
                                    System.out.println("Subject is:- " + rtakesubjectcontains);
                                else
                                    System.out.println("Subject is:- " + rtakesubjectcontains.toUpperCase());
                                                                        
                                if(usenone.equals("true"))
                                    System.out.println("Filtering mail by matching ERROR LINES");
                                
                                if(usenone.equals("false"))
                                {
                                    if(searchforlines.equals("true"))
                                        System.out.println("Parsing mail for error lines");
                                    else
                                        System.out.println("Not parsing mail for any error lines");
                                }

                            
                                System.out.println("*************************************************************");
                                System.out.println("");
                         
                                if((rtakesendername.contains(rtakesendercontains) && rtakesubjectname.contains(rtakesubjectcontains)) || usenone.equals("true"))
                                {
                                    FileInputStream fisnew =new FileInputStream(inputfile);
                                    InputStreamReader isrnew=new InputStreamReader(fisnew);
                                    BufferedReader brnew=new BufferedReader(isrnew);
                                
                                    outputfile = new File("C:\\tmp\\Errorlog\\ErrorMail\\em_"+date+".txt");
                                    outputtext = new FileWriter(outputfile,true);
                    
                                    String str = "";
                                    int linecount = 1;
                
                                    while((str = brnew.readLine())!=null)
                                    {   
                                        if(datecount==0 )
                                        {
                                            outputtext.write("Timestamp is: " + dateNow);
                                            outputtext.write(System.getProperty( "line.separator" ));
                                            datecount=1;   
                                        }
                            
                                        if(str.startsWith(sender) && sendercount==0 )
                                        {
                                            outputtext.write("The Sender of this mail is: " + str.substring(6).toUpperCase());
                                            outputtext.write(System.getProperty( "line.separator" ));
                                            sendername=str.substring(6);
                                            sendername=sendername.trim();
                                            sendercount=1;
                                        }
              
                                        if(str.startsWith(subject) && subjectcount==0 )
                                        {                                          
                                            outputtext.write("The Subject line is: " + subjectline.toUpperCase());
                                            outputtext.write(System.getProperty( "line.separator" ));
                                            
                                            
                                            if(searchforlines.equals("true"))
                                            {
                                                outputtext.write("The error lines are as follows:- ");
                                                outputtext.write(System.getProperty( "line.separator" ));
                                                outputtext.write(System.getProperty( "line.separator" ));
                                            }
                                            else
                                            {
                                                outputtext.write("The entire mail is as follows:- ");
                                                outputtext.write(System.getProperty( "line.separator" ));
                                                outputtext.write(System.getProperty( "line.separator" ));
                                            }  
                                            subjectcount=1;
                                        }
                                        
                                        
                                        if(searchforlines.equals("true"))
                                        {                   
                                                if(speakonlylines.equals("true"))
                                                {
                                                    if(errstart.isEmpty() && errend.isEmpty())
                                                    {
                                                        if(str.contains(errstr))
                                                        {
                                                            outputtext.write(str + "   ");
                                                            counter+=1;
                                                            outputtext.write(System.getProperty( "line.separator" ));
                                                        }
                                                    }
                                                    else
                                                    {
                                                        if(str.startsWith(errstart) || str.contains(errstr) || str.endsWith(errend) )
                                                        {

                                                            {              
                                                                if((counter%3)==0)
                                                                {
                                                                    if(str.startsWith(errstart))
                                                                    {   
                                                                        FileInputStream fistemp =new FileInputStream(inputfile);
                                                                        InputStreamReader isrtemp=new InputStreamReader(fistemp);
                                                                        BufferedReader brtemp=new BufferedReader(isrtemp);
                                                                    
                                                                        String strtemp = "";
                                                                        Boolean continuefurther = true;
                                                                        String jumptonexterror = "true";
                                                                        int linecounttemp = 0;
                                                                        
                                                                        while((strtemp = brtemp.readLine())!=null && continuefurther)
                                                                        {
                                                                            linecounttemp+=1;
                                                                            if(linecounttemp<linecount)
                                                                           continue;
                                                                            
                                                                            if(strtemp.contains(errstr))
                                                                                jumptonexterror = "false";
                                                                                
                                                                       
                                                                            if(strtemp.endsWith(errend))
                                                                                continuefurther = false;                                      
                                                                        }
                                                                        if(jumptonexterror.equals("false"))
                                                                        {         
                                                                            outputtext.write(str + "   ");
                                                                            counter+=1;
                                                                        }
                                                                        brtemp.close();
                                                                        isrtemp.close();
                                                                        fistemp.close();
                                                                    }
                                                                }
                                                                else
                                                                {
                                                                    outputtext.write(str + "   ");
                                                                    counter+=1;
                                                                }     
                                                                if ((counter%3)==0)
                                                                outputtext.write(System.getProperty( "line.separator" ));
                                                            }
                                                        }
                                                    }
                                                }
                                                else
                                                {
                                                    if(str.equals("<html>"))
                                                        htmlparse="true";
                                                    if(str.equals("MIME-Version: 1.0"))
                                                        normalparse="true";
                                            
                                                    if(htmlparse.equals("true"))
                                                    {
                                                        System.out.println("in html parse");
                                                        if(str.equals("<body>"))
                                                        {
                                                            begin="yes";
                                                            System.out.println("found body");
                                                        }
                                                        if(str.equals("</body>"))
                                                            begin="no";
                                     
                                                        if(begin.equals("yes"))
                                                        {
                                                            for (int i = 0; i < str.length(); i++) 
                                                            {
                                                                info[i] = str.charAt(i);
                                                            }
                                                            for (int i = 0; i < str.length(); i++) 
                                                            {
                                                                if (info[i] == '<') 
                                                                    count += 1;
                                                                if (count == 1) 
                                                                {
                                                                    summ[index] = info[i];
                                                                    index++;
                                                                }
                                                                if (info[i] == '>') 
                                                                    count -= 1;
                                                            }
                                                            summ.toString();
                                                            for (int i = 0; i < summ.length; i++) 
                                                            {
                                                                outputtext.write(summ[i]);
                                                            }
                                                        }  
                                                    }
                                                    else
                                                    {
                                                        if(normalparse.equals("true"))
                                                        {
                                                        outputtext.write(str + "  ");
                                                        outputtext.write(System.getProperty( "line.separator" ));
                                                        }
                                                    }
                                                }                                      
                                            
                                        }                                       
                                        else
                                        {
                                            if(str.equals("<html>"))
                                                htmlparse="true";
                                            if(str.equals("MIME-Version: 1.0"))
                                                normalparse="true";
                                            
                                            if(htmlparse.equals("true"))
                                            {
                                                if(str.equals("<body>"))
                                                {
                                                    begin="yes";
                                                    System.out.println("found body");
                                                }
                                                if(str.equals("</body>"))
                                                    begin="no";
                                     
                                                if(begin.equals("yes"))
                                                {
                                                    for (int i = 0; i < str.length(); i++) 
                                                    {
                                                        info[i] = str.charAt(i);
                                                    }
                                                    for (int i = 0; i < str.length(); i++) 
                                                    {
                                                        if (info[i] == '<') 
                                                            count += 1;
                                                        if (count == 1) 
                                                        {
                                                            summ[index] = info[i];
                                                            index++;
                                                        }
                                                        if (info[i] == '>') 
                                                            count -= 1;
                                                    }
                                                    summ.toString();
                                                    for (int i = 0; i < summ.length; i++) 
                                                    {
                                                        outputtext.write(summ[i]);
                                                    }
                                                }  
                                            }
                                            else
                                            {
                                                if(normalparse.equals("true"))
                                                {
                                                outputtext.write(str + "  ");
                                                outputtext.write(System.getProperty( "line.separator" ));
                                                }
                                            }
                                        }
                                        linecount+=1;
                                    }
                                    
                                    if(searchforlines.equals("true"))
                                        {
                                            if(speakonlylines.equals("true"))
                                            {
                                                outputtext.write(System.getProperty( "line.separator" ));
                                                outputtext.write(System.getProperty( "line.separator" ));
                                                outputtext.write("****************************   DONE   *********************************");
                                                outputtext.write(System.getProperty( "line.separator" ));
                                                outputtext.write(System.getProperty( "line.separator" ));
                                                if(errstart.isEmpty() && errend.isEmpty())
                                                    outputtext.write("Total number of errors are:" +(counter));
                                                else
                                                    outputtext.write("Total number of errors are:" +(counter/3));
                                            }
                                            else
                                            {
                                                outputtext.write(System.getProperty( "line.separator" ));
                                                outputtext.write(System.getProperty( "line.separator" ));
                                                outputtext.write("****************************   DONE   *********************************");
                                                outputtext.write(System.getProperty( "line.separator" ));
                                                outputtext.write(System.getProperty( "line.separator" ));
                                                if(errstart.isEmpty() && errend.isEmpty())
                                                    outputtext.write("Total number of errors are:" +(counter));
                                                else
                                                    outputtext.write("Total number of errors are:" +(counter/3));
                                                outputtext.write(System.getProperty( "line.separator" ));
                                                outputtext.write("Entire mail has been written to this file because the user has specified to call and relay entire mail");
                                                
                                            } 
                                        }
                                        else
                                        {
                                            outputtext.write(System.getProperty( "line.separator" ));
                                            outputtext.write(System.getProperty( "line.separator" ));
                                            outputtext.write("****************************   DONE   *********************************");
                                            outputtext.write(System.getProperty( "line.separator" ));
                                            outputtext.write(System.getProperty( "line.separator" ));
                                            outputtext.write("Total number of errors are: 0 - because mail is not being parsed for error lines");
                                            outputtext.write(System.getProperty( "line.separator" ));
                                            outputtext.write("Entire mail has been written to this file because the user has specified to call and relay entire mail");
                                        }
                                
                                        //******************additional info***************************************      
                                        outputtext.write(System.getProperty( "line.separator" )); 
                                        outputtext.write(System.getProperty( "line.separator" ));  
                                        outputtext.write(System.getProperty( "line.separator" ));
                                        outputtext.write("*************************************************************");
                                        outputtext.write(System.getProperty( "line.separator" )); 
                                        outputtext.write(System.getProperty( "line.separator" ));
                                        outputtext.write("The mail filtering details are as follows:- ");
                                        outputtext.write(System.getProperty( "line.separator" ));    
                                        if(rtakesendercontains.equals("not specified"))
                                            outputtext.write("Senders name is:- " + rtakesendercontains);
                                        else
                                            outputtext.write("Senders name is:- " + rtakesendercontains.toUpperCase());
                                    
                                        outputtext.write(System.getProperty( "line.separator" ));    
                            
                                        if(rtakesubjectcontains.equals("not specified"))
                                            outputtext.write("Subject is:- " + rtakesubjectcontains);
                                        else
                                            outputtext.write("Subject is:- " + rtakesubjectcontains.toUpperCase());
                                        outputtext.write(System.getProperty( "line.separator" ));
                                        
                                        if(usenone.equals("true"))
                                            outputtext.write("Filtering mail by matching ERROR LINES");
                                
                                        outputtext.write(System.getProperty( "line.separator" ));
                                        outputtext.write(System.getProperty( "line.separator" )); 
                                        outputtext.write("*************************************************************");
                                    
                                        if(searchforlines.equals("true"))
                                        {
                                            outputtext.write(System.getProperty( "line.separator" ));
                                            outputtext.write(System.getProperty( "line.separator" ));
                                            outputtext.write("The mail parsing details are as follows:- ");
                            
                                            outputtext.write(System.getProperty( "line.separator" ));    
                                    
                                            if(errstr==null)                                   
                                                outputtext.write("--%--User has not specified which line he'd like in mail");                                   
                                            else 
                                                outputtext.write("Mail line specified by user should contain the string:- \""+errstr.toUpperCase() + "\"");

                                            outputtext.write(System.getProperty( "line.separator" ));    
                
                                            if(errstart==null)
                                                outputtext.write("--%--User has not specified by what sequence the line in mail should begin");
                                            else 
                                                outputtext.write("Sequence by which mail line should begin is:- \""+errstart.toUpperCase() + "\"");

                                            outputtext.write(System.getProperty( "line.separator" ));    
                                    
                                            if(errstr==null)
                                                outputtext.write("--%--user has not specified by what sequence the line in mail should end");
                                            else 
                                                outputtext.write("Sequence by which mail line should end is:-  \""+errend.toUpperCase() + "\"");
                                        outputtext.write(System.getProperty( "line.separator" ));
                                        outputtext.write(System.getProperty( "line.separator" )); 
                                        outputtext.write("*************************************************************");
                                }

                                        if(timestillcall.equals("false"))
                                        {
                                        outputtext.write(System.getProperty( "line.separator" ));
                                        outputtext.write(System.getProperty( "line.separator" ));
                                        outputtext.write("#--NOTE--#");
                                        outputtext.write(System.getProperty( "line.separator" ));
                                        outputtext.write("Did not call for this mail, because user had specified not to between " + ts + " hrs - " + te + "hrs");
                                        }
                                        //******************additional info***************************************  

                                        //errorno=(counter/3);
                                        isrnew.close();
                                        fisnew.close();
                                        outputtext.close();
                                        if(timestillcall.equals("true"))
                                            inputfile.delete();
                                        else
                                            if(last.equals("false"))
                                                Thread.sleep(1000);
                                            else
                                                inputfile.delete();
                                
                                }
                                
                                if((rtakesendername.contains(rtakesendercontains) && rtakesubjectname.contains(rtakesubjectcontains)))
                                {
                                    FileInputStream fis=new FileInputStream("C:\\tmp\\Errorlog\\ErrorMail\\" + "em_"+date+".txt");
                                    InputStreamReader isr=new InputStreamReader(fis);
                                    BufferedReader br=new BufferedReader(isr);
                
                                    FileInputStream fis0=new FileInputStream("C:\\tmp\\Errorlog\\ErrorMail\\" + "em_"+date+".txt");
                                    InputStreamReader isr0=new InputStreamReader(fis0);
                                    BufferedReader br0=new BufferedReader(isr0);
                  
                                    while((Errtext0=br0.readLine())!=null)
                                    {
                                        if(Errtext0.startsWith("The Sender of this mail is:"))
                                        {
                                            sendername=Errtext0.substring(28);
                                            sendername=sendername.trim();                                  
                                        }
                                
                                        if(Errtext0.startsWith("The Subject line is:"))
                                        {
                                            subjectline=Errtext0.substring(21);
                                            subjectline=subjectline.trim();                                 
                                        }
                        
                                        if(Errtext0.startsWith("Total number of"))
                                        {
                                            Errtext0.trim();
                                            Errno=Errtext0.substring(27);
                                            break;
                                        }
                                    }
                    
                                    if(errstatus==1)
                                    {

                                        System.out.println("The call details are as follows:-");
                                        
                                        if(searchforlines.equals("false"))
                                        {
                                            System.out.println("Relay the ENTIRE MAIL via call if filtered sucessfully");
                                        }
                                        else
                                        {
                                            if(speakonlylines.equals("false"))
                                            {
                                                System.out.println("Relaying the ENTIRE MAIL via call if error lines found");
                                            }
                                            else
                                                System.out.println("Relaying only ERROR LINES via call if error lines found");
                                        }
                            
                                        if(portno==null)
                                        {
                                            portno=new String("COM3");
                                            System.out.println("Port not specified by user.Default port taken as "+portno);
                                        }
                                        else 
                                        {
                                            System.out.println("Port number specified by user is:- "+portno);
                                        }           
                
                
                                        if(phno==null)
                                        {
                                            phno=new String("8600873804");
                                            System.out.println("Phone num not specified by user.Default taken as "+phno);
                                        }
                                        else 
                                        {
                                            System.out.println("The phone number specified is:- "+phno);
                                        }
                
                
                                        if(useskype==null)
                                        {
                                            useskype=new String("No");
                                            System.out.println("The default status for using skype is "+useskype.toUpperCase());
                                        }
                                        else 
                                        {
                                            System.out.println("The status for using Skype is:- "+useskype.toUpperCase());                                           
                                        }
                                        if(useskype.equals("yes"))
                                        {
                                        System.out.println("The Skype ID that will be called is:- "+skypeid);
                                        }
                                
                                        System.out.println("*************************************************************");
                  
                                        System.out.println("");
                                        
                                        if(searchforlines.equals("true"))
                                        {
                                            if(speakonlylines.equals("true"))
                                            {
                                                NewErrtext=  "Mail Error Status on " +dateNow+
                                                " is as follows:\n"
                                                + "The Sender of this mail is : " + sendername + ":\n"
                                                + "The Subject line is : " + subjectline + ":\n"
                                                +"Total number of errors are " + Errno + ":\n";

                                            }
                                            else
                                            {
                                            NewErrtext=  "Mail Error Status on " +dateNow+
                                                " is as follows:\n"
                                                + "The Sender of this mail is : " + sendername + ":\n"
                                                + "The Subject line is : [" + subjectline + ":\n" 
                                                +"Total number of errors are " + Errno + ":\n"
                                                +"-----------Entire mail will be relayed-------------:\n";
                                                    
                                            }
                                        }
                                        else
                                        {
                                             NewErrtext=  "Mail Error Status on " +dateNow+
                                                " is as follows:\n"
                                                + "The Sender of this mail is : " + sendername + ":\n"
                                                + "The Subject line is : [" + subjectline + ":\n"
                                                +"-------------Entire mail will be relayed-------------:\n";
                                        }
                                        System.out.println(NewErrtext);
                                    }
                
                                    loop: while((Errtext=br.readLine())!=null)
                                    {   
                                        if(errstatus==0)
                                            break loop;
                                
                                        if(Errtext.startsWith("The Sender of this mail is:"))
                                            continue;
                        
                                        if(Errtext.startsWith("The Subject line is:"))
                                            continue;
                        
                                        if(Errtext.startsWith("Timestamp"))
                                            continue;
                        
                                        if(Errtext.startsWith("Total number of"))
                                            break;
                        
                                        if(searchforlines.equals("true"))
                                        {
                                            if(speakonlylines.equals("true"))
                                            {
                                                if(errstart.isEmpty() && errend.isEmpty())
                                                {
                                                    if(Errtext.contains(errstr))
                                                    {
                                                        Errtext=("Error in  " + Errtext);
                                                        NewErrtext+=(cnt + " : " + Errtext.trim() + ": \n");
                                                        System.out.println(cnt + " : " +Errtext);
                                                        cnt+=1;
                                                    }
                                                }
                                                
                                                else
                                                {
                                                    if(Errtext.startsWith(errstart))
                                                    {
                                                        Errtext=("(Error in  " + Errtext.substring(27));
                                                        NewErrtext+=(cnt + ":    " + Errtext + ";    ");
                                                        System.out.println(cnt+" : "+Errtext);
                                                        cnt+=1;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                            NewErrtext += Errtext;
                                            System.out.println(Errtext);
                                            }
                                        }
                                        else
                                        {
                                            NewErrtext += Errtext;
                                            System.out.println(Errtext);
                                        }
                                    }               
                
                                    if(errstatus==1)      
                                    {
                                        if(timestillcall.equals("true"))
                                        {
                                            System.out.println("  \n  The summary is as follows:");
                                            System.out.println("  The Sender of this mail is: " + sendername.toUpperCase());
                                            System.out.println("  The Subject line is: " + subjectline.toUpperCase());
                                            if(searchforlines.equals("true"))
                                            System.out.println("  Total number of errors are " + Errno);
                                            System.out.println("  Sorry for disturbing you.Bye. \n \n");
                        
                  
                                            Thread myThread = new Thread(this);
                                            myThread.start();
                
                                            try 
                                            {
                                                Thread.sleep(15000);
                                            }
                                            catch (Exception ex) 
                                            {
                                                Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                   
                                            //System.out.println(NewErrtext);
                                            //System.out.println(dateNow);
                
                                            objVoiceTest = new VoiceTest();
                                            objVoiceTest.SpeakNOW(NewErrtext.trim().substring(0) 
                                                    + "\nThe summary is as follows:"
                                                    + "\nThe Sender of this mail is: " + sendername + ":"
                                                    + "\nThe Subject line is: " + subjectline + ":"
                                                    + "\nTotal number of errors are " + Errno + ":"
                                                    + "\nSorry for disturbing you:Bye.");
                
                                            objVoiceTest=null;
                                            System.exit(0);
                                        }
                                        else
                                        {
                                            System.out.println("");
                                            System.out.println("#--NOTE--#");
                                            System.out.println("Not calling, because user has specified not to between " + ts + " hrs - " + te + " hrs");
                                            System.out.println("");
                                            System.out.println("");
                                            System.out.println("");
                                        }
                                    }
                                    Errtext0 = null;
                                    Errtext = null;
                                    NewErrtext=null;
                                }
                                else
                                {                                                                        
                                    System.out.println("You have recieved some OTHER MAIL **CONTAINING THE LINES** you have have specified\n"
                                                    + "But *NOT* having the " + relseoutput + "\n"
                                                    + "sent by:- " +sendername.toUpperCase() + "\n"
                                                    + "having subject line:- " + subjectline.toUpperCase() + "\n" 
                                                    + "on date:- " + dateNow + "\n");
                            
                                    outputfile = new File("C:\\tmp\\Errorlog\\OtherMail\\om_"+date+".txt");
                                    outputtext  = new FileWriter(outputfile,true);
                                    outputtext.write("You have recieved some OTHER MAIL **CONTAINING THE LINES** you have have specified");
                                        outputtext.write(System.getProperty( "line.separator" ));
                                        outputtext.write("But*NOT* having the " + relseoutput);
                                        outputtext.write(System.getProperty( "line.separator" ));

                                    outputtext.write("Sent by:- " + sendername.toUpperCase()); 
                                        outputtext.write(System.getProperty( "line.separator" ));  
                                    outputtext.write("having subject line:- " + subjectline.toUpperCase()); 
                                        outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write("on date:- " + dateNow);
                                    
                                    //******************additional info***************************************      
                                    outputtext.write(System.getProperty( "line.separator" )); 
                                    outputtext.write(System.getProperty( "line.separator" ));  
                                    outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write("*************************************************************");
                                    outputtext.write(System.getProperty( "line.separator" )); 
                                    outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write("The mail filtering details are as follows:- ");   
                                    outputtext.write(System.getProperty( "line.separator" ));    
                                    if(rtakesendercontains.equals("not specified"))
                                        outputtext.write("Senders name is:- " + rtakesendercontains);
                                    else
                                        outputtext.write("Senders name is:- " + rtakesendercontains.toUpperCase());
                                    
                                    outputtext.write(System.getProperty( "line.separator" ));    
                            
                                    if(rtakesubjectcontains.equals("not specified"))
                                        outputtext.write("Subject is:- " + rtakesubjectcontains);
                                    else
                                        outputtext.write("Subject is:- " + rtakesubjectcontains.toUpperCase());
                                
                                    outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write(System.getProperty( "line.separator" )); 
                                    outputtext.write("*************************************************************");
                                    
                                    
                                    outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write(System.getProperty( "line.separator" )); 
                                    outputtext.write("The mail parsing details are as follows:- ");
                            
                                    outputtext.write(System.getProperty( "line.separator" ));    
                                    
                                    if(errstr==null)                                   
                                        outputtext.write("--%--User has not specified which line he'd like in mail");                                   
                                    else 
                                        outputtext.write("Mail line specified by user should contain the string:- \""+errstr.toUpperCase() + "\"");

                                    outputtext.write(System.getProperty( "line.separator" ));    
                
                                    if(errstart==null)
                                        outputtext.write("--%--User has not specified by what sequence the line in mail should begin");
                                    else 
                                        outputtext.write("Sequence by which mail line should begin is:- \""+errstart.toUpperCase() + "\"");

                                    outputtext.write(System.getProperty( "line.separator" ));    
                                    
                                    if(errstr==null)
                                        outputtext.write("--%--user has not specified by what sequence the line in mail should end");
                                    else 
                                        outputtext.write("Sequence by which mail line should end is:-  \""+errend.toUpperCase() + "\"");
                                    outputtext.write(System.getProperty( "line.separator" ));
                                    outputtext.write(System.getProperty( "line.separator" )); 
                                    outputtext.write("*************************************************************");
                                     //******************additional info***************************************                                      
                                                         
                                    errstatus=0;
                                    inputfile.delete();

                                    //System.exit(0);          
                                }
                            }
                        } 
                    }
                }
            }
        catch (Exception ex) 
        {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Exiting");
        //System.exit(0);
        return;
    }

    public void run() 
    {
        try 
        {
            if(useskype.equals("yes"))
            {  
                 if(skypeid.isEmpty())
                     System.out.println("You have not given a skype ID to call to");
                 else   
                 {
                    String sk=Skype.getAudioOutputDevice();  
                    System.out.println(""+sk);
                    //Skype.setAudioOutputDevice("Realtek HD Audio output");f
                    Skype.call(skypeid);
                    //Skype.sendSMS("+917411270191" ,NewErrtext);
                 }
                     
            }
            else
            {
                //Runtime.getRuntime().exec("VoiceWikiCall COM5 8600873804");
                Runtime.getRuntime().exec("C:\\tmp\\Beatle-VBM\\Bugs\\CL_05\\VBM_Call.exe "
                                            + portno + " " + phno);
            }
        }               
        catch (Exception e) 
        {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, e);
        }    
    } 
}


