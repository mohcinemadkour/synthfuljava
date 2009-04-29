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
public class Transition
{
    
    /**
     * Creates a new instance of Transition.
     * 
     * @param nextStateId
     */
    public Transition(String nextStateId)
    {
        NextStateId = nextStateId;
    }
    
    /**
     * On transition.
     * 
     * @return On transition as State
     */
    public State onTransition()
    {
        if (Action!=null)
            Action.invoke();
        return NextState;
    }
    
    /**
     * On transient.
     */
    public void onTransient()
    {
        if (Action!=null)
            Action.invoke();
    }

    /**
     * Gets the Action.
     * 
     * @return the Action as ActionInterface
     */
    public ActionInterface getAction()
    {
        return Action;
    }
    
    /**
     * Sets the action.
     * 
     * @param action
     *            the Action
     */
    public void setAction(ActionInterface action)
    {
        Action = action;
    }
    
    /**
     * Gets the Messages.
     * 
     * @return the Messages as Vector
     */
    public Vector getMessages()
    {
        return Messages;
    }
    
    /**
     * Adds the message.
     * 
     * @param message
     */
    public void addMessage(Message message)
    {
        Messages.add(message);
    }

    /**
     * Gets the Dialogs.
     * 
     * @return the Dialogs as Vector
     */
    public Vector getDialogs()
    {
        return Dialogs;
    }
    
    /**
     * Adds the dialog.
     * 
     * @param dialog
     */
    public void addDialog(Dialog dialog)
    {
        Dialogs.add(dialog);
    }

    /**
     * Gets the NextState.
     * 
     * @return the NextState as State
     */
    public State getNextState()
    {
        return NextState;
    }

    /**
     * Sets the next state.
     * 
     * @param NextState
     *            the NextState
     */
    public void setNextState(State NextState)
    {
        this.NextState = NextState;
    }

    /**
     * Gets the NextStateId.
     * 
     * @return the NextStateId as String
     */
    public String getNextStateId()
    {
        return NextStateId;
    }

    /**
     * Sets the next state id.
     * 
     * @param NextStateId
     *            the NextStateId
     */
    public void setNextStateId(String NextStateId)
    {
        this.NextStateId = NextStateId;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return
            "{NextStateId=" + NextStateId + ";" +
            "Action=" + ((Action==null)?"":Action.getClass().toString()) + ";" +
            "}"
            ;
    }

    /** Variable Messages. */
    protected final Vector Messages = new Vector();
    
    /** Variable Dialogs. */
    protected final Vector Dialogs = new Vector();

    /** Variable NextState. */
    public State NextState;
    
    /** Variable NextStateId. */
    public String NextStateId;
    
    /** Variable Action. */
    public ActionInterface Action;
}
