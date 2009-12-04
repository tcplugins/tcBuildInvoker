package buildinvoker.settings;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.settings.ProjectSettings;
import jetbrains.buildServer.serverSide.settings.ProjectSettingsManager;

import org.jdom.Element;

import buildinvoker.BuildInvokerConfig;
import buildinvoker.InvokerBuild;
import buildinvoker.InvokerBuildRankingComparator;

public class BuildInvokerProjectSettings implements ProjectSettings {
	ProjectSettingsManager psm;
	ProjectSettings ps;
	private Boolean updateSucess = false;
	private String tabName = "Invoke Build";
	private CopyOnWriteArrayList<BuildInvokerConfig> buildInvokerConfigs;
	
	public BuildInvokerProjectSettings(){
		buildInvokerConfigs = new CopyOnWriteArrayList<BuildInvokerConfig>();
	}

    @SuppressWarnings("unchecked")
	public void readFrom(Element rootElement)
    /* Is passed an Element by TC, and is expected to persist it to the settings object.
     * Old settings should be overwritten.
     */
    {
    	Loggers.SERVER.debug("readFrom :: " + rootElement.toString());
    	CopyOnWriteArrayList<BuildInvokerConfig> configs = new CopyOnWriteArrayList<BuildInvokerConfig>();
    	
    	if (rootElement.getAttribute("tabName") != null){
    		this.tabName = rootElement.getAttributeValue("tabName");
    	}
    	/* 
    		<buildInvoker buildToInvoke="bt1" invokeBuildButtonText="Run">
        		<invokerBuild invokeFrom="bt3" displayOrder="1" enabled="true"/>
        		<customParameter type="hidden" name="name1" value="value1" scope="env"/>
        */
    	
		List<Element> namedChildren = rootElement.getChildren("buildInvoker");
        if(namedChildren.size() == 0)
        {
            this.buildInvokerConfigs = null;
        } else {
			for(Element e : namedChildren)
	        {
				BuildInvokerConfig biConfig = new BuildInvokerConfig(e);
				Loggers.SERVER.debug(e.toString());
				configs.add(biConfig);
				Loggers.SERVER.debug(this.getClass().getName() + ":readFrom :: myBuildTypeId " + biConfig.getBuildToInvoke());
				Loggers.SERVER.debug(this.getClass().getName() + ":readFrom :: enabled " + String.valueOf(biConfig.isEnabled()));
				//Loggers.SERVER.debug(this.getClass().getName() + ":readFrom :: invokeBuildTypeId " + String.valueOf(biConfig.getInvokeBuildTypeId()));
				//Loggers.SERVER.debug(this.getClass().getName() + ":readFrom :: InvokeBuildTarget " + String.valueOf(biConfig.getInvokebuildTarget()));
	        }
			this.buildInvokerConfigs = configs;
    	}
    }

    public void writeTo(Element parentElement)
    /* Is passed an (probably empty) Element by TC, which is expected to be populated from the settings
     * in memory. 
     */
    {
    	Loggers.SERVER.debug(this.getClass().getName() + ":writeTo :: " + parentElement.toString());
        if(buildInvokerConfigs != null)
        {
            for(Iterator<BuildInvokerConfig> biConf = buildInvokerConfigs.iterator(); biConf.hasNext();)
            {
                BuildInvokerConfig biConfig = biConf.next();
            	//Element el = biConfig.getAsElement();
            	//Loggers.SERVER.debug(el.toString());
                //parentElement.addContent(el);
				Loggers.SERVER.debug(this.getClass().getName() + ":readFrom :: myBuildTypeId " + biConfig.getBuildToInvoke());
				Loggers.SERVER.debug(this.getClass().getName() + ":readFrom :: enabled " + String.valueOf(biConfig.isEnabled()));
				//Loggers.SERVER.debug(this.getClass().getName() + ":readFrom :: invokeBuildTypeId " + String.valueOf(biConfig.getInvokeBuildTypeId()));
				//Loggers.SERVER.debug(this.getClass().getName() + ":readFrom :: invokeBuildTarget " + String.valueOf(biConfig.getInvokebuildTarget()));
            }

        }
    }
    
    public List<BuildInvokerConfig> getBuildsAsList(){
    	return this.buildInvokerConfigs;
    }    
	
    public Boolean updateSuccessful(){
    	return this.updateSucess;
    }
    
	public void dispose() {
		Loggers.SERVER.debug(this.getClass().getName() + ":dispose() called");
	}

	public Integer getBuildInvokersCount(){
		return this.buildInvokerConfigs.size();
	}
	
	public List<BuildInvokerConfig> getWebHooksConfigs() {
		return buildInvokerConfigs;
	}

	public String getTabName() {
		return tabName;
	}

	
    public String getBuildInovokersAsString(){
    	String tmpString = "";
    	for(BuildInvokerConfig biConf : buildInvokerConfigs)
    	{
    		tmpString = tmpString + biConf.getBuildToInvoke() + "" + biConf.isEnabled().toString() + "<br/>";
    	}
    	return tmpString;
    }

	public List<BuildInvokerConfig> findInvokersForBuildType(String buildTypeId) {
		List<BuildInvokerConfig> configs = new ArrayList<BuildInvokerConfig>();
		for (BuildInvokerConfig biConf : buildInvokerConfigs){
			for (InvokerBuild build : biConf.getOrderedBuildCollection()){
				if (build.getBuildTypeId().equals(buildTypeId) && build.isEnabled()){
					configs.add(biConf);
				}
			}
		}
		Comparator<BuildInvokerConfig> rankComparator = new InvokerBuildRankingComparator(buildTypeId);
		Collections.sort(configs, rankComparator);
		return configs;
	}

}
