package in.talentifyU.utils.services;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.viksitpro.core.utilities.DBUTILS;

import websocket.server.CustomEndPoint;

public class EventUtils {

	public String isPresentorAlive(int user_id, String event_id) {
		String color = "";
		String sql = "select cast (event_queue_id as varchar(50)) from  event_queue_events where event_id = '"
				+ event_id + "' and user_id =" + user_id;
		DBUTILS util = new DBUTILS();

		List<HashMap<String, Object>> data = util.executeQuery(sql);
		if (data.size() > 0) {
			ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, javax.websocket.Session>> event_user_queue = CustomEndPoint.event_user_queue;
			String eve_que_id = (String) data.get(0).get("event_queue_id");
			ConcurrentHashMap<Integer, javax.websocket.Session> queues = event_user_queue
					.get(UUID.fromString(eve_que_id));

			String sql2 = "select presentor_id from trainer_presentor where trainer_id = " + user_id;
			com.istarindia.apps.dao.UUIUtils.printlog("sql2" + sql2);
			List<HashMap<String, Object>> pres_data = util.executeQuery(sql2);
			if (pres_data.size() > 0) {
				int pre_id = (int) pres_data.get(0).get("presentor_id");
				if (queues != null && queues.containsKey(pre_id)) {
					color = "green";
				} else {
					color = "red";
				}
			} else {
				color = "red";
			}
		} else {
			color = "red";
		}

		return color;
	}

}
