package Gt.common;

import java.util.ArrayList;
import java.util.List;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.protocol.serialization.SerializableSFSType;

public class SeatInfo   implements SerializableSFSType {
	public List<Seat> seats =null;

	public SeatInfo() {
		seats = new ArrayList<Seat>();
	}

	public void createSeats(int numberOfSeats){
		Seat seat = null;
		for(int i=1;i<=numberOfSeats;i++){
			seat = new Seat(i);
			seats.add(seat);
		}
	}
	
	public Seat getSeatById(int seatId){
		Seat seat =null;
		for(Seat s : this.getSeats()){
			if(s.getSeatId() == seatId){
				seat =s;
				break;
			}
		}
		return seat;
	}
	
	public Seat getSeatByPlayerId(int PlayerId){
		Seat seat =null;
		for(Seat s : seats){
			if(s.getPlayerId() == PlayerId){
				seat =s;
				break;
			}
		}
		return seat;
	}


	public List<Seat> getSeats() {
		return seats;
	}

	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}
	
	  public ISFSObject toSFSObject()
	  {
	    ISFSObject sfso = new SFSObject();
	    sfso.putClass("seatinfo", this);
	    return sfso;
	  }

	public int getNextOccupiedSeat(int seatId) {
		int nextSeatId = seatId;
		
		while(true){
			nextSeatId--;
			
			if(nextSeatId < 1){
				nextSeatId = this.getSeats().size();
			}
			
			if(nextSeatId == seatId){
				return seatId;
			}
			
			if(this.getSeatById(nextSeatId).isOccupied && !this.getSeatById(nextSeatId).isAway()){
				return nextSeatId;
			}
		}
		
		
	}
	
	@Override
	public String toString() {
		return "SeatInfo [seats=" + seats + "]";
	}

	public void reset(){
		for(Seat seat:this.seats){
			seat.setAllIn(false);
			seat.setBet(0);
			seat.setPack(false);
			seat.setLastBetAmount(0);
			seat.setLastAction("");
		}
	}
	  
}
