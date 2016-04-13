package PJ2;
import java.util.*;



public class PJ2_TEST
{
	
    public static void main (String args[])
    {
	// create a LispExpressionEvaluator object
        LispExpressionEvaluator expr= new LispExpressionEvaluator();

     	// scan input expr string
    	Scanner scanner;

	// current expr string and its result
	String inputExpr;
	double result;
        int i=0;

       	scanner = new Scanner( System.in ); // scanner for input
  
        do                                                                  
        {                                                                   
           try 
           {                                                                
              System.out.print( "\ninput an expression string:" );

	      // scan next input line
              inputExpr = scanner.nextLine();                            

	      if (inputExpr.equals("exit"))
		 break; // loop

              i++;
              System.out.println("Evaluate expression #"+ i+" :" + inputExpr);
              expr.reset(inputExpr);
              result = expr.evaluate();
              System.out.printf("Result : %.2f\n", result);

           } // end try                                                     
           catch ( LispExpressionException e )                
           {                                                                
              System.err.printf( "\nException: %s\n", e);
           } // end catch exception here, continue to next loop                                            

        } while ( true ); // end do...while                         
    } // end main






}

