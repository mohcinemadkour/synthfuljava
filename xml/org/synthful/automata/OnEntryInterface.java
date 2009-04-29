/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2008
 * 
 */

package org.synthful.automata;

import java.util.Vector;


/**
 * @author Blessed Geek
 */
public interface OnEntryInterface
{
    
    /**
     * Gets the Messages.
     * 
     * @return the Messages as Vector
     */
    public Vector getMessages();
    
    /**
     * Adds the message.
     * 
     * @param message
     */
    public void addMessage(Message message);
    
    /**
     * Gets the Dialogs.
     * 
     * @return the Dialogs as Vector
     */
    public Vector getDialogs();
    
    /**
     * Adds the dialog.
     * 
     * @param dialog
     */
    public void addDialog(Dialog dialog);
    
    /**
     * Verify.
     * 
     * @return true, if Verify successful
     */
    public boolean verify();
    
    /**
     * Sets the verification.
     * 
     * @param verification
     *            the Verification
     */
    public void setVerification(VerificationInterface verification);
    
    /**
     * Sets the verification value.
     * 
     * @param value
     *            the VerificationValue
     */
    public void setVerificationValue(boolean value);
    
    /**
     * Gets the VerificationValue.
     * 
     * @return the VerificationValue as boolean
     */
    public boolean getVerificationValue();
    
    /**
     * Sets the else state.
     * 
     * @param state
     *            the ElseState
     */
    public void setElseState(State state);
    
    /**
     * Gets the ElseState.
     * 
     * @return the ElseState as State
     */
    public State getElseState();
    
    /**
     * Sets the else action.
     * 
     * @param action
     *            the ElseAction
     */
    public void setElseAction(ActionInterface action);
    
    /**
     * Invoke else action.
     * 
     * @return true, if Invoke else action successful
     */
    public boolean invokeElseAction();
    
    /**
     * Sets the action.
     * 
     * @param action
     *            the Action
     */
    public void setAction(ActionInterface action);
    
    /**
     * Invoke action.
     * 
     * @return true, if Invoke action successful
     */
    public boolean invokeAction();
}
