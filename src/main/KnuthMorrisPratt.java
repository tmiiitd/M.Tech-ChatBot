//mdm

package main;

//mdm


//this is for pattern matching, that is to check if a sub-pattern exists in the pattern


public class KnuthMorrisPratt
	{
	    // Failure array 
	    private int[] failure;
	   
	     
	    /** the function to find the pattern **/
	    /** this function returns the number of occurrances**/
	    public int find(String text, String pat)
	    {
	    	//System.out.println(pat);
	        /** pre construct failure array for a pattern **/
	        failure = new int[pat.length()];
	        fail(pat);
	        /** find match **/
	        
	        /*
	        int pos = posMatch(text, pat);
	        if (pos == -1)
	            this.result = false;
	        else{
	            this.result = true;
	        	System.out.println("\nMatch found at index "+ pos);
	        }*/
	        
	        int match = posMatch(text, pat);
	        return match;
	    }
	    
	    
	    /** Failure function for a pattern **/
	    private void fail(String pat)
	    {
	        int n = pat.length();
	        failure[0] = -1;
	        for (int j = 1; j < n; j++)
	        {
	            int i = failure[j - 1];
	            while ((pat.charAt(j) != pat.charAt(i + 1)) && i >= 0)
	                i = failure[i];
	            if (pat.charAt(j) == pat.charAt(i + 1))
	                failure[j] = i + 1;
	            else
	                failure[j] = -1;
	        }
	    }
	    
	    
	    /** Function to find match for a pattern **/
	    private int posMatch(String text, String pat)
	    {
	    	text=text+" ";
	        int count = 0;
	        int i = 0, j = 0;
	        int lens = text.length();
	        int lenp = pat.length();
	        while (i < lens)
	        {
	        	if(j==lenp){
	        		j=0;
	        		count=count+1;
	        	}
	        	
	            if (text.charAt(i) == pat.charAt(j))
	            {
	                i++;
	                j++;
	            }
	            else if (j == 0)
	                i++;
	            else
	                j = failure[j - 1] + 1;
	        }
	        
	        return count;
	        //return ((j == lenp) ? (i - lenp) : -1); //the index of last occurance
	    }
	   
	    
	    
	    
	    
	    
	    /*
	    
	   // public static void main(String[] args) throws IOException
	        
	      /*  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        System.out.println("Knuth Morris Pratt Test\n");
	        System.out.println("\nEnter Text\n");
	        String text = br.readLine();
	        System.out.println("\nEnter Pattern\n");
	        String pattern = br.readLine();
	        KnuthMorrisPratt kmp = new KnuthMorrisPratt(text, pattern);        
	    
	}*/
	    
	    
	    
	    
	}	