/************************************************************************************
 *
 *  		CSC220 Programming Project#2
 *  
 *
 * Specification: 
 *
 * Taken from Project 6, Chapter 5, Page 137
 * I have modified specification and requirements of this project
 *
 * Ref: http://www.gigamonkeys.com/book/        (see chap. 10)
 *      http://joeganley.com/code/jslisp.html   (GUI)
 *
 * In the language Lisp, each of the four basic arithmetic operators appears 
 * before an arbitrary number of operands, which are separated by spaces. 
 * The resulting expression is enclosed in parentheses. The operators behave 
 * as follows:
 *
 * (+ a b c ...) returns the sum of all the operands, and (+ a) returns a.
 *
 * (- a b c ...) returns a - b - c - ..., and (- a) returns -a. 
 *
 * (* a b c ...) returns the product of all the operands, and (* a) returns a.
 *
 * (/ a b c ...) returns a / b / c / ..., and (/ a) returns 1/a. 
 *
 * Note: + * - / must have at least one operand
 *
 * You can form larger arithmetic expressions by combining these basic 
 * expressions using a fully parenthesized prefix notation. 
 * For example, the following is a valid Lisp expression:
 *
 * 	(+ (- 6) (* 2 3 4) (/ (+ 3) (* 1) (- 2 3 1)) (+ 1))
 *
 * This expression is evaluated successively as follows:
 *
 *	(+ (- 6) (* 2 3 4) (/ 3 1 -2) (+ 1))
 *	(+ -6 24 -1.5 1)
 *	17.5
 *
 * Requirements:
 *
 * - Design and implement an algorithm that uses Java API stacks to evaluate a 
 *   valid Lisp expression composed of the four basic operators and integer values. 
 * - Valid tokens in an expression are '(',')','+','-','*','/',and positive integers (>=0)
 * - Display result as floting point number with at 2 decimal places
 * - Negative number is not a valid "input" operand, e.g. (+ -2 3) 
 *   However, you may create a negative number using parentheses, e.g. (+ (-2)3)
 * - There may be any number of blank spaces, >= 0, in between tokens
 *   Thus, the following expressions are valid:
 *   	(+   (-6)3)
 *   	(/(+20 30))
 *
 *************************************************************************************/


package PJ2;
import java.util.*;

public class LispExpressionEvaluator{
    // Current input Lisp expression
    private String currentExpr;

 // Main expression stack, see algorithm in evaluate()
    private LinkedStack<Object> tokensStack;
    private LinkedStack<Double> currentOpStack;



    // default constructor
    // set currentExpr to "" 
    // create LinkedStack objects
    public LispExpressionEvaluator(){
      currentExpr="";
      tokensStack = new LinkedStack<Object>();
      currentOpStack = new LinkedStack<Double>();
    }

    // constructor with an input expression 
    // set currentExpr to inputExpression 
    // create LinkedStack objects
    public LispExpressionEvaluator(String inputExpression){
	// add statements
	currentExpr=inputExpression;
	tokensStack = new LinkedStack<Object>();
	currentOpStack = new LinkedStack<Double>();
    }

    // set currentExpr to inputExpression 
    // clear stack objects
    public void reset(String inputExpression)
    {
    // add statements
        if(inputExpression == null)
        {
            throw new LispExpressionException();
        }
    currentExpr=inputExpression;
    tokensStack.clear();
    currentOpStack.clear();

    }


    // This function evaluates current operator with its operands
    // See complete algorithm in evaluate()
    //
    // Main Steps:
    // 		Pop operands from tokensStack and push them onto 
    // 			currentOpStack until you find an operator
    //  	Apply the operator to the operands on currentOpStack
    //          Push the result into tokensStack
    //
    
    private void evaluateCurrentOperation(){
    	double result=0;
        if(tokensStack.empty() ){
          throw new LispExpressionException("Error, the stack is empty");
        }
       Object oper = tokensStack.pop();
        
        while ( oper instanceof String ) {
          double value = Double.parseDouble(String.valueOf(oper));
          
          currentOpStack.push(value);

          if(tokensStack.empty() ){
            throw new LispExpressionException("Error1");
          }
          else{
        	  oper = tokensStack.pop();
          }
        }
        
        try{
        	String aToken = oper.toString() ;
        	char item = aToken.charAt(0);
	        switch (item) {
	          case '+':
	              if(currentOpStack==null || currentOpStack.empty()){
	            	  throw new LispExpressionException("error.add");
	              }
	              while (!currentOpStack.empty() ) {
	                result += currentOpStack.pop();
	              }
	              tokensStack.push(String.valueOf(result)); 
	              break;

	            case '-':
		              if(currentOpStack==null || currentOpStack.empty()){
		            	  throw new LispExpressionException("error.sub");
		              }
	              result = currentOpStack.pop();
	              if (currentOpStack.empty()) {
	                result = -result;
	                tokensStack.push(String.valueOf(result));
	              }
	              else {
	                while(!currentOpStack.empty()) {
	                  result -= currentOpStack.pop();
	                }
	                  tokensStack.push(String.valueOf(result));
	              }
	              break;


	            case '*':    
	              if(currentOpStack==null || currentOpStack.empty()){
	            	  throw new LispExpressionException("error.mult");
	              }
	              result = 1;
	              while ( !currentOpStack.empty() ) {
	                result *= currentOpStack.pop();
	              }
	              tokensStack.push(String.valueOf(result));

	              break;

	            case '/':
		          if(currentOpStack==null || currentOpStack.empty()){
		              throw new LispExpressionException("error.div");
		          }
	              result = currentOpStack.pop();

	              if (currentOpStack.empty()) {
	                result = 1/result;
	                tokensStack.push(String.valueOf(result));
	              }
	              else {
	                while(!currentOpStack.empty()) {
	                  result /=currentOpStack.pop();
	                }
	                  tokensStack.push(String.valueOf(result));
	              }
	              break;

	            case '(':
	            default:
	              throw new LispExpressionException(item + " is not a legal expression operator");

	        }
        }
        catch ( LispExpressionException e){
            throw new LispExpressionException( e.getMessage());
          }
        }

    


    /**
     * This funtion evaluates current Lisp expression in currentExpr
     * It return result of the expression 
     *
     * The algorithm:  
     *
     * Step 1   Scan the tokens in the string.
     * Step 2		If you see an operand, push operand object onto the tokensStack
     * Step 3  	    	If you see "(", next token should be an operator
     * Step 4  		If you see an operator, push operator object onto the tokensStack
     * Step 5		If you see ")"  // steps in evaluateCurrentOperation() :
     * Step 6			Pop operands and push them onto currentOpStack 
     * 					until you find an operator
     * Step 7			Apply the operator to the operands on currentOpStack
     * Step 8			Push the result into tokensStack
     * Step 9    If you run out of tokens, the value on the top of tokensStack is
     *           is the result of the expression.
     */
    public double evaluate(){
        Scanner currentExprScanner = new Scanner(currentExpr);
        currentExprScanner = currentExprScanner.useDelimiter("\\s*");
        while (currentExprScanner.hasNext()){
            if (currentExprScanner.hasNextInt()){
                String dataString = currentExprScanner.findInLine("\\d+");
                tokensStack.push(dataString); 
            }
            else{
              try{
                String aToken = currentExprScanner.next();
                char item = aToken.charAt(0);
                switch (item){
                  case '(':
                    aToken = currentExprScanner.next();
                    item = aToken.charAt(0);
                    switch (item){
                      case '+':
                    	  tokensStack.push(item);
                          break;
                      case '-':
                    	  tokensStack.push(item);
                          break;
                      case '*':
		            	  tokensStack.push(item);
		                  break;
                      case '/':
		                    tokensStack.push(item);
		                    break;
                      default:  
                        throw new LispExpressionException(item + " is not a legal expression operator");
                    }
                    break;
                  
                  case ')':
                		  evaluateCurrentOperation();
                    break;
                    
                  default:
                    throw new LispExpressionException(item + " is not a legal expression operator");
                } // end switch
              }//end try
              catch ( LispExpressionException e){
                throw new LispExpressionException( e.getMessage());
              }//end catch
            } // end else
        } // end while
        double result= Double.parseDouble(String.valueOf(tokensStack.pop()));
        if (!tokensStack.empty()){
        	throw new LispExpressionException ("This stack still has values, but there is no operand");
        }
        return result;
    }
    
    //=====================================================================
    // DO NOT MODIFY ANY STATEMENTS BELOW
    // Quick test is defined in main()
    //=====================================================================

    // This static method is used by main() only
    private static void evaluateExprTest(String s, LispExpressionEvaluator expr, String expect)
    {
        Double result;
        System.out.println("Expression " + s);
        System.out.printf("Expected result : %s\n", expect);
	expr.reset(s);
        try {
           result = expr.evaluate();
           System.out.printf("Evaluated result : %.2f\n", result);
        }
        catch (LispExpressionException e) {
            System.out.println("Evaluated result :"+e);
        }
        
        System.out.println("-----------------------------");
    }

    // define few test cases, exception may happen
    public static void main (String args[])
    {
        LispExpressionEvaluator expr= new LispExpressionEvaluator();
        //expr.setDebug();
        String test1 = "(+ (- 6) (* 2 3 4) (/ (+ 3) (* 1) (- 2 3 1)) (+ 1))";
        String test2 = "(+ (- 632) (* 21 3 4) (/ (+ 32) (* 1) (- 21 3 1)) (+ 0))";
        String test3 = "(+ (/ 2) (* 2) (/ (+ 1) (+ 1) (- 2 1 ))(* 1))";
        String test4 = "(+ (/2)(+ 1))";
        String test5 = "(+ (/2 3 0))";
        String test6 = "(+ (/ 2) (* 2) (/ (+ 1) (+ 3) (- 2 1 ))))";
        String test7 = "(+ (*))";
        String test8 = "(+ (- 6) (* 2 3 4) (/ (+ 3) (* 1) (- 2 3 1)) (+ ))";
	evaluateExprTest(test1, expr, "17.50");
	evaluateExprTest(test2, expr, "-378.12");
	evaluateExprTest(test3, expr, "4.50");
	evaluateExprTest(test4, expr, "1.5");
	evaluateExprTest(test5, expr, "Infinity or LispExpressionException");
	evaluateExprTest(test6, expr, "LispExpressionException");
	evaluateExprTest(test7, expr, "LispExpressionException");
	evaluateExprTest(test8, expr, "LispExpressionException");
    }
}
