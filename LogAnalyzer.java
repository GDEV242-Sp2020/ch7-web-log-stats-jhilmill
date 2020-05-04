

import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.lang.Integer;


/**
 * Read web server data and analyse hourly access patterns.
 * 
 * @author David J. Barnes and Michael Kölling.
 * @version    2016.02.29
 */
public class LogAnalyzer
{
    // Where to calculate the hourly access counts.
    private int[] hourCounts;    
    
    // Where to calculate the hourly access counts.
    private int[] dayCounts;  
    
    // Where to calculate the monthly access counts.
    private int[] monthCounts;  
        
    // Use a LogfileReader to access the data.
    private LogfileReader reader;  
  
    /**
     * Create an object to analyze hourly web accesses. 
     * 7.12 - modified constructor to accept the filename
     */
    public LogAnalyzer(String filename)
    { 
        // Create the array object to hold the hourly access counts.
        hourCounts = new int[24];  
        // Create the array object to hold the daily access counts.
        dayCounts = new int[32];
        // Create the array object to hold the month access counts.
        monthCounts = new int[13];       
      
        // Create the reader to obtain the data.
        reader = new LogfileReader(filename);             
    }

    /**
     * Analyze the hourly access data from the log file.
     */
    private void analyzeHourlyData()
    {   	
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int hour = entry.getHour();
            hourCounts[hour]++;            
        }
    }
    /**
     * Find the beginning of the year
     */
    private int maxYr() {
    	this.reader.reset();
    	int maxYr = 0;
    	while (reader.hasNext()) {
    		LogEntry entry = reader.next();
    		int year = entry.getYear();    		
    		if (year > maxYr) 
    			maxYr = year;   		
      	} return maxYr;
    }
    
    /**
     * Find the total numbers of years in the log file     * 
     */
    private int numOfYrs() {
    	this.reader.reset();
    	Set <Integer> years = new HashSet<>();
    	int numYrs = 0;
    	while (reader.hasNext()) {
    		LogEntry entry = reader.next();
    		int year = entry.getYear();    		
    		years.add(year);
    		numYrs = years.size();
    	}		
    	return numYrs;	
    }
    
    /**
     * Analyze monthly data for given month and year
     * @return 
     */
    private  int alyzPerYrMonData(int findyr,int findmn)
    {   this.reader.reset();          
    	ArrayList <Integer> mnYr = new ArrayList<>();
        while(reader.hasNext()) {
            LogEntry entry = reader.next();            
            int year = entry.getYear();
            int month = entry.getMonth();
            if (year == findyr && month == findmn)
            	mnYr.add(month);
            } 
        	int mnCount = mnYr.size();        	     
        return mnCount;             
    }
    
    /**
     * Trend analysis of incidents by month by year
     * 7.19 - Trend analysis of monthly incidents over years
     */
    public void analyzeMonthYear() {    	
    	this.reader.reset();      	
    	int mnCount = 0;
    	int startingYr = this.maxYr() - (this.numOfYrs() -1);
    	int numYrs = this.numOfYrs() + startingYr - 1;    	
    	for (int i = startingYr; i <= numYrs; i++) {
    		for (int j = 1; j <= 12; j++) {
    			mnCount = this.alyzPerYrMonData(i,j);
    			System.out.println("Year: " + i + "  Month: "+ j + "  Count: "+ mnCount);    			
    		}
    	}    	
    }
    
    /**
     * Analyze hourly count for given hour and year
     * @return 
     */
    private  int alyzPerYrHrData(int findYr,int findHr)
    {   this.reader.reset();          
    	ArrayList <Integer> hrYr = new ArrayList<>();
        while(reader.hasNext()) {
            LogEntry entry = reader.next();            
            int year = entry.getYear();
            int hr = entry.getHour();
            if (year == findYr && hr == findHr)
            	hrYr.add(hr);
            } 
        	int hrCount = hrYr.size();        	     
        return hrCount;             
    }
    
    /**
     * Busiest hour by year
     * 7.15 - Which hour is the busiest over a year?
     * @return 
     */
	 public void busiestHrYr() {
		    this.reader.reset();   
			int startingYr = this.maxYr() - (this.numOfYrs() -1);
			int numYrs = this.numOfYrs() + startingYr - 1;    	
			for (int i = startingYr; i <= numYrs; i++) {
				int maxHrCnt = 0;
				int maxHr = 0;
				for (int j = 0; j <= 24; j++) {
					int hrCount = this.alyzPerYrHrData(i,j);
					if (hrCount > maxHrCnt) {
						maxHrCnt = hrCount;
						maxHr = j;
					}					
				}
				System.out.println("Year: " + i + " Busiest Hour: " + maxHr + " Max Hourly Count: "+ maxHrCnt);
			}    	
	}
 
	 /**
	     * Quietest hour by year
	     * 7.16 - Which hour is the quietest over a year?
	     * @return 
	     */
		 public void quietestHrYr() {
			    this.reader.reset();   
				int startingYr = this.maxYr() - (this.numOfYrs() -1);
				int numYrs = this.numOfYrs() + startingYr - 1;    	
				for (int i = startingYr; i <= numYrs; i++) {
					int minHrCnt = this.alyzPerYrHrData(i,0);
					int quiteHr = 0;
					for (int j = 1; j <= 24; j++) {
						int hrCount = this.alyzPerYrMonData(i,j);
						if (hrCount < minHrCnt) {
							minHrCnt = hrCount;
							quiteHr = j;
						}					
					}
					System.out.println("Year: " + i + " Quietest Hour: " + quiteHr +"  Min Hourly Count: "+ minHrCnt);
				}    	
		}
    
    /**
     * Analyze the daily access data from the log file
     */
    private void analyzedailyData()
    {   	
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int day = entry.getDay();
            dayCounts[day]++;            
        }
    }    
   
    /**
    *  Print the daily counts
    */
    public void printDailyCounts() {
    	analyzedailyData();    	
        System.out.println("Day: Count");      
        for(int day = 1; day < dayCounts.length; day++) {
            System.out.println(day + ": " + dayCounts[day]);
        }
    }
         
    /**
     * Print the hourly counts.
     * These should have been set with a prior
     * call to analyzeHourlyData.
     */
    public void printHourlyCounts()
    {	
    	analyzeHourlyData();
        System.out.println("Hr: Count");      
        for(int hour = 0; hour < hourCounts.length; hour++) {
            System.out.println(hour + ": " + hourCounts[hour]);
        }
    }
               
    /**
     * Return the busiest hour of the entire population of logfile
     */
    public int busiestHour() {
    	analyzeHourlyData();
    	int maxHr = 0;
    	int max = hourCounts[0];
    	for(int hour = 1; hour < hourCounts.length; hour++) {
            if (hourCounts[hour] > max) {
            	max = hourCounts[hour];
            	maxHr = hour;
            }
    	}
    	return maxHr;
    }
    
    /**
     * Return the busiest day of the entire population
     * 7.19 - busiest day
     */
    public int busiestDay() {
    	analyzedailyData();
    	int maxDay = 1;
    	int max = dayCounts[1];
    	for(int day = 2; day < dayCounts.length; day++) {
            if (dayCounts[day] > max) {
            	max = dayCounts[day];
            	maxDay = day;
            }
    	}
    	return maxDay;
    }    
    
    // add leading zero for single digit value
    private String addLeadingZero(int value) {
    	String valStr =Integer.toString(value); 	
    	if (value < 10) {
    		valStr = "0" + Integer.toString(value);
    	}
    	return valStr;
    }
    
    /**
     * Busiest two-hour periods, but return the first of these two hours
     * 7.18 - added two hours method 
     */
    public String twoHr() {
    	analyzeHourlyData(); 
    	String amPm = "AM";
    	int maxTwoHrFt = 0;
    	int maxTwoHr = hourCounts[0] + hourCounts[1];
    	for(int hour = 1; hour < hourCounts.length - 1; hour++) {
    		int tempMaxTwo = hourCounts[hour] +hourCounts[hour+1];
            if (tempMaxTwo  > maxTwoHr) {
            	maxTwoHr = tempMaxTwo;
            	maxTwoHrFt = hour;
            }   
    	} if (maxTwoHrFt > 11) {
    		maxTwoHrFt = maxTwoHrFt - 12;
    		amPm = "PM";
    	}
    	return addLeadingZero(maxTwoHrFt)+" "+ amPm;
    }              
    
    /**
     * Return the quietest day
     * 7.19 - added quietest day
     */
    public int quickestDay() {
    	this.analyzedailyData();;
    	int minDay = 1;  
    	int min = dayCounts[1];
    	for(int day = 2; day < dayCounts.length; day++) {    		
    		if (dayCounts[day] < min) {    			
    			min = dayCounts[day];
    			minDay = day;
    		}    	  
    	}
    	return minDay;	
    }        
    
    /**
     * Return the number of accesses recorded in the log file
     * 7.14 - added the numberOfAccess method 
     */
    public int numberOfAccesses() {
    	reader.reset();
    	int total = 0; 	
    	while(reader.hasNext()) { 
    		LogEntry entry = reader.next();
    		if (entry.getHour() >= 0) {
    			total++;
    		}   		
    	}
        return total;         
    }   
    
    /**
     * Analyze number of per month
     */
    private int anlyzMonthlyAccess (int month) {   
    	reader.reset();
    	ArrayList <Integer> mn = new ArrayList<>();
          while(reader.hasNext()) {
            LogEntry entry = reader.next();            
            int mon = entry.getMonth();       
            if (mon == month)
            	mn.add(mon);            
            } 
        int mnCount = mn.size();        	     
        return mnCount;   
    }
    /**
     * Return the number of accesses recorded per month in the log file
     * 7.19 - added the totalAccessesPerMonth method 
     */
    public void totalAccessesPerMonth() {
    	reader.reset();
    	for (int i = 0; i <= 23; i++) {
    		int mnCnt = this.anlyzMonthlyAccess(i);
    		System.out.println("Month: " + i + " Monthly Access: "+ mnCnt);
    	}            
    } 
            
     /**
     * Print the lines of data read by the LogfileReader
     */
    public void printData()
    {
        reader.printData();
    }    
    
}
