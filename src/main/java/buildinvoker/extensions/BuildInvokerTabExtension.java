package buildinvoker.extensions;



import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.auth.Permission;
import jetbrains.buildServer.serverSide.settings.ProjectSettingsManager;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.ViewLogTab;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.buildServer.web.util.SessionUser;

import org.jetbrains.annotations.NotNull;

import buildinvoker.BuildInvokerConfig;
import buildinvoker.settings.BuildInvokerProjectSettings;



public class BuildInvokerTabExtension extends ViewLogTab {
	BuildInvokerProjectSettings settings;
	ProjectSettingsManager projSettings;
	String myPluginPath;
	SBuildServer server;

	protected BuildInvokerTabExtension(
			PagePlaces pagePlaces, ProjectManager projectManager, 
			ProjectSettingsManager settings, WebControllerManager manager,
			PluginDescriptor pluginDescriptor, SBuildServer server){
		//super(myTitle, myTitle, null, projectManager);
		//projectManager.g
		//super("webHooks", "WebHooks", manager, projectManager);
		super("buildInvoker", "BuildInvoker", pagePlaces, server);
		this.projSettings = settings;
		this.server = server;
		myPluginPath = pluginDescriptor.getPluginResourcesPath();
		Loggers.SERVER.info("BuildInvokerTabExtension Constructor is being called");
	}

	public boolean isAvailable(@NotNull HttpServletRequest request) {
		Loggers.SERVER.info("isAvailable is being called");
		this.settings = 
			(BuildInvokerProjectSettings)this.projSettings.getSettings(this.server.findBuildInstanceById(Long.parseLong(request.getParameter("buildId"))).getProjectId(), "buildInvokers");
			
		if (this.server.findBuildInstanceById(Long.parseLong(request.getParameter("buildId"))).isFinished() 
			&& settings.findInvokersForBuildType(request.getParameter("buildTypeId")).size() > 0){
			this.setTabTitle(settings.getTabName());
			return true;
		} 
		return false;
	}

	
	@SuppressWarnings("unchecked")
	protected void fillModel(Map model, HttpServletRequest request,
			 @NotNull SBuildType buildType, SUser user) {
		Loggers.SERVER.info("BuildInvokerTabExtension Constructor is being called");
		this.settings = 
			(BuildInvokerProjectSettings)this.projSettings.getSettings(buildType.getProject().getProjectId(), "buildInvokers");
    	String message = this.settings.getBuildInovokersAsString();
    	model.put("webHookCount", this.settings.getBuildInvokersCount());
    	if (this.settings.getBuildInvokersCount() == 0){
    		model.put("noWebHooks", "true");
    		model.put("webHooks", "false");
    	} else {
    		model.put("noWebHooks", "false");
    		model.put("webHooks", "true");
    		model.put("webHookList", this.settings.getBuildsAsList());
    	}
    	model.put("messages", message);
    	model.put("messages2", "blasdflkdfl");
    	model.put("projectId", buildType.getProject().getProjectId());
    	model.put("projectName", buildType.getProject().getName());
	}

	@Override
	public String getIncludeUrl() {
		return myPluginPath + "buildTabBuildInvokers.jsp";
	}

	@Override
	protected void fillModel(Map model, HttpServletRequest request, SBuild sBuild) {
		this.settings = 
			(BuildInvokerProjectSettings)this.projSettings.getSettings(sBuild.getProjectId(), "buildInvokers");
    	String message = this.settings.getBuildInovokersAsString();
    	//this.setTabTitle(settings.getTabName());
    	
        SUser myUser = SessionUser.getUser(request);
        model.put("hasPermission", myUser.isPermissionGrantedForProject(sBuild.getProjectId(), Permission.RUN_BUILD));
        
        model.put("invokerBuildId", sBuild.getBuildId());
        model.put("invokerBuildTypeId", sBuild.getBuildTypeId());
        model.put("invokerBuildTypeName", sBuild.getBuildTypeName());
    	
        List<BuildInvokerConfig> invokers = settings.findInvokersForBuildType(sBuild.getBuildTypeId());
        for (BuildInvokerConfig invoker : invokers){
        	//if (server.getProjectManager().findBuildTypeById(invoker.getBuildToInvoke()).getFullName() != null){
        	//if (invoker.getBuildNameToInvoke() == null){
        	if (server.getProjectManager().findBuildTypeById(invoker.getBuildToInvoke()) != null){
        		invoker.setBuildNameToInvoke(server.getProjectManager().findBuildTypeById(invoker.getBuildToInvoke()).getFullName());
        		//invoker.setBuildNameToInvoke(invoker.getBuildToInvoke());
        	} else {
        		invoker.setEnabled(false);
        	}
        }
        model.put("invokers", invokers);
    	model.put("webHookCount", this.settings.getBuildInvokersCount());
    	if (this.settings.getBuildInvokersCount() == 0){
    		model.put("noWebHooks", "true");
    		model.put("webHooks", "false");
    	} else {
    		model.put("noWebHooks", "false");
    		model.put("webHooks", "true");
    		model.put("webHookList", this.settings.getBuildsAsList());
    	}
    	model.put("messages", message);
    	model.put("messages2", "blasdflkdfl");
    	model.put("projectId", sBuild.getProjectId());
    	//model.put("projectName", sBuild.getProjectId() getProject().getName());		
	}


	
}
