/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2008
 * 
 */

package org.synthful.automata;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.WeakHashMap;
import org.jdom.Element;
import org.synthful.xml.XmlParser;

/**
 * @author Blessed Geek
 */
public class State
{
    
    /**
     * Instantiates a new State.
     * 
     * @param id
     */
    public State(String id)
    {
        Id = id;
        isTransient = false;
    }
    
    /**
     * Instantiates a new State.
     * 
     * @param id
     * @param name
     * @param istransient
     */
    public State(String id, String name, boolean istransient)
    {
        Id = id;
        Name = name;
        isTransient = istransient;
    }
    
    /**
     * On entry.
     * 
     * @return On entry as State
     */
    public State onEntry()
    {
//        CurrentMessages.clear();
//        CurrentDialogs.clear();
        
        for(int j=0; j<OnEntryTasks.size(); j++)
        {
            OnEntryInterface enj = getOnEntryTask(j);
            if (enj != null)
            {
                CurrentMessages.addAll(enj.getMessages());
                CurrentDialogs.addAll(enj.getDialogs());
                
                try{
                  enj.invokeAction();
                }
                catch(Exception e){}

                if (!isTransient)
                  if (enj.verify()!=enj.getVerificationValue())
                  {
                      enj.invokeElseAction();
                      State elseState = enj.getElseState();
                      if (elseState!=null)
                      return elseState;
                  }
            }
        }
                
        return null;
    }
    
    /**
     * On transition.
     * 
     * @param transition
     * @return On transition as State
     */
    public State onTransition(Transition transition)
    {
        if (transition != null)
            return transition.onTransition();
        
        return null;
    }
    
    /**
     * On transition.
     * 
     * @param transitionName
     * @return On transition as State
     */
    public State onTransition(String transitionName)
    {
        Transition tj = getTransition(transitionName);
        return onTransition(tj);
    }
    
    /* On Entry Methods */    
    /**
     * Gets the OnEntryTask.
     * 
     * @param j
     * @return the OnEntryTask as OnEntryInterface
     */
    public OnEntryInterface getOnEntryTask(int j)
    {
        Object vj = OnEntryTasks.get(j);
        if (vj !=null && vj instanceof OnEntryInterface)
            return (OnEntryInterface)vj;
        return null;
    }
        
    /**
     * Adds the on entry task.
     * 
     * @param onEntryTask
     */
    public void addOnEntryTask(OnEntryInterface onEntryTask)
    {
        OnEntryTasks.add(onEntryTask);
    }
        
    /**
     * Gets the OnEntryTasks.
     * 
     * @return the OnEntryTasks as Vector
     */
    public Vector getOnEntryTasks()
    {
        return OnEntryTasks;
    }
    
   
    /* Transition Methods */
    /**
     * Gets the Transition.
     * 
     * @param transitionName
     * @return the Transition as Transition
     */
    public Transition getTransition(String transitionName)
    {
        Object tj = Transitions.get(transitionName);
        if (tj !=null && tj instanceof Transition)
            return (Transition)tj;
        return null;
    }
    
    /**
     * Gets the TransitionNames.
     * 
     * @return the TransitionNames as String[]
     */
    public String[] getTransitionNames()
    {
        Enumeration enu = Transitions.keys();
        String[] tnames = new String[Transitions.size()];
        int t = 0;
        while (enu.hasMoreElements())
            tnames[t++] = (String)enu.nextElement();
        
        return tnames;
    }
    
    /**
     * Gets the Transitions.
     * 
     * @return the Transitions as Hashtable
     */
    public Hashtable getTransitions()
    {
        return Transitions;
    }
    
    /**
     * Adds the transition.
     * 
     * @param transitionName
     * @param transition
     */
    public void addTransition(String transitionName, Transition transition)
    {
        Transitions.put(transitionName, transition);
    }
    
    /**
     * Adds the transient.
     * 
     * @param transientName
     * @param transition
     */
    public void addTransient(String transientName, Transition transition)
    {
        Transients.put(transientName, transition);
    }
    
    /**
     * Gets the CurrentMessages.
     * 
     * @return the CurrentMessages as Vector
     */
    public Vector getCurrentMessages()
    {
        return CurrentMessages;
    }
    
    /**
     * Gets the CurrentDialogs.
     * 
     * @return the CurrentDialogs as Vector
     */
    public Vector getCurrentDialogs()
    {
        return CurrentDialogs;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return
            "{Id=" + Id + ";" +
            "Name=" + Name + ";" +
            "Verified=" + Verified + "}"
            ;
    }
    
    /** Variable OnEntryTasks. */
    protected final Vector OnEntryTasks = new Vector();
    
    /** Variable Transitions. */
    protected final Hashtable Transitions = new Hashtable();

    /** Variable TransientTasks. */
    protected final Vector TransientTasks = new Vector();
    
    /** Variable Transients. */
    protected final Hashtable Transients = new Hashtable();
    
    /**
     * Current Messages & Dialogs are not read from fist xml but is accumulated
     * everytime a state is reached, because it is not known which transition or
     * onEntry would fire when parsing fist file.
     */
    protected final Vector CurrentMessages = new Vector();
    
    /** Variable CurrentDialogs. */
    protected final Vector CurrentDialogs = new Vector();
    
    /** The Constant OnEntryKeyWord. */
    public final static String OnEntryKeyWord = "onEntry";    
    
    /** The Constant OnTransitKeyWord. */
    public final static String OnTransitKeyWord = "transition";
    
    /** Variable Id. */
    public String Id;
    
    /** Variable Name. */
    public String Name;
    
    /** Variable Verified. */
    public boolean Verified;
    
    /** Variable isTransient. */
    public boolean isTransient;
}
