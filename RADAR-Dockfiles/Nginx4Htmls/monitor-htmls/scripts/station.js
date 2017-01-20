function createxmlhttp(){
  if (window.XMLHttpRequest)
	  {// code for IE7+, Firefox, Chrome, Opera, Safari
	      return new XMLHttpRequest();
	  }
  else
	  {// code for IE6, IE5
	      return new ActiveXObject("Microsoft.XMLHTTP");
	  }
}

function loadLogDirs(stationID){
	var xmlhttp = createxmlhttp();
	xmlhttp.onreadystatechange=function()
	  {
		  if(xmlhttp.readyState==4 && xmlhttp.status==200)
		  	{
			    var data=JSON.parse(xmlhttp.responseText);
			    var num = data["num"];
			    var stationName = data["stationName"]
			    document.getElementsByTagName("title")[0].innerText = stationName;
			    var list = data["list"];
			    var div =document.getElementById("logdirs-show");
			    for(var i=0;i<list.length;i++){
					var logDir = list[i];
					var newLink = document.createElement("button");
					newLink.onclick = function (event){
						loadLogs(stationID,event.target.innerText);
					};
					newLink.innerText = logDir;
					newLink.style.margin = "0 1";
					newLink.style.color="red";
					div.appendChild(newLink);
				}
			    loadLogs(stationID,list[0]);
		    }
	  }
	xmlhttp.open("GET","http://localhost:8882/radarKafkaMonitorApi/rest/station?stationID="+stationID,true);
	xmlhttp.send();
}

function loadLogs(stationID,logDir){
	var xmlhttp = createxmlhttp();
	xmlhttp.onreadystatechange=function()
	  {
		  if(xmlhttp.readyState==4 && xmlhttp.status==200)
		  	{
			    var data=JSON.parse(xmlhttp.responseText);
			    var num = data["num"];
			    var map = data["map"];
				var div =document.getElementById("show");
				div.innerHTML = "";
				for (var logFile in map){
					var list = map[logFile];
					var logDiv = document.createElement("div");
					var head = document.createElement("h4");
					head.innerText = logFile;
					logDiv.appendChild(head);
					var idx = 0;
					var date = parseInt(logFile.substring(logFile.length-12,logFile.length-4));
					for (var int = 0; int < 24; int++) {
						for (var j = 0; j < 10; j++) {
							var span = document.createElement("button");
							var time = date*10000+int*100+j*6;
							span.title=time;
							span.style.width="20px";
							span.style.height="20px";
							if(list[idx]!=time){
								span.style.background = "red";
							}else{
								span.style.background = "green";
								idx+=idx==list.length?0:1;
							}
							logDiv.appendChild(span);
						}
					}
					div.appendChild(logDiv);
				}
		    }
	  };
	xmlhttp.open("GET","http://localhost:8882/radarKafkaMonitorApi/rest/getLogs?stationID="
			+stationID+"&logDir="+logDir,true);
	xmlhttp.send();
}

function getImage(){
	
}
