package Gt.HibernateMapping;


public class GtDomainName implements java.io.Serializable {

	private Long id;
	private String domainUrl;
	private String gameName;
	private Integer status;
	
	
	
	public GtDomainName() {
		
	}

	
	public GtDomainName(Long id, String domainUrl, String gameName, Integer status) {
		super();
		this.id = id;
		this.domainUrl = domainUrl;
		this.gameName = gameName;
		this.status = status;
	}


	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getDomainUrl() {
		return domainUrl;
	}



	public void setDomainUrl(String domainUrl) {
		this.domainUrl = domainUrl;
	}



	public String getGameName() {
		return gameName;
	}



	public void setGameName(String gameName) {
		this.gameName = gameName;
	}



	public Integer getStatus() {
		return status;
	}



	public void setStatus(Integer status) {
		this.status = status;
	}

	
	@Override
	public String toString() {
		return "GtDomainName [id=" + id + ", domainUrl=" + domainUrl + ", gameName=" + gameName + ", status=" + status
				+ "]";
	}
	
	
}
