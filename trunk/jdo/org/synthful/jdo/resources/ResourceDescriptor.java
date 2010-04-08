package org.synthful.jdo.resources;

import java.io.Serializable;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.synthful.jdo.SynJDO;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ResourceDescriptor
extends SynJDO<ResourceDescriptor>
implements Serializable
{
	private static final long serialVersionUID = 1L;

	public ResourceDescriptor(){}

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;

	@Persistent
	private String name;

	@Persistent
	private String contentType;
	
	@Persistent
	private List<String> tags;

	
	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setContentType(
		String contentType)
	{
		this.contentType = contentType;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setTags(
		List<String> tags)
	{
		this.tags = tags;
	}

	public List<String> getTags()
	{
		return tags;
	}

	static public
		ResourceDescriptor exists(PersistenceManager pm, String name)
	{
		Query query = pm.newQuery(String.class);
		query.setFilter("name == nameParam");
		query.declareParameters("String nameParam");

		return  getFirst(query, name);
	}
}
