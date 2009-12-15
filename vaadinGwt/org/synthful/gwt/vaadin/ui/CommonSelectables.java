package org.synthful.gwt.vaadin.ui;

@SuppressWarnings("serial")
public class CommonSelectables
implements SelectableItems
{
	public CommonSelectables(SelectableItem[] selectableItems)
	{
		this.selectableItems = (selectableItems==null)?intensity:selectableItems;
	}
	
	static final SelectableItem
		NONE = new SelectableItem("None"),
		SLIGHTLY = new SelectableItem("Slightly"),
		AVERAGE = new SelectableItem("Average"),
		EXTREMELY = new SelectableItem("Extremely"),
		TOTALLY = new SelectableItem("Totally");
	
	static final SelectableItem
		VERYBad = new SelectableItem("Very Bad"),
		BAD = new SelectableItem("Bad"),
		GOOD = new SelectableItem("Good"),
		VERYGood = new SelectableItem("Very Good");
	
	static final SelectableItem
		FEMALE = new SelectableItem("Female"),
		INTERSEX = new SelectableItem("Intersex"),
		MALE = new SelectableItem("Male")
		;
	
	static final SelectableItem
		ASEXUAL = new SelectableItem("Asexual"),
		BISEXUAL = new SelectableItem("Bisexual"),
		HETEROSEXUAL = new SelectableItem("Heterosexual"),
		HOMOSEXUAL = new SelectableItem("Homosexual")
		;
	
	static final public SelectableItem[] intensity = {
		NONE, SLIGHTLY, AVERAGE, EXTREMELY, TOTALLY
	};
	
	static final public SelectableItem[] goodness = {
		VERYBad, BAD, AVERAGE, GOOD, VERYGood
	};
	
	static final public SelectableItem[] gender = {
		FEMALE, INTERSEX, MALE
	};

	static final public SelectableItem[] sexOrientation = {
		ASEXUAL, BISEXUAL, HETEROSEXUAL, HOMOSEXUAL
	};
	
	static final SelectableItem
		RESIDENCE = new SelectableItem("Residence"),
		INN = new SelectableItem("Inn/Motel"),
		RV = new SelectableItem("RV"),
		SHED = new SelectableItem("Shed"),
		PARKING = new SelectableItem("Parking Space")
		;
	
	static final SelectableItem
		APARTMENT = new SelectableItem("Apartment"),
		CONDOMINIUM = new SelectableItem("Condominium"),
		DUPLEX = new SelectableItem("Duplex House"),
		MULTIPLEX = new SelectableItem("Multiplex House"),
		SINGLE = new SelectableItem("Single House")
		;

	static final public SelectableItem[] itemType = {
		RESIDENCE, INN, RV, SHED, PARKING
	};
	
	static final public SelectableItem[] unitType = {
		APARTMENT, CONDOMINIUM, DUPLEX, MULTIPLEX, SINGLE
	};
	
	


	private SelectableItem[] selectableItems;
	
	@Override
	public SelectableItem[] getSelectableItems()
	{
		return this.selectableItems;
	}

	@Override
	public SelectableItem getDefaultItem()
	{
		return NONE;
	}

	@Override
	public String getDefaultWidth()
	{
		return "10em";
	}
}
