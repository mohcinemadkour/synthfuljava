package org.synthful.gwt.vaadin.ui;

import com.vaadin.data.Validator;

public class IntegerValidator
	implements Validator
{

	private static final long serialVersionUID = 1L;
	private String message;
	
	public IntegerValidator(String message)
	{
		this.message = message;
	}

	@Override
	public boolean isValid(
		Object value)
	{
		if (value == null || !(value instanceof String))
		{
			return false;
		}
		try
		{
			Integer.parseInt((String) value);
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}

	@Override
	public void validate(
		Object value)
		throws InvalidValueException
	{
		if (!isValid(value))
		{
			throw new InvalidValueException(message);
		}
	}
}
