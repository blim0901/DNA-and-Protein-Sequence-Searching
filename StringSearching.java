import java.util.ArrayList;	
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;	


public class StringSearching {

    public static void main (String args []) throws IOException 
    {
    	Scanner sc = new Scanner(System.in);

    	System.out.print("Please enter the file name of the text to perform searching(e.g. genomic.fna): ");
    	String fileName = sc.nextLine();

    	if (fileName == ""){
    		System.out.println("Default file: genomic.fna");
    		fileName = "genomic.fna";
    		System.out.println();	
    	}
    	
    	String text = "";
    	
    	try{
	    	Path GfilePath = Paths.get(fileName);
	        text = Files.readString(GfilePath);
	    } catch (Exception e) {
	      	System.out.println("Invalid File. Default file genomic.fna is used for searching.");
	      	Path GfilePath = Paths.get("genomic.fna");
	        text = Files.readString(GfilePath);
	      	
	    }
    	
    	
        String pattern = "";	
        do{
        	System.out.print("Please enter the string pattern to search(e.g. ATGAATCC): ");
        	pattern = sc.nextLine();
        	System.out.println();	
        } while (pattern.length() == 0);
        boolean flag = false;
        int choice = 0;
        do {
		    System.out.println("========String Searching Algorithm===========");
		    System.out.println("1. Brute Force Algorithm");
		  	System.out.println("2. Improved Brute Force Algorithm");
		  	System.out.println("3. Knuth-Morris-Pratt Algorithm");
		  	System.out.println("4. Exit");
		  	System.out.print("Please choose the algorithm you want to implement(enter 1 or 2 or 3 or 4):");
		  	choice = sc.nextInt();

		  	long initialTime = 0;
	  		long finalTime = 0;
		  	List<Integer> matchedIndexes = new ArrayList<Integer>();
		  	System.out.println();
		  	switch(choice){
		  		case 1:
		  			System.out.println("========Brute Force===========");	       	        
				    initialTime = System.nanoTime();	        
				    matchedIndexes = bruteForceStringMatcher(text, pattern);	        
				    finalTime = System.nanoTime();	
				    break;
				case 2:

				    System.out.println("========improved Brute Force===========");	        
				    	        
				    initialTime = System.nanoTime();	//current time        
				    matchedIndexes = improvedBFStringMatcher(text, pattern);	  //apply the string search method 
				    															//and get the starting indices of matched segment      
				    finalTime = System.nanoTime();	   //the time immediately after the execution    
				    break;
				case 3:
					System.out.println("========KMP Approach===========");	        
				    	        
				    initialTime = System.nanoTime();       
				    matchedIndexes = KMPSearch(text, pattern);	        
				    finalTime = System.nanoTime();	 
				    
				    break;	

				case 4:
					flag = true;
					break;
				default:
					flag = true;
					break;
		  	}

		  	if (flag)
		  		break;

		  	for(Integer matchedIndex : matchedIndexes){	            
				System.out.println("Pattern found at "+matchedIndex);	//print the indices if any      
			}	

		  	if(matchedIndexes.size() == 0 && choice != 4)	            
				    	System.out.println("Pattern not found");
		    System.out.println("Time taken for matching "+(finalTime-initialTime) + " nanoseconds."); //print the time taken for matching
		    System.out.println();

	  	} while (choice != 4);

	  	       
	    System.out.println("Search ended. Thank you!");
    }	

    public static List<Integer> bruteForceStringMatcher(String text, String pattern){	
        char[] textArray = text.toCharArray();	        
        char[] patternArray = pattern.toCharArray();	
        int textLength = textArray.length;	        
        int patternLength = patternArray.length;	
        List<Integer> matchedIndexes = new ArrayList<>();//start indices of matched segments will be stored in this array
        int textIndex = 0;	

        for(textIndex = 0; textIndex < textLength; textIndex++){//controlling the start index of each segment	            
	        int textIndexLocal = textIndex;//generate a copy of the start index for the comparison in the inner loop	            
	        Boolean match = true;	            
	        int matchedIndex = textIndex;	            
	        for(int patternIndex = 0; patternIndex < patternLength; patternIndex++){//compare the segment with the pattern element by element	    
	        	if(textArray[textIndexLocal] != patternArray[patternIndex]) {//if index not matched	        		
	        		match = false;	       
	        		break;//exit the inner loop(stop comparing the following elements)	               
	        	}	                
	        	textIndexLocal = textIndexLocal + 1;//if this element matched,continue to compare the next element	           
	        }	            
	        if(match)              
	        	matchedIndexes.add(matchedIndex);//store the start index of this matched segment in the array	        
        }	        
        return matchedIndexes;	    
        }


    public static List<Integer> improvedBFStringMatcher(String text, String pattern){	
        char[] textArray = text.toCharArray();	//converting the string to char array   
        char[] patternArray = pattern.toCharArray();	
        int textLength = textArray.length;	    //get the length of each array    
        int patternLength = patternArray.length;	
        
        List<Integer> matchedIndexes = new ArrayList<>(); //create an empty list to store indices
        int textIndex = 0;	

        for(textIndex = 0; textIndex < textLength - patternLength + 1; textIndex++){	
        										//outer for loop to get each segment from text            
	        int textIndexLocal = textIndex;	            
	        Boolean match = true;	            
	        int matchedIndex = textIndex;
	        if (textArray[textIndex] != patternArray[0]) //compare the first char, if not match just skip
	        	continue;
	        if (textArray[textIndex + patternLength - 1] != patternArray[patternLength - 1]) //compare the last char
	        	continue;
	        if (patternLength == 2 || textArray[(2*textIndex + patternLength - 1)/2] != patternArray[(patternLength - 1)/2])
	        											//compare the mid char
	        	continue;
	        for(int patternIndex = 0; patternIndex < patternLength; patternIndex++){ //compare the last char	    
	        	if(textArray[textIndexLocal] != patternArray[patternIndex]) {	        		
	        		match = false;	       
	        		break;	               
	        	}	                
	        	textIndexLocal = textIndexLocal + 1;	           
	        }	            	            
	        
	        if(match)	                
	        	matchedIndexes.add(matchedIndex); //if all the chars match, get the index of the starting char	        
        }	        
        return matchedIndexes;	

        //javac improvedBRSearch.java;java improvedBRSearch genomic.fna ATGAATCC
    
     }

	public static List<Integer> KMPSearch(String gene, String query) 
    { 
        //compute length of gene and length
        int N = gene.length();  
        int M = query.length(); 
        
        //create lps[] array that will be the same length as query, that will store the longest prefix suffix values for query
        int lps[] = new int[M];  //initialize using M
        int j = 0;  //index for query, this pointer will start pointing at the first index of query
  
        //preprocess the pattern (by calculating the lps[] array)
        List<Integer> matchedIndexes = new ArrayList<>();
        buildLPSArray(query, M, lps); 
        
        //need to run through the entire gene input length N to find the query
        int i = 0;
        while (i < N) {
            //if the character of gene and query matches, then increment both pointers by 1
            if (gene.charAt(i) == query.charAt(j)) {
                i++;
                j++;
            }
            //if all characters have matched, j=M
            if (j == M) {
                //store the occurrence of the query
                //System.out.println("Found query " + "at index " + (i - j));
                //reset j to lps[j-1] to continue searching for other occurrences of the query
                matchedIndexes.add(i-j);
                j = lps[j-1];
            }
            
            //if there is a mismatch
            else if (i < N && query.charAt(j) != gene.charAt(i)) {
                //if the first character is not matched, cannot find lps[0-1], hence place j!=0 condition
                if (j!=0) {
                    //check the value of the lps table at (j-1) index, will get the length of the longest prefix that is also a suffix
                    j = lps[j-1]; //the index from 0 to j-1 have been a perfect match at this point
                }
                //for the case when j=0, simply have to increment i, while j remains the same
                else {
                    i = i + 1;
                }
            
            
            
         }
        }
        return matchedIndexes; 
        
    } 
    
    public static void buildLPSArray(String query, int m, int lps[]) 
    { 
        // length of the previous longest prefix that is also a suffix 
        int len = 0; 
        int i = 1; //i pointer to iterate through the query
        lps[0] = 0; // lps[0] is always 0 
        
        while (i < m) {
            if (query.charAt(i) == query.charAt(len)) {
                //increment lps[] count
                lps[i] = len +1;
                //compare next pair
                len++;
                i++;
                
            }
            
            else {
                if (len>0) {
                    len = lps[len-1];
                }
                
                else { //if (len == 0)
                    lps[i] = 0;
                    i++;
                }
            }
        }
    }
        //javac improvedBRSearch.java;java improvedBRSearch genomic.fna ATGAATCC
    
 }

