package buildinvoker;

import java.util.ArrayList;

import org.jdom.Element;

public class CustomParameter {
	/* <customParameter type="artifact" name="artifact1" scope="env" filter=".*\.tar\.gz$"/> */
	
	String type, name, value, scope, defaultValue, filter, description;
	Boolean required;
	String uniqueKey;
	ArrayList<Artifact> filteredArtifactList = null;
	
	public CustomParameter(String type, String name, String value, String scope, String uniqueKey){
		this.type  = type;
		this.name  = name;
		this.value = value;
		this.scope = scope;
		this.uniqueKey = uniqueKey;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}

	public String getScope() {
		return scope;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}

	public String getFilter() {
		return filter;
	}

	public String getDescription(){
		return description;
	}
	
	public void setDefaultValue(String defaultValue) {
		// We only offer defaults for the "option" type
		if (this.type.equals("option"))
			this.defaultValue = defaultValue;
	}

	public void setFilter(String filter) {
		// There is only "filter" support for the artifact type.
		if (this.getIsArtifact())
			this.filter = filter;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}	
	
	public ArrayList<Artifact> getFilteredArtifacts() {
		return filteredArtifactList;
	}

	public void setFilteredArtifacts(ArrayList<Artifact> filteredArtifactList) {
		this.filteredArtifactList = filteredArtifactList;
	}
	
	public int getFilteredArtifactsSize() {
		return this.filteredArtifactList.size();
	}

	public boolean getIsArtifact(){
		return this.type.equals("artifact");
	}
	
	public String getAsHtml(){
		StringBuffer s = new StringBuffer();
		if (this.type.equals("hidden")) {
			s.append("<input type=\"hidden\" name=\"" + this.scope + ".name\" value=\"" + this.name + "\" />");
			s.append("<input type=\"hidden\" name=\"" + this.scope + ".value\" value=\"" + this.value + "\" />");
			s.append("<tr><td class=\"hiddenParam\">" + this.scope + "." + this.name + "</td><td class=\"hiddenParam\">" + this.value + "</td></tr>");
		} else if (this.type.equals("option")){
			s.append("<input type=\"hidden\" name=\"" + this.scope + ".name\" value=\"" + this.name + "\" />");
			if (this.description != null){
				s.append("<tr><td>" + this.description + "</td>");
			} else {
				s.append("<tr><td>" + this.scope + "." + this.name + "</td>");
			}
			s.append("<td><select id=\"" + this.uniqueKey + this.name + "\" name=\"" + this.scope + ".value\"");
			if (this.required != null && this.required)
				s.append(" class=\"required\" ");
			s.append(">");
			s.append("<option value=\"\">Choose...</option>");
			for (String str : this.value.split(":")){
				if (this.defaultValue != null && this.defaultValue.equals(str)){
					s.append("<option value=\"" + str + "\" selected>" + str + "</option>");
				} else {
					s.append("<option value=\"" + str + "\">" + str + "</option>");
				}
			}
			s.append("</select></td></tr>");
		} else if (this.type.equals("artifact")){
		}
		return s.toString();
	}

	public String getArtifactStartAsHtml(){
		StringBuffer s = new StringBuffer();
			s.append("<input type=\"hidden\" name=\"" + this.scope + ".name\" value=\"" + this.name + "\" />");
			if (this.description != null){
				s.append("<tr><td>" + this.description + "</td>");
			} else {
				s.append("<tr><td>" + this.scope + "." + this.name + "</td>");
			}
			s.append("<td><select id=\"" + this.uniqueKey + this.name + "\" name=\"" + this.scope + ".value\"");
			if (this.required != null && this.required)
				s.append(" class=\"required\" ");
			s.append(">");
			s.append("<option value=\"\">Choose...</option>");
		return s.toString();
	}
	
	public String getArtifactEndAsHtml(){
		return("</select></td></tr>");
	}

	public String getNoArtifactsAsHtml(){
		return("<tr><td style='color:#aaa'>" + this.scope + "." + this.name + "</td><td style='color:#aaa'> no artifacts found</td></tr>");
		
	}
	
	public Element getAsElement(){
		Element el = new Element("customParameter");
		el.setAttribute("type", this.type);
		el.setAttribute("name", this.name);
		if (!this.getIsArtifact()){
			el.setAttribute("value", this.value);
		}
		el.setAttribute("scope", this.scope);
		if (this.filter != null){
			el.setAttribute("filter", this.filter);
		}
		if (this.defaultValue != null){
			el.setAttribute("default", this.defaultValue);
		}
		if (this.description != null){
			el.setAttribute("description", this.description);
		}
		if (this.required != null){
			el.setAttribute("required", this.required.toString());
		}

		
		return el;
	}
}
