//--------------------------------------------
// Recognizer for simple Java Class grammar
//--------------------------------------------

/*	
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *  FRANCIS MINA
 *  CSC 135 Assignment 1   
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

import java.io.IOException;
import java.util.Scanner;

public class JClassParser {
   static String inputString;
   static int index = 0;
   static int errorflag = 0;
	 
   private char token()
   { 
      return(inputString.charAt(index)); }
	 
   private void advancePtr()
   { 
      if (index < (inputString.length()-1)) index++; }
	 
   private void match(char T)
   { 
      if (T == token()) advancePtr(); 
      else error(); }
	 
   private void error()
   {
      System.out.println("error at position: " + index);
      errorflag = 1;
      advancePtr();
   }
	//----------------------
   private void jClass()
   {
   
      className();
      if(token() == 'X')
      {
         match('X');
         className();
      }
      match('B');
      varlist();
      match(';');
      while(token() == 'P' || token() == 'V') 
      {
         method();
      }
      match('E');
   }
	// WRITE YOUR REST OF THE PARSER HERE
	 
   private void className()
   {
      if(token() == 'C')
         match('C');
      else if(token() == 'D')
         match('D');
      else
         error();
   }
	 
   private void varlist()
   {
      vardef();
      while(token() == ',')
      {
         match(',');
         vardef();
      }
   }
	 
   private void vardef()
   {
      if(token() == 'I' || token() =='S')
      {
         type();
         varname();
      }
      else if(token () == 'C'||token()=='D')
      {
         className();
         varref();
      }
      else
         error();
    
   }
	 
   private void type()
   {
      if(token() == 'I')
         match('I');
      else if(token() == 'S')
         match('S');
      else
         error();
   }
	 
   private void varname()
   {
      letter();
      while(token() == 'Y' || token() == 'Z' || token() == '0' || token() == '1' ||
      		token() == '2' || token() == '3')
      {
         mchar();
      }
   	
   }
	 
   private void letter()
   {
      if(token() == 'Y')
         match('Y');
      else if(token() == 'Z')
         match('Z');
      else
         error();
   }
	 
   private void mchar()
   {
      if(token() == 'Y' || token() == 'Z')
         letter();
      else if(token() == '0' || token() == '1' ||
      		token() == '2' || token() == '3')
         digit();
   }
	 
   private void digit()
   {
      if(token() == '0')
         match('0');
      else if(token() =='1')
         match('1');
      else if(token() =='2')
         match('2');
      else if(token() =='3')
         match('3');
      else
         error();
   }
	 
   private void integer() 
   {
      digit();
      while(token() == '0' || token() == '1' ||
      		token() == '2' || token() == '3')
         digit();
   	 
   }
	 
   private void varref()
   {
      if(token() == 'J')
         match('J');
      else if(token() =='K')
         match('K');
      else
         error();
   }
	 
   private void method() 
   {
      accessor();
      type();
      methodname();
      match('(');
      while(token() == 'I' || token() == 'S' || token() == 'C' || token() == 'D') //put first of varlist
         varlist();
      match(')');
      match('B');
      while(token() == 'F' || token() =='Y' || token() == 'Z' || token() == 'J' || token() == 'K' ||
      		 token()=='W')
         statemt();
      returnstatemt();
      match('E');
   }
	 
   private void accessor()
   {
      if(token() == 'P')
         match('P');
      else if(token() == 'V')
         match('V');
      else
         error();
   }
	 
   private void methodname()
   {
      if(token() == 'M')
         match('M');
      else if(token() == 'N')
         match('N');
      else
         error(); 
   }
	 
   private void statemt() 
   {
      if(token() == 'F')
         ifstatemt();
      else if(token() == 'Y' || token() == 'Z' || token() == 'J' || token() == 'K') //insert first of assignstatemt 
      {
         assignstatemt();
         match(';');
      }
      else if(token() == 'W')
         whilestatemt();
      else 
         error();
   }
	 
   private void ifstatemt() 
   {
      match('F');
      cond();
      match('T');
      match('B');
      while(token() == 'F' || token () == 'Y' || token() == 'J' || token() == 'K' 
      		 || token() == 'W' || token() =='Z') //insert first of statemt
         statemt();
      match('E');
      if(token() == 'L')
      {
         match('L');
         match('B');
         while(token() == 'F'||token()=='Y'||token()=='Z'||token()=='J'||token()=='K'
          || token() =='W')
            statemt();
         match('E');
      }
   	
   }
	 
   private void assignstatemt() 
   {
      if(token() =='Y'||token()=='Z')
      {
         varname();
         match('=');
         if(token() =='0' ||token()=='1'||token()=='2'||token()=='3' ||token()=='Y'||
         	 token()=='Z'||token()=='('||token()=='J'||token()=='K')
            mathexpr();
      }
      else
      {
         if(token()=='J' || token()=='K')
            varref();
         match('=');
         getvarref();
      	 
      }
   }
	 
   private void mathexpr() 
   {
      factor();
      while(token() == '+')
      {
         match('+');
         factor();
      }
   
   }
	 
   private void factor()
   {
      oprnd();
      while(token() == '*')
      {
         match('*');
         oprnd();
      }
   }
   private void oprnd() 
   {
      if(token() =='0' ||token()=='1'||token()=='2'||token()=='3')
         integer();
      else if(token() =='Y'||token()=='Z')
         varname();
      else if(token()=='(')
      {
      		
         match('(');
         mathexpr();
         match(')');
      }
      else if(token()=='J'||token()=='K')
         methodcall();
   }
	 
   private void getvarref() 
   {
      if(token()== 'O')
      {
         match('O');
         className();
         match('(');
         match(')');
      }
       
      else if(token()== 'J'||token()=='K')
         methodcall();
   }
	 
   private void whilestatemt() 
   {
      match('W');
      cond();
      match('T');
      match('B');
      if(token()=='F'||token()=='Y'||token()=='Z'||token()=='J'||token()=='K'||token()=='W')
         statemt();
      match('E');
   }
	 
   private void cond()
   {
      match('(');
      if(token() == '0' || token() == '1' || token() =='2' ||token() =='3'
      		 || token() =='Y' || token() == 'Z' || token() == '(' || token() == 'J'
      		 || token() =='K')
         oprnd();
      operator();
      oprnd();
      match(')');
   }
	 
   private void operator()
   {
      if(token() == '<')
         match('<');
      else if(token() == '=')
         match('=');
      else if(token() == '>')
         match('>');
      else if(token() == '!')
         match('!');
      else
         error();
   }
	 
   private void returnstatemt()
   {
      match('R');
      varname();
      match(';');
   }
	 
   private void methodcall() 
   {
      varref();
      match('.');
      methodname();
      match('(');
      if(token() == 'I' ||token()=='C'||token()=='S'||token()=='D')
         varlist();
      match(')');
   }
	 

	//----------------------
   private void start()
   {
      jClass();
      match('$');
      if (errorflag == 0)
         System.out.println("legal." + "\n");
      else
         System.out.println("errors found." + "\n");
   }
	//----------------------
   public static void main (String[] args) throws IOException
   {
      JClassParser rec = new JClassParser();
      @SuppressWarnings("resource")
         Scanner input = new Scanner(System.in);
      System.out.print("\n" + "enter an expression: ");
      inputString = input.nextLine();
   
      rec.start();
   }
}
