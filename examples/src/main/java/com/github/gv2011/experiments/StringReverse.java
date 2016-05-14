package com.github.gv2011.experiments;

public class StringReverse {

public static void main(final String[] args)throws Exception{
  final String test = "A𤽜nn𤽜a";
  System.out.println("Test string: "+test);
  System.out.println("Number of chars: "+test.length());
  System.out.println("Number of code points: "+test.codePointCount(0, test.length()));
  System.out.println("Reversed: "+reverse(test));
  System.out.println("Reversed with case correction: "+reverse(test, true));
  System.out.println("Is palindrome (v1): "+isPalindromeV1(test));
  System.out.println("Is palindrome (v2): "+isPalindromeV2(test));
  System.out.println("Is palindrome (v3): "+isPalindromeV3(test));
}

public static boolean isPalindromeV1(final String s){
  return s.equalsIgnoreCase(reverse(s));
}

public static boolean isPalindromeV2(final String s){
  return s.equals(reverse(s, true));
}

public static boolean isPalindromeV3(final String s){
  int indexLeft = 0;
  int indexRight = s.length();
  boolean isPalindrome = true;
  while(isPalindrome && indexRight>indexLeft){
    final int codepointLeft = s.codePointAt(indexLeft);
    final int codepointRight = s.codePointBefore(indexRight);
    if(!match(codepointLeft, codepointRight, indexLeft)) isPalindrome = false;
    else{
      indexLeft += Character.charCount(codepointLeft);
      indexRight -= Character.charCount(codepointRight);
    }
  }
  return isPalindrome;
}

private static boolean match(final int codepointLeft, final int codepointRight, final int indexLeft) {
  if(indexLeft!=0){
    return codepointLeft==codepointRight;
  }else{//Special handling for comparison of first with last codepoint.
    if(Character.isUpperCase(codepointLeft)) return codepointLeft==Character.toUpperCase(codepointRight);
    else return codepointLeft==codepointRight;
  }
}

public static String reverse(final String s){
  return reverse(s, false);
}

public static String reverse(final String s, final boolean correctCase){
  final StringBuilder result = new StringBuilder();
  final int length = s.length();
  int i = length;
  while(i>0){//Iterate backwards over string codepoints.
    final int cp = s.codePointBefore(i);
    if(correctCase && i==length){//Last codepoint is moved to start.
      if(Character.isUpperCase(s.codePointAt(0))) result.appendCodePoint(Character.toUpperCase(cp));
    }else if(correctCase && i==1){//First codepoint is moved to end.
      if(Character.isLowerCase(s.codePointBefore(length))) result.appendCodePoint(Character.toLowerCase(cp));
    }else{
      result.appendCodePoint(cp);
    }
    i -= Character.charCount(cp); //Decrement by two for surrogate pairs.
  }
  return result.toString();
}

}
