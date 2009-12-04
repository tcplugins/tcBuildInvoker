<%@ include file="/include.jsp" %>
	
<c:if test="not ${hasPermission}" >

	Sorry, you need "Run Build" permission to run a build for this project.

</c:if>

<c:if test="${hasPermission}" >
			<c:forEach items="${invokers}" var="invoker">
			<div style="background-color:#F5F5F5; padding: 0.5em 1em 1em 1em; border: solid 1px #ccc; margin:1em; width:60%;">
			<jsp:useBean id="invoker" type="buildinvoker.BuildInvokerConfig"/>
		  	 <form method="get" action="action.html">
		  	 	<input type="hidden" name="add2Queue" value="${invoker.buildToInvoke}" />
		  	  	<input type="hidden" name="env.name" value="testname1" />
		  	  	<input type="hidden" name="env.value" value="testvalue1" />
		  	  	<input type="hidden" name="env.name" value="testname2" />
		  	  	<input type="hidden" name="env.value" value="testvalue2" />

				<p>Build : <span style="font-weight:bold;"> ${invoker.buildNameToInvoke}</span></p>
		  	  	<table class="projectTable" style="background-color: white;">
		  	  	<tr style="font-size:90%;"><th style="width:50%;">Parameter Name</th><th style="width:50%;">Parameter Value</th></tr>
		  	  	<c:forEach items="${invoker.orderedParameterCollection}" var="parameter">
		  	  	<jsp:useBean id="parameter" type="buildinvoker.CustomParameter"/>
		  	  		${parameter.asHtml}
		  	  	</c:forEach>

		  	  	<input type="hidden" name="env.name" value="buildIdToInvoke" />
		  	  	<input type="hidden" name="env.value" value="${invoker.buildToInvoke}" />
		  	  	<tr><td>env.buildIdToInvoke</td><td>${invoker.buildToInvoke}</td></tr>

		  	  	<input type="hidden" name="env.name" value="buildIdInvokedFrom" />
		  	  	<input type="hidden" name="env.value" value="${invokerBuildTypeId}" />		  	  	
		  	  	<tr><td>env.buildIdInvokedFrom</td><td>${invokerBuildTypeId}</td></tr>

		  	  	<input type="hidden" name="env.name" value="buildInvokedFrom" />
		  	  	<input type="hidden" name="env.value" value="${invokerBuildId}" />		  	  	
		  	  	<tr><td>env.buildInvokedFrom</td><td>${invokerBuildId}</td></tr>

		  		<tr>
		  			<td colspan=2 style="text-align:right;"><input type="submit" value="${invoker.invokeBuildButtonText}" /></td>
		  		</tr>
		  		</table>
		  		</form>
		  	</div>		
		  	</c:forEach>  
</c:if>
