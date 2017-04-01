package websocket.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.viksitpro.core.dao.entities.IstarUser;
import com.viksitpro.core.utilities.DBUTILS;

@ServerEndpoint(value = "/ratesrv", configurator = GetHttpSessionConfigurator.class)
public class CustomEndPoint {

	// private static ConcurrentHashMap<Integer, javax.websocket.Session> queue
	// = new ConcurrentHashMap();

	public static ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, javax.websocket.Session>> event_user_queue = new ConcurrentHashMap();

	@OnOpen
	public void open(javax.websocket.Session session, EndpointConfig config) {
		com.istarindia.apps.dao.UUIUtils.printlog(this.getClass().getName(), "----> Recieved ws request ");
		try {
			session.setMaxIdleTimeout(7200000);
			HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
			IstarUser u = (IstarUser) httpSession.getAttribute("user");
			com.istarindia.apps.dao.UUIUtils.printlog(u.getName());
			registerUser(u, session, httpSession);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void registerUser(IstarUser user, javax.websocket.Session ws_session, HttpSession httpSession) {
		String event_queue_id = "";
		String event_id = (String) httpSession.getAttribute("event_id");
		DBUTILS utils = new DBUTILS();
		ws_session.setMaxIdleTimeout(7200000);
		// finding event_queue_id from event_id
		String get_event_que = "select cast (EQE.event_queue_id as varchar(50)) from event_queue_events EQE, student S where EQE.user_id = S.id and EQE.event_id = '"
				+ event_id + "'";
		List<HashMap<String, Object>> result = utils.executeQuery(get_event_que);
		if (result.size() > 0 && event_id != null) {

			try {

				HashMap<String, Object> hmap = result.get(0);
				event_queue_id = (String) hmap.get("event_queue_id");
				String sql = "insert into event_que_user (event_que_id, user_id, user_type) values ('"
						+ UUID.fromString(event_queue_id) + "', " + user.getId() + ", '" + user.getUserType() + "')";

				utils.executeUpdate(sql);
				//
				if (event_user_queue.get(UUID.fromString(event_queue_id)) != null) {
					ConcurrentHashMap<Integer, javax.websocket.Session> queue = event_user_queue
							.get(UUID.fromString(event_queue_id));
					queue.put(user.getId(), ws_session);
					event_user_queue.put(UUID.fromString(event_queue_id), queue);
				} else {
					ConcurrentHashMap<Integer, javax.websocket.Session> queue = new ConcurrentHashMap();
					queue.put(user.getId(), ws_session);
					event_user_queue.put(UUID.fromString(event_queue_id), queue);
				}

				com.istarindia.apps.dao.UUIUtils.printlog("New session opened: by " + user.getEmail()
						+ " with websocket session :" + ((javax.websocket.Session) ws_session).getId()
						+ " with timeout of -- " + ws_session.getMaxIdleTimeout());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				String sql1 = "update user_profile set jsession_id='" + httpSession.getId() + "' , websocket_id='"
						+ ws_session.getId() + "' where id=" + user.getId();
				utils.executeUpdate(sql1);

				// queue.put(user.getId(), ws_session);
				com.istarindia.apps.dao.UUIUtils.printlog("New session opened: by " + user.getEmail()
						+ " with websocket session :" + ((javax.websocket.Session) ws_session).getId());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@OnMessage
	public void onMessage(Session session, String msg) {
		try {
			com.istarindia.apps.dao.UUIUtils
					.printlog("received msg " + msg + " from " + ((javax.websocket.Session) session).getId());

			UUID current_que_id = UUID.randomUUID();

			for (UUID ev_que_id : event_user_queue.keySet()) {
				ConcurrentHashMap<Integer, javax.websocket.Session> queue = event_user_queue.get(ev_que_id);
				for (int i : queue.keySet()) {
					if (session.getId() == queue.get(i).getId()) {
						current_que_id = ev_que_id;
						break;
					}
				}
			}

			switch (msg) {
			case "slide_event___right":
				sendAll2(msg, current_que_id);
				break;
			case "slide_event___fragement_show":
				sendAll2(msg, current_que_id);
				break;

			default:
				sendAll2(msg, current_que_id);
				break;
			}
			//

			// sendAll(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnError
	public void error(Session session, Throwable t) {
		com.istarindia.apps.dao.UUIUtils.printlog("closed baby on error");

		for (UUID ev_que_id : event_user_queue.keySet()) {
			ConcurrentHashMap<Integer, javax.websocket.Session> queue = event_user_queue.get(ev_que_id);
			for (int i : queue.keySet()) {
				if (session.getId() == queue.get(i).getId()) {
					queue.remove(i);
					event_user_queue.put(ev_que_id, queue);
				}
			}
		}

		// user_role.remove(session.getId());
		/*
		 * for(int i : user_ws_session.keySet()) {
		 * if(user_ws_session.get(i).equalsIgnoreCase(session.getId())) {
		 * user_ws_session.remove(i); } }
		 */
		t.printStackTrace();
		com.istarindia.apps.dao.UUIUtils.printlog(this.getClass().getName(),
				"Error on session " + ((javax.websocket.Session) session).getId());
	}

	@OnClose
	public void closedConnection(Session session) {
		// queue.remove(session);
		com.istarindia.apps.dao.UUIUtils.printlog("closed baby");
		for (UUID ev_que_id : event_user_queue.keySet()) {
			ConcurrentHashMap<Integer, javax.websocket.Session> queue = event_user_queue.get(ev_que_id);
			for (int i : queue.keySet()) {
				if (session.getId() == queue.get(i).getId()) {
					com.istarindia.apps.dao.UUIUtils.printlog("size of queue before deletion" + queue.size());
					queue.remove(i);

				}
			}
			event_user_queue.put(ev_que_id, queue);
			com.istarindia.apps.dao.UUIUtils.printlog("size of queue after deletion" + queue.size());

		}
		/*
		 * user_role.remove(session.getId()); for(int i :
		 * user_ws_session.keySet()) {
		 * if(user_ws_session.get(i).equalsIgnoreCase(session.getId())) {
		 * user_ws_session.remove(i); } }
		 */
		com.istarindia.apps.dao.UUIUtils
				.printlog("session closed in closed connection: " + ((javax.websocket.Session) session).getId());
	}

	public static void sendAll(String msg) {
		try {
			ArrayList<javax.websocket.Session> closedSessions = new ArrayList<>();
			for (UUID ev_que_id : event_user_queue.keySet()) {
				ConcurrentHashMap<Integer, javax.websocket.Session> queue = event_user_queue.get(ev_que_id);
				for (Integer uid : queue.keySet()) {

					if (!queue.get(uid).isOpen()) {
						com.istarindia.apps.dao.UUIUtils.printlog("CustomEndPoint",
								"Closed session: " + ((javax.websocket.Session) queue.get(uid)).getId());
						closedSessions.add(queue.get(uid));

					} else {
						((javax.websocket.Session) queue.get(uid)).getBasicRemote().sendText(msg);
					}
				}
				for (int k : queue.keySet()) {
					if (closedSessions.contains(queue.get(k))) {
						queue.remove(k);
					}
				}
				event_user_queue.put(ev_que_id, queue);
				com.istarindia.apps.dao.UUIUtils.printlog("Sending " + msg + " to " + queue.size() + " clients");

			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void sendAllWithEvent(String msg, UUID event_id) {
		DBUTILS utils = new DBUTILS();
		String ev_que_id = "";
		String get_event_que = "select cast (EQE.event_queue_id as varchar(50)) from event_queue_events EQE, student S where EQE.user_id = S.id and EQE.event_id = '"
				+ event_id + "'";
		List<HashMap<String, Object>> result = utils.executeQuery(get_event_que);
		if (result.size() > 0) {
			ev_que_id = (String) result.get(0).get("event_queue_id");
		}
		try {
			ArrayList<javax.websocket.Session> closedSessions = new ArrayList<>();
			// for( UUID ev_que_id :event_user_queue.keySet())
			// {
			ConcurrentHashMap<Integer, javax.websocket.Session> queue = event_user_queue
					.get(UUID.fromString(ev_que_id));
			for (Integer uid : queue.keySet()) {

				if (!queue.get(uid).isOpen()) {
					com.istarindia.apps.dao.UUIUtils.printlog("CustomEndPoint",
							"Closed session: " + ((javax.websocket.Session) queue.get(uid)).getId());
					closedSessions.add(queue.get(uid));

				} else {
					try {
						((javax.websocket.Session) queue.get(uid)).getBasicRemote().sendText(msg);
					} catch (Exception e) {
						System.err.println("invalid message --- " + msg);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			for (int k : queue.keySet()) {
				if (closedSessions.contains(queue.get(k))) {
					queue.remove(k);
				}
			}
			event_user_queue.put(UUID.fromString(ev_que_id), queue);
			com.istarindia.apps.dao.UUIUtils.printlog("Sending " + msg + " to " + queue.size() + " clients");

			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendAll2(String msg, UUID current_que_id) {
		try {
			ArrayList<javax.websocket.Session> closedSessions = new ArrayList<>();
			// for( UUID ev_que_id :event_user_queue.keySet())
			// {
			ConcurrentHashMap<Integer, javax.websocket.Session> queue = event_user_queue.get(current_que_id);
			for (Integer uid : queue.keySet()) {

				if (!queue.get(uid).isOpen()) {
					com.istarindia.apps.dao.UUIUtils.printlog("CustomEndPoint",
							"Closed session: " + ((javax.websocket.Session) queue.get(uid)).getId());
					closedSessions.add(queue.get(uid));

				} else {
					((javax.websocket.Session) queue.get(uid)).getBasicRemote().sendText(msg);
				}
			}
			for (int k : queue.keySet()) {
				if (closedSessions.contains(queue.get(k))) {
					queue.remove(k);
				}
			}
			event_user_queue.put(current_que_id, queue);
			com.istarindia.apps.dao.UUIUtils.printlog("Sending " + msg + " to " + queue.size() + " clients");

			// }

			// queue.removeAll(closedSessions);

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
