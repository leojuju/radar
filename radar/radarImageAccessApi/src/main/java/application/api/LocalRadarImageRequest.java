package application.api;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import infrastruction.radarprocess.Jigsaw;
import infrastruction.radarprocess.geo.LatAndLongt;

@Path("localImage")//?startlat={startlat}&endlat={endlat}&startlongt={startlongt}&endlongt={endlongt}
public class LocalRadarImageRequest {

	private static Logger logger = LoggerFactory.getLogger(LocalRadarImageRequest.class);
	
	@GET
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public byte[] getRadarImage(@QueryParam("startlat")double startlat,@QueryParam("endlat") 
     double endlat,@QueryParam("startlongt") double startlongt,@QueryParam("endlongt") double endlongt,
     @QueryParam("time") long time) {
		
		//为每个请求创造个ID.
		String  requestID = UUID.randomUUID().toString(); 
		long startTime = System.currentTimeMillis();
		//startLAL为西北方向的顶点坐标,endLAL为东南方向的顶点坐标,注意这和请求参数不一样.
		//请求参数中startlat表示最小的纬度,endlat表示最大的纬度;startlongt,endlongt同理.
		LatAndLongt startLAL = new LatAndLongt(endlat, startlongt);
		LatAndLongt endLAL = new LatAndLongt(startlat, endlongt);
		logger.info("[RequestID:"+requestID+"] Recieved request for localImage,StartLAL:"
				+startLAL+",EndLAL:"+endLAL+",time="+time+".");
		//拼图处理.
		byte[] imageMatrix = Jigsaw.jigsaw(startLAL, endLAL, 1, time,requestID);
		logger.info("[RequestID:"+requestID+"] Process time:"+(System.currentTimeMillis()-startTime));
		
        return imageMatrix;
    }
}
