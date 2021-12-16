package bmstu.iu9.requests;

public class Answer {

    private String url;
    private Long responseTime;

    public Answer(String url, Long responseTime) {
        this.url = url;
        this.responseTime = responseTime;
    }

    public String getUrl() {
        return url;
    }

    public Long getResponseTime() {
        return responseTime
    }

}
