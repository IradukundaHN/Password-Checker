

import java.util.ArrayList;

/**
 * PasswordCheckerUtility Class 
 *
 */
public class PasswordCheckerUtility {
	
	private static ArrayList<String> list;
	private static String password;
	
	/**
	 * 
	 * @param passwordString
	 * @return a boolean
	 * @throws LengthException
	 * @throws NoDigitException
	 * @throws NoUpperAlphaException
	 * @throws NoLowerAlphaException
	 * @throws InvalidSequenceException
	 */
	public static boolean isValidPassword(String passwordString) throws LengthException, NoDigitException, NoUpperAlphaException, NoLowerAlphaException, InvalidSequenceException
	{
		password = passwordString;
		char ch = password.charAt(0);
		
		if(password.length() < 6)
		{
			throw new LengthException ("The password must be at least 6 characters long");
		}
		
		if(!Character.isDigit(ch))
		{
			boolean hasDigit = false;
			
			for(int i=0; i<password.length(); i++)
			{
				ch = password.charAt(i);
				if(Character.isDigit(ch))
					hasDigit = true;
			}
			if(!hasDigit)
			{
				throw new NoDigitException("The password must contain at least one digit");
			}
		}	
				
		if(!Character.isUpperCase(ch))
		{
			boolean hasUpperCase = !password.equals(password.toLowerCase());
			
			if(!hasUpperCase)
			{
				throw new NoUpperAlphaException("The password must contain at least one uppercase alphabetic character");	
			}
		}
		
		if(!Character.isLowerCase(ch))
		{
			boolean hasLowerCase = !password.equals(password.toUpperCase());
			if(!hasLowerCase)
			{
				throw new NoLowerAlphaException("The password must contain at least one lowercase alphabetic character");	
			}
		}
		if(Character.isLowerCase(ch) || Character.isUpperCase(ch) || Character.isDigit(ch) )
		{
			for (int i = 0; i < password.length() - 2; i++)
			{

				if( (password.charAt(i) == password.charAt(i + 1)) && (password.charAt(i) == password.charAt(i+2)) )
	
				{
						throw new InvalidSequenceException ("The password cannot contain more than two of the same character in sequence.");
				}
			}
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param passwordString
	 * @return a boolean
	 * @throws LengthException
	 */
	public static boolean isWeakPassword(String passwordString) throws LengthException
	{
		password = passwordString;
		if((password.length() > 5)&&(password.length()<=10))
		{
			try
			{
				throw new WeakPasswordException("Password is Ok but WeaK");
			}
			catch(WeakPasswordException e)
			{
				e.printStackTrace();
			}
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param passwords
	 * @return list
	 */
	public static ArrayList<String> validPasswords(ArrayList<String> passwords)
	{
			list = new ArrayList<String>();
			String errorMessage = null;

			for(int i = 0; i < passwords.size(); i++)
			{
				try
				{
					isValidPassword(passwords.get(i));
				}
				catch (LengthException e)
				{
					errorMessage = passwords.get(i) + " The password must be at least 6 characters long.";
					list.add(errorMessage);
				}
				catch (NoDigitException e) {
	
					errorMessage = passwords.get(i) + " The password must contain at least one digit.";
					list.add(errorMessage);
				}
				catch (NoUpperAlphaException e) {
	
					errorMessage = passwords.get(i) + " The password must contain at least one uppercase alphabetic character.";
					list.add(errorMessage);
				}
				catch (NoLowerAlphaException e) {
					errorMessage = passwords.get(i) + " The password must contain at least one lowercase alphabetic character.";
					list.add(errorMessage);
				}
				catch (InvalidSequenceException e) {
					errorMessage = passwords.get(i) + " The password cannot contain more than two of the same character in sequence.";
					list.add(errorMessage);
				}
			}
			return list;
		}		
}