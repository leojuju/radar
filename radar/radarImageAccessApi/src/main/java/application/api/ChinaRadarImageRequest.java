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

@Path("chinaImage")
public class ChinaRadarImageRequest {
	
	private static Logger logger = LoggerFactory.getLogger(ChinaRadarImageRequest.class);
	
	@GET
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public byte[] getRadarImage(@QueryParam("time") long time) {
		
		//为每个请求创造个ID.
		String  requestID = UUID.randomUUID().toString(); 
		long startTime = System.currentTimeMillis();
		LatAndLongt startLAL = new LatAndLongt(55, 70);
		LatAndLongt endLAL = new LatAndLongt(15, 140);
		logger.info("[RequestID:"+requestID+"] Recieved request for chinaImage,StartLAL:"
				+startLAL+",EndLAL:"+endLAL+",time="+time+".");
		//拼图处理.返回缩小到1/16的图像.
		byte[] imageMatrix = Jigsaw.jigsaw(startLAL, endLAL, 16, time,requestID);
		logger.info("[RequestID:"+requestID+"] Process time:"+(System.currentTimeMillis()-startTime));
		
        return imageMatrix;
    }
	
}
