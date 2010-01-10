package org.synthful.gwt.vaadin.ui.FieldValidators;

import com.vaadin.data.Validator;

public class PasswordValidator
	implements Validator
{

	private static final long serialVersionUID = 1L;
	private String message;
	
	public PasswordValidator(String message)
	{
		this.message = message;
		this.msgdetails = "";
	}

	public PasswordValidator(
		int minLength, int maxLength, int minNumbers,
		int minSpecials, int minUppers, int minLowers)
	{
		this.minNumbers=minNumbers;
		this.minSpecials=minSpecials;
		this.minUppers=minUppers;
		this.minLowers=minLowers;
		
		int n = minNumbers + minSpecials + minUppers + minLowers;
		
		this.minLength=minLength<n?n:minLength;
		this.maxLength=maxLength<this.minLength?this.minLength:maxLength;
		
		final String z = "at least ";
		this.msgdetails =
			(this.minLength>0?z + this.minLength + " characters\n<br/>":"")+
			(this.maxLength>0?"have most " + this.maxLength + " characters\n<br/>":"") +
			(this.minNumbers>0?z + this.minNumbers + " numbers\n<br/>":"")+
			(this.minSpecials>0?z + this.minSpecials +
				" special characters:("+this.specialchars+")\n<br/>":"")+
			(this.minUppers>0?z + this.minUppers + " upper case characters\n<br/>":"")+
			(this.minLowers>0?z + this.minLowers + " lower case characters\n<br/>":"");
	}

	@Override
	public boolean isValid(
		Object value)
	{
		if (value == null || !(value instanceof String))
		{
			return false;
		}
		
		boolean
			minLengthOK=true, maxLengthOK=true, minNumbersOK=true,
			minSpecialsOK=true, minUppersOK=true, minLowersOK=true;
		int numbers = 0, specials = 0, uppers = 0, lowers = 0;
		
		String s = (String)value;
		StringBuffer invalids = new StringBuffer(s.length()/2);
		
		minLengthOK = s.length() >= this.minLength;
		maxLengthOK = s.length() <= this.maxLength;
		
		for (int i=0; i<s.length();i++)
		{
			char c = s.charAt(i);
			if (Character.isDigit(c)) numbers++;
			else if (Character.isLowerCase(c)) lowers++;
			else if (Character.isUpperCase(c)) uppers++;
			else if (this.specialchars.indexOf(c)>=0) specials++;
			else invalids.append(c);
		}
		
		minNumbersOK = numbers >= this.minNumbers;
		minSpecialsOK = specials >= this.minSpecials;
		minLowersOK  = lowers >= this.minLowers ;
		minUppersOK  = uppers >= this.minUppers ;
		
		if ((!minLengthOK)|(!maxLengthOK)|(!minNumbersOK)|
			(!minSpecialsOK)|(!minUppersOK)|(!minLowersOK)| invalids.length()>0)
			return false;

		
		return true;
	}

	@Override
	public void validate(
		Object value)
		throws InvalidValueException
	{
		if (!isValid(value))
		{
			throw new InvalidValueException(getMessage());
		}
	}
	
	private String getMessage()
	{
		return this.message + this.msgdetails;
	}
	
	private int
		minLength=5, maxLength=20, minNumbers=0, minSpecials=0, minUppers=0, minLowers=0;
	
	private final String specialchars = "~`!@#$%^&*()_-+=|\\;:'\",<.>/?[{]} ";
	private final String msgdetails;
}
