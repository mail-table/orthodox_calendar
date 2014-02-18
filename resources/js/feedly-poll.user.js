// ==UserScript==
// @name           Feedly Saved to Pocket
// @namespace      http://feedly.com
// @description    Auto Refresh feedly every 10 minutes.
// @version        1.0
// @date           2014-02-10
// @author         Klim
// @include        http://feedly.com/index.*
// @grant          GM_getValue
// @grant          GM_setValue
// @grant          GM_xmlhttpRequest
// ==/UserScript==

INTERVAL = 1 * 60 * 1000;
EMAIL = "save-pocket@orthodox-calendar-hrd.appspotmail.com";

function checkSaved(url) {
	url = url || document.URL;
	var re_url = /index.html#saved/ig;
	return re_url.exec(url) != null;
}

function saveToPocked() {
	var saved_list = document.querySelectorAll('.u0Entry.quicklisted');
	if ( saved_list && saved_list.length > 0) {
		try {
			var top_bookmark = saved_list[0].getAttribute("data-alternate-link");
			var old_top = GM_getValue( "feedly_last_saved", "" );

			if ( old_top != top_bookmark ) {
				GM_setValue( "feedly_last_saved", top_bookmark );

				for (var i = 0; i < saved_list.length; i++) {

					if (old_top == saved_list[i].getAttribute("data-alternate-link"))
						break;

					var sendData = "email=" + EMAIL + "&" + "url=" + saved_list[i].getAttribute("data-alternate-link");
					xmlhttp=GM_xmlhttpRequest({
						method: "POST",
						url: "http://orthodox-calendar-hrd.appspot.com/savePocket",
						headers: {
							"Content-Type": "application/x-www-form-urlencoded"
						},
						data: sendData,
						onload: function(response) {
							var responseXML = null;
							if (!response.responseXML) {
								responseXML = new DOMParser()
								.parseFromString(response.responseText, "text/html");
							};
							console.log([
								response.status,
								response.statusText,
								response.readyState,
								response.responseHeaders,
								response.responseText,
								response.finalUrl,
								response
							].join("\n"));
						}
					});
				}
			}
		}
		catch(e) {
			console.log(e);
		}
	}
	location.reload();
}

window.setInterval(function() {
		if (!checkSaved()) {
			var saved_link = document.querySelector('#savedtab_label');
			if (saved_link)
				saved_link.click();
		} else {
			saveToPocked();
		}
	},
	INTERVAL);
