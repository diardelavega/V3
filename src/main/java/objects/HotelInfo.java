package objects;

/**
 * @author Administrator
 *
 *         To store some of the basic informations regarding the hotel in hand
 */
public class HotelInfo {
	private String name;
	private String htmlAddress;
	private String location;
	private String url;
	private String imgUrl;
	private String price;
	private String id;

	public HotelInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HotelInfo(String name, String htmlAddress, String location, String url, String imgUrl, String price,
			String id) {
		super();
		this.name = name;
		this.htmlAddress = htmlAddress;
		this.location = location;
		this.url = url;
		this.imgUrl = imgUrl;
		this.price = price;
		this.id = id;
	}

	public void printHotelInfo() {
		String address = extractHotelAddress(htmlAddress);
		System.out.println("address: " + address == null ? "-" : address);
//		System.out.println("url: " + url == null ? "-" : url);
//		System.out.println("imgUrl: " + imgUrl == null ? "-" : imgUrl);
		System.out.println("price: ************************" + price == null ? "-" : price);
		System.out.println("rev. ID: " + id == null ? "-" : id);
	}

	private String extractHotelAddress(String s) {
		if (s == null)
			return null;
		StringBuilder sb = new StringBuilder();
		String[] temp = s.split("[><]");
		for (int i = 0; i < temp.length; i += 2) {
			if (temp[i].length() >= 1)
				sb.append(temp[i]);
		}
		return sb.toString();
	}

	public String getName() {
		return name;
	}

	public String getHtmlAddress() {
		return htmlAddress;
	}

	public void setHtmlAddress(String htmlAddress) {
		this.htmlAddress = htmlAddress;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}
