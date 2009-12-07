package buildinvoker;

import org.jdom.Attribute;
import org.jdom.Element;

public class CustomParameter {
	/* <customParameter type="artifact" name="artifact1" scope="env"/> */
	
	String type, name, value, scope;
	
	public CustomParameter(String type, String name, String value, String scope){
		this.type = type;
		this.name = name;
		this.value = value;
		this.scope= scope;
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
	
	public boolean getIsArtifact(){
		return this.type.equals("artifact");
	}
	
	public String getAsHtml(){
		StringBuffer s = new StringBuffer();
		if (this.type.equals("hidden")) {
			s.append("<input type=\"hidden\" name=\"" + this.scope + ".name\" value=\"" + this.name + '"');
			s.append("<input type=\"hidden\" name=\"" + this.scope + ".value\" value=\"" + this.value + '"');
			s.append("<tr><td>" + this.scope + "." + this.name + "</td><td>" + this.value + "</td></tr>");
		} else if (this.type.equals("option")){
			s.append("<input type=\"hidden\" name=\"" + this.scope + ".name\" value=\"" + this.name + '"');
			s.append("<tr><td>" + this.scope + "." + this.name + "</td>");
			s.append("<td><select name=\"" + this.scope + ".value\">");
			s.append("<option value=\"\">Choose...</option>");
			for (String str : this.value.split(":")){
				s.append("<option value=\"" + str + "\">" + str + "</option>");
			}
			s.append("</select></td></tr>");
		} else if (this.type.equals("artifact")){
		}
		return s.toString();
	}

	public String getArtifactStartAsHtml(){
		StringBuffer s = new StringBuffer();
			s.append("<input type=\"hidden\" name=\"" + this.scope + ".name\" value=\"" + this.name + '"');
			s.append("<tr><td>" + this.scope + "." + this.name + "</td>");
			s.append("<td><select name=\"" + this.scope + ".value\">");
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
		return el;
	}
}
