package buildinvoker;

import java.util.Comparator;

public class ArtifactRankingComparator implements Comparator<Artifact> {

	public int compare(Artifact o1, Artifact o2) {
		return o1.FullName.compareToIgnoreCase(o2.FullName);
	}
}
