package buildinvoker;


import java.util.ArrayList;
import java.util.List;

import jetbrains.buildServer.serverSide.SBuildServer;

import org.jdom.Element;

/*
 *  <buildInvoker invokeBuildTypeId="bt1" invokeBuildButtonText="Run">
      <invokerBuild buildTypeId="bt3" displayOrder="1" enabled="true"/>
      <invokerBuild buildTypeId="bt2" displayOrder="2" enabled="true"/>
      <customParameter type="hidden" name="name1" value="value1" scope="env"/>
      <customParameter type="hidden" name="name2" value="value2" scope="env"/>
      <customParameter type="artifact" name="artifact1" scope="env"/>
    </buildInvoker>
 * 
 */


public class BuildInvokerConfig {
	private Boolean enabled = true;
	private String buildToInvoke;
	private String buildNameToInvoke = null;
	private String invokeBuildButtonText;

	private List <InvokerBuild> orderedBuildCollection;
	private List <CustomParameter> orderedParameterCollection;
	
	
	@SuppressWarnings("unchecked")
	public BuildInvokerConfig (Element e){

		orderedBuildCollection = new ArrayList<InvokerBuild>();
		orderedParameterCollection = new ArrayList<CustomParameter>();
		
		if (e.getAttribute("buildToInvoke") != null){
			this.buildToInvoke = e.getAttributeValue("buildToInvoke");
		}


		if (e.getAttribute("invokeBuildButtonText") != null){
			this.invokeBuildButtonText = e.getAttributeValue("invokeBuildButtonText");
		}
		
		List<Element> customParameterList = e.getChildren("customParameter");
		if (customParameterList.size() > 0){
			for(Element customParameter : customParameterList)
			{
				this.orderedParameterCollection.add(
						new CustomParameter(
								customParameter.getAttributeValue("type"),
								customParameter.getAttributeValue("name"),
								customParameter.getAttributeValue("value"),
								customParameter.getAttributeValue("scope")
								)
						);
			}
		}
		
		
		// <invokerBuild buildTypeId="bt2" displayOrder="2" enabled="true"/>
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
	
	/*
	private void populateBuildName(){
		this.buildNameToInvoke = server.getProjectManager().findBuildTypeById(this.buildToInvoke).getFullName();
	}*/

/*	
	private Element getKeyAndValueAsElement(String key, String elementName){
		Element e = new Element(elementName);
		if (this.extraParameters.containsKey(key)){
			e.setAttribute("name", key);
			e.setAttribute("value",this.extraParameters.get(key));
		}
		return e;
	}
	
	public Element getAsElement(){
		Element el = new Element("buildInvoker");
		el.setAttribute("myBuildTypeId", this.getMyBuildTypeId());
		el.setAttribute("enabled", String.valueOf(this.enabled));
		el.setAttribute("invokeBuildTypeId", this.getInvokeBuildTypeId());
		el.setAttribute("invokebuildTarget", this.getInvokebuildTarget());
		
		if (this.extraParameters.size() > 0){
			Element paramsEl = new Element("parameters");
			for (Iterator<String> i = this.extraParameters.values().iterator(); i.hasNext();){
				paramsEl.addContent(this.getKeyAndValueAsElement(i.next(), "param"));
			}
			el.addContent(paramsEl);
		}
		return el;
	}
	*/

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

}
