package buildinvoker;

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
		}
		
		return s.toString();
	}
	
}
