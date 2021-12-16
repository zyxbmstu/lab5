package bmstu.iu9.requests;

public class Request {

    private String url;
    private int count;

    public Request(String url, int count) {
        this.url = url;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getUrl() {
        return url;
    }

}
