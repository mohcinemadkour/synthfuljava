package org.synthful.gdatastore;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class BasicData
{
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key id;
    
    static protected void setStringValue(String property, String value)
    {
        property
            = (value.length()>500)
            ? value.substring(0,499)
            : value;
    }
    
    static protected void setStringValue(String property, String[] values)
    {
        StringBuffer value = new StringBuffer();
        for(int i=0; i<values.length; i++)
        {
            value.append(values[i]);
            if (value.length()>500)
            {
                value.setLength(500);
                return;
            }
        }
            
    }
}
