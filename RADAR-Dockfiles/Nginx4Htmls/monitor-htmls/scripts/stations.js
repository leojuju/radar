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
function loadStations(){
	var xmlhttp = createxmlhttp();
	xmlhttp.onreadystatechange=function()
	  {
		  if(xmlhttp.readyState==4 && xmlhttp.status==200)
		  	{
			    var data=JSON.parse(xmlhttp.responseText);
			    var num = data["num"];
			    var map = data["map"];
			    document.getElementById("statistics").innerText="Station Num:"+num+".";
				var div =document.getElementById("list-div");
				for(var stationID in map){
					var newLink = document.createElement("a");
					newLink.href = "downloadlog-detail.html?"+stationID;
					newLink.innerText = map[stationID];
					newLink.style.margin = "1 3";
					newLink.style.color="red";
					div.appendChild(newLink);
				}
		    }
	  }
	xmlhttp.open("GET","http://localhost:8882/radarKafkaMonitorApi/rest/stations",true);
	xmlhttp.send();
}

function loadRealTimeStations(){
	var xmlhttp = createxmlhttp();
	xmlhttp.onreadystatechange=function()
	  {
		  if(xmlhttp.readyState==4 && xmlhttp.status==200)
		  	{
			    var data=JSON.parse(xmlhttp.responseText);
			    var num = data["num"];
			    var realTime = data["time"];
			    var date = parseInt(realTime/10000);
			    var map = data["map"];
			    document.getElementById("statistics").innerText="Station Num:"+num+".";
				var div =document.getElementById("show");
				for(var stationID in map){
					var obj = map[stationID];
					var list = obj["list"];
					var logDiv = document.createElement("div");
					var head = document.createElement("h4");
					head.innerText = obj["stationName"];
					logDiv.appendChild(head);
					var idx = 0;
					for (var int = 0; int < 24; int++) {
						var time2hour = date*10000+int*100;
						if(time2hour>realTime){
							break;
						}
						for (var j = 0; j < 10; j++) {
							var time = time2hour+j*6;
							if(time>realTime){
								break;
							}
							var span = document.createElement("button");
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
	  }
	xmlhttp.open("GET","http://localhost:8882/radarKafkaMonitorApi/rest/realtime_stations",true);
	xmlhttp.send();
}
