package buildinvoker;

import java.util.Comparator;

public class InvokerBuildRankingComparator implements Comparator<BuildInvokerConfig> {

	String buildTypeId;
	
	public InvokerBuildRankingComparator(String bt){
		this.buildTypeId = bt;
	}
	
	public int compare(BuildInvokerConfig o1, BuildInvokerConfig o2) {
		if (o1.getRankForBuildTypeId(buildTypeId) > o2.getRankForBuildTypeId(buildTypeId)){
			return 1;
		} else if (o1.getRankForBuildTypeId(buildTypeId) < o2.getRankForBuildTypeId(buildTypeId)){
			return -1;
		} else {
			return 0;
		}
	}
}
