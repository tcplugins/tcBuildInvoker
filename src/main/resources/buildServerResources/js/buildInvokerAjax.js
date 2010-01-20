jQuery(document).ready(function(){
	jQuery(".validateForm").validate();
});

jQuery(document).ajaxError(function(){
    if (window.console && window.console.error) {
        console.error(arguments);
    }
});


function submitForm(formId, buildNumber, buildName) {
	if (jQuery("#form" + formId).valid()){
		var str = jQuery(formId).serialize();
		jQuery("#log").show().html("<img src='img/ajax-loader.gif' align=bottom width='16' height='16' /> Build request submitted");
		jQuery.get("action.html?" + jQuery("#form" + formId).serialize(), {}, function(){
			jQuery("#log").show().html("<b>Build request sent for <a href='viewType.html?buildTypeId=" + buildNumber + "&tab=buildTypeStatusDiv'>" + buildName + "</a></b>");
			
		});
	}
}
	
