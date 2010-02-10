package com.blessedgeek.gwt.examples.greet;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HeapSort
{
    public int[] a;
    public int n;

    public void buildheap(int[] a0){
        a=a0;
        n=a.length;
        
        logger.info("n="+n+"\n"+prna(a).toString());
        for (int v=n/2-1; v>=0; v--)
            downheap (v);
    }

    public void sort(){      
        logger.info("n="+n+"\n"+prna(a).toString());
        while (n>1)
        {
            n--;
            exchange (a, 0, n);
            downheap (0);
        } 
    }

    public void downheap(int v){
        int w=2*v+1;    // first descendant of v
        while (w<n)
        {
        	System.out.println(
            	vwn(null,v,w,n)
            	.insert(0,"#####################################")
            	.toString());
            if (w+1<n)    // is there a second descendant?
                if (a[w+1]>a[w])
                	w++;
            // w is the descendant of v with maximum label
            System.out.println(vwn(null,v,w,n));

            if (a[v]>=a[w])
            	return;  // v has heap property
            // otherwise
            exchange(a, v, w);  // exchange labels of v and w
            //System.out.println(
            //	prna(v,w,n).insert(0,"2:\n").toString());
            v=w;        // continue
            w=2*v+1;
        }
    }

    static public void exchange(int[] a, int i, int j){
        int t=a[i];
        a[i]=a[j];
        a[j]=t;
    }
    
    private StringBuffer vwn(StringBuffer buf, int v, int w, int n){
    	if (buf==null)
    		buf = new StringBuffer();
    	return
	    	buf
				.append("\nv=").append(v)
				.append(",w=").append(w)
				.append(",n=").append(n)
			;
    }
    
    public StringBuffer prna(int v, int w, int n){
    	return
	    	prna(a)
	    		.append("v=").append(v)
	    		.append(",w=").append(w)
	    		.append(",n=").append(n)
    		;
    }
    
    static public StringBuffer prna(int[] a){
    	StringBuffer outbuf = new StringBuffer();
    	int j=0;
    	for(int i: a){
    		outbuf.append(i);
    		if (j++==16){
    			j=0;
    			outbuf.append('\n');
    		}
    		else
    			outbuf.append('\t');
    	}
		outbuf.append('\n');
    	
    	return outbuf;
    }
    
	final static private Logger logger = Logger.getLogger(HeapSort.class.getName());
}
