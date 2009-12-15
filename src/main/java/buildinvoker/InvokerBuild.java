package buildinvoker;

import org.jdom.Element;

public class InvokerBuild {
	/* <invokerBuild buildTypeId="bt2" displayOrder="2" enabled="true"/> */
	
	String buildTypeId;
	int displayOrder;
	Boolean enabled;
	
	public InvokerBuild(String attributeValue, int parseInt,
			boolean parseBoolean) {
		this.buildTypeId = attributeValue;
		this.displayOrder = parseInt;
		this.enabled = parseBoolean;
	}

	public String getBuildTypeId() {
		return buildTypeId;
	}
	
	public int getDisplayOrder() {
		return displayOrder;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public Element getAsElement(){
		Element el = new Element("invokerBuild");
		el.setAttribute("invokeFrom", this.buildTypeId);
		el.setAttribute("displayOrder", String.valueOf(this.displayOrder));
		el.setAttribute("enabled", String.valueOf(this.enabled));
		return el;
	}
}
