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
public class OnEntry
    implements OnEntryInterface
{
    
    /* (non-Javadoc)
     * @see org.synthful.automata.OnEntryInterface#getMessages()
     */
    public Vector getMessages()
    {
        return Messages;
    }
    
    /* (non-Javadoc)
     * @see org.synthful.automata.OnEntryInterface#addMessage(org.synthful.automata.Message)
     */
    public void addMessage(Message alert)
    {
        Messages.add(alert);
    }

    /* (non-Javadoc)
     * @see org.synthful.automata.OnEntryInterface#getDialogs()
     */
    public Vector getDialogs()
    {
        return Dialogs;
    }
    
    /* (non-Javadoc)
     * @see org.synthful.automata.OnEntryInterface#addDialog(org.synthful.automata.Dialog)
     */
    public void addDialog(Dialog dialog)
    {
        Dialogs.add(dialog);
    }

    /* (non-Javadoc)
     * @see org.synthful.automata.OnEntryInterface#setVerification(org.synthful.automata.VerificationInterface)
     */
    public void setVerification(VerificationInterface verification)
    {
        Verification = verification;
    }
    
    /* (non-Javadoc)
     * @see org.synthful.automata.OnEntryInterface#setVerificationValue(boolean)
     */
    public void setVerificationValue(boolean v)
    {
        VerificationValue = v;
    }
    
    /* (non-Javadoc)
     * @see org.synthful.automata.OnEntryInterface#getVerificationValue()
     */
    public boolean getVerificationValue()
    {
        return VerificationValue;
    }
    
    /* (non-Javadoc)
     * @see org.synthful.automata.OnEntryInterface#verify()
     */
    public boolean verify()
    {
        if (Verification==null)
            return true;
        
        return Verification.verify();
    }

    /* (non-Javadoc)
     * @see org.synthful.automata.OnEntryInterface#setElseState(org.synthful.automata.State)
     */
    public void setElseState(State state)
    {
        ElseState = state;
    }

    /* (non-Javadoc)
     * @see org.synthful.automata.OnEntryInterface#getElseState()
     */
    public State getElseState()
    {
        return ElseState;
    }

    /* (non-Javadoc)
     * @see org.synthful.automata.OnEntryInterface#setAction(org.synthful.automata.ActionInterface)
     */
    public void setAction(ActionInterface action)
    {
        Action = action;
    }

    /* (non-Javadoc)
     * @see org.synthful.automata.OnEntryInterface#invokeAction()
     */
    public boolean invokeAction()
    {
        if (Action!=null)
        {
            Action.invoke();
            return true;
        }
        return false;
    }
    
    /* (non-Javadoc)
     * @see org.synthful.automata.OnEntryInterface#setElseAction(org.synthful.automata.ActionInterface)
     */
    public void setElseAction(ActionInterface action)
    {
        ElseAction = action;
    }

    /* (non-Javadoc)
     * @see org.synthful.automata.OnEntryInterface#invokeElseAction()
     */
    public boolean invokeElseAction()
    {
        if (ElseAction!=null)
        {
            ElseAction.invoke();
            return true;
        }
        return false;
    }
        
    /** Variable Verification. */
    public VerificationInterface Verification;
    
    /** Variable VerificationValue. */
    public boolean VerificationValue = true;
    
    /** Variable Action. */
    public ActionInterface Action;
    
    /** Variable ElseAction. */
    public ActionInterface ElseAction;
    
    /** Variable ElseState. */
    public State ElseState;
    
    /** Variable Messages. */
    protected final Vector Messages = new Vector();
    
    /** Variable Dialogs. */
    protected final Vector Dialogs = new Vector();
}
