<%@ include file="/include.jsp" %>
<%@ page import="jetbrains.buildServer.web.ArtifactsTreePrinter"%>

<c:if test="not ${hasPermission}" >

	Sorry, you need "Run Build" permission to run a build for this project.

</c:if>

<c:if test="${hasPermission}" >
			<script type="text/javascript" src="..${jspHome}js/jquery-1.3.2.min.js"></script>
			<script type="text/javascript">
				jQuery.noConflict();
				jQuery(document).ready( function() {
					jQuery("td.hiddenParam").hide();
				});

				function toggleHiddenParams(formID){
					jQuery('div#invokerDiv' + formID + ' td.hiddenParam').toggle();
				}
			</script>
			<c:forEach items="${invokers}" var="invoker">
			<c:if test="${invoker.enabled}">
			<div id="invokerDiv${invoker.uniqueKey}" style="background-color:#F5F5F5; padding: 0.5em 1em 1em 1em; border: solid 1px #ccc; margin:1em; width:60%;">
			<jsp:useBean id="invoker" type="buildinvoker.BuildInvokerConfig"/>
		  	 <form method="get" action="action.html">
		  	 	<input type="hidden" name="add2Queue" value="${invoker.buildToInvoke}" />
		  	  	<input type="hidden" name="env.name" value="testname1" />
		  	  	<input type="hidden" name="env.value" value="testvalue1" />
		  	  	<input type="hidden" name="env.name" value="testname2" />
		  	  	<input type="hidden" name="env.value" value="testvalue2" />

				<p>Build : <span style="font-weight:bold;">${invoker.buildNameToInvoke}</span>
				<span style="float:right;text-decoration:underline;cursor:pointer;" onclick="toggleHiddenParams(${invoker.uniqueKey});"> show/hide extra parameters </span></p>
		  	  	<table class="projectTable" style="background-color: white;">
		  	  	<tr style="font-size:90%;"><th style="width:50%;">Parameter Name</th><th style="width:50%;">Parameter Value</th></tr>
		  	  	<c:forEach items="${invoker.orderedParameterCollection}" var="parameter">
		  	  	<jsp:useBean id="parameter" type="buildinvoker.CustomParameter"/>
		  	  		<c:if test="${parameter.isArtifact}" >
		  	  			<c:if test="${artifactsSize > 0}" >
			  	  			${parameter.artifactStartAsHtml}
							<c:forEach items="${artifacts}" var="artifact">
								<option value="${artifact.fullName}">${artifact.fullName} (${artifact.sizeHumanReadable})</option>
							</c:forEach>
							${parameter.artifactEndAsHtml}
						</c:if>
						<c:if test="${artifactsSize == 0}" >
							${parameter.noArtifactsAsHtml}
						</c:if>
					</c:if>
					<c:if test="${not parameter.isArtifact}" >	
			  	  		 ${parameter.asHtml}
			  	  	</c:if>
		  	  	</c:forEach>

		  	  	<input type="hidden" name="env.name" value="buildIdToInvoke" />
		  	  	<input type="hidden" name="env.value" value="${invoker.buildToInvoke}" />
		  	  	<tr><td class="hiddenParam">env.buildIdToInvoke</td><td class="hiddenParam">${invoker.buildToInvoke}</td></tr>

		  	  	<input type="hidden" name="env.name" value="buildIdInvokedFrom" />
		  	  	<input type="hidden" name="env.value" value="${invokerBuildTypeId}" />		  	  	
		  	  	<tr><td class="hiddenParam">env.buildIdInvokedFrom</td><td class="hiddenParam">${invokerBuildTypeId}</td></tr>

		  	  	<input type="hidden" name="env.name" value="buildInvokedFrom" />
		  	  	<input type="hidden" name="env.value" value="${invokerBuildId}" />		  	  	
		  	  	<tr><td class="hiddenParam">env.buildInvokedFrom</td><td class="hiddenParam">${invokerBuildId}</td></tr>

		  		<tr>
		  			<td colspan=2 style="text-align:right;"><input type="submit" value="${invoker.invokeBuildButtonText}" /></td>
		  		</tr>
		  		</table>
		  		</form>
		  	</div>		
		  	</c:if>
		  	</c:forEach>  
</c:if>
