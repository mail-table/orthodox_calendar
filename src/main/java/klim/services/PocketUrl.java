package klim.services;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Ignore;

@Entity
public class PocketUrl {
	@Id
    private Long id;

    private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public PocketUrl(String url) {		
		this.url = url;
	}

}
