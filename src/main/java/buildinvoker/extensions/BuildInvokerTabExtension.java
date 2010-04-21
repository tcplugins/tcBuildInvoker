package buildinvoker.extensions;



import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.ProjectManager;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.auth.Permission;
import jetbrains.buildServer.serverSide.settings.ProjectSettingsManager;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.web.openapi.PagePlaces;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import jetbrains.buildServer.web.openapi.ViewLogTab;
import jetbrains.buildServer.web.openapi.WebControllerManager;
import jetbrains.buildServer.web.util.SessionUser;

import org.jetbrains.annotations.NotNull;

import buildinvoker.Artifact;
import buildinvoker.ArtifactListBuilder;
import buildinvoker.BuildInvokerConfig;
import buildinvoker.CustomParameter;
import buildinvoker.settings.BuildInvokerProjectSettings;
import static buildinvoker.ArtifactFilterer.filterArtifactList;;


public class BuildInvokerTabExtension extends ViewLogTab {
	BuildInvokerProjectSettings settings;
	ProjectSettingsManager projSettings;
	String myPluginPath;
	SBuildServer server;

	protected BuildInvokerTabExtension(
			PagePlaces pagePlaces, ProjectManager projectManager, 
			ProjectSettingsManager settings, WebControllerManager manager,
			PluginDescriptor pluginDescriptor, SBuildServer server){

		super("buildInvoker", "BuildInvoker", pagePlaces, server);
		this.projSettings = settings;
		this.server = server;
		myPluginPath = pluginDescriptor.getPluginResourcesPath();
		Loggers.SERVER.debug("BuildInvokerTabExtension Constructor is being called");
	}

	public boolean isAvailable(@NotNull HttpServletRequest request) {
		try{
			this.settings = 
				(BuildInvokerProjectSettings)this.projSettings.getSettings(this.server.findBuildInstanceById(Long.parseLong(request.getParameter("buildId"))).getProjectId(), "buildInvokers");
				
			if (this.server.findBuildInstanceById(Long.parseLong(request.getParameter("buildId"))).isFinished() 
				&& settings.findInvokersForBuildType(request.getParameter("buildTypeId")).size() > 0){
				this.setTabTitle(settings.getTabName());
				return true;
			} 
		} catch (NumberFormatException nfe){
			return false;
		}
		return false;
	}

	@Override
	public String getIncludeUrl() {
		return myPluginPath + "buildTabBuildInvokers.jsp";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void fillModel(Map model, HttpServletRequest request, SBuild sBuild) {
		this.settings = 
			(BuildInvokerProjectSettings)this.projSettings.getSettings(sBuild.getProjectId(), "buildInvokers");
    	String message = this.settings.getBuildInovokersAsString();
    	model.put("jspHome",myPluginPath);
    	
    	ArrayList<Artifact> artifacts = new ArrayList<Artifact>();
    	
    	File artPath = new File(server.getArtifactsDirectory() + File.separator + server.getProjectManager().findProjectById(sBuild.getProjectId()).getName() + File.separator  + sBuild.getBuildTypeName() + File.separator + String.valueOf(sBuild.getBuildId()) + File.separator);
    	try {
			artifacts = ArtifactListBuilder.getArtifactFilesWithSizeAndWithoutPrefix(artPath);
		} catch (FileNotFoundException e) {
			// So there's no artifacts. That's ok. They might not exist, or might have been cleaned up.
		}
    	
        SUser myUser = SessionUser.getUser(request);
        
        //model.put("artifacts", artifacts);
        model.put("artifactsSize", artifacts.size());
        model.put("hasPermission", myUser.isPermissionGrantedForProject(sBuild.getProjectId(), Permission.RUN_BUILD));
        
        model.put("invokerBuildId", sBuild.getBuildId());
        model.put("invokerBuildTypeId", sBuild.getBuildTypeId());
        model.put("invokerBuildTypeName", sBuild.getBuildTypeName());
        model.put("invokerBuildNumber", sBuild.getBuildNumber());
    	
        int enabledCount = 0;
        List<BuildInvokerConfig> invokers = settings.findInvokersForBuildType(sBuild.getBuildTypeId());
        for (BuildInvokerConfig invoker : invokers){
        	if (server.getProjectManager().findBuildTypeById(invoker.getBuildToInvoke()) != null){
        		invoker.setBuildNameToInvoke(server.getProjectManager().findBuildTypeById(invoker.getBuildToInvoke()).getFullName());
        		enabledCount++;
        		for (CustomParameter cust : invoker.getOrderedParameterCollection()){
        			if (cust.getIsArtifact()){
        				cust.setFilteredArtifacts(filterArtifactList(artifacts, cust.getFilter()));
        			}
        		}
        	} else {
        		invoker.setEnabled(false);
        	}
        }
        model.put("invokers", invokers);
        model.put("invokersSize", enabledCount);
    	model.put("messages", message);
	}


	
}
