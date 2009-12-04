package buildinvoker.beans;

import jetbrains.buildServer.serverSide.SBuildServer;

public class ProjectNameResolverBean {
	SBuildServer server;
	public ProjectNameResolverBean(SBuildServer server) {
		this.server = server;
	}

	public String resolveBuildName(String bt){
		return server.getProjectManager().findBuildTypeById(bt).getFullName();
	}
}
