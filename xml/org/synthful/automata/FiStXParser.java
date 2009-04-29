/*
 * Offered under Apache Public Licence 2.0
 * blessedgeek [@] synthful.org
 * www.synthful.org 2008
 * 
 */

package org.synthful.automata;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import org.jdom.Element;
import org.jdom.Text;
import org.synthful.automata.State;
import org.synthful.automata.Transition;
import org.synthful.xml.XmlParser;

import java.util.Properties;


/**
 * @author Blessed Geek
 */
public class FiStXParser
    extends XmlParser
{
    
    /**
     * Creates a new instance of FiStXParser.
     */
    public FiStXParser()
    {
        initHash(true);
    }
    
    /* (non-Javadoc)
     * @see org.synthful.xml.XmlParser#parse(java.lang.String, java.lang.String)
     */
    public void parse(String folder, String filename)
    {
        parse(new File(folder, filename));
    }
    
    /* (non-Javadoc)
     * @see org.synthful.xml.XmlParser#parse(java.lang.String)
     */
    public void parse(String filename)
    {
        parse(new File(filename));
    }

    /**
     * Digest.
     * 
     * @param BeanRegistry
     * @throws IOException
     */
    public void digest (FiStBeanRegistryInterface BeanRegistry)
    throws java.io.IOException
    {
        Actions = BeanRegistry.getActionHash();
        Verifications = BeanRegistry.getVerificationHash();
        Element root = getDocument().getRootElement();
        registerProperties(root);
        registerStates(root);
        reviewStates();
        registerInitState(root);
    }
    
    /**
     * Digest.
     * 
     * @throws IOException
     */
    public void digest ()
    throws java.io.IOException
    {
        Element root = getDocument().getRootElement();
        registerProperties(root);
        registerBeanContainers(root);
        registerActions(root);
        registerVerifications(root);
        registerStates(root);
        reviewStates();
        registerInitState(root);
    }
    
    /* (non-Javadoc)
     * @see org.synthful.xml.XmlParser#digest(org.jdom.Element)
     */
    public void digest (Element ei)
    throws java.io.IOException
    {}
    
    /**
     * Inits the hash.
     * 
     * @param reset
     */
    public void initHash(boolean reset)
    {
        if(States==null || reset)
            States = new Hashtable();
        
        if(Actions==null || reset)
            Actions = new Hashtable();
        
        if(Verifications==null || reset)
            Verifications = new Hashtable();
        
        if(Properties==null || reset)
            Properties = new Properties();
    }
    
    /**
     * Register properties.
     * 
     * @param ei
     */
    protected void registerProperties(Element ei)
    {
        String scheme = getElementAttributeValue(ei, "scheme");
        Properties.setProperty("scheme", scheme);
        
        Iterator iter = ei.getChildren("property").iterator();
        while (iter.hasNext())
        {
            Object oj = iter.next();
            if (oj!=null && oj instanceof Element)
            {
                Element ej = (Element)oj;
                registerProperty(ej);
            }
        }
    }
    
    /**
     * Register property.
     * 
     * @param ej
     */
    protected void registerProperty(Element ej)
    {
        String name = getElementAttributeValue(ej, "name");            
        String value = getElementAttributeValue(ej, "value");
        if (name == null) return;
        Properties.setProperty(name, value);
    }
    
    /**
     * Register init state.
     * 
     * @param ei
     */
    public void registerInitState(Element ei)
    {
        Element initStateTag = ei.getChild("init");
        
        if (initStateTag==null) return;
            
        String initstate = getElementAttributeValue(initStateTag, "state");
        String ioerror = getElementAttributeValue(initStateTag, "ioerror");
                                
        if (initstate!=null)
        {
          InitState = (State)States.get(initstate);
          if (States.get("INIT")==null && InitState!=null)
              States.put("INIT", InitState);
        }
        
        if (ioerror!=null)
        {
          State st = (State)States.get(ioerror);
          if (States.get("IOERROR")==null && st!=null)
              States.put("IOERROR", st);
        }
    }
    
    /**
     * Register bean containers.
     * 
     * @param ei
     */
    public void registerBeanContainers(Element ei)
    {
        Iterator iter = ei.getChildren("beanContainer").iterator();
        while (iter.hasNext())
        {
            Object oj = iter.next();
            if (oj!=null && oj instanceof Element)
            {
                Element ej = (Element)oj;
                registerBeanContainer(ej);
            }
        }
        
    }
    
    /**
     * Register bean container.
     * 
     * @param ej
     */
    protected void registerBeanContainer(Element ej)
    {
        String classname = getElementAttributeValue(ej, "class");
        String name = getElementAttributeValue(ej, "name");            

        if (classname == null || name == null) return;

        try{
            Class classclass = Class.forName(classname);
            Object classinst = classclass.newInstance();
            BeanContainers.put(name, classinst);
        }
        catch (ClassNotFoundException e)
        {
        }
        catch (InstantiationException e)
        {
        }
        catch (IllegalAccessException e)
        {
        }
    }
    
    /**
     * Register actions.
     * 
     * @param ei
     */
    public void registerActions(Element ei)
    {
        Iterator iter = ei.getChildren("action").iterator();
        while (iter.hasNext())
        {
            Object oj = iter.next();
            if (oj!=null && oj instanceof Element)
            {
                Element ej = (Element)oj;
                registerAction(ej);
            }
        }
        
    }
    
    /**
     * Register action.
     * 
     * @param ej
     */
    protected void registerAction(Element ej)
    {
        String name = getElementAttributeValue(ej, "name");            
        String containerName = getElementAttributeValue(ej, "beanContainer");
        String beanName = getElementAttributeValue(ej, "bean");
        if (containerName == null || beanName == null || name == null) return;

        Object actionObj = getBeanFromContainer(containerName, beanName);
        
        registerAction(name, actionObj);
    }
    
    /**
     * Register action.
     * 
     * @param name
     * @param actionObj
     */
    protected void registerAction(String name, Object actionObj)
    {
        if (actionObj!=null && actionObj instanceof ActionInterface)
            Actions.put(name, actionObj);
    }

    /**
     * Register verifications.
     * 
     * @param ei
     */
    public void registerVerifications(Element ei)
    {
        Iterator iter = ei.getChildren("verification").iterator();
        while (iter.hasNext())
        {
            Object oj = iter.next();
            if (oj!=null && oj instanceof Element)
            {
                Element ej = (Element)oj;
                registerVerification(ej);
            }
        }
        
    }
    
    /**
     * Register verification.
     * 
     * @param ej
     */
    protected void registerVerification(Element ej)
    {
        String name = getElementAttributeValue(ej, "name");            
        String containerName = getElementAttributeValue(ej, "beanContainer");
        String beanName = getElementAttributeValue(ej, "bean");
        if (containerName == null || beanName == null || name == null) return;

        Object actionObj = getBeanFromContainer(containerName, beanName);
        
        if (actionObj!=null && actionObj instanceof VerificationInterface)
            Verifications.put(name, actionObj);
    }

    
    /**
     * Register states.
     * 
     * @param ei
     */
    public void registerStates(Element ei)
    {
        Iterator iter = ei.getChildren("state").iterator();
        while (iter.hasNext())
        {
            Object oj = iter.next();
            registerState((Element)oj);
        }
    }
    
    /**
     * Register state.
     * 
     * @param ej
     */
    protected void registerState(Element ej)
    {
        String id = getElementAttributeValue(ej, "id");
        String name = getElementAttributeValue(ej, "name");
        String isTransient = getElementAttributeValue(ej, "transient");            
        
        if (id == null) return;
        
        State state =
          new State(
            id, name,
            Boolean.parseBoolean(isTransient)
          );
        States.put(id, state);
        
        registerStateOnEntryTasks(ej);
        registerStateTransitions(ej);
    }
    
    /**
     * Register state on entry tasks.
     * 
     * @param pj
     */
    protected void registerStateOnEntryTasks(Element pj)
    {
        Iterator iter = pj.getChildren("onEntry").iterator();
        while (iter.hasNext())
        {
            Object oj = iter.next();
            registerStateOnEntryTask(pj, (Element)oj);
        }
    }
    
    /**
     * Register state on entry task.
     * 
     * @param pj
     * @param ej
     */
    protected void registerStateOnEntryTask(Element pj, Element ej)
    {
        if (!pj.getName().equalsIgnoreCase("state") )
            return;
        String verificationName = getElementAttributeValue(ej, "verification");
        String ActionName = getElementAttributeValue(ej, "action");
        String elseActionName = getElementAttributeValue(ej, "elseAction");
        String elseStateId = getElementAttributeValue(ej, "elseState");

        String pjid = getElementAttributeValue(pj, "id");
        if (pjid == null) return;

        State state = (State)States.get(pjid);

        if (state!=null)
        {
            OnEntryInterface onEntry = new OnEntry();
            state.addOnEntryTask(onEntry);
            
            if (verificationName!=null)
            {
                int a = -1, b = 0;
                while(verificationName.startsWith("!", ++a));
                b = a/2;
                if (a>b*2)
                {
                    verificationName = verificationName.substring(a);
                    onEntry.setVerificationValue(false);
                }
                else
                    onEntry.setVerificationValue(true);
                
                Object ovj = Verifications.get(verificationName);
                
                if (ovj!=null && ovj instanceof VerificationInterface)
                     onEntry.setVerification((VerificationInterface) ovj);
                else
                    onEntry.setVerificationValue(true);
            }

            if (ActionName!=null)
            {
                Object oej = Actions.get(ActionName);
                if (oej!=null && oej instanceof ActionInterface)
                    onEntry.setAction((ActionInterface)oej);
            }
            
            if (elseActionName!=null)
            {
                Object oej = Actions.get(elseActionName);
                if (oej!=null && oej instanceof ActionInterface)
                    onEntry.setElseAction((ActionInterface)oej);
            }
            
            // Set dummy state to be replaced when all states have been registered.
            // Verification is embedded within state, so there are states down the
            // state transition xml that have yet to be registered.
            if (elseStateId!=null)
            {
                State dummy = new State(elseStateId);
                onEntry.setElseState(dummy);
            }
            
            Message.digestMessage(ej, onEntry.getMessages());
            Dialog.digestDialog(ej, onEntry.getDialogs());
        }
    }
    
    /**
     * Register state transitions.
     * 
     * @param pj
     */
    protected void registerStateTransitions(Element pj)
    {
        Iterator iter = pj.getChildren("transition").iterator();
        while (iter.hasNext())
        {
            Object oj = iter.next();
            registerStateTransition(pj, (Element)oj);
        }
    }

    /**
     * Register state transition.
     * 
     * @param pj
     * @param ej
     */
    protected void registerStateTransition(Element pj, Element ej)
    {
        if (!pj.getName().equalsIgnoreCase("state"))
            return;
        
        String transitionName = getElementAttributeValue(ej, "name");            
        String actionName = getElementAttributeValue(ej, "action");            
        String nextStateId = getElementAttributeValue(ej, "nextState");
        String pjid = getElementAttributeValue(pj, "id");
        if (transitionName == null || pjid == null) return;
                
        Object opj = States.get(pjid);

        if (opj instanceof State)
        {
            State state = (State)opj;

            // If nextState not defined defaults to same state
            // NextStateId of a transition must not be null and be ID of
            // existent state for transition handler and verification handler
            // to act on a transistion.
            if (nextStateId == null)
              nextStateId = state.Id;

            Transition tj =
              new Transition(nextStateId);
            
            state.addTransition(transitionName, tj );
            
            Message.digestMessage(ej, tj.getMessages());
            Dialog.digestDialog(ej, tj.getDialogs());

            if (actionName!=null)
            {
                Object oej = Actions.get(actionName);

                AttachActionToStateTransition(tj, oej);
            }
        }
        
    }
    


    /**
     * Attach action to state transition.
     * 
     * @param tj
     * @param actionObj
     */
    protected void AttachActionToStateTransition(Transition tj, Object actionObj)
    {
        if (actionObj!=null && actionObj instanceof ActionInterface)
            tj.setAction((ActionInterface)actionObj);
    }
    
    
    /**
     * Since the states may refer to yet to be defined nextState, verification
     * actions and transition states have to be resolved by a second/review
     * sweep.
     */
    public void reviewStates()
    {        
        Enumeration statesEnum = States.keys();
        while(statesEnum.hasMoreElements())
        {
            String stateid = (String)statesEnum.nextElement();
            State state = (State)States.get(stateid);
            reviewStateOnEntryTasks(state);
            reviewStateTransitions(state);
        }
    }
    
    /**
     * Review state on entry tasks.
     * 
     * @param state
     */
    protected void reviewStateOnEntryTasks(State state)
    {
        Vector ve = state.getOnEntryTasks();
        
        for (int j=0; j<ve.size();j++)
        {
            OnEntryInterface enj = state.getOnEntryTask(j);
            if (enj==null) continue;

            if (enj.getElseState()==null || enj.getElseState().Id==null)
                continue;

            // Replace dummy state with one found when all states have been registered.
            State sj = (State)States.get(enj.getElseState().Id);
            if (sj!=null)
                enj.setElseState(sj);
        }
     }
    
    /**
     * Review state transitions.
     * 
     * @param state
     */
    protected void reviewStateTransitions(State state)
    {
        String[] tnames = state.getTransitionNames();
        
        for (int j=0; j<tnames.length;j++)
        {
            Transition tj = state.getTransition(tnames[j]);
            if (tj==null||tj.getNextStateId()==null)
                continue;

            State sj = (State)States.get(tj.getNextStateId());
            if (sj!=null)
                tj.NextState = sj;
        }
    }
    
    /**
     * Gets the BeanFromContainer.
     * 
     * @param containerName
     * @param beanName
     * @return the BeanFromContainer as Object
     */
    protected Object getBeanFromContainer(String containerName, String beanName)
    {
        Object container = BeanContainers.get(containerName);
        if (container==null) return null;
        Field beanField = null;
        
        try
        {
            beanField = container.getClass().getField(beanName);
        }
        catch (SecurityException ex)
        {}
        catch (NoSuchFieldException ex)
        {}
        
        if (beanField==null) return null;
        
        Object bean;
        try
        {
            bean = beanField.get(container);
            return bean;
        }
        catch (IllegalArgumentException ex)
        {}
        catch (IllegalAccessException ex)
        {}
        
        return null;
    }
    
    /** Variable InitState. */
    public State InitState;
    
    /** Variable States. */
    public Hashtable States;
    
    /** Variable Actions. */
    public Hashtable Actions;
    
    /** Variable Verifications. */
    public Hashtable Verifications;
    
    /** Variable BeanContainers. */
    public Hashtable BeanContainers;
    
    /** Variable Properties. */
    public Properties Properties;
}
