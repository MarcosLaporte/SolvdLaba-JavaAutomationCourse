package entities.lists;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.Feedback;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "feedbacks")
public class FeedbackList {
    @JsonProperty("feedbacks")
    private List<Feedback> feedbackList;

    @XmlElement(name = "feedback")
    public List<Feedback> getFeedbackList() {
        return this.feedbackList;
    }

    public void setFeedbackList(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
    }
}