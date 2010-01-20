package buildinvoker;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ArtifactFilterer {
	
	public static ArrayList<Artifact> filterArtifactList(ArrayList<Artifact> artifacts, String filter) {
		if (filter == null)
			return artifacts;
		
		ArrayList<Artifact> artList = new ArrayList<Artifact>();
		Pattern p = Pattern.compile(filter);
		for (Artifact art : artifacts){
			Matcher m = p.matcher(art.getFullName());
			if (m.find()){
				artList.add(art);
			}
		}
		return artList;
	}
}