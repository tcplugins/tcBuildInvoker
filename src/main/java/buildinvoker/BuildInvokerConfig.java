package buildinvoker;


import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

/*
 *  
    <buildInvoker buildToInvoke="bt1" invokeBuildButtonText="Run">
      <invokerBuild invokeFrom="bt3" displayOrder="1" enabled="true"/>
      <invokerBuild invokeFrom="bt2" displayOrder="6" enabled="true"/>
      <customParameter type="hidden" name="name1" value="value1" scope="env"/>
      <customParameter type="hidden" name="name2" value="value2" scope="env"/>
      <customParameter type="artifact" name="artifact1" scope="env"/>
      <customParameter type="artifact" name="artifact2" scope="system"/>
      <customParameter type="artifact" name="artifact3" scope="env"/>
      <customParameter type="option" name="deployTo" value="dev:uat:integration" scope="env"/>
    </buildInvoker>
 * 
 */


public class BuildInvokerConfig {
	private Boolean enabled = true;
	private String buildToInvoke;
	private String buildNameToInvoke = null;
	private String invokeBuildButtonText;
	private String uniqueKey = "";

	private List <InvokerBuild> orderedBuildCollection;
	private List <CustomParameter> orderedParameterCollection;
	
	
	@SuppressWarnings("unchecked")
	public BuildInvokerConfig (Element e){
		
		int Min = 1000000, Max = 1000000000;
		Integer Rand = Min + (int)(Math.random() * ((Max - Min) + 1));
		this.setUniqueKey(Rand.toString());

		orderedBuildCollection = new ArrayList<InvokerBuild>();
		orderedParameterCollection = new ArrayList<CustomParameter>();
		
		if (e.getAttribute("buildToInvoke") != null){
			this.buildToInvoke = e.getAttributeValue("buildToInvoke");
		}


		if (e.getAttribute("invokeBuildButtonText") != null){
			this.invokeBuildButtonText = e.getAttributeValue("invokeBuildButtonText");
		}
		
		/*
		  <customParameter type="hidden" name="name2" value="value2" scope="env"/>
	      <customParameter type="artifact" name="artifact1" scope="env"/>
	      <customParameter type="option" name="deployTo" value="dev:uat:integration" scope="env"/>
		*/
		
		List<Element> customParameterElementList = e.getChildren("customParameter");
		if (customParameterElementList.size() > 0){
			for(Element paramElement : customParameterElementList)
			{
				CustomParameter customParameter = 
						new CustomParameter(
								paramElement.getAttributeValue("type"),
								paramElement.getAttributeValue("name"),
								paramElement.getAttributeValue("value"),
								paramElement.getAttributeValue("scope"),
								this.uniqueKey
							);
				if (paramElement.getAttribute("filter") != null){
					customParameter.setFilter(paramElement.getAttributeValue("filter"));
				}
				if (paramElement.getAttribute("default") != null){
					customParameter.setDefaultValue(paramElement.getAttributeValue("default"));
				}
				if (paramElement.getAttribute("description") != null){
					customParameter.setDescription(paramElement.getAttributeValue("description"));
				}				
				if (paramElement.getAttribute("required") != null){
					customParameter.setRequired(Boolean.parseBoolean(paramElement.getAttributeValue("required")));
				}				
				this.orderedParameterCollection.add(customParameter);
			}
		}
		
		
		/* <invokerBuild invokeFrom="bt3" displayOrder="1" enabled="true"/> */
		List<Element> invokerBuildList = e.getChildren("invokerBuild");
		if (invokerBuildList.size() > 0){
			for(Element invoker : invokerBuildList)
			{
				this.orderedBuildCollection.add(
						new InvokerBuild(
								invoker.getAttributeValue("invokeFrom"),
								Integer.parseInt(invoker.getAttributeValue("displayOrder")),
								Boolean.parseBoolean(invoker.getAttributeValue("enabled"))
								)
						);
			}
		}
	}
	
	public Integer getRankForBuildTypeId(String btId){
		for (InvokerBuild ib : this.orderedBuildCollection){
			if (ib.getBuildTypeId().equals(btId)){
				return ib.displayOrder;
			}
		}
		return null;
	}
	
	public Element getAsElement(){
		Element el = new Element("buildInvoker");
		el.setAttribute("buildToInvoke", this.getBuildToInvoke());
		el.setAttribute("invokeBuildButtonText", this.getInvokeBuildButtonText());

		if (this.orderedBuildCollection.size() > 0){
			for (InvokerBuild build : this.orderedBuildCollection){
				el.addContent(build.getAsElement());
			}
		}
		if (this.orderedParameterCollection.size() > 0){
			for (CustomParameter param : this.orderedParameterCollection){
				el.addContent(param.getAsElement());
			}
		}
		return el;
	}

	// Getters and Setters..

	public Boolean isEnabled() {
		return enabled;
	}

	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getBuildToInvoke() {
		return this.buildToInvoke;
	}

	public String getBuildNameToInvoke() {
		return buildNameToInvoke;
	}

	public void setBuildNameToInvoke(String buildNameToInvoke) {
		this.buildNameToInvoke = buildNameToInvoke;
	}

	public String getInvokeBuildButtonText() {
		return invokeBuildButtonText;
	}

	public List<InvokerBuild> getOrderedBuildCollection() {
		return orderedBuildCollection;
	}

	public List<CustomParameter> getOrderedParameterCollection() {
		return orderedParameterCollection;
	}

	/**
	 * @param uniqueKey the uniqueKey to set
	 */
	private void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	/**
	 * @return the uniqueKey
	 */
	public String getUniqueKey() {
		return uniqueKey;
	}

}
