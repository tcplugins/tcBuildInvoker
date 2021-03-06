package buildinvoker;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.serverSide.SBuildType;
import jetbrains.buildServer.serverSide.settings.ProjectSettingsManager;

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.junit.Before;
import org.junit.Test;

import buildinvoker.settings.BuildInvokerProjectSettings;
import buildinvoker.settings.BuildInvokerProjectSettingsFactory;



public class TestXmlSettings {

	static SBuildServer server = mock(SBuildServer.class);
	jetbrains.buildServer.serverSide.ProjectManager projman;
	ProjectSettingsManager projectSettingsManager = mock(ProjectSettingsManager.class);
	Loggers Loggers = mock(Loggers.class);
	SBuildType buildType;
	
	
	
	@Before
	public void setup(){
		//projman = (jetbrains.buildServer.serverSide.ProjectManager) mock(ProjectManager.class);
		/*
		buildType = mock(SBuildType.class);
		when(server.getProjectManager()).thenReturn(projman);
		when(projman.findBuildTypeById("bt1")).thenReturn(buildType);
		when(server.getProjectManager().findBuildTypeById("bt2")).thenReturn(buildType);
		when(server.getProjectManager().findBuildTypeById("bt3")).thenReturn(buildType);
		when(server.getProjectManager().findBuildTypeById("bt4")).thenReturn(buildType);
		when(server.getProjectManager().findBuildTypeById("bt5")).thenReturn(buildType);
		when(server.getProjectManager().findBuildTypeById("bt6")).thenReturn(buildType);
		when(buildType.getFullName()).thenReturn("SomeProjectName");
		*/
	}
	
	@Test
	public void TestProjectSettingsConfig(){
		
		BuildInvokerProjectSettingsFactory settingsFactory = new BuildInvokerProjectSettingsFactory(this.projectSettingsManager);
		BuildInvokerProjectSettings settings = settingsFactory.createProjectSettings("project1");
		settings.readFrom(this.getFullConfigElement("src/test/resources/plugin-settings-test.xml"));
		
		System.out.println(settings.getTabName());
		
		Element e = new Element("buildInvokers");
		settings.writeTo(e);
		System.out.println(e.toString());
		
		Document d = new Document(e);
		
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();

		org.jdom.output.XMLOutputter outputter = new org.jdom.output.XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		
		String x1="foo", x2="bar";

		try {
			outputter.output(d, baos);
			System.out.println("writeTo::");
			System.out.println(baos.toString());
			x1 = baos.toString();
			
			baos.reset();
			//baos.
			outputter.output(this.getFullConfigElement("src/test/resources/plugin-settings-test.xml"), baos);
						System.out.println("fromFile::");
			System.out.println(baos.toString());
			x2 = baos.toString();
			

			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//assertEquals(this.getFullConfigElement("src/test/resources/plugin-settings-test.xml"),e);
		//assertTrue(x1.equals(x2));
	}
	
	@Test
	public void TestGetTabName(){
		BuildInvokerProjectSettings settings = new BuildInvokerProjectSettings();
		settings.readFrom(this.getFullConfigElement("src/test/resources/plugin-settings-test.xml"));
		assertTrue(settings.getTabName().equals("Run Deploy"));
	}
	
	@Test
	public void TestXmlLoading01(){
		BuildInvokerProjectSettings settings = new BuildInvokerProjectSettings();
		settings.readFrom(this.getFullConfigElement("src/test/resources/plugin-settings-test.xml"));
		List<BuildInvokerConfig> configs = settings.findInvokersForBuildType("bt2");
		System.out.println("configs size: " + configs.size());
		
		assertTrue(configs.get(0).getBuildToInvoke().equals("bt6"));
		assertTrue(configs.get(1).getBuildToInvoke().equals("bt5"));
		assertTrue(configs.get(2).getBuildToInvoke().equals("bt4"));
		assertTrue(configs.get(3).getBuildToInvoke().equals("bt3"));
		assertTrue(configs.get(4).getBuildToInvoke().equals("bt2"));
		assertTrue(configs.get(5).getBuildToInvoke().equals("bt1"));
		assertTrue(configs.size() == 6);
		
		for (BuildInvokerConfig config : configs){
			System.out.println("We will be invoking build: " + config.getBuildToInvoke());
			System.out.println("This build is enabled: " + config.isEnabled().toString());
			System.out.println("invokeBuildButtonText: " + config.getInvokeBuildButtonText().toString());
			//System.out.println("This build has a displayOrder of:" config.get);
			for (CustomParameter cp : config.getOrderedParameterCollection()){
				System.out.println(cp.getScope() + "." + cp.getName() + ": " + cp.getScope() + "." + cp.getValue() + "(" + cp.getType() + ")");
			}
			
		}

		List<BuildInvokerConfig> configs2 = settings.findInvokersForBuildType("bt3");
		System.out.println("configs size: " + configs2.size());
		
		assertTrue(configs2.get(0).getBuildToInvoke().equals("bt1"));
		assertTrue(configs2.get(1).getBuildToInvoke().equals("bt2"));
		assertTrue(configs2.size() == 2);
		
	}

	@Test
	public void TestXmlLoading02(){
		BuildInvokerProjectSettings settings = new BuildInvokerProjectSettings();
		settings.readFrom(this.getFullConfigElement("src/test/resources/plugin-settings-test-3.xml"));
		List<BuildInvokerConfig> configs = settings.findInvokersForBuildType("bt433");
		System.out.println("configs size: " + configs.size());
		
		/*
		assertTrue(configs.get(0).getBuildToInvoke().equals("bt6"));
		assertTrue(configs.get(1).getBuildToInvoke().equals("bt5"));
		assertTrue(configs.get(2).getBuildToInvoke().equals("bt4"));
		assertTrue(configs.get(3).getBuildToInvoke().equals("bt3"));
		assertTrue(configs.get(4).getBuildToInvoke().equals("bt2"));
		assertTrue(configs.get(5).getBuildToInvoke().equals("bt1"));
		assertTrue(configs.size() == 6);
		*/
		
		for (BuildInvokerConfig config : configs){
			System.out.println("We will be invoking build: " + config.getBuildToInvoke());
			System.out.println("This build is enabled: " + config.isEnabled().toString());
			System.out.println("invokeBuildButtonText: " + config.getInvokeBuildButtonText().toString());
			//System.out.println("This build has a displayOrder of:" config.get);
			for (CustomParameter cp : config.getOrderedParameterCollection()){
				System.out.println(cp.getScope() + "." + cp.getName() + ": " 
						+ cp.getScope() + "." + cp.getValue() 
						+ "(" + cp.getType() + ") :: default: " 
						+ cp.getDefaultValue() + " :: filter: " + cp.getFilter());
			}
			
		}

		// <customParameter type="hidden" name="copyFileTo" value="dev" scope="env" filter="tar"/>
		System.out.println(configs.get(0).getOrderedParameterCollection().get(0).getAsHtml());
		assertNull(configs.get(0).getOrderedParameterCollection().get(0).getDefaultValue());
		assertNull(configs.get(0).getOrderedParameterCollection().get(0).getFilter());
		
		
		// <customParameter type="option" name="extractAndUpdateSymlinks" value="yes:no" scope="env" default="no" />
		System.out.println(configs.get(0).getOrderedParameterCollection().get(1).getAsHtml());
		assertTrue(configs.get(0).getOrderedParameterCollection().get(1).getDefaultValue().equals("no"));
		assertNull(configs.get(0).getOrderedParameterCollection().get(1).getFilter());

		
		// <customParameter type="artifact" name="artifact1" scope="env" filter=".+\.tar\.gz$" />
		System.out.println(configs.get(0).getOrderedParameterCollection().get(2).getAsHtml());
		assertNull(configs.get(0).getOrderedParameterCollection().get(2).getDefaultValue());
		assertTrue(configs.get(0).getOrderedParameterCollection().get(2).getFilter().equals(".+\\.tar\\.gz$"));
		

		// <customParameter type="artifact" name="artifact2" scope="env" filter="tar" default="123456" />
		System.out.println(configs.get(0).getOrderedParameterCollection().get(3).getAsHtml());
		assertNull(configs.get(0).getOrderedParameterCollection().get(3).getDefaultValue());
		assertTrue(configs.get(0).getOrderedParameterCollection().get(3).getFilter().equals("tar"));
		
		
		List<BuildInvokerConfig> configs2 = settings.findInvokersForBuildType("bt433");
		System.out.println("configs size: " + configs2.size());
		
		
		/*
		assertTrue(configs2.get(0).getBuildToInvoke().equals("bt1"));
		assertTrue(configs2.get(1).getBuildToInvoke().equals("bt2"));
		assertTrue(configs2.size() == 2);
		*/
		
	}	
	
	private Element getFullConfigElement(String filename){
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);
		try {
			Document doc = builder.build(filename);
			return doc.getRootElement();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
