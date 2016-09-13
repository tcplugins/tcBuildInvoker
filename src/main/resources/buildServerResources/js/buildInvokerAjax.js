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
		var str = jQuery(formId).serialize();
		jQueryBuildInvoker("#log").show().html("<img src='img/ajax-loader.gif' align=bottom width='16' height='16' /> Build request submitted");
		jQueryBuildInvoker.get("action.html?" + jQuery("#form" + formId).serialize(), {}, function(){
			jQueryBuildInvoker("#log").show().html("<b>Build request sent for <a href='viewType.html?buildTypeId=" + buildNumber + "&tab=buildTypeStatusDiv'>" + buildName + "</a></b>");
			
		});
	}
}
	
