jQueryBuildInvoker(document).ready(function(){
	jQueryBuildInvoker(".validateForm").validate();
});

jQueryBuildInvoker(document).ajaxError(function(){
    if (window.console && window.console.error) {
        console.error(arguments);
    }
});


function submitForm(formId, buildNumber, buildName) {
	if (jQueryBuildInvoker("#form" + formId).valid()){
		jQueryBuildInvoker("#log").show().html("<img src='img/ajax-loader.gif' align=bottom width='16' height='16' /> Build request submitted");

		var parameters = jQueryBuildInvoker("#form" + formId).serializeArray();
		var jsonPayload = {
			    buildTypeId: buildNumber,
			    comment: {
			        text: "Triggered by BuildInvoker",
			    },
			    properties: {
			        property: parameters
			    }
			};

		jQueryBuildInvoker.ajax({
			  url:"app/rest/buildQueue",
			  type:"POST",
			  data: JSON.stringify(jsonPayload),
			  contentType:"application/json; charset=utf-8",
			  accept:"application/json",
			  dataType:"json",
			  success: function(){
				  jQueryBuildInvoker("#log").show().html("<b>Build request sent for <a href='viewType.html?buildTypeId=" + buildNumber + "&tab=buildTypeStatusDiv'>" + buildName + "</a></b>");
			  },
			  error: function(error){
				  jQueryBuildInvoker("#log").show().html("<b>An error occured while sending the build queue request to the TeamCity REST API.</b><br><dl><dt><b>" + error.statusText + "</b> (HTTP Status " + error.status + ")</dt><dd>" + error.responseText + "</dd></dl>");
			  }
			});
	}
}
	
