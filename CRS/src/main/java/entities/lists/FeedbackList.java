package entities.lists;

import entities.Feedback;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "feedbacks")
public class FeedbackList {
    private List<Feedback> feedbacks;

    @XmlElement(name = "feedback")
    public List<Feedback> getFeedbacks() {
        return this.feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }
}