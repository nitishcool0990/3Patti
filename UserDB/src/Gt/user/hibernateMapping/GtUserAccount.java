package Gt.user.hibernateMapping;

/**
 * GtUserAccountId entity. @author MyEclipse Persistence Tools
 */

public class GtUserAccount implements java.io.Serializable {

	// Fields

	private Integer userId;
	private Double realBal;
	private Double dummyBal;

	// Constructors

	/** default constructor */
	public GtUserAccount() {
	}

	/** minimal constructor */
	public GtUserAccount(Integer userId, Double realBal) {
		this.userId = userId;
		this.realBal = realBal;
	}

	/** full constructor */
	public GtUserAccount(Integer userId, Double realBal, Double dummyBal) {
		this.userId = userId;
		this.realBal = realBal;
		this.dummyBal = dummyBal;
	}

	// Property accessors

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Double getRealBal() {
		return this.realBal;
	}

	public void setRealBal(Double realBal) {
		this.realBal = realBal;
	}

	public Double getDummyBal() {
		return this.dummyBal;
	}

	public void setDummyBal(Double dummyBal) {
		this.dummyBal = dummyBal;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof GtUserAccount))
			return false;
		GtUserAccount castOther = (GtUserAccount) other;

		return ((this.getUserId() == castOther.getUserId()) || (this.getUserId() != null
				&& castOther.getUserId() != null && this.getUserId().equals(castOther.getUserId())))
				&& ((this.getRealBal() == castOther.getRealBal()) || (this.getRealBal() != null
						&& castOther.getRealBal() != null && this.getRealBal().equals(castOther.getRealBal())))
				&& ((this.getDummyBal() == castOther.getDummyBal()) || (this.getDummyBal() != null
						&& castOther.getDummyBal() != null && this.getDummyBal().equals(castOther.getDummyBal())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result + (getRealBal() == null ? 0 : this.getRealBal().hashCode());
		result = 37 * result + (getDummyBal() == null ? 0 : this.getDummyBal().hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "GtUserAccount [userId=" + userId + ", realBal=" + realBal + ", dummyBal=" + dummyBal + "]";
	}
	
	
	
	
}