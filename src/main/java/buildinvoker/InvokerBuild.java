package buildinvoker;

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
}
