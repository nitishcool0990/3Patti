package Gt.common;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserJoinedRoomsUtil
{
	 private static final Integer MAX_DIRTY_TIME = Integer.valueOf(420);
  private static Logger log = LoggerFactory.getLogger("Gt.main.Extension.ThreePattiExtension");
  private HashMap<Integer, JoinedRoomInfo> dataStore = new HashMap<Integer, JoinedRoomInfo>();
  
  public List<String> getJoinedRooms(int userId)
  {
    List<String> rooms = null;
    

    JoinedRoomInfo info = getJoinedRoomInfo(userId);
    if (info != null)
    {
      long now = Calendar.getInstance().getTimeInMillis();
      long last = info.getLastUpdate();
      long diff_sec = (now - last) / 1000L;
      if (diff_sec > MAX_DIRTY_TIME.intValue())
      {
        log.info("UserJoinedRoomsUtil getJoinedRooms: diff > MAX_DIRTY, removed all rooms ");
        synchronized (this)
        {
          this.dataStore.remove(Integer.valueOf(userId));
        }
      }
      rooms = getJoinedRoomInfo(userId).getRooms();
    }
    if (rooms == null) {
      rooms = new ArrayList<String>();
    }
    log.info("UserJoinedRoomsUtil getJoinedRooms: returned rooms: " + rooms + " to user :" + userId);
    
    return rooms;
  }
  
  public void joinRoom(int userId, String roomName)
  {
    log.info("UserJoinedRoomsUtil joinRoom: adding room : " + roomName + " to user: " + userId);
    
    JoinedRoomInfo info = getJoinedRoomInfoCreated(userId);
    if (info != null) {
      info.addRoom(roomName);
    }
  }
  
  public void leaveRoom(int userId, String roomName)
  {
    log.info("UserJoinedRoomsUtil leaveRoom: removing room : " + roomName + " to user: " + userId);
    
    JoinedRoomInfo info = getJoinedRoomInfo(userId);
    if (info != null)
    {
      info.removeRoom(roomName);
      synchronized (this)
      {
        if (info.getRooms().size() == 0) {
          this.dataStore.remove(Integer.valueOf(userId));
        }
      }
    }
  }
  
  public void updatLastTime(int userId)
  {
    log.info("UserJoinedRoomsUtil updatLastTime:  to user: " + userId);
    JoinedRoomInfo info = getJoinedRoomInfo(userId);
    if (info != null) {
      info.setLastUpdate(Calendar.getInstance().getTimeInMillis());
    }
  }
  
  private synchronized JoinedRoomInfo getJoinedRoomInfo(int userId)
  {
    if (userId < 1) {
      return null;
    }
    JoinedRoomInfo info = (JoinedRoomInfo)this.dataStore.get(Integer.valueOf(userId));
    
    return info;
  }
  
  private synchronized JoinedRoomInfo getJoinedRoomInfoCreated(int userId)
  {
    if (userId < 1) {
      return null;
    }
    JoinedRoomInfo info = (JoinedRoomInfo)this.dataStore.get(Integer.valueOf(userId));
    if (info == null)
    {
      info = new JoinedRoomInfo(userId);
      this.dataStore.put(Integer.valueOf(userId), info);
    }
    return info;
  }
  
  class JoinedRoomInfo
  {
    int userId = -1;
    long lastUpdate = 0L;
    List<String> rooms = new ArrayList<String>();
    
    public JoinedRoomInfo(int userId)
    {
      this.userId = userId;
      this.lastUpdate = Calendar.getInstance().getTimeInMillis();
    }
    
    public synchronized List<String> getRooms()
    {
      return this.rooms;
    }
    
    public synchronized boolean addRoom(String roomName)
    {
      boolean ret = false;
      if ((this.rooms != null) && (!this.rooms.contains(roomName)))
      {
        this.rooms.add(roomName);
        ret = true;
      }
      return ret;
    }
    
    public synchronized boolean removeRoom(String roomName)
    {
      boolean ret = false;
      if (this.rooms != null) {
        ret = this.rooms.remove(roomName);
      }
      return ret;
    }
    
    public long getLastUpdate()
    {
      return this.lastUpdate;
    }
    
    public void setLastUpdate(long lastUpdate)
    {
      this.lastUpdate = lastUpdate;
    }
  }
}

