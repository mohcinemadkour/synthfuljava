/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2008
 * 
 */

package org.synthful.automata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Hashtable;

/**
 * @author Blessed Geek
 */
public class PollingTurnPike
    extends StateTurnPike
  implements
    StateTransitionNames
{
    
    /**
     * Creates a new instance of PollingTurnPike.
     */
    public PollingTurnPike()
    {
        LOG.info(this + " instantiated");
    }
    
    /**
     * Poll.
     * 
     * @throws InterruptedException
     */
    public void Poll()
      throws
        InterruptedException
    {
        CurrentState = FistParser.InitState;
        RootState = FistParser.InitState;
        
        Hashtable sh = FistParser.States;
        if(sh!=null)
        {
          Object so = sh.get("IOERROR");
          if (so!=null && so instanceof State)
              IOErrorState = (State)so;
        }
        if (IOErrorState==null)
            IOErrorState = RootState;
        
        try{
            String maxDebounceMillis = 
                FistParser.Properties.getProperty("maxDebounceMillis", "15000");
            String debounceMillis = 
                FistParser.Properties.getProperty("debounceMillis", "0");
            String debounceCycles = 
                FistParser.Properties.getProperty("debounceCycles", "0");
            
            MaxDebounceMillis = Integer.parseInt(maxDebounceMillis);
            DebounceMillis = Integer.parseInt(debounceMillis);
            DebounceCycles = Integer.parseInt(debounceCycles);
        }
        catch(Exception e){}
        
        startOfPoll();
        PollCycle();
        endOfPoll();
    }
    
    /**
     * Poll cycle.
     * 
     * @throws InterruptedException
     */
    protected void PollCycle()
      throws
        InterruptedException
    {
        int activityCount = 0;
        while(isOpen())
        {
            updateCalendarWithCurrentTime();
            Datetime = Calendar.getTime();
            
            getData();
            
            // Update receipt with entry ticket
            // At this point, Receipt, Entry, Exit tickets should be in-sync.
            generateTransitionsFromData();
            issueReceiptOfEntry();
            activityCount = TransitionQueueHandler();            
            activityCount += onEntryVerification(5);
            endOfPollCycle(activityCount);
        }
    }

    /**
     * Transition queue handler.
     * 
     * @return Transition queue handler as int
     */
    protected int TransitionQueueHandler()
    {
        int transitionCount = 0;
        while(TransitionQueue.size()>0)
        {
            String transitionName = (String)TransitionQueue.remove(0);
            if (TransitionHandler(transitionName))
                transitionCount++;
        }
        
        return transitionCount;
    }
    
    /**
     * TurnPike.getCurrentState would never return null Would at least return
     * RootState. Except if RootState is null. RootState should be specified as
     * init state in fist xml.
     * 
     * @param transitionName
     * @return
     */
    protected boolean TransitionHandler(String transitionName)
    {
        if (transitionName==null || transitionName.length()==0)
            return false;
        
        State CurrentState = getCurrentState();
        Transition transition = CurrentState.getTransition(transitionName);

        return TransitionHandler(CurrentState, transition);
    }

    /**
     * Transition handler.
     * 
     * @param CurrentState
     * @param transition
     * @return true, if Transition handler successful
     */
    protected boolean TransitionHandler(
        State CurrentState, Transition transition)
    {
        if (transition==null) return false;
        
        cumulateMessages(transition.getMessages());
        setDialogs(transition.getDialogs());
        LOG.info("transition:"+transition);
        
        State nextState = null;
        
        try{
            nextState = CurrentState.onTransition(transition);
        }
        catch (Exception e)
        {
            LOG.info("transition:"+transition +'\n'+ e);
        }
        
        if (nextState==null)
            return false;
        
        if (nextState.isTransient)
        {
            nextState.onEntry();
            
            cumulateMessages(nextState.getCurrentMessages());
            nextState.getCurrentMessages().removeAllElements();
            
            setDialogs(nextState.getCurrentDialogs());
            nextState.getCurrentDialogs().removeAllElements();
        }
        
        else
            setCurrentState(nextState);
        
        /**
         * Deverify former state because if that state may be entered into
         * inadvertently without the TransitionHandler. In such a situation,
         * we would ensure a state is entered into unverified.
         * Also, deverify new state.
         * 
         * A state needs to be deverified, to ensure Verification routine is
         * run on that state first to ensure all onEntry items are verified.
         * Then Verification routine will set state to verified. Only the
         * Verification routine should be allowed to set state to verified.
         * Verification routine checks that a state is unverified before
         * running verification.
         * 
         * Even if a transient state has just been executed, the current
         * state needs to be deverified and reverified for effects due to the
         * transient.
         */
        CurrentState.Verified = false;
        CurrentState = getCurrentState();
        CurrentState.Verified = false;

        return true;
    }
    
    /**
     * On entry verification.
     * 
     * @param maxVerificationLoops
     * @return On entry verification as int
     */
    protected int onEntryVerification(int maxVerificationLoops)
    {
        int verificationLoops = 0, verifications = 0;
        while (verificationLoops++<maxVerificationLoops &&
            !CurrentState.Verified)
        {
            State currentState = getCurrentState();

            State errorState = null;
            try{
              errorState = currentState.onEntry();
            }
            catch (Exception e)
            {
              errorState = IOErrorState;
            }
            
            if (errorState!=null)
            {
                setCurrentState(errorState);
                CurrentState.Verified = false;
            }
            else
                CurrentState.Verified = true;
            
            verifications++;
        }
        
        return verifications;
    }


    /**
     * Start of poll.
     */
    protected void startOfPoll()
    {
        getData();
        issueReceiptOfEntry();
        setCurrentState(RootState);
        TransitionHandler(Startup);
        CurrentState.Verified = false;
        onEntryVerification(5);
    }

    /**
     * Gets the Data.
     * 
     * @return the Data as void
     */
    protected void getData()
    {
    }
    
    /**
     * End of poll cycle.
     * 
     * @param activityCount
     */
    protected void endOfPollCycle(int activityCount)
    {
        if (activityCount<=0) return;
        
        cumulateMessages(CurrentState.getCurrentMessages());
        CurrentState.getCurrentMessages().removeAllElements();
        
        setDialogs(CurrentState.getCurrentDialogs());
        CurrentState.getCurrentDialogs().removeAllElements();
        
    }

    /**
     * End of poll.
     */
    protected void endOfPoll()
    {
        TransitionHandler(Shutdown);
        onEntryVerification(3);
    }
    
    /**
     * Issue receipt of entry.
     */
    protected void issueReceiptOfEntry()
    {
        
    }
    
    /**
     * Generate transitions from data.
     */
    public void generateTransitionsFromData()
    {
        
    }

    private final static Log LOG = LogFactory.getLog(PollingTurnPike.class);
    
    /** Variable MaxDebounceMillis. */
    protected long MaxDebounceMillis = 20000;
    
    /** Variable DebounceMillis. */
    protected long DebounceMillis = 0;
    
    /** Variable DebounceCycles. */
    protected int DebounceCycles = 0;
}
