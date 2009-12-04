package buildinvoker.settings;


import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.settings.ProjectSettingsFactory;
import jetbrains.buildServer.serverSide.settings.ProjectSettingsManager;

public class BuildInvokerProjectSettingsFactory implements ProjectSettingsFactory {
	
	public BuildInvokerProjectSettingsFactory(ProjectSettingsManager projectSettingsManager){
		Loggers.SERVER.info("BuildInvokerProjectSettingsFactory :: Registering");
		projectSettingsManager.registerSettingsFactory("buildInvokers", this);
	}

	public BuildInvokerProjectSettings createProjectSettings(String projectId) {
		Loggers.SERVER.info("BuildInvokerProjectSettingsFactory: re-reading settings for " + projectId);
		BuildInvokerProjectSettings bis = new BuildInvokerProjectSettings();
		return bis;
	}


}
